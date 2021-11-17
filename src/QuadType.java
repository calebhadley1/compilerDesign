public class QuadType {
    String op, arg1, arg2, result; //Does not have to be a string

    public QuadType(){
        op = "";
        arg1 = "";
        arg2 = "";
        result = "";
    }

    public QuadType(String op, String arg1, String arg2, String result){
        this.op=op;
        this.arg1=arg1;
        this.arg2=arg2;
        this.result=result;
    }
}
