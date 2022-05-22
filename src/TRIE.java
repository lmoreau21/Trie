import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TRIE {
    static Node root = new Node();
    static String forbidden = "";
    static String yellow;
    static String green;
        
    public static void main(String[] args) throws FileNotFoundException {
        File doc = new File("WORD.LST");
        Scanner scan = new Scanner(doc);
        String word;
       
        while(scan.hasNext()){
            word = scan.next();
            if(word.length()==5)
                insert(word);
        }    
        
        
        Scanner user = new Scanner(System.in);
        String escapeWord = "no";
        String userInput = "";

        while(!userInput.equalsIgnoreCase(escapeWord)){
            System.out.print("Are you ready? (no/yes): ");
            userInput = user.nextLine();
            if(userInput.equalsIgnoreCase(escapeWord)){
                break;
            }
            System.out.print("Forbidden Letters: "+forbidden);
            forbidden += user.nextLine();
            System.out.print("Green Letters(enter # when missing): ");
            green = user.nextLine();
            if(green.length()!=5)
                green = "#####";
            System.out.print("Yellow Letters(enter num pos they appear): ");
            yellow = user.nextLine();
            System.out.println("Possible Words: ");
            int index = 0;
            while(green.charAt(index)!='#')
                index++;
            search(prefixNode(green.substring(0,index)).getFChild());
        }
          
        user.close();
        scan.close();
    }
    
    static void insert(String key){
        if(root.getFChild()==null)
            root.setFChild( new Node(key.charAt(0)));
        Node x = root;
        Node parent = x;
        for(int index = 0; index < key.length(); index++){
            
            if(parent.getFChild() == null){
                parent.setFChild(new Node(key.charAt(index)));
            }
            x = parent.getFChild();
            while(key.charAt(index)!= x.getLead()&&x.getRSibling()!=null){
                x = x.getRSibling();
            }
            if(x.getRSibling()==null&& key.charAt(index)!=x.getLead()){
                x.setRSibling(new Node(key.charAt(index)));
                x = x.getRSibling();
            }
            parent = x; 
        }
        x.setFullWord(key);
        x.setEndT();   
    }    
    
    static void search(Node x){
        if(x!=null&&x.getFChild()!=null)
            search(x.getFChild());
        
        if(x!=null&&x.getRSibling()!=null) 
            search(x.getRSibling());  
                       
        if(x.isEndOfWord()&&checkForb(x.getFullWord())&&checkYellow(x.getFullWord())&&checkGreen(x.getFullWord())){
            System.out.println("    "+x.getFullWord());
        }
    }
    static boolean checkForb(String word){
        for(int i=0; i < word.length(); i++){
            if(green.charAt(i)=='#'&&forbidden.indexOf(word.charAt(i))!=-1)
                return false;
        }
        return true;
    }
    
    static boolean checkGreen(String word){
        for(int i=0; i < green.length(); i++){
            if(green.charAt(i) != '#' && green.charAt(i)!=word.charAt(i)){
                return false;
            }
        } 
        return true;
    }
    static boolean checkYellow(String word){
        if(yellow.length()==0)
            return true;
        char preChar = yellow.charAt(0);
        for(int i=0; i < yellow.length(); i++){
            char curChar = yellow.charAt(i);
            if(Character.isLetter(curChar)&&word.indexOf(curChar)==-1)
                return false;
            if(Character.isLetter(curChar))
                preChar = yellow.charAt(i);
            if(Character.isDigit(curChar)&&word.charAt(Character.getNumericValue(curChar)-1)==preChar){
                return false;
            }
        } 
        return true;
    }
  
    
    static Node prefixNode(String pre){
        Node x = root;
        for(int i=0; i<pre.length(); i++){
            x = x.getFChild();
            while(x!=null && x.getLead()!=pre.charAt(i))
                x = x.getRSibling();               
        }
        return x;
    }
}

class Node{
    private char leadChar;
    private Node rightSibling = null;
    private Node firstChild = null;
    private boolean isEndOfWord = false;
    private String fullWord;
    public Node(){
        leadChar = '0';
    }
    public Node(char value){
        leadChar = value;
    }
    public Node getRSibling(){
        return rightSibling;
    }
    public void setRSibling(Node right){
        rightSibling = right;
    }
    public Node getFChild(){
        return firstChild;
    }
    public void setFChild(Node first){
        firstChild = first;
    }
    public char getLead(){
        return leadChar;
    }
    public boolean isEndOfWord(){
        return isEndOfWord;
    }
    public void setEndT(){
        isEndOfWord = true;
    }
    public void setFullWord(String value){
        fullWord = value;
    }
    public String getFullWord(){
        return fullWord;
    }
    public void setKey(char x){
        leadChar = x;
    }
}