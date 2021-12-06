import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;


public class Parser{
    Scan scanner;
    SymbolTable symT;
    Quads quads;

    String filename;
    Token tok;

    PrintWriter pw; // for symboltable_output.txt
    int[] locals = new int[20]; //index by the scope number (for interpreter)

    HelperMethods hm;

    public Parser(String filename)throws Exception{
        symT = new SymbolTable();
        scanner = new Scan(filename, symT);
        quads = new Quads();
        pw = new PrintWriter(new File("symbolTableOutput.txt"));  
        hm = new HelperMethods();  
    }

    public void writeSymbolTable()throws Exception{
        pw.write("Symbol Table:");
        pw.println("");
        pw.write("Index\t" + "Name\t" + "Scope\t"+ "tokenType\t"+ "Declared\t" + "NumArgs\t" + "Kind\t" + "Start\t" + "Offset\t");
        int i=0;
        while(symT.symbols[i]!=null){
            pw.println();
            pw.write(i + "\t\t" + symT.symbols[i].name + "\t" + symT.symbols[i].scope + "\t\t" + symT.symbols[i].tokenType + "\t\t\t" + symT.symbols[i].declared + "\t\t" + symT.symbols[i].numArgs + "\t\t" + symT.symbols[i].kind + "\t\t" + symT.symbols[i].start + "\t\t" + symT.symbols[i].offset);
            i++;
        }
        pw.println();
        pw.write("String Table:");
        i=1000;
        while(symT.symbols[i]!=null){
            pw.println();
            pw.write("Index: " + i + " String: " + symT.symbols[i].name + " " + symT.symbols[i].scope);
            i++;
        }
        pw.close();
    }

    public void parse()throws Exception{
        System.out.println("Program"); //for debugging and tracing
        if(tok.tokenType==T.PROGRAM){
            tok = scanner.nextToken();
            symT.symbols[symT.symbolIndex-1].tokenType=T.PROGRAM;
            symT.symbols[symT.symbolIndex-1].kind=T.PROGRAM;
            symT.symbols[symT.symbolIndex-1].declared=true;
        }
        else
            scanner.setError("Expecting program",scanner.line);
        
        if(tok.tokenType==T.IDENTIFIER)
            tok=scanner.nextToken(); //scope here??
        else
            scanner.setError("Expecting program name", scanner.line);
        
        if(tok.tokenType==T.SEMI)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting semi", scanner.line);

        variableDeclarations();

        int loc1 = quads.getQuad();
        quads.insertQuad("BR", "-", "-", 0+"");
        
        subprogramDeclarations();

        scanner.procedureNum=0; //Reset scope since we are entering main block

        int loc2 = quads.getQuad();
        quads.setResult(loc1, loc2+"");

        compoundStatement();

        if(tok.tokenType==T.PERIOD){
            quads.insertQuad("End","-","-","-");
            if(scanner.error=="")//no errors during parse
                System.out.println("Success");
            else
                System.out.println("Unsuccessful Parse/Scan");
        }
        else
            scanner.setError("Expecting Period", scanner.line);
        
        scanner.pw.close(); //Successful parse, close the printwriter.
    }

    public void variableDeclarations()throws Exception{
        System.out.println("Variable Declarations");

        if(tok.tokenType==T.VAR){
            int temp = symT.symbolIndex;
            tok = scanner.nextToken();
            Semantics s = new Semantics();
            variableDeclaration(s);
            
            for(int j=0; j<s.count; j++){
                quads.insertQuad("DCL", "-", "-", s.start+j+"");
                symT.symbols[s.start+j].tokenType=s.type;
                symT.symbols[s.start+j].kind=T.LOCAL;
                symT.symbols[s.start+j].declared=true;
                locals[scanner.procedureNum]++;
                symT.symbols[s.start+j].offset=locals[scanner.procedureNum];
            }

            if(tok.tokenType==T.SEMI)
                tok = scanner.nextToken();
            else
                scanner.setError("Expecting semi", scanner.line);
    
            temp = symT.symbolIndex;
            while(tok.tokenType==T.IDENTIFIER){
                variableDeclaration(s);
                for(int j=0; j<s.count; j++){
                    symT.symbols[temp+j].tokenType=s.type;
                    symT.symbols[temp+j].kind=T.LOCAL;
                    symT.symbols[s.start+j].declared=true;
                    locals[scanner.procedureNum]++;
                    symT.symbols[s.start+j].offset=locals[scanner.procedureNum];
                }
                
                if(tok.tokenType==T.SEMI)
                    tok = scanner.nextToken();
                else
                    scanner.setError("Expecting semi", scanner.line);

            }
        }
    }
    
    public void variableDeclaration(Semantics s)throws Exception{
        System.out.println("Variable Declaration");

        identifierList(s,false);

        if(tok.tokenType==T.COLON)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting colon", scanner.line);

        type(s);
    }

    public void identifierList(Semantics s, boolean readFlag)throws Exception{
        System.out.println("Identifier List");

        s.count=0;
        if(tok.tokenType==T.IDENTIFIER){
            s.start=symT.symbolIndex-1;
            s.count++;
            if(readFlag){
                quads.insertQuad("INPUT", "-", "-", tok.value+"");
            }
            tok = scanner.nextToken();
        }
        else
            scanner.setError("Expecting ID", scanner.line);

        while(tok.tokenType==T.COMMA){
            tok = scanner.nextToken();
            if(tok.tokenType==T.IDENTIFIER){
                s.count++;
                if(readFlag){
                    quads.insertQuad("INPUT", "", "", tok.value+"");
                }
                tok = scanner.nextToken();
            }
            else
                scanner.setError("Expecting ID", scanner.line);
        }
    }

    public void type(Semantics s)throws Exception{
        System.out.println("Type");

        if(tok.tokenType==T.INTEGER){
            tok = scanner.nextToken();
            s.type=T.INTEGER;
        }
        else
            scanner.setError("Expecting integer", scanner.line);
    }

    public void subprogramDeclarations()throws Exception{
        System.out.println("Subprogram Declarations");

        //subprogram_declaration ; subprogram_declarations | ε
        if(tok.tokenType==T.PROCEDURE){
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

        scanner.procedureNum++;

        //subprogram_head variableDeclarations compound_statement
        subprogramHead();

        variableDeclarations();

        compoundStatement();
        quads.insertQuad("EXIT", "-", "-", "-");
    }

    public void subprogramHead()throws Exception{
        System.out.println("Subprogram Head");

        //procedure id arguments ;
        int place = symT.symbolIndex;

        if(tok.tokenType==T.PROCEDURE){
            tok = scanner.nextToken();
            symT.symbols[place].scope=0;
            symT.symbols[place].tokenType=T.PROCEDURE;
            symT.symbols[place].kind=T.PROCEDURE;
            symT.symbols[place].start=symT.symbolIndex-1;
            if(symT.symbols[place].declared==false)
                symT.symbols[place].declared=true;
            else
                scanner.setError("Error: Procedure already declared", scanner.line);
            quads.insertQuad("PROCDEC", "-", "-", place+"");
        }
        else
            scanner.setError("Expecting Procedure", scanner.line);

        if(tok.tokenType==T.IDENTIFIER)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting ID", scanner.line);
        
        Semantics s = new Semantics();
        arguments(s, place);

        if(tok.tokenType==T.SEMI)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Semi-Colon", scanner.line);
    }

    public void arguments(Semantics s, int place)throws Exception{
        System.out.println("Arguments");

        //( parameter_list)
        if(tok.tokenType==T.LPAREN){
            tok = scanner.nextToken();
        }
        else
            scanner.setError("Expecting Left Paren", scanner.line);

        parameterList(s, place);

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Right Paren", scanner.line);
    }

    public void parameterList(Semantics s, int place)throws Exception{
        System.out.println("Parameter List");

        //identifier_list : type { ; identifier_list : type}
        identifierList(s,false);
        
        if(tok.tokenType==T.COLON)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Colon", scanner.line);

        type(s);
        
        while(tok.tokenType==T.SEMI){
            tok = scanner.nextToken();
            identifierList(s,false);

            if(tok.tokenType==T.COLON)
                tok=scanner.nextToken();
            else
                scanner.setError("Expecting Colon", scanner.line);
            
            type(s);
        }
        //s.start--; //Compensating for the LParen in Arguments as opposed to no paren in VarDecs
        for(int j= s.start; j<s.start+s.count; j++){
            symT.symbols[place].numArgs++;
            symT.symbols[j].tokenType=s.type;
            symT.symbols[j].kind=T.PARM;
            symT.symbols[j].offset=symT.symbols[place].numArgs;
            if(symT.symbols[j].declared==false)
                symT.symbols[j].declared=true;
            else
                scanner.setError("Parameter already declared", scanner.line);
            quads.insertQuad("PARAM", "-", "-", j+"");
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
        int place = tok.value;
        if(tok.tokenType==T.IDENTIFIER){
            if(symT.symbols[place].declared==false){ //error not declared
                scanner.setError("Error: ID " + place + " is not declared", scanner.line);
            }
            tok = scanner.nextToken();
        }
        else
            scanner.setError("Expecting ID", scanner.line);
        
        if(tok.tokenType==T.ASSIGN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting AssignOp", scanner.line);
        Semantics s = new Semantics();
        Exp x = new Exp();
        expression(s, x);
        System.out.println(place);
        //not working due to Integer Number comparison
        //if(symT.symbols[place].tokenType!=x.type){
        //     scanner.setError("Error: Type Mismatch! "+symT.symbols[place].tokenType + " " + x.type, scanner.line);
        // }
        //else{
        if(x.number){
            quads.insertQuad("ASSIGN","*"+x.value,"-",place+"");
        }
        else{
            quads.insertQuad("ASSIGN",x.value+"","-",place+"");
        }
        //}
    }

    public void expression(Semantics s, Exp x)throws Exception{
        System.out.println("Expression");

        //simple_expression [ relop simple_expression]
        simpleExpression(s, x);
        if(tok.tokenType==T.EQUAL || tok.tokenType==T.LT || tok.tokenType==T.LE || tok.tokenType==T.GT || tok.tokenType==T.NE || tok.tokenType==T.MOD || tok.tokenType==T.DIV || tok.tokenType==T.TIMES || tok.tokenType==T.PLUS || tok.tokenType==T.MINUS){
            //int opCode = tok.tokenType;
            String opCode = hm.getFieldByValue(tok.tokenType);
            tok = scanner.nextToken();
            Exp w = new Exp();
            simpleExpression(s, w); //w must come back integer or error
            //if(w.number || symT.symbols[w.value].tokenType==T.INTEGER){//W must be a NUM ex. *9 or if it's a ID the ID must be an integer
                int t = symT.getTemp(); //address of temp
                locals[scanner.procedureNum]++;
                symT.symbols[t].offset=locals[scanner.procedureNum];
                symT.symbols[t].tokenType=T.BOOL;
                symT.symbols[t].kind=T.TEMP;
                symT.symbols[t].scope=scanner.procedureNum;
                //handling nums for x and w
                String xVal=x.value+"";
                String wVal=w.value+"";
                if(x.number)
                    xVal="*"+xVal;
                if(w.number)
                    wVal="*"+wVal;
                quads.insertQuad(opCode, xVal, wVal, t+"");
                x.type=T.BOOL;
                x.value=t;
                x.number=false;
            //}
            // else{
            //     scanner.setError("Error: Expecting number in expression()", scanner.line);
            // }
        }
    }

    public void simpleExpression(Semantics s, Exp x)throws Exception{
        System.out.println("Simple Expression");
        int type1;
        int type2;
        Exp y = new Exp();
        Exp z = new Exp();
        int t; //address of a temp

        //[-]term {addop term}
        if(tok.tokenType==T.MINUS)
            tok = scanner.nextToken();

        term(s, y);

        while(tok.tokenType==T.PLUS){
            tok = scanner.nextToken();
            term(s, z);

            type1 = y.type;
            type2 = z.type;
            // if(type1!=type2){//error
            //     scanner.setError("Error, type mismatch ("+type1+" and "+type2+" do not match)", scanner.line);
            // }
            t=symT.getTemp();
            locals[scanner.procedureNum]++;
            symT.symbols[t].offset=locals[scanner.procedureNum];
            symT.symbols[t].tokenType=T.BOOL;
            symT.symbols[t].kind=T.TEMP;
            symT.symbols[t].scope=scanner.procedureNum;
            String yVal=y.value+"";
            String zVal=z.value+"";
            if(y.number)
                yVal="*"+yVal;
            if(z.number)
                zVal="*"+zVal;
            //quads.insertQuad(opCode+"", yVal, zVal, t+"");
            quads.insertQuad("ADD", yVal, zVal, t+"");
            y.type=type1;
            y.number=false;
            y.value=t;
        }
        x.type=y.type;
        x.value=y.value;
        x.number=y.number;
    }

    public void term(Semantics s, Exp x)throws Exception{
        System.out.println("Term");
        int type1;
        int type2;
        Exp y = new Exp();
        Exp z = new Exp();
        int t; //address of a temp

        //factor {mulop factor }
        factor(s, y);

        while(tok.tokenType==T.TIMES){
            String opCode = hm.getFieldByValue(tok.tokenType);
            tok = scanner.nextToken();
            factor(s, z);
            type1=y.type;
            type2=z.type;
            // if(type1!=type2){ //type mismatch
            //     scanner.setError("Error, type mismatch ("+type1+" and "+type2+" do not match)", scanner.line);
            // }
            t=symT.getTemp();
            locals[scanner.procedureNum]++;
            symT.symbols[t].offset=locals[scanner.procedureNum];
            symT.symbols[t].tokenType=T.BOOL;
            symT.symbols[t].kind=T.TEMP;
            symT.symbols[t].scope=scanner.procedureNum;
            String yVal=y.value+"";
            String zVal=z.value+"";
            if(y.number)
                yVal="*"+yVal;
            if(z.number)
                zVal="*"+zVal;
            quads.insertQuad(opCode+"", yVal, zVal, t+"");
            y.type=type1;
            y.number=false;
            y.value=t;
        }
        x.type=y.type;
        x.value=y.value;
        x.number=y.number;
    }

    public void factor(Semantics s, Exp x)throws Exception{
        System.out.println("Factor");
        //id | num | true | false | (expression) | not factor
        if(tok.tokenType==T.IDENTIFIER){
            //check declared
            if(symT.symbols[tok.value].declared==false){ //error not declared
                scanner.setError("Error: ID " + tok.value + " is not declared", scanner.line);
            }
            x.type=symT.symbols[tok.value].tokenType;
            x.value=tok.value;
            x.number=false;
            s.count++;
            tok = scanner.nextToken();
        }
        else if(tok.tokenType==T.NUMBER){
            x.type=tok.tokenType;            
            x.value=tok.value;
            x.number=true;
            tok = scanner.nextToken();
        }
        else if(tok.tokenType==T.BOOL){
            x.type=tok.tokenType;
            x.value=tok.value;
            x.bool=true;
            tok = scanner.nextToken();
        }
        else if(tok.tokenType==T.LPAREN){
            tok = scanner.nextToken();
            expression(s, x);
            if(tok.tokenType==T.RPAREN)
                tok = scanner.nextToken();
            else
                scanner.setError("Expecting RParen", scanner.line);
        }
        else if(tok.tokenType==T.NOT){
            tok = scanner.nextToken();
            factor(s, x);
            if(x.type==T.INTEGER)
                scanner.setError("Error: Cannot apply boolean NOT to Integer "+x.value, scanner.line);
            int t = symT.getTemp();
            locals[scanner.procedureNum]++;
            symT.symbols[t].offset=locals[scanner.procedureNum];
            symT.symbols[t].kind=T.TEMP;
            symT.symbols[t].scope=scanner.procedureNum;
            symT.symbols[t].tokenType=T.BOOL;
            quads.insertQuad("NEG", x+"", "-", t+"");
            x.type=T.BOOL;
            x.value=t;
            x.number=false;
        }
        else
            scanner.setError("Expecting ID or Num or Bool or (Expression) or Not Factor", scanner.line);
    }

    public void procedureStatement()throws Exception{
        System.out.println("Procedure Statement");
        //call id (expression_list)
        int parameterCount=0;
        int start=0;
        if(tok.tokenType==T.CALL)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Call", scanner.line);

        if(tok.tokenType==T.IDENTIFIER){
            start=tok.value;
            //quads.insertQuad("CALL", tok.value+"", 0+"", "-");
            tok = scanner.nextToken();
        }
        else
            scanner.setError("Expecting ID", scanner.line);

        if(tok.tokenType==T.LPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting LParen", scanner.line);
        
        Semantics s = new Semantics();
        s.start=symT.symbolIndex-1;
        s.type=T.INTEGER;
        parameterCount=expressionList(s);

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);

        if(symT.symbols[start].numArgs!=parameterCount){
            System.out.println("start"+start);
            scanner.setError("Error: Num parameters: "+parameterCount+" != num args required:"+symT.symbols[start].numArgs, scanner.line);
        }
        else{
            quads.insertQuad("CALL", start+"", parameterCount+"", "-");
        }
    }

    public int expressionList(Semantics s)throws Exception{
        System.out.println("Expression List");

        //expression { , expression }
        int count = 0;
        Exp x = new Exp();
        expression(s, x);
        quads.insertQuad("ARG", "-", "-", x.value+"");
        // if(x.type!=T.INTEGER){
        //     scanner.setError("Expecting Integer", scanner.line);
        // }
        count++;
        while(tok.tokenType==T.COMMA){
            tok = scanner.nextToken();
            x = new Exp();
            expression(s, x);
            quads.insertQuad("ARG", "-", "-", x.value+"");
            // if(x.type!=T.INTEGER){
            //     scanner.setError("Expecting Integer", scanner.line);
            // }
            count++;
        }
        return count;
    }

    public void ifStatement()throws Exception{
        System.out.println("If Statement");
        //if expression then statement [else statement]
        int loc1, loc2;
        if(tok.tokenType==T.IF)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting IF", scanner.line);

        Semantics s = new Semantics();
        Exp x = new Exp();
        expression(s, x);
        if(x.type!=T.BOOL)
            scanner.setError("Error: Expecting Boolean expression in ifStatement()", scanner.line);
        loc1=quads.getQuad();
        quads.insertQuad("BR0", x.value+"", "-", 0+"");
        if(tok.tokenType==T.THEN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Then", scanner.line);

        statement();

        if(tok.tokenType==T.ELSE){
            tok = scanner.nextToken();
            loc2=quads.getQuad();
            quads.insertQuad("BR", "-", "-", 0+"");
            quads.setResult(loc1, loc2+"");
            statement();
            quads.setResult(loc2, quads.getQuad()+"");
        }
        else
            quads.setResult(loc1, quads.getQuad()+"");
    }

    public void whileStatement()throws Exception{
        System.out.println("While Statement");
        //while expression do statement
        int loc1;
        int loc2;

        if(tok.tokenType==T.WHILE)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting While", scanner.line);
        
        loc1 = quads.getQuad();
        Semantics s = new Semantics();
        Exp x = new Exp();
        expression(s, x);
        if(x.type!=T.BOOL){
            scanner.setError("Error: expecting boolean expression in whileStatement()", scanner.line);
        }
        loc2 = quads.getQuad();
        quads.insertQuad("BR0", x.value+"", "-", 0+"");
        if(tok.tokenType==T.DO)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting Do", scanner.line);
        
        statement();
        quads.insertQuad("BR", "-", "-", loc1+"");
        quads.setResult(loc2, quads.getQuad()+"");
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

        Semantics s = new Semantics();
        identifierList(s,true);

        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);
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
        Exp x = new Exp();
        outputItem(x);
        quads.insertQuad("WRITE", "-", "-", x.value+"");
        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);
        
    }

    public void outputItem(Exp x)throws Exception{
        System.out.println("Output Item");
        //string | expression
        if(tok.tokenType==T.STRING){
            x.bool=false;
            x.number=false;
            x.type=T.STRING;
            x.value=tok.value;
            tok = scanner.nextToken();
        }
        else{
            Semantics s = new Semantics();
            expression(s, x);
        }
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
        Exp x = new Exp();
        outputItem(x);
        quads.insertQuad("WRITELN", "-", "-", x.value+"");
        if(tok.tokenType==T.RPAREN)
            tok = scanner.nextToken();
        else
            scanner.setError("Expecting RParen", scanner.line);

    }

    public static void main(String[] args)throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("Enter filename");
        String filen = input.next();
        Parser p = new Parser(filen);
        p.tok = p.scanner.nextToken(); //p.scope??
        p.parse();
        p.writeSymbolTable();
        p.quads.writeQuadsTable();
        input.close();
    }
}