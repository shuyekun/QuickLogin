package tools.quicklogin.command;

import jdk.internal.net.http.common.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tools.quicklogin.support.LoginMethod;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.DARK_RED + "非玩家无法使用该命令");
            return false;
        }
        if(args.length == 0){
            sender.sendMessage(ChatColor.DARK_RED + "Usage:/login <密码>");
            return false;
        }
        Player p = (Player) sender;
        if(!LoginMethod.isUnLogin(p.getName())){
            sender.sendMessage(ChatColor.DARK_RED + "您已经登录过了!");
            return false;
        }
        if(!LoginMethod.isRegister(p.getName())) {
            sender.sendMessage(ChatColor.DARK_RED + "您还未注册!");
            return false;
        }
        if(LoginMethod.comparePassword(p.getName(),args[0])){
            LoginMethod.loginPlayer(p);
            LoginMethod.setLogoutTime(p.getName());
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE  + "您已登录");
            LoginMethod.clearPasswordRecord(p.getName());
            return true;
        }else {
            sender.sendMessage(ChatColor.DARK_RED + "密码错误");
            if (LoginMethod.isEnterPasswordTooManyTimes(p.getName())){
                p.kickPlayer(ChatColor.DARK_RED + "密码错误次数过多!");
            }
        }
        return false;
    }
}
