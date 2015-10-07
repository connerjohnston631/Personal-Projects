This is an incomplete form of text compression for large text files.

To demonstrate, the program can be run from the command line (once in the 
same directory) as

>>java TextEncryptor Gettysburg.txt

(Gettysburg.txt is a 

and the output will be the original text followed by the output.

This is how the compression works:
 1) The file is read through and assembled into a String object.
    This string is fed into a tree that builds from a root to various 
    Nodes. The String is broken into words, and assumes that there is only
    1 space between each word. The words are fed into the tree, each letter
    being a new node from the root if the letter in that path hasn't existed
    yet, or continuing on existing paths as long as possible. 

2) Once the word is entered, the last node will contain an instance of that 
   word, in the representation of a number -- the order that word occurs in 
   the file.

3) The tree is recursively read, outwards in the first priority ordering of
   each node's branches, printing each node's character until a node with 
   instances is read. There is a special character printed, the instances, 
   then another special character. 

4) If the current node has more instances, they are printed out and steps 3 
   and 4 are repeated until a leaf node is hit, then we go back towards the 
   root and find the next priority branch is hit, printing another special
   character and the number of spaces backed up. This is where the space
   is saved, as longer words are built from shorter words.

Desired Input Output (in characters, no valuable compression)

hi hello helloWorld this is isn't instead 

hello{1}World{2}<9i{0}:this{3}:instead{6}<6s{4}n't{5}

Bugs: duplicate instance values are printed

Areas of improvement: BINARY vs BYTES! Huffman compression
                      More efficient file reading (and write to a file)
                      Smaller Node sizes (less data stored in node)
                      
Pros: 
                      
                      

    