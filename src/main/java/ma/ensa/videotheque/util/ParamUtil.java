package ma.ensa.videotheque.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ParamUtil {
    public static Long toLong(String s) {
        try { return (s == null || s.isBlank()) ? null : Long.valueOf(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }
    public static Integer toInteger(String s) {
        try { return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }
    public static int toInt(String s, int defaut) {
        Integer v = toInteger(s);
        return v == null ? defaut : v;
    }
    public static String encode(String s) {
        return s == null ? "" : URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}