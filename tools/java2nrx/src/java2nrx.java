import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;

 
 
public class java2nrx {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println("java2nrx, the translator translator, version 1.0.6");
		
		if (args.length == 0 || args[0].equals("-h") || args[0].equals("-H") || args[0].equals("--help") || args[0].equals("-?") ){
	      System.err.println("\nUsage : java -jar java2nrx <infile.java> [out.nrx]");
	      System.err.println("   Provide Java source file as input");
	      System.err.println("   Optionally followed by NetRexx output source file, if not given output goes to stdout");
			System.exit(2);	
		}
		
		String argfile  = args[0];
		String infile = argfile;
		String stubFile = null;
		
		System.out.println("Translating " + infile);
      
		CompilationUnit cu = null;

		File stub = null;
		
		int tries = 0;
		boolean success = false;
		while  (tries < 4 && ! success) {
			tries = tries + 1;					

//			System.out.println("Tries " + tries);
			FileInputStream in = new FileInputStream(infile);
	
			try {
				cu = JavaParser.parse(in);
				success = true;
			} catch (japa.parser.ParseException e) {

				in.close();
				if (stubFile != null) {
//					System.out.println("Deleting stub file " + stubFile);
					new File(stubFile).delete();
					stubFile  = null;
				}	

				if (tries == 1) {
					System.out.println("Try adding class stub");
					stubFile = stubFile("class j2nrxstub {\n", "\n}\n", argfile);

					infile = stubFile;

				} else if (tries == 2) {
					System.out.println("Try adding class and method stub");
					stubFile = stubFile("class j2nrxstub {\n   void j2nrxstub() {\n", "\n   }\n}\n", argfile);

					infile = stubFile;
					
				} else if (tries == 3) {
					System.out.println("Trying original");
//					stubFile = stubFile("class j2nrxstub {\n   void j2nrxstub() {\n", "\n   }\n}\n", argfile);

					infile = argfile;

				} else {
					throw e;
				}	
			} finally {
				in.close();
			}
      }
		if (stubFile != null) {
			System.out.println("Deleting stub file " + stubFile);
			new File(stubFile).delete();
			stubFile  = null;
		}	

      
      // prints the resulting compilation unit to default system output
      if (args.length == 2) {
      	OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(args[1]));
        	out.write(cu.toString());
        	out.close();
        	System.out.println("Written " + args[1]);
      } else {	
        	System.out.println(cu.toString());
      }
	}
	
	public static String stubFile(String pre, String post, String infile) throws Exception {
		String stubFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "j2nrx" + System.nanoTime() + ".java";
//		System.out.println("Creating stub file " + stubFile);
		
		FileReader inr = null;
		FileWriter outr = null;

		try {
			inr = new FileReader(infile);
			outr = new FileWriter(stubFile);
			
			outr.write(pre);
			
			int c;
			while ((c = inr.read()) != -1) {
				outr.write(c);
			}
			outr.write(post);
		} finally {
			if (inr != null) {
				inr.close();
			}
			if (outr != null) {
				outr.close();
			}
		}	
	   return stubFile;	
		
	}	
}
