package ex07.insertmethodbody;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;

public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) {
      try {
    	  while(true){
    		  UtilMenu.showMenuOptions();
    		  switch (UtilMenu.getOption()) {
    		  case 1: 
    			  System.out.println("Enter a class name, method name and method parameter index");
    			  String[] clazNames = UtilMenu.getArguments();
    			if (clazNames.length != 3) {
						System.out.println("[WRN] Invalid Input");
						continue;
					} else{
						 ClassPool pool = ClassPool.getDefault();
				         pool.insertClassPath(INPUT_DIR);
				         CtClass cc = pool.get("target."+clazNames[0]);
				         CtMethod m = cc.getDeclaredMethod(clazNames[1]);
				         String block1 = "{ " + _L_ //
				               + "System.out.println(\"[DBG] param1: \" + " + clazNames[2] +"); " + _L_ //
				             +   "}";
				         System.out.println(block1);
				         m.insertBefore(block1);
				         cc.writeFile(OUTPUT_DIR);
				         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
				         System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
					}
    		  }
    		  
         }
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}
