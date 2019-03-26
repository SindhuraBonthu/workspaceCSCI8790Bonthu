package ex13.newfield;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import target.Author;
import util.UtilMenu;

public class AnnotatedFieldExample3 {
	static String workDir = System.getProperty("user.dir");
	static String inputDir = workDir + File.separator + "classfiles";
	static String outputDir = workDir + File.separator + "output";
	public static String target_annotation = null;
	static Class<?> c;

	public static void main(String[] args) throws CannotCompileException, IOException {

		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter Class Name and the target annotation");
					String[] input = UtilMenu.getArguments();
					if (input.length != 2) {
						System.out.println("[WRN]: Invalid Input size!!");
						continue;
					} else {
						ClassPool pool = ClassPool.getDefault();
						pool.insertClassPath(inputDir);
						target_annotation = "target." + input[1];
						c = Class.forName(target_annotation);
						CtClass ct = pool.get("target." + input[0]);
						CtField cf[] = ct.getDeclaredFields();
						for (CtField m : cf) {
							process(m.getAnnotations());
						}
						ct.writeFile(outputDir);
					}
					break;
				default:
					break;
				}

			}

		} catch (NotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	static void process(Object[] annoList) {
		boolean exist = false;
		if (c.isAnnotation()) {
			for (int i = 0; i < annoList.length; i++) {
				if (c.isInstance(annoList[i])) {
					exist = true;
				}
			}
		}

		if (exist) {
			for (int i = 0; i < annoList.length; i++) {
				if (annoList[i] instanceof Author) {
					Author author = (Author) annoList[i];
					System.out.println("Name: " + author.name() + ", Year: " + author.year());
				}
			}
		}
	}
}
