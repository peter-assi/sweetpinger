package appchk;

public class Stuff {

    public final static String FLASH="flash";
    public final static String URL="url";
    public static final String OK = "OK";
    public static final String NOK = "NOK";
    public static final String USER = "user";
    public static final String LIST = "list";
    public static final String ID = "id";
    public static final String PINGQ = "ping";
    public static final String PINGROUTE = "/ping";
    public static final String DELETEROUTE = "/delete";
    public static final String MAILFROM = "noreply@mediachk.net";
    public static final String MAILPRE = "[sweetpinger]";
    public static final String MAILNAME = "Sweetpinger";
    public static final String TEST = "test";

    
    public static boolean empty(String newUrl) {
        if( newUrl == null || newUrl.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }
    
    
}
