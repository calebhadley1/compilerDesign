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
    private int[] reservedInts = {T.PROGRAM, T.VAR, T.INTEGER, T.BOOL, T.PROCEDURE, T.CALL, T.BEGIN,
        T.END, T.IF, T.THEN, T.ELSE, T.WHILE, T.DO, T.AND, T.OR, T.NOT, T.READ, T.WRITE, T.WRITELN};

    File f; // input program
    FileReader fileReader; // needed for buffered reader
    BufferedReader br;

    PrintWriter pw; // for output.txt

    char ch = ' '; // curr char in inputted program

    int line = 1; // line numbers
    int state = 0; // start state

    String error = "";

    SymbolTable symT;

    int procedureNum=0;

    //Constructor: Builds FSM and sets up file reading/writing
    public Scan(String filename, SymbolTable symT) throws IOException {
        this.symT = symT;
        f  = new File (filename);
        fileReader = new FileReader(f);
        br = new BufferedReader(fileReader);

        try {
            pw = new PrintWriter(new File("output.txt"));
            pw.write(line+" ");
        }
        catch (IOException e) {
            e.printStackTrace();
         }
        fsm = new int[15][11]; // build the fsm table
        Scanner input = new Scanner (new File("FSM.txt"));
        for (int row = 0; row < 15; row++)
            for (int col = 0; col< 11; col++)
                fsm[row][col] = input.nextInt();
    }

    public void removeSpaces()throws IOException {
        while (ch ==' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '!'){
            if(ch=='\n' || ch=='\r'){
                line++;
                pw.println();
                pw.write(line+" ");
            }
            else if(ch==' '){
                pw.write(" ");
            }
            else if(ch=='\t'){
                pw.write("\t");
            }
            else if(ch=='!'){
                while(ch!='\n' && ch!='\r'){
                    pw.write(ch);
                    ch = (char)br.read();
                }
                line++;
                pw.println();
                pw.write(line+" ");
            }
            ch = (char)br.read();
        }
    }

    //Checks Identifier Token against Reserved Word array and returns appropriate int
    public int checkReserved(String buf){
        for(int i=0; i<reserved.length; i++){
            if(buf.equals(reserved[i])){
                return reservedInts[i];
            }
        }
        return T.IDENTIFIER;
    }

    //Returns char class of first char in a Token, used for determining start state of FSM
    public int getCharClass(char ch) { // gives proper column in fsm
        if (ch >= 'A'&& ch <='Z' || ch >= 'a'&& ch <='z') //letter
            return 0;
        else if(ch>='0'&& ch<='9') // digit
            return 1;
        else if(ch == '\'') //quote (escape sequence for single quote)
            return 2;
        else if(ch == ':') //colon
            return 3;
        else if(ch == '\n' || ch == '\r') //new line
            return 4;
        else if(ch == '>') //greater than
            return 5;
        else if(ch == '<') //less than
            return 6;
        else if(ch == '=') //equal to
            return 7;
        else if(ch == '(' || ch == ')' || ch == '+' || ch == '*' || ch == '/' || ch == '-' || ch == '%' || ch == '.' || ch == ',' || ch == ';') //other punctuation
            return 8;
        else if(ch == ' ') //white space
            return 9;
        else
            return 10; //other
    }

    //Returns the next Token in inputted program
    public Token nextToken() throws Exception {
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

        Token t = finalState(state, buf);
        //System.out.println(t.tokenType);
        //System.out.println(t.value);
        
        if(t.tokenType==T.IDENTIFIER){
            t.value = symT.insert(buf, procedureNum, t.tokenType, false, 0, 0, 0, 0, 0); //Default ID entry in symbol table -> modified by Semantics/Parser classes
        }
        else if(t.tokenType==T.STRING){
            t.value = symT.insert(buf, procedureNum, t.tokenType, false, 0, 0, 0, 0, 0);
        }
        else if(t.tokenType==T.NUMBER){
            t.value = Integer.parseInt(buf);
        }

        pw.write(buf);
        return t; 
    }
    
    //Returns a Token based on the final state in FSM
    public Token finalState(int state, String buf) throws Exception {
        Token t= new Token();
        if (state == 1) {
            t.tokenType=checkReserved(buf);
        }
        else if(state == 2) {
            t.tokenType=T.NUMBER;
        }
        else if(state == 4) {
            t.tokenType=T.STRING;
        }
        else if(state == 5) {
            t.tokenType=T.COLON;
        }
        else if(state == 6) {
            t.tokenType=T.ASSIGN;
        }
        else if(state == 7) {
            t.tokenType=T.GT;
        }
        else if(state == 8) {
            t.tokenType=T.GE;
        }
        else if(state == 9) {
            t.tokenType=T.LT;
        }
        else if(state == 10) {
            t.tokenType=T.LE;
        }
        else if(state == 11) {
            t.tokenType=T.NE;
        }
        else if(state == 12) {
            if(buf.equals("=")){
                t.tokenType=T.EQUAL;
            }
            else if(buf.equals("+")){
                t.tokenType=T.PLUS;
            }
            else if(buf.equals("-")){
                t.tokenType=T.MINUS;
            }
            else if(buf.equals("*")){
                t.tokenType=T.TIMES;
            }
            else if(buf.equals("/")){
                t.tokenType=T.DIV;
            }
            else if(buf.equals("%")){
                t.tokenType=T.MOD;
            }
            else if(buf.equals("(")){
                t.tokenType=T.LPAREN;
            }
            else if(buf.equals(")")){
                t.tokenType=T.RPAREN;
            }
            else if(buf.equals(".")){
                t.tokenType=T.PERIOD;
            }
            else if(buf.equals(",")){
                t.tokenType=T.COMMA;
            }
            else if(buf.equals(";")){
                t.tokenType=T.SEMI;
            }
        }
        else if(state == 13) {
            setError("Error! Illegal Character",line);
        }
        else if(state == 14) {
            setError("Error! String not Terminated",line);
        }
        return t;
    }

    public void setError(String s, int ln){
        if(error==""){
            error = s+" at line "+ln;
            //writeError();
            pw.println();
            pw.write(error);
            pw.close();
        }
    }

    public void writeError(){
        if(error!=""){
            pw.println();
            pw.write(error);
        }
        pw.close();
    }
}
