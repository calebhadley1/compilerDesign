import java.util.Scanner;


public class Parser{
    Scan scanner;
    SymbolTable symT;
    StringTable strT;

    String filename;
    Token tok;

    public Parser(String filename)throws Exception{
        symT = new SymbolTable();
        strT = new StringTable();
        scanner = new Scan(filename, symT, strT);
    }

    public void writeSymbolTable()throws Exception{
        //print symbol table to a file for debugging
    }

    public void parse()throws Exception{
        System.out.println("Program"); //for debugging and tracing
        if(tok.tokenType==T.PROGRAM)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting program",scanner.line);
        
        if(tok.tokenType!=T.IDENTIFIER)
            scanner.setError("Expecting program name", scanner.line);
        else
            tok=scanner.nextToken(); //scope here??
        
        if(tok.tokenType==T.SEMI)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting semi", scanner.line);

        variableDeclarations();
        
        subprogramDeclarations();

        compoundStatement();

        if(tok.tokenType==T.PERIOD)
            System.out.println("Success");
        else
            scanner.setError("Expecting Period", scanner.line);
    }

    public void variableDeclarations()throws Exception{
        System.out.println("Variable Declarations");

        if(tok.tokenType==T.VAR)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Var", scanner.line);

        variableDeclaration();

        if(tok.tokenType==T.SEMI)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting semi", scanner.line);

        //if token is id then we have more variableDecs otherwise E
        while(tok.tokenType==T.IDENTIFIER){
            variableDeclaration();
        }
    }
    
    public void variableDeclaration()throws Exception{
        System.out.println("Variable Declaration");

        identifierList();

        if(tok.tokenType==T.COLON)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting colon", scanner.line);

        type();
    }

    public void identifierList()throws Exception{
        System.out.println("Identifier List");
        
        if(tok.tokenType==T.IDENTIFIER)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting ID", scanner.line);

        //more ids
        while(tok.tokenType==T.COMMA){
            tok = scanner.nextToken();
            if(tok.tokenType==T.IDENTIFIER)
                tok = scanner.nextToken();
            else
                scanner.setError("Expecting ID", scanner.line);
        }
        
    }

    public void type()throws Exception{
        System.out.println("Type");

        if(tok.tokenType==T.INTEGER)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting integer", scanner.line);
    }

    public void subprogramDeclarations()throws Exception{
        System.out.println("Subprogram Declarations");

        //subprogram_declaration ; subprogram_declarations | ε
        while(tok.tokenType==T.PROCEDURE){
            subprogramDeclaration();

            if(tok.tokenType==T.SEMI)
                tok = scanner.nextToken();
            else
                scanner.setError("Expecting semi-colon", scanner.line);

            subprogramDeclarations();
        }
    }

    public void subprogramDeclaration()throws Exception{
        System.out.println("Subprogram Declaration");

        //subprogram_head variableDeclarations compound_statement
        subprogramHead();

        variableDeclarations();

        compoundStatement();
    }

    public void subprogramHead()throws Exception{
        System.out.println("Subprogram Head");

        //procedure id arguments ;
        if(tok.tokenType==T.PROCEDURE)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Procedure", scanner.line);

        if(tok.tokenType==T.IDENTIFIER)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting ID", scanner.line);
        
        arguments();

        if(tok.tokenType==T.SEMI)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Semi-Colon", scanner.line);
    }

    public void arguments()throws Exception{
        System.out.println("Arguments");

        //( parameter_list)
        if(tok.tokenType==T.LPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Left Paren", scanner.line);

        parameterList();

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Right Paren", scanner.line);
    }

    public void parameterList()throws Exception{
        System.out.println("Parameter List");

        //identifier_list : type { ; identifier_list : type}
        identifierList();

        if(tok.tokenType==T.COLON)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Colon", scanner.line);

        type();

        while(tok.tokenType==T.SEMI){
            tok = scanner.nextToken();
            identifierList();

            if(tok.tokenType==T.COLON)
                tok=scanner.nextToken();
            else
                scanner.setError("Expecting Colon", scanner.line);
            
            type();
        }

    }

    public void compoundStatement()throws Exception{
        System.out.println("Compound Statement");

        //begin <statement_list> end
        if(tok.tokenType==T.BEGIN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Begin", scanner.line);
        
        statementList();
        System.out.println(tok.tokenType);
        if(tok.tokenType==T.END)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting End", scanner.line);
            
    }

    public void statementList()throws Exception{
        System.out.println("Statement List");

        statement();

        while(tok.tokenType==T.SEMI){
            tok = scanner.nextToken();
            statement();
        }
    }

    public void statement() throws Exception{
        System.out.println("Statement");
        //assignment_statement | procedure_statement | compound_statement | if_statement| while_statement | read_statement | write_statement | writeLn_statement |ε
        if(tok.tokenType==T.IDENTIFIER)
            assignmentStatement();
        else if(tok.tokenType==T.CALL)
            procedureStatement();
        else if(tok.tokenType==T.BEGIN)
            compoundStatement();
        else if(tok.tokenType==T.IF)
            ifStatement();
        else if(tok.tokenType==T.WHILE)
            whileStatement();
        else if(tok.tokenType==T.READ)
            readStatement();
        else if(tok.tokenType==T.WRITE)
            writeStatement();
        else if(tok.tokenType==T.WRITELN)
            writelnStatement();
    }

    public void assignmentStatement()throws Exception{
        System.out.println("Assignment Statement");
        //id assignop expression

        if(tok.tokenType==T.IDENTIFIER)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting ID", scanner.line);
        
        if(tok.tokenType==T.ASSIGN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting AssignOp", scanner.line);
        
        expression();
    }

    public void expression()throws Exception{
        System.out.println("Expression");

        //simple_expression [ relop simple_expression]
        simpleExpression();
        if(tok.tokenType==T.EQUAL || tok.tokenType==T.LT || tok.tokenType==T.LE || tok.tokenType==T.GT || tok.tokenType==T.NE || tok.tokenType==T.MOD || tok.tokenType==T.DIV || tok.tokenType==T.TIMES || tok.tokenType==T.PLUS || tok.tokenType==T.MINUS){
            tok = scanner.nextToken();
            simpleExpression();
        }
    }

    public void simpleExpression()throws Exception{
        System.out.println("Simple Expression");

        //[-]term {addop term}
        if(tok.tokenType==T.MINUS)
            tok = scanner.nextToken();

        term();

        while(tok.tokenType==T.PLUS){
            tok = scanner.nextToken();
            term();
        }
    }

    public void term()throws Exception{
        System.out.println("Term");

        //factor {mulop factor }
        factor();

        while(tok.tokenType==T.TIMES){
            tok = scanner.nextToken();
            factor();
        }
    }

    public void factor()throws Exception{
        System.out.println("Factor");
        //id | num | true | false | (expression) | not factor
=        if(tok.tokenType==T.IDENTIFIER)
            tok = scanner.nextToken();
        else if(tok.tokenType==T.NUMBER)
            tok = scanner.nextToken();
        else if(tok.tokenType==T.BOOL)
            tok = scanner.nextToken();
        else if(tok.tokenType==T.LPAREN){
            tok = scanner.nextToken();
            expression();
            if(tok.tokenType==T.RPAREN)
                tok = scanner.nextToken();
            else
                scanner.setError("Expecting RParen", scanner.line);
        }
        else if(tok.tokenType==T.NOT){
            tok = scanner.nextToken();
            factor();
        }
        else
            scanner.setError("Expecting ID or Num or Bool or (Expression) or Not Factor", scanner.line);
    }

    public void procedureStatement()throws Exception{
        System.out.println("Procedure Statement");
        //call id (expression_list)
        if(tok.tokenType==T.CALL)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Call", scanner.line);

        if(tok.tokenType==T.IDENTIFIER)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting ID", scanner.line);

        if(tok.tokenType==T.LPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting LParen", scanner.line);

        expressionList();

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);
    }

    public void expressionList()throws Exception{
        System.out.println("Expression List");

        //expression { , expression }
        expression();
        while(tok.tokenType==T.COMMA){
            tok = scanner.nextToken();
            expression();
        }
    }

    public void ifStatement()throws Exception{
        System.out.println("If Statement");
        //if expression then statement [else statement]

        if(tok.tokenType==T.IF)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting IF", scanner.line);

        expression();

        if(tok.tokenType==T.THEN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Then", scanner.line);

        statement();

        if(tok.tokenType==T.ELSE){
            tok = scanner.nextToken();
            statement();
        }
    }

    public void whileStatement()throws Exception{
        System.out.println("While Statement");
        //while expression do statement

        if(tok.tokenType==T.WHILE)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting While", scanner.line);
        
        expression();

        if(tok.tokenType==T.DO)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Do", scanner.line);
        
        statement();
    }

    public void readStatement()throws Exception{
        System.out.println("Read Statement");
        //read ( input_list)

        if(tok.tokenType==T.READ)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Read", scanner.line);

        if(tok.tokenType==T.LPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting LParen", scanner.line);

        inputList();

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);
    }

    public void inputList()throws Exception{
        System.out.println("Input List");
        //id {,id}
        //same as identifier list??
        identifierList();
    }

    public void writeStatement()throws Exception{
        System.out.println("Write Statement");
        //write(output_item);

        if(tok.tokenType==T.WRITE)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Write", scanner.line);

        if(tok.tokenType==T.LPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting LParen", scanner.line);

        outputItem();
        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);
        
    }

    public void outputItem()throws Exception{
        System.out.println("Output Item");
        //string | expression
        if(tok.tokenType==T.STRING)
            tok = scanner.nextToken();
        else
            expression();
    }
    
    public void writelnStatement()throws Exception{
        System.out.println("Writeln Statement");

        //writeln(output_item)
        if(tok.tokenType==T.WRITELN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Writeln", scanner.line);
        
        if(tok.tokenType==T.LPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting LParen", scanner.line);
        
        outputItem();

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);

        if(tok.tokenType==T.SEMI)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Semicolon", scanner.line);

    }

    public static void main(String[] args)throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("Enter filename");
        String filen = input.next();
        Parser p = new Parser(filen);
        p.tok = p.scanner.nextToken(); //p.scope??
        p.parse();
        input.close();

        //write sym table for debugging

    }
}