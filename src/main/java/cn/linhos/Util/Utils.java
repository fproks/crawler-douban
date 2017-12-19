package cn.linhos.Util;

public class Utils {

    private static final String URLRegex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 判断是否为url
     * @param url
     * @return
     */
    public static Boolean isUrl(String url) {
        if (url.matches(URLRegex))
            return true;
        else return false;
    }
}
