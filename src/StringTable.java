
public class StringTable{

    class StringTableObject{
        String name;
        int scope;
        //boolean declared;

        public StringTableObject(String name, int scope){
            this.name=name;
            this.scope=scope;
            //this.declared=declared;
        }
    }

    int stringIndex;
    StringTableObject[] strings;

    public StringTable(){
        stringIndex=0;
        strings = new StringTableObject[1000];

    }

    //Returns the index of name or -1 if not found. Searches String table
    public int search(String name, int scope, int tokenType){
        if(tokenType==37){
            for(int i=0; i<stringIndex; i++){
                if(strings[i].name.equals(name) && strings[i].scope == scope)
                    return i;
            }
        }
        return -1;
    }

    //Inserts a new item into the String table
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
                strings[curr] = new StringTableObject(name, scope);
                stringIndex++;
            }
        }
        return curr;
    }
}