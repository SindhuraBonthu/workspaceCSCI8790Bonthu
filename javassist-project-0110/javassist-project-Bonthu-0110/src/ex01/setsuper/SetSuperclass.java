package ex01.setsuper;

import java.io.File;
import java.io.IOException;

import org.omg.CORBA.PRIVATE_MEMBER;

import javassist.CannotCompileException;
/*import javassist.ClassClassPath;*/
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
/*import target.Rectangle;*/

public class SetSuperclass {
	static String _S = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + _S + "output";
	static ClassPool pool;


	public static void main(String[] args) {
		String argument1 = null;
		String argument2 = null;
		CtClass cc=null;
		
		try {
			if (args.length != 2) {
				System.out.println("Invalid Number of Arguments");
				return;
			}
			 pool = ClassPool.getDefault();

			/*
			 * boolean useRuntimeClass = true; if (useRuntimeClass) {
			 * insertClassPathRunTimeClass(pool); } else {
			 */
			insertClassPath(pool);
			if(args.length == 2){
				argument1 = args[0];
				argument2 = args[1];
				if(argument1.startsWith("Common") && argument2.startsWith("Common"))
				{
					int lenarg1 = argument1.length();
					int lenarg2 = argument2.length();
					if(lenarg1>=lenarg2)
					{
//						CtClass cc = pool.get(argument2);
//						setSuperclass(cc, argument1, pool);
						cc = setClassNames(argument1, argument2);
					}
					else
					{
//						CtClass cc = pool.get(argument1);
//						setSuperclass(cc, argument2, pool);
						cc = setClassNames(argument2, argument1);
					}
				}
				else if(argument1.startsWith("Common"))
				{
//					CtClass cc = pool.get(argument2);
//					setSuperclass(cc, argument1, pool);
					cc = setClassNames(argument1, argument2);
				}
				else if(argument2.startsWith("Common"))
				{
//					CtClass cc = pool.get(argument1);
//					setSuperclass(cc, argument2, pool);
					cc = setClassNames(argument2, argument1);
				}
				else
				{
//					CtClass cc = pool.get(argument2);
//					setSuperclass(cc, argument1, pool);
					cc = setClassNames(argument1, argument2);
				}
					
			}
			/* } */
//			for (int i = 0; i < args.length; i++) {
//				//argument1 = "target." + args[0];
//				//argument2 = "target." + args[1];
//				if(argument2.startsWith("Common"))
//				{
//					CtClass cc = pool.get(argument1);
//					setSuperclass(cc, argument2, pool);
//				}
//				else if(argument1.startsWith("Common")&& (argument2.startsWith("Common")))
//					{
//						int lenarg1 = argument1.length();
//						int lenarg2 = argument2.length();
//						if(lenarg2 > lenarg1){
//							CtClass cc = pool.get(argument1);
//							setSuperclass(cc, argument2, pool);	
//					}
//				}
//				else{
//					CtClass cc = pool.get(argument2);
//					setSuperclass(cc, argument1, pool);
//				}
				
			//}

			//CtClass cc = pool.get(argument2);
			//setSuperclass(cc, argument1, pool);

			// CtClass cc = pool.get("target.Rectangle");
			// setSuperclass(cc, "target.Point", pool);

		
			cc.writeFile(outputDir);
			System.out.println("[DBG] write output to: " + outputDir);
		} catch (NotFoundException | CannotCompileException | IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * static void insertClassPathRunTimeClass(ClassPool pool) throws
	 * NotFoundException { ClassClassPath classPath = new ClassClassPath(new
	 * Rectangle().getClass()); pool.insertClassPath(classPath);
	 * System.out.println("[DBG] insert classpath: " + classPath.toString()); }
	 */
	private static CtClass setClassNames(String superClassName, String subClassName){
		CtClass cc = null;
		try {
			cc = pool.get("target." + subClassName);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			setSuperclass(cc, "target." + superClassName, pool);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cc;
	}
	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = workDir + _S + "classfiles";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}
                                                         
	static void setSuperclass(CtClass curClass, String superClass, ClassPool pool)
			throws NotFoundException, CannotCompileException {
		curClass.setSuperclass(pool.get(superClass));
		System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
				", subclass: " + curClass.getName());
	}
}
