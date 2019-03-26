package ex02.setsuper;

import java.io.File;
import java.io.IOException;

import ex02.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.Rectangle;

public class SetSuperclass {
	static String _S = File.separator;
	static String WORK_DIR = System.getProperty("user.dir");
	static String CLASSPATH_DIR = WORK_DIR + _S + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + _S + "output";
	static String subclass;
	static String superclass;

	public static void main(String[] args) {
		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter two class names:");
					String[] clazNames = UtilMenu.getArguments();
					if (clazNames.length != 2) {
						System.out.println("Please enter 2 arguments");
						return;
					}
					if (clazNames[0].startsWith("Common") && clazNames[1].startsWith("Common")) {
						int lenarg1 = clazNames[0].length();
						int lenarg2 = clazNames[1].length();
						if (lenarg1 > lenarg2) {
							subclass = clazNames[1];
							superclass = clazNames[0];
						} else
							subclass = clazNames[0]; 
							superclass = clazNames[1];
					} else if (clazNames[1].startsWith("Common")) {
						subclass = clazNames[1];
						superclass = clazNames[0];
					}
					
					ClassPool pool = ClassPool.getDefault();
					pool.insertClassPath(OUTPUT_DIR);
					System.out.println("[DBG] class path: " + OUTPUT_DIR);

					CtClass ccPoint2 = pool.makeClass(superclass);
					ccPoint2.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					System.out.println("[DBG]\t new class: " + ccPoint2.getName());

					CtClass ccRectangle2 = pool.makeClass(subclass);
					ccRectangle2.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					System.out.println("[DBG]\t new class: " + ccRectangle2.getName());

					ccRectangle2.defrost();
					System.out.println("[DBG] modifications of the class definition will be permitted.");

					ccRectangle2.setSuperclass(ccPoint2);
					System.out
							.println("[DBG] set super class, " + ccRectangle2.getName() + " -> " + ccPoint2.getName());

					ccRectangle2.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	static void setSuperClass(String clazSub, String clazSuper) {
//		try {
//			ClassPool pool = ClassPool.getDefault();
//			insertClassPathRunTimeClass(pool);
//
//			CtClass ctClazSub = pool.get("target." + clazSub);
//			CtClass ctClazSuper = pool.get("target." + clazSuper);
//			ctClazSub.setSuperclass(ctClazSuper);
//			System.out.println("[DBG] set superclass: " //
//					+ ctClazSub.getSuperclass().getName() //
//					+ ", subclass: " + ctClazSub.getName());
//
//			ctClazSub.writeFile(OUTPUT_DIR);
//			System.out.println("[DBG] write output to: " + OUTPUT_DIR);
//		} catch (NotFoundException | CannotCompileException | IOException e) {
//			e.printStackTrace();
//		}
//	}

	static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException {
		Rectangle rectangle = new Rectangle();
		Class<?> runtimeObject = rectangle.getClass();
		ClassClassPath classPath = new ClassClassPath(runtimeObject);
		pool.insertClassPath(classPath);
		System.out.println("[DBG] insert classpath: " + classPath.toString());
	}

	static void insertClassPath(ClassPool pool) throws NotFoundException {
		pool.insertClassPath(CLASSPATH_DIR);
		System.out.println("[DBG] insert classpath: " + CLASSPATH_DIR);
	}

	/*
	 * static void setSuperclass(CtClass curClass, String superClass, ClassPool
	 * pool) // throws NotFoundException, CannotCompileException {
	 * curClass.setSuperclass(pool.get(superClass));
	 * System.out.println("[DBG] set superclass: " // +
	 * curClass.getSuperclass().getName() // + ", subclass: " +
	 * curClass.getName()); }
	 */
}
