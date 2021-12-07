import java.util.Scanner;

public class Interpreter {
    Scanner input;

    Parser p;

    Memory memory;
    int top;
    int sp;
    int pc;

    int numActivationRecords=0;

    class Memory{
        public int[] memoryData;
        public String[] memoryNotes;

        public Memory(){
            memoryData = new int[16];
            memoryNotes = new String[16];
        }

        public void printMemory(){
            for(int i=0; i<memoryData.length; i++){
                System.out.println(i + " " + memoryData[i] + " " + memoryNotes[i]);
            }
        }
    }

    public Interpreter(Parser p){
        input = new Scanner(System.in);
        memory = new Memory();
        top = 16;
        sp = 16;
        pc = 0;
        this.p = p;
    }

    public void runInterpreter(){
        System.out.println();
        System.out.println("Running Interpreter:");
        QuadType q = p.quads.quads[pc];
        while(!q.arg1.equals("END")){ //Run until we hit END quad
            //q.printQuad();
            if(q.op.equals("ADD")){
                handleAdd(q);
            }
            else if(q.op.equals("ARG")){
                handleArgument(q);
            }
            else if(q.op.equals("ASSIGN")){
                handleAssignment(q);
            }
            else if(q.op.equals("BR")){
                handleBranch(q);
            }
            else if(q.op.equals("BR0")){
                handleBranchZero(q);
            }
            else if(q.op.equals("CALL")){
                handleCall(q);
            }
            else if(q.op.equals("DIV")){
                handleDiv(q);
            }
            else if(q.op.equals("DCL")){
                handleDeclaration(q);
            }
            else if(q.op.equals("END")){
                handleEnd(q);
            }
            else if(q.op.equals("EQUAL")){
                handleEqual(q);
            }
            else if(q.op.equals("EXIT")){
                handleExit(q);
            }
            else if(q.op.equals("INPUT")){
                handleInput(q);
            }
            else if(q.op.equals("LT")){
                handleLT(q);
            }
            else if(q.op.equals("MINUS")){
                handleMinus(q);
            }
            else if(q.op.equals("MOD")){
                handleMod(q);
            }
            else if(q.op.equals("NE")){
                handleNotEqual(q);
            }
            else if(q.op.equals("PROCDEC")){
                handleProcedure(q);
            }
            else if(q.op.equals("PARAM")){
                handleParameter(q);
            }
            else if(q.op.equals("TIMES")){
                handleTimes(q);
            }
            else if(q.op.equals("WRITE")){
                handleWrite(q);
            }
            else if(q.op.equals("WRITELN")){
                handleWriteln(q);
            }
            // System.out.println("PC"+pc);
            // System.out.println("SP"+sp);
            // System.out.println("TOP"+top);
            // memory.printMemory();
            q = p.quads.quads[pc];
        }
    }

    // public void handleAdd(QuadType q){
    //     boolean arg1NumFlag=false;
    //     boolean arg2NumFlag=false;
    //     if(q.arg1.substring(0,1).equals("*")) //* number val
    //         arg1NumFlag=true;
    //     if(q.arg2.substring(0,1).equals("*")) //* number val
    //         arg2NumFlag=true;
        
    //     int offset=0;
    //     int argcount=0;
    //     int R=0;
    //     int offset2=0;
    //     int S=0;
    //     if(arg1NumFlag==false){
    //         offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
    //         if(numActivationRecords==0)
    //             R=sp-offset;
    //         else{
    //             // argcount = memory.memoryData[sp+3];
    //             // R = sp + 3 + argcount - offset + 1;
    //             R=sp-offset;
    //         }
    //     }

    //     if(arg2NumFlag==false){
    //         offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
    //         if(numActivationRecords==0)
    //             S=sp-offset2;
    //         else
    //             S=sp-offset2;
    //             //S = sp + 3 + argcount - offset2 + 1;
    //     } 

    //     int result;
    //     if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
    //         result = memory.memoryData[R]+memory.memoryData[S];
    //     }
    //     else if(arg1NumFlag==true && arg2NumFlag==false){
    //         result = Integer.parseInt((q.arg1).substring(1))+memory.memoryData[S];
    //     }
    //     else if(arg1NumFlag==false && arg2NumFlag==true){
    //         result = memory.memoryData[R]+Integer.parseInt((q.arg2).substring(1));
    //     }
    //     else{
    //         result = Integer.parseInt((q.arg1).substring(1))+Integer.parseInt((q.arg2).substring(1));
    //     }
    //     //store result
    //     if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
    //         // top--;
    //         // String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
    //         // memory.memoryData[top]=Integer.parseInt(nameSubStr);
    //         // memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
    //         // int T = top;
    //         // memory.memoryData[T]=result;
            
    //         //int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
    //         int T=0;
    //         if(numActivationRecords==0)
    //             T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
    //         else
    //             T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
    //             //T = sp + 3 + argcount - p.symT.symbols[Integer.parseInt(q.result)].offset + 1;
    //         String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
    //         memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
    //         memory.memoryData[T]=result;
    //     }
    //     else{//non temp symT address
    //         // top--;
    //         // String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
    //         // memory.memoryData[top]=Integer.parseInt(nameSubStr);
    //         // memory.memoryNotes[top]="Storage for local "+nameSubStr;
    //         // int T = top;
    //         // memory.memoryData[T]=result;
            
    //         //int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
    //         int T=0;
    //         if(numActivationRecords==0)
    //             T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
    //         else
    //             T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
    //             //T = sp + 3 + argcount - p.symT.symbols[Integer.parseInt(q.result)].offset + 1;
    //         String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
    //         memory.memoryNotes[T]="Storage for local @"+nameSubStr;
    //         memory.memoryData[T]=result;
    //     }
    //     pc++;
    // }
    public void handleAdd(QuadType q){
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(p.symT.symbols[Integer.parseInt(q.arg1)].kind==T.LOCAL || p.symT.symbols[Integer.parseInt(q.arg1)].kind==T.TEMP)
                R=sp-offset;
            else{//T.PARM
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(p.symT.symbols[Integer.parseInt(q.arg2)].kind==T.LOCAL || p.symT.symbols[Integer.parseInt(q.arg2)].kind==T.TEMP)
                S=sp-offset2;
            else{//T.PARM
                argcount = memory.memoryData[sp+3];
                S = sp + 3 + argcount - offset2 + 1;
            }
        } 

        int result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]+memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))+memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]+Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))+Integer.parseInt((q.arg2).substring(1));
        }
        //store result
        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
            int T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.LOCAL){//non temp symT address
            int T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for local @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else{//T.PARM
            int T=0;
            T = sp + 3 + argcount - p.symT.symbols[Integer.parseInt(q.result)].offset + 1;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            //memory.memoryNotes[T]="Storage for parmeter @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        pc++;
    }

    public void handleArgument(QuadType q){
        String name = p.symT.symbols[Integer.parseInt(q.result)].name;
        int address = sp-p.symT.symbols[Integer.parseInt(q.result)].offset;
        int val = memory.memoryData[address];
        top--;
        memory.memoryData[top]=val;
        memory.memoryNotes[top]="Param "+name;
        pc++;
    }

    // public void handleAssignment(QuadType q){
    //     int offset = p.symT.symbols[Integer.parseInt(q.result)].offset;
    //     int T = sp - offset;
        
    //     if(q.arg1.substring(0,1).equals("*")){
    //         memory.memoryData[T]=Integer.parseInt(q.arg1.substring(1));
    //     }
    //     else{
    //         //find address of other var and assign it's value
    //         int offset2 = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            
    //         //int W = sp - offset2;
    //         int W =0;
    //         int argcount=0;
    //         if(numActivationRecords==0)
    //             W=sp-offset2;
    //         else{
    //             W= sp + 3 + argcount - offset2 + 1;
    //         }
    //         memory.memoryData[T]=memory.memoryData[W];
    //     }
    //     pc++;
    // }

    public void handleAssignment(QuadType q){
        boolean arg1NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(p.symT.symbols[Integer.parseInt(q.arg1)].kind==T.LOCAL || p.symT.symbols[Integer.parseInt(q.arg1)].kind==T.TEMP)
                R=sp-offset;
            else{//T.PARM
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        int result;
        if(arg1NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R];
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1));
        }
        //store result
        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
            int T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.LOCAL){//non temp symT address
            int T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryNotes[T]="Storage for local @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else{//T.PARM
            int T=0;
            T = sp + 3 + argcount - p.symT.symbols[Integer.parseInt(q.result)].offset + 1;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            //memory.memoryNotes[T]="Storage for parmeter @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        pc++;
    }

    public void handleBranch(QuadType q){
        pc=Integer.parseInt(q.result);
    }

    public void handleBranchZero(QuadType q){
        //arg1
        //if(p.symT.symbols[Integer.parseInt(q.arg1)].kind == T.TEMP){//temp
            int offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            //int T = sp - offset;
            int T = top;
            // System.out.println("Debugging"+memory.memoryData[T]);
            // System.out.println("T"+T);            
            if(memory.memoryData[T]==1)
                pc++;
            else
                pc=Integer.parseInt(q.result);
        // }
        // else{//non temp
        //     System.out.println("Not sure what to do");
        //     System.exit(0);
        // }
    }

    public void handleCall(QuadType q){
        top--;
        memory.memoryData[top]=Integer.parseInt(q.arg2);
        memory.memoryNotes[top]="arg count";
        top--;
        memory.memoryData[top]=pc+1;
        memory.memoryNotes[top]="return address";
        top--;
        memory.memoryData[top]=-999;
        memory.memoryNotes[top]="return value";
        top--;
        memory.memoryData[top]=sp;
        memory.memoryNotes[top]="old sp";
        int startAddress = p.symT.symbols[Integer.parseInt(q.arg1)].start;
        pc=startAddress;
        numActivationRecords++;
        // System.out.println("pc"+pc);
        // System.out.println("sp"+sp);
        // System.out.println("top"+top);
    }
    
    public void handleDeclaration(QuadType q){
        int temp = sp-p.symT.symbols[Integer.parseInt(q.result)].offset;
        memory.memoryNotes[temp]="storage for "+p.symT.symbols[Integer.parseInt(q.result)].name;
        top=temp;
        pc++;
    }

    public void handleDiv(QuadType q){
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        int result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]/memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))/memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]/Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))/Integer.parseInt((q.arg2).substring(1));
        }
        //store result
        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
            // top--;
            // String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            // memory.memoryData[top]=Integer.parseInt(nameSubStr);
            // memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            // int T = top;
            // memory.memoryData[T]=result;
            int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else{//non temp symT address
            // top--;
            // String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            // memory.memoryData[top]=Integer.parseInt(nameSubStr);
            // memory.memoryNotes[top]="Storage for local "+nameSubStr;
            // int T = top;
            // memory.memoryData[T]=result;
            int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        pc++;
    }

    public void handleEnd(QuadType q){
        // System.out.println("pc"+pc);
        // System.out.println("sp"+sp);
        // System.out.println("top"+top);
        System.exit(0);
    }

    public void handleEqual(QuadType q){
        // int offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
        // int argcount = memory.memoryData[sp+3];
        // int R = sp + 3 + argcount - offset + 1;

        // int offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
        // int S = sp + 3 + argcount - offset2 + 1;
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        boolean result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]==memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))==memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]==Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))==Integer.parseInt((q.arg2).substring(1));
        }

        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){
            top--;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        else{
            top--;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for local "+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        pc++;
    }

    public void handleExit(QuadType q){
        //find return address
        top = sp + 2;
        int RA = memory.memoryData[top];
        sp=memory.memoryData[sp];
        top++;
        int argcount = memory.memoryData[top];
        for(int i=1; i<=argcount+1; i++){
            top++;
        }
        pc=RA;
        numActivationRecords--;
    }

    public void handleInput(QuadType q){
        int address = sp-p.symT.symbols[Integer.parseInt(q.result)].offset;
        System.out.println("Enter input");
        String inputStr = input.next();
        int inputInt=0;
        try{
            inputInt = Integer.parseInt(inputStr);
        }
        catch(Exception e){
            System.out.println("Invalid Input :[ exiting");
            System.exit(0);
        }
        memory.memoryData[address]=inputInt;
        pc++;
    }

    public void handleGE(QuadType q){
        // int offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
        // int argcount = memory.memoryData[sp+3];
        // int R = sp + 3 + argcount - offset + 1;

        // int offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
        // int S = sp + 3 + argcount - offset2 + 1;
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        boolean result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]>=memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))>=memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]>=Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))>=Integer.parseInt((q.arg2).substring(1));
        }

        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){
            top--;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        else{
            top--;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for local "+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        pc++;
    }

    public void handleGT(QuadType q){
        // int offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
        // int argcount = memory.memoryData[sp+3];
        // int R = sp + 3 + argcount - offset + 1;

        // int offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
        // int S = sp + 3 + argcount - offset2 + 1;
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        boolean result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]>memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))>memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]>Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))>Integer.parseInt((q.arg2).substring(1));
        }

        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){
            top--;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        else{
            top--;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for local "+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        pc++;
    }

    public void handleLE(QuadType q){
        // int offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
        // int argcount = memory.memoryData[sp+3];
        // int R = sp + 3 + argcount - offset + 1;

        // int offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
        // int S = sp + 3 + argcount - offset2 + 1;
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        boolean result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]<=memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))<=memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]<=Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))<=Integer.parseInt((q.arg2).substring(1));
        }

        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){
            top--;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        else{
            top--;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for local "+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        pc++;
    }

    public void handleLT(QuadType q){
        // int offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
        // int argcount = memory.memoryData[sp+3];
        // int R = sp + 3 + argcount - offset + 1;

        // int offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
        // int S = sp + 3 + argcount - offset2 + 1;
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        boolean result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]<memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))<memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]<Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))<Integer.parseInt((q.arg2).substring(1));
        }

        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){
            top--;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        else{
            top--;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for local "+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        pc++;
    }

    public void handleMinus(QuadType q){
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        int result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]-memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))-memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]-Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))-Integer.parseInt((q.arg2).substring(1));
        }
        //store result
        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
            // top--;
            // String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            // memory.memoryData[top]=Integer.parseInt(nameSubStr);
            // memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            // int T = top;
            // memory.memoryData[T]=result;
            int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else{//non temp symT address
            // top--;
            // String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            // memory.memoryData[top]=Integer.parseInt(nameSubStr);
            // memory.memoryNotes[top]="Storage for local "+nameSubStr;
            // int T = top;
            // memory.memoryData[T]=result;
            int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for local @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        pc++;
    }

    public void handleMod(QuadType q){
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        int result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]%memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))%memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]%Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))%Integer.parseInt((q.arg2).substring(1));
        }
        //store result
        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
            // top--;
            // String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            // memory.memoryData[top]=Integer.parseInt(nameSubStr);
            // memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            // int T = top;
            // memory.memoryData[T]=result;
            int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else{//non temp symT address
            // top--;
            // String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            // memory.memoryData[top]=Integer.parseInt(nameSubStr);
            // memory.memoryNotes[top]="Storage for local "+nameSubStr;
            // int T = top;
            // memory.memoryData[T]=result;
            int T = sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for local @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        pc++;
    }

    public void handleNotEqual(QuadType q){
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(numActivationRecords==0)
                R=sp-offset;
            else{
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(numActivationRecords==0)
                S=sp-offset2;
            else
                S = sp + 3 + argcount - offset2 + 1;
        } 

        boolean result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]!=memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))!=memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]!=Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))!=Integer.parseInt((q.arg2).substring(1));
        }

        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){
            top--;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for temp @"+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        else{
            top--;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryData[top]=Integer.parseInt(nameSubStr);
            memory.memoryNotes[top]="Storage for local "+nameSubStr;
            int T = top;
            if(result)
                memory.memoryData[T]=1;
            else
                memory.memoryData[T]=0;
        }
        pc++;
    }

    public void handleParameter(QuadType q){
        pc++;
    }

    public void handleProcedure(QuadType q){
        sp=top;
        pc++;
    }

    public void handleTimes(QuadType q){
        boolean arg1NumFlag=false;
        boolean arg2NumFlag=false;
        if(q.arg1.substring(0,1).equals("*")) //* number val
            arg1NumFlag=true;
        if(q.arg2.substring(0,1).equals("*")) //* number val
            arg2NumFlag=true;
        
        int offset=0;
        int argcount=0;
        int R=0;
        int offset2=0;
        int S=0;
        if(arg1NumFlag==false){
            offset = p.symT.symbols[Integer.parseInt(q.arg1)].offset;
            if(p.symT.symbols[Integer.parseInt(q.arg1)].kind==T.LOCAL || p.symT.symbols[Integer.parseInt(q.arg1)].kind==T.TEMP)
                R=sp-offset;
            else{//T.PARM
                argcount = memory.memoryData[sp+3];
                R = sp + 3 + argcount - offset + 1;
            }
        }

        if(arg2NumFlag==false){
            offset2 = p.symT.symbols[Integer.parseInt(q.arg2)].offset;
            if(p.symT.symbols[Integer.parseInt(q.arg2)].kind==T.LOCAL || p.symT.symbols[Integer.parseInt(q.arg2)].kind==T.TEMP)
                S=sp-offset2;
            else{//T.PARM
                argcount = memory.memoryData[sp+3];
                S = sp + 3 + argcount - offset2 + 1;
            }
        } 

        int result;
        if(arg1NumFlag==false && arg2NumFlag==false){ //neither are number (*1)
            result = memory.memoryData[R]*memory.memoryData[S];
        }
        else if(arg1NumFlag==true && arg2NumFlag==false){
            result = Integer.parseInt((q.arg1).substring(1))*memory.memoryData[S];
        }
        else if(arg1NumFlag==false && arg2NumFlag==true){
            result = memory.memoryData[R]*Integer.parseInt((q.arg2).substring(1));
        }
        else{
            result = Integer.parseInt((q.arg1).substring(1))*Integer.parseInt((q.arg2).substring(1));
        }
        //store result
        if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.TEMP){ //temp symT address
            int T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = (p.symT.symbols[Integer.parseInt(q.result)].name).substring(2);
            memory.memoryNotes[T]="Storage for temp @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else if(p.symT.symbols[Integer.parseInt(q.result)].kind==T.LOCAL){//non temp symT address
            int T =sp - p.symT.symbols[Integer.parseInt(q.result)].offset;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            memory.memoryNotes[T]="Storage for local @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        else{//T.PARM
            int T=0;
            T = sp + 3 + argcount - p.symT.symbols[Integer.parseInt(q.result)].offset + 1;
            String nameSubStr = p.symT.symbols[Integer.parseInt(q.result)].name;
            //memory.memoryNotes[T]="Storage for parmeter @"+nameSubStr;
            memory.memoryData[T]=result;
        }
        pc++;
    }

    public void handleWrite(QuadType q){
        if(Integer.parseInt(q.result)>=1000){//string
            System.out.print(p.symT.symbols[Integer.parseInt(q.result)].name);
        }
        else{//var in memory
            int offset = p.symT.symbols[Integer.parseInt(q.result)].offset;
            int T = sp - offset;
            System.out.print(memory.memoryData[T]);
        }
        pc++;
    }

    public void handleWriteln(QuadType q){
        if(Integer.parseInt(q.result)>=1000){//string
            System.out.println(p.symT.symbols[Integer.parseInt(q.result)].name);
        }
        else{//var in memory
            int offset = p.symT.symbols[Integer.parseInt(q.result)].offset;
            int T = sp - offset;
            System.out.println(memory.memoryData[T]);
        }
        pc++;
    }

    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("Enter filename");
        String filen = input.next();
        Parser p = new Parser(filen);
        p.tok = p.scanner.nextToken(); //p.scope??
        p.parse();
        p.writeSymbolTable();
        p.quads.writeQuadsTable();
        Interpreter interp = new Interpreter(p);
        interp.runInterpreter();
        input.close();
    }
}
