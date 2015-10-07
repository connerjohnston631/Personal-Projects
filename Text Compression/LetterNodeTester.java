public class LetterNodeTester{

	public static void main(String[] args){
		LetterNodeController controller = new LetterNodeController();
		char[] hi = {'h','i'};
		char[] highly = {'h','i','g','h','l','y'};
		char[] highest = {'h','i','g','h','e','s','t'};
		char[] test = {'t','e','s','t'};
		controller.addWord(hi, 1);
		controller.addWord(highly, 2);
		controller.addWord(highest , 3);
		controller.addWord(test, 4);
		controller.addWord(highest, 5);
		//System.out.println("---");
		//for (int i = 0; i < controller.getMain().getBranchCount(); i++){
			//System.out.println(controller.getMain().getBranch(i).getName());

//		}
		
		controller.recursiveRead(controller.getMain());
		
		//args[0]
	}
	
}