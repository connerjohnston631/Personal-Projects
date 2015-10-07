import java.util.*;
import java.io.*;

public class TextEncryptor{
	
	private static char[] txtFile;
	private static LetterNodeController controller = new LetterNodeController();
	
	public static void main(String[] args){
	
		//verifies proper argument
		if (!verify(args)){return;}
		//System.out.println("success?");
		System.out.println(txtFile);
		
		controller.addToTree(txtFile);
		
		/*int start = 0;
		int finish = 0;
		int instance = 1;
		
		while (start < txtFile.length){
			while (badCharacter(txtFile[start])) {start++;}
			finish = start+1;
			while (finish < txtFile.length && !badCharacter(txtFile[finish])){finish++;}
			
			char[] word = Arrays.copyOfRange(txtFile, start, finish);
			System.out.print("\n" + start + " " + finish + " " + instance + " ");
			for (int k = 0; k < word.length; k++){
				System.out.print(word[k]);
			}
			//controller.addWord(java.util.Arrays.copyOfRange(txtFile, start, finish), instance);
			//for(int j = start; j <finish; j++){
				//System.out.println(txtFile[j]);
			//}
			instance++;
			start = finish + 1;
		}*/
		
		controller.recursiveRead(controller.getMain());
	}
	
	
	private static boolean badCharacter(char ch){
		byte bt = (byte) ch;
		if(bt == 10 || bt == 32){
			return true;
		}
		return false;
	}
	
	private static boolean verify(String[] args){
		System.out.println("verifying");
		if (args.length != 1){
					System.out.println("number of args = " + args.length);

		return false;}
		
		String file = args[0];
		System.out.println("file: " + file);

		if(file.length() < 4 || 
			!file.substring(file.length()-4, file.length()).equals(".txt")){
			System.out.println("File name is not entered correctly");
			return false;}
		
		try{
					System.out.println("trying...");

			Scanner sc = new Scanner(new File(file));
			
			String str = sc.nextLine();
			//System.out.println(str);

			while(sc.hasNextLine()){
				str =  str + "\n" + sc.nextLine();
			}
			//System.out.println(str);
			txtFile = str.toCharArray();
			return true;
		}
		catch(Exception e){
			System.out.println("File has an issue loading");
			return false;
		}			
	}
}