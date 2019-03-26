package classloader;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class SampleLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String TARGET_APP;
   static String FIELD_NAME;
   private ClassPool pool;
   static String _S = File.separator;
   static String OUTPUT_DIR = WORK_DIR + _S + "output";
  

   public static void main(String[] args) throws Throwable {
	  System.out.println("Enter application name and member field name");
	  String[] input = UtilMenu.getArguments();
	  TARGET_APP = input[0];
	  FIELD_NAME = input[1];
      SampleLoader s = new SampleLoader();
      Class<?> c = s.loadClass(TARGET_APP);
      c.getDeclaredMethod("main", new Class[] { String[].class }). //
            invoke(null, new Object[] { input });
   }

   public SampleLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the bytecode.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(name);
         if (name.equals(TARGET_APP)) {
            CtField f = new CtField(CtClass.intType,FIELD_NAME , cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f);
            cc.writeFile(OUTPUT_DIR);
         }
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}
