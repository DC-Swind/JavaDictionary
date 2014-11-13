import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

//Dictionary main class
public class Dictionary extends JFrame{
    //unique object
    static Dictionary dict;
    
    //window size
    public static int width = 600;
	public static int height = 500;
    
    //top
    static JPanel TopField = new JPanel();
    //input text
	protected JTextField InputMessage = new JTextField(); 
    //unused search button
	protected JButton Search = new JButton("Find more");
    public static int findmore = 0;
    
    //down
    static JPanel DownField = new JPanel();
    
    //words detail area, include word yinbiao mean and others
	static JTextArea RightArea = new JTextArea();
    
    //left wordlist
    static JList LeftList = new JList();
    
    //dictionary.txt 
    public static ReadDictionary wordlist;
    
    //search answer
    public SearchWords searchAns;
    
    //input valueChanged event 
	class InputValueChanged implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			System.out.println("Attribute Changed"+e);
		}
		public void insertUpdate(DocumentEvent e){         
			System.out.println("Text Inserted:"+InputMessage.getText());
            update();
		}
		public void removeUpdate(DocumentEvent e){
			System.out.println("Text Removed:"+InputMessage.getText());
            update();
		}
        public void update(){
            //search 
            searchAns = new SearchWords(wordlist,InputMessage.getText());
            //----------------------------------can delete finally-------------------
            if(searchAns.ans == null){
                System.out.println("searchAns is null");
            }
            //findmore <= 0
            findmore = 0;
            
            
            //update leftlist
            LeftList.setListData(searchAns.ans);
            
            if (searchAns.ans2 == null){
                System.out.println("BK-tree searchAns is null");
            }
            //LeftList.setListData(searchAns.ans2);
        }
	}
    
    //list word select event 
    class ListSelectionChanged implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            //two event occur include click on and click off, but the first one
            //getValueIsAdjusting is true, and the last one is false
            if (e.getValueIsAdjusting() == true) showDetail();
        }
        public void showDetail(){
            //clear
            RightArea.setText("");
            
            int index = LeftList.getSelectedIndex();
            //---------------------when index occur 0 , it will be wrong    can delete this if  add more detail
            if (index >= 0){
                if (findmore == 0){
                    RightArea.append(searchAns.ansnodes[index].word + "\r\n\r\n");
                    RightArea.append("Phonetic symbols: \r\n" + searchAns.ansnodes[index].yinbiao + "\r\n");
                    RightArea.append("Translation: \r\n" + searchAns.ansnodes[index].mean + "\r\n");
                    RightArea.paintImmediately(RightArea.getBounds());
                
                    //InputMessage.setText(searchAns.ansnodes[index].word);
                }else{
                    RightArea.append(searchAns.ansbknodes[index].word + "\r\n\r\n");
                    RightArea.append("Phonetic symbols: \r\n" + searchAns.ansbknodes[index].yinbiao + "\r\n");
                    RightArea.append("Translation: \r\n" + searchAns.ansbknodes[index].mean + "\r\n");
                    RightArea.paintImmediately(RightArea.getBounds());
                }
            }
            
            
        }
    }
    
    //searchButton event class
    class SearchListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.out.println("Find more clicked");
            if (searchAns != null){
                LeftList.setListData(searchAns.ans2);
                findmore = 1;
            }
        }
    }
    //Close introduction Listener event 
    class CloseListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.out.println("Close introduction");
            dict.remove(DownField);
            dict.setVisible(true);
        }
    }
    
	Dictionary(){
        Color bg = Color.white;
		//Top
		TopField.setLayout(new BorderLayout(5,0));
		TopField.add(new JLabel("  Input your word: "),BorderLayout.WEST);
		TopField.add(InputMessage,BorderLayout.CENTER);
		TopField.add(Search,BorderLayout.EAST);
        //TopField.setBorder(BorderFactory.createTitledBorder("TopField"));
        TopField.setBackground(bg);
        
		Search.setMnemonic('F');
		Search.setToolTipText("Find nearly word");
        //Top-event
		InputMessage.getDocument().addDocumentListener(new InputValueChanged()); 
        
        //Down introduction
        DownField.setBorder(BorderFactory.createTitledBorder("Introduction"));
        DownField.setBackground(bg);
        JTextArea Introduction = new JTextArea("After loading the dictionary file, you can input your words in the top area. \n "
        +"If you can not find what you want to find, you can click Find more Button to get more. \n"
        +"Click the button on the right to close this introduction. >>>");
        Introduction.setEditable( false );
        DownField.add(Introduction);
       
        JButton CloseIntroduction = new JButton("Close");
        DownField.add(CloseIntroduction);
        //close event 
        CloseIntroduction.addActionListener(new CloseListener());
        
        add(DownField,BorderLayout.SOUTH);
        
        
        //search 
        Search.addActionListener(new SearchListener());
        //Down-left
        JScrollPane LeftArea = new JScrollPane(LeftList);
        LeftArea.setBorder(BorderFactory.createTitledBorder("Word List"));
        LeftArea.setBackground(bg);
		//Down-right
		RightArea.setPreferredSize(new Dimension(width*3/5, 0));
		RightArea.setFont(new Font("Serif",0,18));
        RightArea.setBorder(BorderFactory.createTitledBorder("Word Detail"));
        RightArea.setBackground(bg);
        //auto breakline
		RightArea.setLineWrap(true);
        RightArea.setWrapStyleWord(true);
        
		//LeftList event
        LeftList.addListSelectionListener(new ListSelectionChanged());
            
		//add to jframe
		//add(TopField,BorderLayout.NORTH);
        add(LeftArea,BorderLayout.CENTER);
		add(RightArea,BorderLayout.EAST);
		InputMessage.setHorizontalAlignment(JTextField.LEFT);
	}
	public static void main(String[] args){
		dict = new Dictionary();
        dict.setTitle("Dictionary By Swind");
		dict.setSize(width,height);
		dict.setLocationRelativeTo(null);
		dict.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //topfield before load file 
        JPanel reading = new JPanel();
        reading.add(new JLabel("  Reading dictionary file...  Please wait a moment... "),BorderLayout.WEST);
        dict.add(reading,BorderLayout.NORTH);   
		dict.setVisible(true);
		
        
        //read file
        String fileurl = "dictionary.txt";
        try{
            wordlist = new ReadDictionary(fileurl);
        }catch(Exception e){
            System.out.println("occur exception from read dictionary");
        }
        
        //refresh topfield
        dict.remove(reading);
        dict.add(TopField,BorderLayout.NORTH);
        dict.setVisible(true);
	}
}


//search event class
class SearchWords{
    //answer words show in leftlist
    //TreeNode ans to show 
    public Vector ans;
    //BKNode ans to show
    public Vector ans2;
    //answer node use dictionary tree and bk-tree
    public TreeNode[] ansnodes;
    public BKNode[] ansbknodes;
    
    SearchWords(final ReadDictionary wordlist,String word){
        //can delete 
        System.out.println("search word : " + word);
        //create vector
        ans = new Vector();
        ans2 = new Vector();
        
        //check 
        if(wordlist == null || word == null){
            System.out.println("wordlist or word is null!");
            return ;
        }
        
        //search the dictionary tree
        ansnodes = wordlist.DicTree.search(word);     
        //number of ans , max is Tree.MaxNSearch
        int len = Tree.MaxNSearch;
        for (int i=0,l=ansnodes.length; i<l; i++) if (ansnodes[i] == null){len = i; break;}
        for (int i=0; i<len; i++){
            ans.add(ansnodes[i].word);
        }
        
        //search the bk-tree
        ansbknodes = wordlist.BkTree.search(word);
        //number of ans
        int len2 = Tree.MaxNSearch;
        for (int i=0,l=ansbknodes.length; i<l; i++) if (ansbknodes[i] == null){len2 = i; break;}
        System.out.println("len2 is " + len2);
        for (int i=0; i<len2; i++) ans2.add(ansbknodes[i].word);
    }
}

//dictionary tree node    
class TreeNode{
    //a-z space . ' / - , ! ? 2 ( ) and others char
    //total is Size, I ignore one, it is key ~
    public final static int Size = 39; 
    //number of words through this node 
    public int sum;
    //next node from this node,
    public TreeNode[] son;
    //until this node, whether it is a word or not
    public boolean isWord;  
    //the next 3 string can make a new class called word
    //the details about the word, if it is not a word, yinbiao and mean doesn't mean anything, but word is the string until this node    
    public String word;
    public String yinbiao;
    public String mean;

    TreeNode(){
        //when a node is created, mean that there is a word through it, so the value is 1
        sum = 1;
        son = new TreeNode[Size];
        isWord = false;
        word="";
    }
}

//dictionary tree
class Tree{
    //the number of words show in the leftlist 
    public final static int MaxNSearch = 40;
    //use the Size defined in TreeNode
    public final static int Size = TreeNode.Size;
    //at least have a root, it mean ""
    public TreeNode root;
    Tree(){
        root = new TreeNode();
    }
    
    //convert char to offset to save in the dictionary tree
    //
    public int charToOffset(char ch){
        if (ch >= 'a' && ch <= 'z') return ch - 'a';
        if (ch >= 'A' && ch <= 'Z') return ch - 'A';
        if (ch == ' ') return 26;
        if (ch == '.') return 27;
        if (ch == '-') return 28;
        if (ch == '/') return 29;
        if (ch == '\'') return 30;
        if (ch == ',') return 31;
        if (ch == '!') return 32;
        if (ch == '?') return 33;
        if (ch == '2') return 34;
        if (ch == '(') return 35;
        if (ch == ')') return 36;
        if (ch == '"') return 37;
        if (ch == '7') return 38;
        return -1;
    }
    
    //insert a word with yinbiao and mean
    public void insert(String strword,String yinbiao,String mean){
        int len = strword.length();
        if(strword == null || len == 0) return;
        TreeNode node = root;
        char[] word = strword.toCharArray();
        for(int i=0; i<len; i++){
            //use charToOffset() convert char to offset
            int offset = charToOffset(word[i]);
            
            //can delete this sentence after test
            //if use another dictionary.txt , use this sentence debug first
            if (offset < 0) System.out.println("insert error !! " + word[i] + " " + strword); 
            
            if (node.son[offset] == null){
                node.son[offset] = new TreeNode();
                node.son[offset].word = node.word + word[i];
            }else {
                node.son[offset].sum++;
            }
            node = node.son[offset];
        }
        node.isWord = true;
        node.yinbiao = yinbiao;
        node.mean = mean;
    }
    
    //dfs search the words through this node
    public void dfs_get(TreeNode node,TreeNode[] ans){
        //searched max size 
        if(ans[MaxNSearch-1] != null) return;
        if(node.isWord){
            int index=-1;
            for (int i=0; i<MaxNSearch;i++) if (ans[i] == null){index = i; break;}
            if (index == -1) System.out.println("index is -1");
            ans[index] = node;
        }
        if(node.sum == 0)return;
        for(int i=0; i<Size; i++) if(node.son[i] != null) dfs_get(node.son[i],ans);
        return;
    }
    
    //search word
    public TreeNode[] search(String str){
        TreeNode[] ans = new TreeNode[MaxNSearch];
        TreeNode node = root;
        char[] word = str.toCharArray();
        int len=str.length();
        for(int i=0; i<len; i++){
            int offset = charToOffset(word[i]);
            node = node.son[offset];
            if (node == null) return ans;
        }
        System.out.println(str + " sum= " + node.sum);
        if (node.sum > 0){
            dfs_get(node,ans);
        }
        return ans;
    }
   
}

class BKNode{
    String word;
    String yinbiao;
    String mean;
    BKNode[] son;
    public final static int MaxLen = BKTree.MaxLen;
    
    BKNode(String word,String yinbiao,String mean){
        this.word = word;
        this.yinbiao = yinbiao;
        this.mean = mean;
        son = new BKNode[MaxLen];
    }
    int calculateLevenshteinLength(String tmp){
        //f can be int[len1+1][len2+1];  test 
        int [][] f = new int[MaxLen][MaxLen];
        
        int len1 = word.length();
        int len2 = tmp.length();
        if (len1 == 0) return len2;else if (len2 == 0) return len1;
        
        //init f
        for (int i=0; i<=len1; i++) f[i][0] = i;
        for (int i=0; i<=len2; i++) f[0][i] = i;
        //start calculate 
        for(int i=1; i<=len1; i++)
            for(int j=1; j<=len2; j++){
                int a = f[i-1][j-1] + ((word.charAt(i-1) == tmp.charAt(j-1))?0:1);
                f[i][j] = Math.min(a,Math.min(f[i][j-1]+1,f[i-1][j]+1));
            } 
        return f[len1][len2];
    }
    
    public void insert(String word,String yinbiao,String mean){
        int dis = calculateLevenshteinLength(word);
        if (son[dis] == null){
            son[dis] = new BKNode(word,yinbiao,mean);
        }else{
            son[dis].insert(word,yinbiao,mean);
        }
    }
}
class BKTree{
    BKNode root;
    //max levenshtein length
    public final static int MaxLen = 45;
    //search distance
    public final static int SearchDis = 2;
    BKTree(){
        //root = new BKNode(word,yinbiao,mean);
    }
    
    public void insert(String word,String yinbiao,String mean){
        if (root == null){
            root = new BKNode(word,yinbiao,mean);
        } else root.insert(word,yinbiao,mean);
    }
    
    public void dfs_get(String word,BKNode node,BKNode[] ans){
        if (ans[Tree.MaxNSearch-1] != null) return;
        int dis = node.calculateLevenshteinLength(word);
        if (dis <= SearchDis){
            int index = -1;
            for(int i=0; i<Tree.MaxNSearch; i++) if (ans[i] == null){index = i; break; }
            if (index == -1) System.out.println("error! index == -1");
            ans[index] = node;
        }
        
        //
        for (int i=Math.max(0,dis-SearchDis); i<=Math.min(dis+SearchDis,MaxLen-1); i++) 
            if (node.son[i] != null) dfs_get(word,node.son[i],ans);
    }
    
    public BKNode[] search(String word){
        BKNode[] ans = new BKNode[Tree.MaxNSearch];
        
        //same as dictionary tree, dfs to get answer
        dfs_get(word,root,ans);
        return ans;
    }
}


//read file class
class ReadDictionary{
    //save the words in a dictionary tree
    Tree DicTree;
    BKTree BkTree;
    //words number
    public static int currentsize;
    ReadDictionary(String fileurl) throws Exception{
        DicTree = new Tree();
        BkTree = new BKTree();
        currentsize = 0;
        File sourceFile = new File(fileurl);
		if(!sourceFile.exists()){
			System.out.println("Source file" + fileurl + "does not exist");
			return;
		}
        
        //start input 
		Scanner input = new Scanner(sourceFile);
        input.nextLine();
		while(input.hasNext()){
            String[] tmp = (input.nextLine()).split("\t");
            //insert to 2 trees 
            DicTree.insert(tmp[1],tmp[2],tmp[3]);
            BkTree.insert(tmp[1],tmp[2],tmp[3]);
			currentsize++;
		}
		input.close();
        System.out.println("Read "+currentsize+" words");
        
    }
}
