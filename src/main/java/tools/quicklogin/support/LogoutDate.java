package tools.quicklogin.support;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogoutDate extends Date {
    //日期类
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return simpleDateFormat.format(this);
    }
}
