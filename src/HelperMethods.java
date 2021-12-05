public class HelperMethods {
    public String getFieldByValue(int value){
        String fieldStr;
        switch (value) {
            //T.EQUAL T.LT || T.LE || T.GT || T.NE || T.MOD || T.DIV || ==T.TIMES || T.PLUS || =T.MINUS
            case T.EQUAL: fieldStr = "EQUAL";
                     break;
            case T.LT:  fieldStr = "LT";
                     break;
            case T.LE:  fieldStr = "LE";
                     break;
            case T.GT:  fieldStr = "GT";
                     break;
            case T.NE:  fieldStr = "NE";
                     break;
            case T.MOD:  fieldStr = "MOD";
                     break;
            case T.DIV:  fieldStr = "DIV";
                     break;
            case T.TIMES:  fieldStr = "TIMES";
                     break;
            case T.PLUS:  fieldStr = "PLUS";
                     break;
            case T.MINUS:  fieldStr = "MINUS";
                     break;
            default: fieldStr = "";
                     break;
        }
        return fieldStr;
    }
}
