package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil
{
    public boolean phoneCheck(String phone){
        Pattern pattern = Pattern.compile("^\\+[0-9]{11}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }

    public boolean priceCheck(String price){
        Pattern pattern = Pattern.compile("^(?!0.*$)(?!,$)([0-9]{1,9}(\\.[0-9]{2})?)$");
        Matcher matcher = pattern.matcher(price);
        return matcher.find();
    }

    public boolean countCheck(String price){
        Pattern pattern = Pattern.compile("^[0-9]*[.]?[0-9]+$");
        Matcher matcher = pattern.matcher(price);
        return matcher.find();
    }

    public boolean passportCheck(String passport){
        Pattern pattern = Pattern.compile("^([0-9]{10})$");
        Matcher matcher = pattern.matcher(passport);
        return matcher.find();
    }

    public boolean dateSheetNumberCheck(String dateSheetNumber){
        Pattern pattern = Pattern.compile("^([1-9]{1}[0-9]{5})$");
        Matcher matcher = pattern.matcher(dateSheetNumber);
        return matcher.find();
    }

    public boolean fullNameCheck(String fullName){
        Pattern pattern = Pattern.compile("([А-ЯЁ][а-яё]+[\\-\\s]?){3,}");
        Matcher matcher = pattern.matcher(fullName);
        return matcher.find();
    }

    public boolean stateNumberCheck(String stateNumber){
        Pattern pattern = Pattern.compile("[А-Я]\\d{3}[А-Я]{2}\\d{2,3}");
        Matcher matcher = pattern.matcher(stateNumber);
        return matcher.find();
    }

    public boolean countryCheck(String country){
        Pattern pattern = Pattern.compile("^[А-Я]{1}[а-я]{1,35}$");
        Matcher matcher = pattern.matcher(country);
        return matcher.find();
    }
}
