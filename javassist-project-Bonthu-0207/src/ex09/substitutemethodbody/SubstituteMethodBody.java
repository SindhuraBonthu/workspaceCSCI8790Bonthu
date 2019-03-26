package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	
	static final String DRAW_METHOD = "draw";
	static String AppName = null;
	static String MethodName = null;
	static int ParameterIndex = 0;
	static int Value = 0;
	
	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws Throwable {

		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter classname,methodname,parameterindex and its value");
					String[] input = UtilMenu.getArguments();
					if (input.length != 4) {
						System.out.println("[WRN]: Invalid Input");
						continue;
					} else {
						AppName = "target." + input[0];
						MethodName = input[1];
						ParameterIndex = Integer.parseInt(input[2]);
						Value = Integer.parseInt(input[3]);
						
						SubstituteMethodBody s = new SubstituteMethodBody();
							Class<?> c = s.loadClass(AppName);
							Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
							mainMethod.invoke(null, new Object[] { args });
						
					}
					break;
				default:
					break;
				}
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	

	private ClassPool pool;

	public SubstituteMethodBody() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(INPUT_PATH); // "target" must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException {
					String className = m.getClassName();
					String methodName = m.getMethodName();

					if (className.equals(AppName) && methodName.equals(DRAW_METHOD)) {
						System.out.println(
								"[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
						String block1 = "{" + _L_ //
								+ "System.out.println(\"Before a call to " + methodName + ".\"); " + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "System.out.println(\"After a call to " + methodName + ".\"); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK1: " + block1);
						System.out.println("------------------------");
						m.replace(block1);
					} else if (className.equals(AppName) && methodName.equals(MethodName)) {
						System.out.println(
								"[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
						String block2 = "{" + _L_ //
								+ "System.out.println(\"\tReset param to zero.\"); " + _L_ //
								+ "$1 = 0; " + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK2: " + block2);
						System.out.println("------------------------");
						m.replace(block2);
					}
				}
			});
			cc.writeFile(OUTPUT_DIR);
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
}
