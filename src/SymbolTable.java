
public class SymbolTable{

    class SymbolTableObject{
        String name;
        int scope;
        int tokenType;
        boolean declared;
        int numArgs;
        int kind;
        int start;
        int offset;
        int memAddress;

        public SymbolTableObject(String name, int scope, int tokenType, boolean declared, int numArgs, int kind, int start, int offset, int memAddress){
            this.name=name;
            this.scope=scope;
            this.tokenType=tokenType;
            this.declared=declared;
            this.numArgs=numArgs;
            this.kind=kind;
            this.start=start;
            this.offset=offset;
            this.memAddress=memAddress;
        }
    }

    int symbolIndex;
    int stringIndex;
    int tempNum;
    SymbolTableObject[] symbols;

    public SymbolTable(){
        symbolIndex=0;
        stringIndex=1000;
        tempNum=1;
        symbols = new SymbolTableObject[2000];
    }

    //Returns the index of name or -1 if not found. Searches Symbol or String table based on its tokenType -> 37==string and 35==symbol
    public int search(String name, int scope, int tokenType){
        if(tokenType!=37){
            for(int i=0; i<symbolIndex; i++){
                //if there is a procedure entry then return entry
                if(symbols[i].name.equals(name) && symbols[i].scope == scope)
                    return i;
                else if(symbols[i].name.equals(name) && symbols[i].tokenType==T.PROCEDURE){
                    return i;
                }
            }
        }
        else{
            for(int i=1000; i<stringIndex; i++){
                if(symbols[i].name.equals(name) && symbols[i].scope == scope)
                    return i;
            }
        }
        return -1;
    }

    //Inserts a new item into the Symbol or String table based on its tokenType -> 37==string and 35==symbol
    public int insert(String name, int scope, int tokenType, boolean declared, int numArgs, int kind, int start, int offset, int memAddress){
        int curr = -1;
        if(tokenType!=37){
            int index = search(name,scope,tokenType);
            if(index!=-1){//already in table
                return index;
            }
            else{
                curr = symbolIndex;
                symbols[curr] = new SymbolTableObject(name, scope, tokenType, declared, numArgs, kind, start, offset, curr);
                symbolIndex++;
            }
        }
        else{
            int index = search(name,scope,tokenType);
            if(index!=-1){//already in table
                return index;
            }
            else{
                curr = stringIndex;
                symbols[curr] = new SymbolTableObject(name, scope, tokenType, declared, numArgs, kind, start, offset, curr);
                stringIndex++;
            }
        }
        return curr;
    }

    public int getTemp(){
        String temp = "@t" + tempNum;
        int place = symbolIndex;
        insert(temp, 0, 0, true, 0, 0, 0, 0, place);
        tempNum++;
        return place;
    }
}