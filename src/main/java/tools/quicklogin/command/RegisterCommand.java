package tools.quicklogin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tools.quicklogin.support.LoginMethod;

public class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.DARK_RED + "非玩家无法使用该命令");
            return false;
        }
        Player p = ((Player) sender).getPlayer();
        if(LoginMethod.isRegister(p.getName())){
            sender.sendMessage(ChatColor.DARK_RED + "您已经注册过了!");
            return false;
        }
        if(args.length != 2){
            sender.sendMessage(ChatColor.DARK_RED + "Usage:/register <密码> <确认密码>");
            return false;
        }
        if(!args[0].equals(args[1])){
            sender.sendMessage(ChatColor.DARK_RED + "两次输入的密码必须相同");
            return false;
        }
        if(args[0].length() > 4){
            LoginMethod.registerPlayer((Player)sender,args[0]);
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "您已注册成功!");
            return false;
        }
        sender.sendMessage(ChatColor.DARK_RED + "密码的长度必须大于4");
        return false;
    }
}
