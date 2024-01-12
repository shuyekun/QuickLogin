package tools.quicklogin.support;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
public class CommandFilter implements Filter {
    String[] text = {"/login","/l","/register","/reg","/exchangepassword"};
    @Override
    public boolean isLoggable(LogRecord record) {
        String message = record.getMessage();
        for (String s : text){
            if(message.contains(s)){
                return false;
            }
        }
        return true;
    }
}
