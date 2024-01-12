package tools.quicklogin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tools.quicklogin.support.LoginMethod;

import java.util.HashMap;

public class ChangePasswordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.DARK_RED + "非玩家无法使用该命令");
            return false;
        }
        if(args.length < 3){
            sender.sendMessage(ChatColor.DARK_RED + "Usage:/exchangepassword <旧密码> <新密码> <确认新密码>");
            return false;
        }
        Player p = (Player) sender;
        if(!LoginMethod.comparePassword(p.getName(),args[0])){
            sender.sendMessage(ChatColor.DARK_RED + "旧密码错误");
            return false;
        }
        if(args[0].equals(args[1])){
            sender.sendMessage(ChatColor.DARK_RED + "旧密码和新密码不能重复");
            return false;
        }
        if(!args[1].equals(args[2])){
            sender.sendMessage(ChatColor.DARK_RED + "两次输入的密码必须相同");
            return false;
        }
        if(args[1].length() <= 4){
            sender.sendMessage(ChatColor.DARK_RED + "新密码的长度必须大于4");
            return false;
        }

        LoginMethod.exchangePassword(p.getName(),args[1]);
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "您已成功更改密码");
        return true;
    }
}
