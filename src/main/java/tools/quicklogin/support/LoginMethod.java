package tools.quicklogin.support;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import tools.quicklogin.QuickLogin;

import java.io.*;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.*;

public class LoginMethod implements Listener {
    //监听方法
    private static final HashMap<String,Integer> playerList = new HashMap<>();
    private static final HashMap<String,ItemStack[]> unLoginList = new HashMap<>();
    private static final ArrayList<String> onlineList = new ArrayList<>();
    private static final HashMap<String, LogoutDate> logoutTime = new HashMap<>();
    private static final HashMap<String,InetAddress> autoLogin = new HashMap<>();

    private static final HashMap<String,Integer> enterPasswordTimes = new HashMap<>();

    private static final String fileName = "plugins/QuickLogin/playerlist.dat";
    private static final String fileName2 = "plugins/QuickLogin/playerlogout.dat";
    private static final String tempFile = "plugins/QuickLogin/temp.dat";
    private static QuickLogin ql = null;
    static {//初始化数据
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
            String tempName;
            while((tempName = (String)input.readObject()) != null){
                playerList.put(tempName,(Integer) input.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("...50%...");
        }
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName2))){
            String tempName;
            while ((tempName = (String)input.readObject()) != null){
                logoutTime.put(tempName,(LogoutDate) input.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("...75%...");
        }
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(tempFile))){
            String tempName;
            while ((tempName = (String)input.readObject()) != null){
                ItemStack[] itemStack = new ItemStack[41]; //玩家背包大小为41
                for (int i = 0; i < 41; i++){
                    LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>)input.readObject();
                    if(obj != null){
                        itemStack[i] = ItemStack.deserialize(obj);
                    }else{
                        itemStack[i] = new ItemStack(Material.AIR);
                    }
                }
                unLoginList.put(tempName,itemStack);
            }
        }catch (IOException | ClassNotFoundException e){
            System.out.println("玩家数据加载完成");
        }
    }

    public LoginMethod(QuickLogin ql){
        LoginMethod.ql = ql;
    }

    public static void overWritePlayerList(){
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName,false))) {
            for(String name : playerList.keySet()){
                output.writeObject(name);
                output.writeObject(playerList.get(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overWriteLogoutList(){
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName2,false))) {
            for(String name : logoutTime.keySet()){
                output.writeObject(name);
                output.writeObject(logoutTime.get(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overWriteUnLoginList(){
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tempFile,false))){
            if(unLoginList.isEmpty()) return;
            for(String name : unLoginList.keySet()){
                output.writeObject(name);
                for (ItemStack itemStack : unLoginList.get(name)){
                    if(itemStack != null){
                        output.writeObject(itemStack.serialize());
                    } else{
                        output.writeObject(null);
                    }
                }
            }
        }catch (IOException ignored){
        }
    }

    public static void registerPlayer(Player p, String password){//玩家注册
            playerList.put(p.getName(),password.hashCode());
            loginPlayer(p);
            overWritePlayerList();
    }

    public static void registerPlayer(String name, String password){//管理员添加
        playerList.put(name,password.hashCode());
        logoutTime.put(name,new LogoutDate());
        overWritePlayerList();
        overWriteLogoutList();
    }

    public static void exchangePassword(String name,String password){
        playerList.put(name,password.hashCode());
        LoginMethod.overWritePlayerList();
    }

    public static boolean isRegister(String name){
        if(playerList.isEmpty()) return false;
        for(String pname : playerList.keySet()){
            if(pname.equals(name)){
                return true;
            }
        }
        return false;
    }

    public static boolean isUnLogin(String name){
        for(String pname : unLoginList.keySet()){
            if(pname.equals(name)){
                return true;
            }
        }
        return false;
    }

    public static boolean isOnline(String name){
        if(onlineList.isEmpty()) return false;
        for(String pname : onlineList){
            if(pname.equals(name)){
                return true;
            }
        }
        return false;
    }

    public static void removeFromOnlineList(String name){
        onlineList.remove(name);
    }

    public static ItemStack[] getTemporaryPacket(String name){
        return unLoginList.get(name);
    }

    public static void removeTemporaryPacket(String name) {
        unLoginList.remove(name);
        overWriteUnLoginList();
    }

    public static void savePacket(String name, ItemStack[] item){
        unLoginList.put(name, item);
        overWriteUnLoginList();
    }

    public static boolean comparePassword(String name, String password){
        return playerList.get(name).equals(password.hashCode());
    }

    public static void loginPlayer(Player p){
        if(p!=null){
            p.setInvulnerable(false);
            p.setInvisible(false);

            p.getInventory().setContents(unLoginList.get(p.getName()));
            unLoginList.remove(p.getName());
            overWriteUnLoginList();
            onlineList.add(p.getName());

            p.playSound(p.getLocation(),Constant.getLoginSound(),Constant.getLoginVolume(),0.5f);
            joinMessage(p.getName());
        }
    }

    public static void joinMessage(String name){
        Server server = ql.getServer();
        String[] temp = Constant.getJoinMessage().split("%");
        StringBuilder builder = new StringBuilder();
        for (String s : temp) {
            if ("player".equals(s)) {
                builder.append(name);
            } else {
                builder.append(s);
            }
        }
        String temp2 = ChatColor.translateAlternateColorCodes('&',builder.toString());
        server.broadcastMessage(temp2);
    }

    public static void leaveMessage(String name){
        Server server = ql.getServer();
        String[] temp = Constant.getLeaveMessage().split("%");
        StringBuilder builder = new StringBuilder();
        for (String s : temp) {
            if ("player".equals(s)) {
                builder.append(name);
            } else {
                builder.append(s);
            }
        }
        String temp2 = ChatColor.translateAlternateColorCodes('&',builder.toString());
        server.broadcastMessage(temp2);
    }

    public static LogoutDate lastLoginTime(String name){
        return logoutTime.get(name);
    }

    public static void setLogoutTime(String name){
        setLogoutTime(name,null);
    }

    public static void setLogoutTime(String name, LogoutDate date){
        logoutTime.put(name,date);
        overWriteLogoutList();
    }

    public static LogoutDate getLogoutTime(String name){
        return logoutTime.get(name);
    }

    public static InetAddress lastLoginAddress(String name){
        return autoLogin.get(name);
    }

    public static void setLoginAddress(String name,InetAddress address){
        autoLogin.put(name,address);
    }

    public static HashMap<String, Integer> getPlayerList(){
        return playerList;
    }

    public static void setPlugins(QuickLogin ql){
         LoginMethod.ql = ql;
    }

    public static QuickLogin getPlugin(){
        return ql;
    }

    public static boolean isEnterPasswordTooManyTimes(String name){
        if(enterPasswordTimes.containsKey(name)){
            if((enterPasswordTimes.get(name) + 1) > Constant.getPasswordEnterTimes()){
                //大于设定的次数后清除(踢出玩家)
                enterPasswordTimes.remove(name);
                return true;
            }else {
                enterPasswordTimes.put(name,enterPasswordTimes.get(name) + 1);
            }
        }else {
            enterPasswordTimes.put(name,1);
        }
        return false;
    }

    public static void clearPasswordRecord(String name) {
        enterPasswordTimes.remove(name);
    }
}

