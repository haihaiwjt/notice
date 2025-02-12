package src.main.java.com.notice.utils;

import src.main.java.com.notice.model.Operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputMatch {
    private static final String ADDPATTERN = "add \\d{2} \\d{2} \\d{2}";
    private static final  String DELETEPATTERN = "^delete \\d{2} \\d{2} \\d{2}$";
    private static final  String CHANGEPATTERN = "^change \\d{2} \\d{2} \\d{2} to \\d{2} \\d{2} \\d{2}$";
    private static final  String EXITPATTERN = "^exit$";
    private static final String LISTPATTERN = "^list$";
    private static final String HELPPATTERN = "^help$";
    
    public static Operation matchOperation(String input) {
        Pattern pattern;

        pattern = Pattern.compile(ADDPATTERN);
        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()){
            return Operation.ADDNOTICE;
        }

        pattern = Pattern.compile(DELETEPATTERN);
        matcher = pattern.matcher(input);
        if(matcher.matches()){
            return Operation.DELETENOTICE;
        }

        pattern = Pattern.compile(CHANGEPATTERN);
        matcher = pattern.matcher(input);
        if(matcher.matches()){
            return Operation.CHANGENOTICE;
        }

        pattern = Pattern.compile(EXITPATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        if(matcher.matches()){
            return Operation.EXIT;
        }

        pattern = Pattern.compile(LISTPATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        if(matcher.matches()){
            return Operation.LIST;
        }

        pattern = Pattern.compile(HELPPATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(input);
        if(matcher.matches()){
            return Operation.HELP;
        }

        return Operation.UNKNOWNOPERATION;
    }
}
