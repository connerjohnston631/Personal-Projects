//LetterNode object for sorting words into a tree

public class LetterNode{
	
	private LetterNode root;
	private LetterNode[] branches = new LetterNode[10]; 
	private int[] instances = new int [10];
	private char[] name;
	
	private int branchCount;
	private int instanceCount;
	
	private boolean hasBranches = false;
	private boolean hasInstances = false;
	
	public LetterNode(LetterNode root, char[] name){
		this.root = root;
		this.name = name;
	}
	
	public char[] getName(){
		return this.name;
	}
	
	public int hasBranch(char[] searchName ){ //returns index of branch. -1 if nonexistent
		if (!this.hasBranches()){
		//System.out.println("no branches in " + this.getName()[0]);
		return -1;}
		//otherwise, scan for the particular branch
		for (int i = 0; i< branchCount; i++){
			//if(branches[i].getName().equals(searchName)){return i;}
			if(branches[i].getName().length == 1 && branches[i].getName()[0] == searchName[0])
				{return i;}
		}
		return -1;
	}

	public void addBranch(LetterNode newNode){
		if(branches.length <= branchCount ) {this.increaseBranches();}
		branches[branchCount] = newNode;
		branchCount++;
		hasBranches = true;
	}
	
	private void increaseBranches(){ //increases branches array size by 10 whenever full
		LetterNode[] copy = new LetterNode[branches.length+10];
		for(int i = 0; i < branches.length; i++){
			copy[i] = branches[i];
		}		
		branches = copy;
	}
	
	private void increaseInstances(){
		int [] copy = new int[instances.length + 10];
		for (int i = 0; i < instances.length; i++){
			copy[i] = instances[i];
		}
		instances = copy;
	}
	
	public boolean hasBranches(){
		return this.hasBranches;
	}
	
	public boolean hasInstances(){
		return this.hasInstances;
	}
	
	public void addInstance(int instance){
		if(instances.length <= instanceCount){this.increaseInstances();}
		instances[instanceCount] = instance;
		instanceCount++;
		hasInstances = true;
	}
	
	public LetterNode getBranch(int idx){
		if(idx >= branchCount){return null;}
		return this.branches[idx];
	}
	
	public int getInstanceCount(){
		return this.instanceCount;
	}
	
	public int getInstance(int idx){
		return this.instances[idx];
	}
	
	public int getBranchCount(){
		return this.branchCount;
	}
	
}