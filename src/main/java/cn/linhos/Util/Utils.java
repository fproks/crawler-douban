package cn.linhos.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fprok on 2017/12/19.
 */
public class Utils {

    private static final String URLRegex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    private  static  final  String floatRegex ="\\d+[.]\\d+";

    /**
     * 判断是否为url
     *
     * @param url
     * @return
     */
    public static Boolean isUrl(String url) {
        if (url.matches(URLRegex))
            return true;
        else return false;
    }

    public static float getFloatFormString(String str){
        Pattern pattern =Pattern.compile(floatRegex);
        Matcher matcher =pattern.matcher(str);

        if(matcher.find())
            return Float.parseFloat(matcher.group());
        else  return 0;
    }
}
