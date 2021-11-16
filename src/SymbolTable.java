
public class SymbolTable{

    class SymbolTableObject{
        String name;
        int scope;
        //boolean declared;

        public SymbolTableObject(String name, int scope){
            this.name=name;
            this.scope=scope;
            //this.declared=declared;
        }
    }

    int symbolIndex;
    int stringIndex;
    SymbolTableObject[] symbols;

    public SymbolTable(){
        symbolIndex=0;
        stringIndex=1000;
        symbols = new SymbolTableObject[2000];
    }

    //Returns the index of name or -1 if not found. Searches Symbol or String table based on its tokenType -> 37==string and 35==symbol
    public int search(String name, int scope, int tokenType){
        if(tokenType==35){
            for(int i=0; i<symbolIndex; i++){
                if(symbols[i].name.equals(name) && symbols[i].scope == scope)
                    return i;
            }
        }
        else if(tokenType==37){
            for(int i=1000; i<stringIndex; i++){
                if(symbols[i].name.equals(name) && symbols[i].scope == scope)
                    return i;
            }
        }
        return -1;
    }

    //Inserts a new item into the Symbol or String table based on its tokenType -> 37==string and 35==symbol
    public int insert(String name, int scope, int tokenType){
        int curr = -1;
        if(tokenType==35){
            int index = search(name,scope,tokenType);
            if(index!=-1){//already in table
                return index;
            }
            else{
                curr = symbolIndex;
                symbols[curr] = new SymbolTableObject(name, scope);
                symbolIndex++;
            }
        }
        else if(tokenType==37){
            int index = search(name,scope,tokenType);
            if(index!=-1){//already in table
                return index;
            }
            else{
                curr = stringIndex;
                symbols[curr] = new SymbolTableObject(name, scope);
                stringIndex++;
            }
        }
        return curr;
    }
}