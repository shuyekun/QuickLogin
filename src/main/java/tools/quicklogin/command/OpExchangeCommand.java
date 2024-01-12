package tools.quicklogin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tools.quicklogin.support.LoginMethod;

import java.util.HashMap;

public class OpExchangeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.DARK_RED + "非玩家无法使用该命令");
            return false;
        }
        if(args.length < 2){
            sender.sendMessage(ChatColor.DARK_RED + "Usage:/opexchange <玩家> <新密码>");
            return false;
        }
        if(!LoginMethod.isRegister(args[0])){
            sender.sendMessage(ChatColor.DARK_RED + "该玩家不存在");
            return false;
        }
        if(args[1].length() < 5){
            sender.sendMessage(ChatColor.DARK_RED + "新密码的长度必须大于4");
        }
        LoginMethod.exchangePassword(args[0],args[1]);
        LoginMethod.overWritePlayerList();
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.UNDERLINE + "已将" +
                ChatColor.RESET + " " + ChatColor.GOLD + args[0] + " " + ChatColor.GREEN +
                ChatColor.UNDERLINE + "的密码修改为" + ChatColor.RESET + " " + ChatColor.AQUA + args[1]);
        return true;
    }
}
