import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Quads{
    QuadType[] quads = new QuadType[100];
    public int quadSize = 0;
    PrintWriter pw;
    
    public Quads() throws FileNotFoundException{
        pw = new PrintWriter(new File("quadsTableOutput.txt")); 
    }

    public int getQuad(){
        return quadSize;
    }

    public void insertQuad(String op, String arg1, String arg2, String result){
        System.out.println("Insert Quad");
        QuadType q = new QuadType(op, arg1, arg2, result);
        quads[quadSize] = q;
        quadSize++;
    }

    public void setArg1(int n, String s){
        quads[n].arg1 = s;
    }

    public void setArg2(int n, String s){
        quads[n].arg2 = s;
    }

    public void setResult(int n, String s){
        quads[n].result = s;
    }

    public void writeQuadsTable()throws Exception{
        pw.write("Symbol Table:");
        pw.println("");
        pw.write("OP\t" + "ARG1\t" + "ARG2\t"+ "RESULT");
        int i=0;
        while(quads[i]!=null){
            pw.println();
            pw.write(quads[i].op + "\t" + quads[i].arg1 + "\t" + quads[i].arg2 + "\t" + quads[i].result);
            i++;
        }
        pw.println();
        pw.close();
    }
}