package Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String PASSWORD_REGEX = "^[a-z0-9_-]{3,16}$";
    private static final String USERNAME_REGEX = "^[a-z0-9_-]{3,16}$";

    public static boolean matchEmail(String email) {
        Pattern p = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(email);
        return matcher.find();
    }
    
    public static boolean matchUsername(String username){
        Pattern p = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = p.matcher(username);
        return matcher.find();
    }
    
    public static boolean matchPassword(String password){
        Pattern p = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = p.matcher(password);
        return matcher.find();
    }
}
