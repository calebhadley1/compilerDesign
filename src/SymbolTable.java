
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
    SymbolTableObject[] strings;

    public SymbolTable(){
        symbolIndex=0;
        stringIndex=0;
        symbols = new SymbolTableObject[1000];
        strings = new SymbolTableObject[1000];

    }

    //Returns the index of name or -1 if not found. Searches Symbol or String table based on its tokenType -> 37==string and 35==symbol
    public int search(String name, int scope, int tokenType){
        if(tokenType==37){
            for(int i=0; i<stringIndex; i++){
                if(strings[i].name.equals(name) && strings[i].scope == scope)
                    return i;
            }
        }
        else if(tokenType==35){
            for(int i=0; i<symbolIndex; i++){
                if(symbols[i].name.equals(name) && symbols[i].scope == scope)
                    return i;
            }
        }
        return -1;
    }

    //Inserts a new item into the Symbol or String table based on its tokenType -> 37==string and 35==symbol
    public int insert(String name, int scope, int tokenType){
        int curr = -1;
        if(tokenType==37){
            //If we find the index it doesn't need to be inserted
            int index = search(name,scope,tokenType);
            if(index!=-1){
                return index;
            }
            else{
                curr = stringIndex;
                strings[curr] = new SymbolTableObject(name, scope);
                stringIndex++;
            }
        }
        else if(tokenType==35){
            int index = search(name,scope,tokenType);
            if(index!=-1){
                return index;
            }
            else{
                curr = symbolIndex;
                symbols[curr] = new SymbolTableObject(name, scope);
                symbolIndex++;
            }
        }
        return curr;
    }
}