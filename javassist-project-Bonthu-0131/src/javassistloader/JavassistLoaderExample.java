package javassistloader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import util.UtilMenu;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Loader;
import target.CommonServiceA;
import target.CommonComponentB;

public class JavassistLoaderExample {
	private static final String WORK_DIR = System.getProperty("user.dir");
	private static final String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	private static final String TARGET_A = "target.CommonServiceA";
	private static final String TARGET_B = "target.CommonComponentB";
	static String fileseparator = File.separator;
	static String OUTPUT_DIR = WORK_DIR + fileseparator + "output";

	public static void main(String[] args) {
		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter a class name and 3 field names");
					String[] clazNames = UtilMenu.getArguments();
					if (clazNames.length != 3) {
						System.out.println("[WRN] Invalid Input");
						continue;
					} else {

						ClassPool cp = ClassPool.getDefault();
						cp.insertClassPath(INPUT_DIR);
						CtClass cc = cp.get("target." + clazNames[0]);
						System.out.println("[DBG] insert classpath: " + INPUT_DIR);

						char printChar = clazNames[1].toString().charAt(2);

						CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
						declaredConstructor.insertBefore("{ " //
								// + "System.out.println(\"[TR] After calling a
								// constructor: \"); }");
								+ "System.out.println(\"" + printChar + ":\"+" + clazNames[1] + ");}");

						Class<?> c = cc.toClass();
						CommonServiceA A = (CommonServiceA) c.newInstance();
						CommonComponentB B = (CommonComponentB) c.newInstance();
						A.getClass();
						B.getClass();

					}
					break;
				default:
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * static void insertClassPath(ClassPool pool) throws NotFoundException {
	 * String strClassPath = WORK_DIR + File.separator + "classfiles";
	 * pool.insertClassPath(strClassPath);
	 * System.out.println("[DBG] insert classpath: " + strClassPath); }
	 */
}
