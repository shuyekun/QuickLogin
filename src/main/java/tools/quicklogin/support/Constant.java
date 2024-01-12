package tools.quicklogin.support;

import org.bukkit.configuration.file.FileConfiguration;
import tools.quicklogin.QuickLogin;

public class Constant {
    private static String loginSound = null;
    private static float loginVolume = 0.5f;
    private static String joinMessage = null;
    private static String leaveMessage = null;
    private static int kickTime = 0;
    private static boolean skullState = false;
    private static long autoLoginTime = 0;

    private static int passwordEnterTimes = 5;

    public static void loadConfig() {
        FileConfiguration config = LoginMethod.getPlugin().getConfig();
        loginSound = (String)config.get("sound");
        loginVolume = (float)config.getDouble("volume");
        joinMessage = (String)config.get("joinMessage");
        leaveMessage = (String)config.get("leaveMessage");
        kickTime = config.getInt("kickTime");
        skullState = config.getBoolean("skull");
        autoLoginTime = config.getInt("auto");
        passwordEnterTimes = config.getInt("passwordEnterTimes");
    }

    public static void reWrite(){
        FileConfiguration config = LoginMethod.getPlugin().getConfig();
        config.set("sound",loginSound);
        config.set("volume",loginVolume);
        config.set("joinMessage",joinMessage);
        config.set("leaveMessage",leaveMessage);
        config.set("kickTime",kickTime);
        config.set("skull",skullState);
        config.set("auto",autoLoginTime);
        config.set("passwordEnterTimes",passwordEnterTimes);
        LoginMethod.getPlugin().reloadConfig();
    }

    public static void setLoginSound(String sound, float volume){
        loginSound = sound;
        loginVolume = volume;
        reWrite();
    }

    public static void setJoinMessage(String text){
        joinMessage = text;
        reWrite();
    }

    public static void setLeaveMessage(String text){
        leaveMessage = text;
        reWrite();
    }

    public static void setKickTime(int time){
        kickTime = time;
        reWrite();
    }

    public static void setSkullState(boolean bool){
        skullState = bool;
        reWrite();
    }

    public static void setAutoLoginTime(long time){
        autoLoginTime = time;
        reWrite();
    }

    public static void setPasswordEnterTimes(int times){
        passwordEnterTimes = times;
        reWrite();
    }

    public static String getLoginSound() {
        return loginSound;
    }

    public static float getLoginVolume() {
        return loginVolume;
    }

    public static String getJoinMessage() {
        return joinMessage;
    }

    public static String getLeaveMessage(){
        return leaveMessage;
    }

    public static int getKickTime() {
        return kickTime;
    }

    public static boolean isSkullShow(){
        return skullState;
    }

    public static long getAutoLoginTime() {
        return autoLoginTime;
    }

    public static int getPasswordEnterTimes(){return passwordEnterTimes;}
}
