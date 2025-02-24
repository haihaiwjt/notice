package src.main.java.com.notice.utils;

import src.main.java.com.notice.model.Operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputMatch {
    private static final String ADDPATTERN = "^\\s*add\\s+\\d{2}\\s+\\d{2}\\s+\\d{2}\\s*$";
    private static final  String DELETEPATTERN = "^\\s*delete\\s+\\d{2}\\s+\\d{2}\\s+\\d{2}\\s*$";
    private static final  String CHANGEPATTERN = "^\\s*change\\s+\\d{2}\\s+\\d{2}\\s+\\d{2}\\s+to\\s+\\d{2}\\s+\\d{2}\\s+\\d{2}\\s*$";
    private static final  String EXITPATTERN = "^\\s*exit\\s*$";
    private static final String LISTPATTERN = "^\\s*(list|/l)\\s*$";
    private static final String HELPPATTERN = "^\\s*(help|/help|/h)\\s*$";
    
    public static Operation matchOperation(String input) {
        Pattern pattern;

        pattern = Pattern.compile(ADDPATTERN);
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            return Operation.ADDNOTICE;
        }

        pattern = Pattern.compile(DELETEPATTERN);
        matcher = pattern.matcher(input);
        if(matcher.find()){
            return Operation.DELETENOTICE;
        }

        pattern = Pattern.compile(CHANGEPATTERN);
        matcher = pattern.matcher(input);
        if(matcher.find()){
            return Operation.CHANGENOTICE;
        }

        pattern = Pattern.compile(EXITPATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        if(matcher.find()){
            return Operation.EXIT;
        }

        pattern = Pattern.compile(LISTPATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        if(matcher.find()){
            return Operation.LIST;
        }

        pattern = Pattern.compile(HELPPATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        if(matcher.find()){
            return Operation.HELP;
        }

        return Operation.UNKNOWNOPERATION;
    }
}
