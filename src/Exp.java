public class Exp {
    public int type; //data type of an expression
    public int value; //symbol table address or numerical value
    public boolean number; //is the expression a number ie 246
    public boolean bool; //is the expression a boolean
    
    public Exp(){
        type=0;
        value=0;
        number=false;
        bool=false;
    }

    public Exp(int type, int value, boolean number, boolean bool){
        this.type=type;
        this.value=value;
        this.number=number;
        this.bool=bool;
    }
}
