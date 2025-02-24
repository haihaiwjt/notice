package src.main.java.com.notice.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//常用的方法集合在一个类中
public class CommonUtil {

    // 将字符串时间转换为localtime格式
    public static LocalTime stringTimeToLocalTime(String sTime) {
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(sTime);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        matcher.find();
        return LocalTime.parse(matcher.group(), dateFormatter);
    }

    // 将空格转换为时间格式
    public static String changeToTimePattern(String inputString) {
        String tempInput = inputString.trim();
        String oneSpaceInput = tempInput.replaceAll("\\s+"," ");
        String[] parts = oneSpaceInput.split(" ");
        return parts[1] + ":" + parts[2] + ":" + parts[3];
    }

    public static String[] findChangeTime(String input) {
        Pattern pattern = Pattern.compile(" \\d{2} \\d{2} \\d{2}");
        Matcher matcher = pattern.matcher(input);
        int i = 0;
        String[] string = {"",""};
        while (matcher.find()) {
            String a = matcher.group();
            string[i] = changeToTimePattern(a);
            i++;
        }
        return string;
    }
}
