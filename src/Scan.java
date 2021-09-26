import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Scan {
    private int[][] fsm; // finite state machine table

    // table (array) of reserved words  
    private String[] reserved = {"program","var","integer", "bool","procedure","call", "begin", 
        "end","if","then", "else", "while", "do", "and","or", "not", "read", "write","writeln"};

    File f; // input program
    FileReader fileReader; // needed for buffered reader
    BufferedReader br;

    PrintWriter pw; // for output.txt

    char ch = ' '; // curr char in inputted program

    int line = 1; // line numbers
    int state = 0; // start state

    //Constructor: Builds FSM and sets up file reading/writing
    public Scan(String filename) throws IOException {
        f  = new File (filename);
        fileReader = new FileReader(f);
        br = new BufferedReader(fileReader);

        pw = new PrintWriter("output.txt");

        fsm = new int[15][11]; // build the fsm table
        Scanner input = new Scanner (new File("FSM.txt"));
        for (int row = 0; row < 15; row++)
            for (int col = 0; col< 11; col++)
                fsm[row][col] = input.nextInt();
    }

    public void removeSpaces()throws IOException {
        while (ch ==' ' || ch == '\n' || ch == '\r')
            ch = (char)br.read();
    }

    public int getCharClass(char ch) { // gives proper column in fsm
        if (ch >= 'A'&& ch <='Z' || ch >= 'a'&& ch <='z' )
        return 0; if(ch>='0'&& ch<='9')
        return 1;
        else return 2; 
    }

    //Returns the next Token in inputted program
    public String nextToken() throws Exception {
        removeSpaces();
        int state = 0;
        int inchar= getCharClass(ch);
        String buf = "";
        while( fsm[state][inchar] >0) {
            buf = buf+ ch;
            state = fsm[state][inchar];
            ch = (char)br.read();
            inchar= getCharClass(ch);
        }
        System.out.print(buf+ "\t");
        String t = finalState(state, buf);
        return t; 
    }
    
    //Returns the type of Token based on the final state in FSM
    String finalState(int state, String buf) throws Exception {
        String t= "";
        if (state == 1) {
            t= "ID";
        }
        else if(state == 2) {
            t = "NUM";
        }
        return t;
    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter filename");
        String filename = input.nextLine();

        Scan s = new Scan (filename);

        Token t =new Token();
        while(t.tokenType!= T.PERIOD) {
            t = s.nextToken(); // will have printed the token string already
            System.out.println(t.tokenType ); // prints code number
        }   
        s.pw.close();
    }
}
