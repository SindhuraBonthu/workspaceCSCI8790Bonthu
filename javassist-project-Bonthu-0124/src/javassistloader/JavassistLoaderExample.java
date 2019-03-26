package javassistloader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import util.UtilMenu;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

public class JavassistLoaderExample {
   private static final String WORK_DIR = System.getProperty("user.dir");
   private static final String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   private static final String TARGET_POINT = "target.Point";
   private static final String TARGET_RECTANGLE = "target.Rectangle";
   static String fileseparator = File.separator;
   static String OUTPUT_DIR = WORK_DIR + fileseparator + "output";
   
   
   public static void main(String[] args) {
      try {
    	  while (true)
    	  {
    		  UtilMenu.showMenuOptions();
    		  switch (UtilMenu.getOption()) 
    		{
    		  case 1: 
    			  System.out.println("Enter three method names:");
    			  String[] clazNames = UtilMenu.getArguments();
                  if(clazNames.length != 3)
                  {
               	   System.out.println("Please enter 3 arguments");
               	   continue;
                  }
                  else
                  {
                	
                	  ClassPool cp = ClassPool.getDefault();
                      cp.insertClassPath(INPUT_DIR);
                      System.out.println("[DBG] insert classpath: " + INPUT_DIR);
                      
                	  char printChar = clazNames[1].toString().charAt(3);
                      CtClass cc = cp.get(TARGET_RECTANGLE);
                      //cc.defrost();
                      cc.setSuperclass(cp.get(TARGET_POINT));
                      CtMethod m1 = cc.getDeclaredMethod(clazNames[0]);
                      m1.insertBefore("{ " //
                            + clazNames[1].toString() + "();" //
                    		+ "System.out.println(\"" + printChar + ":\"+" + clazNames[2] + "());}");

                      Loader cl = new Loader(cp);
                      Class<?> c = cl.loadClass(TARGET_RECTANGLE);
                      Object rect = c.newInstance();
                      System.out.println("[DBG] Created a Rectangle object.");

                      Class<?> rectClass = rect.getClass();
                      Method m = rectClass.getDeclaredMethod(clazNames[0], new Class[] {});
                      System.out.println("[DBG] Called getDeclaredMethod.");
                    
                      Object invoker = m.invoke(rect, new Object[] {});
                      System.out.println("[DBG] getVal result: " + invoker); 
                      cc.writeFile(OUTPUT_DIR);
                      
                      
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

   /*static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }*/
}
