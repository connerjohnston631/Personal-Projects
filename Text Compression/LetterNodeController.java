//LetterNode controller

public class LetterNodeController{
	
	private char[] mainName = {'m','a','i','n'};
	private LetterNode main = new LetterNode(null, mainName);
	
	private int backCount = 0;
	
	private final boolean PRINT = true;
	
	public LetterNodeController(){}
	
	public void addWord(char[] word, int instance){
		LetterNode root = main;
		for (int i = 0; i< word.length; i++){
			int idx = root.hasBranch(toCharArray(word[i]));
			//if it does not have the branch:
			if(idx == -1){
				addNewWord(root, java.util.Arrays.copyOfRange(word, i, word.length), instance);
				return;
			}
			//if it does exist in the branches:
			root = root.getBranch(idx);			
		}
		root.addInstance(instance);
		
	}
	
	public void addToTree(char[] chArray){
		int totalChars = chArray.length;
		int position = 0;
		LetterNode root = main;
		
		
		while (position < totalChars){//only handles single space
			if((byte)chArray[position] == 32){
				root.addInstance(position);
				root = main;
				//position++;
			}
			else{ //add to tree
				char[] ch = toCharArray(chArray[position]);
				if(root.hasBranch(ch)== -1){
					LetterNode newNode = new LetterNode(root, ch);
					root.addBranch(newNode);
					root = newNode;
				}
				else{
					root = root.getBranch(root.hasBranch(ch));
				}
				//position++;
			}
			position++;
		}
	}
	
	private char[] toCharArray(char ch){
		char[] output = {ch};
		return output;
	}
	
	private void addNewWord(LetterNode root, char[] word, int instance){
		//System.out.println("add word: " + word[0]);
		for (int i = 0; i< word.length; i++){
			//if(PRINT){System.out.println(root.getName());}
			LetterNode newNode = new LetterNode(root, toCharArray(word[i]));
			//if(PRINT){System.out.println(newNode.getName());}
			root.addBranch(newNode);
			root = newNode;			
		}
		root.addInstance(instance);
	}
	
	public LetterNode getMain(){
		return main;
	}
	
	public void recursiveRead(LetterNode root){
		//print the first branch of the root. branch out until
		//instances are found. print the instances. if more branches,
		//print them as well. then go back and repeat for all branches 
		//if (!root.hasBranches()){return;}
		backCount = 0;
		for (int i = 0; i < root.getBranchCount(); i++){
			//backCount = 0;
			LetterNode branch = root.getBranch(i); 
			//print the first branch
			System.out.print(branch.getName());
			//if there are instances, print them out
			if(branch.hasInstances()){
				System.out.print("{");
				for (int j = 0; j < branch.getInstanceCount() - 1; j++){
					System.out.print(branch.getInstance(i)+",");
				}
				System.out.print(branch.getInstance(branch.getInstanceCount()-1) + "}");
			}
			//System.out.print(backCount);
			recursiveRead(branch);
			if(i<root.getBranchCount()-1){System.out.print(backCount + "-");}
			//backCount++;
		}
		backCount++;
		return;		
	}
}