public class Token {
    public int tokenType;
    public int value;  // address in symbol table or numerical value

    public Token() {
        tokenType = -1;
        value = -1;
    }
    
    public Token(int tokenType, int value) {
        this.tokenType = tokenType;
        this.value = value;
    }
}
