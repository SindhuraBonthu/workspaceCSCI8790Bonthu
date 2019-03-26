package ex04.toclass;

import java.io.File;
import java.io.IOException;

import ex04.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ToClass {
	   static String _S = File.separator;
	   static String WORK_DIR = System.getProperty("user.dir");
	   static String OUTPUT_DIR = WORK_DIR + _S + "output";
   public static void main(String[] args) {
	   
	   
      try {
         // Hello orig = new Hello(); // java.lang.LinkageError
    	  while (true) {
              UtilMenu.showMenuOptions();
              switch (UtilMenu.getOption()) {
              case 1:
                 System.out.println("Enter two class names:");
                 String[] classnames = UtilMenu.getArguments();
                 if(classnames.length != 2){
              	   System.out.println("[WRN] Invalid Input");
              	   continue;
                 }
                 else{
                	 for (int i=0;i < classnames.length;i++) {
                		 ClassPool cp = ClassPool.getDefault();
							CtClass cc = cp.get("target." + classnames[i]);
							CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
							declaredConstructor.insertAfter("{ " //
									+ "System.out.println(\"[TR]After calling a constructor: id:\" + id);"
									+ "System.out.println(\"[TR]After calling a constructor: year:\" + year); }");

							Class<?> c = cc.toClass();
							c.newInstance();
							cc.writeFile(OUTPUT_DIR);
						}
                 }
              }
    	  
      }
    	  }catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
}
