package tools.quicklogin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import tools.quicklogin.support.Constant;
import tools.quicklogin.support.LoginMethod;
import tools.quicklogin.support.LogoutDate;
import tools.quicklogin.support.Menu;

import java.util.*;

public class QuickLoginCommand implements TabExecutor, Listener {
    private final List<String> firstTips = Arrays.asList("sound","message","kick","players","add","skull","auto","password");
    private final List<String> soundEffect = Arrays.asList("block.anvil.place","entity.villager.work_toolsmith",
            "block.glass.break","block.beacon.activate","entity.tnt.primed","block.piston.contract",
            "block.shulker_box.open","block.bell.use");

    private final List<String> messageTips = Arrays.asList("join","leave");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql <子命令>");
            return false;
        }
        switch(args[0]){
            case "sound" :{
                if(args.length < 3){
                    sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql sound <声音名字> <音量>");
                    return false;
                }
                if(Float.parseFloat(args[2]) <= 0 || Float.parseFloat(args[2]) > 1){
                    sender.sendMessage(ChatColor.DARK_RED + "你的声音设置的超出了范围(0,1]");
                    return false;
                }
                if(args[1] != null){
                Constant.setLoginSound(args[1],Float.parseFloat(args[2]));
                sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "成功设置声音");
                Constant.reWrite();
                return true;
                }
                break;
            }
            case "message":{
                if(args.length < 2){
                    sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql message <join/leave> <消息>");
                    return false;
                }
                if((!args[1].equals("join")) && (!args[1].equals("leave"))){
                    return false;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]);
                    if(i < args.length - 1){
                        builder.append(" ");
                    }
                }
                if(args[1].equals("join")){
                    Constant.setJoinMessage(builder.toString().replaceAll("\\\\n","\n"));
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "成功设置登录消息");
                }else {
                    Constant.setLeaveMessage(builder.toString().replaceAll("\\\\n","\n"));
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "成功设置离开消息");
                }
                return true;
            }
            case "kick":{
                if(args.length < 2){
                    sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql kick <时间(秒)>");
                    return false;
                }
                if(Integer.parseInt(args[1]) >= 0){
                    Constant.setKickTime(Integer.parseInt(args[1]));
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "成功设置登录时间限制");
                    Constant.reWrite();
                    return true;
                }
                else{
                    sender.sendMessage(ChatColor.DARK_RED + "时间不能为负数");
                    return false;
                }
            }
            case "players":{
                Player player = (Player)sender;
                player.openInventory(showPlayerOnline()[0]);
                return true;
            }
            case "add":{
                if(args.length < 3){
                    sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql add <玩家名> <密码>");
                    return false;
                }
                LoginMethod.registerPlayer(args[1],args[2]);
                sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "成功创建玩家账号");
                return true;
            }
            case "skull":{
                if(!Constant.isSkullShow()){
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "头颅皮肤已打开");
                }else{
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "头颅皮肤已关闭");
                }
                Constant.setSkullState(!Constant.isSkullShow());
                return true;
            }
            case "auto":{
                if(args.length < 2){
                    sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql auto <时间(秒)>");
                    return false;
                }
                long temp = Long.parseLong(args[1]);
                Constant.setAutoLoginTime(temp);
                sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "自动登录的时间已设为" + ChatColor.RESET +
                        ChatColor.AQUA + " " + temp + " " + ChatColor.GREEN + ChatColor.UNDERLINE + "秒");
                return true;
            }
            case "password":{
                if(args.length < 2){
                    sender.sendMessage(ChatColor.DARK_RED + "Usage:/ql password <次数>");
                    return false;
                }
                Constant.setPasswordEnterTimes(Integer.parseInt(args[1]));
                sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "最大密码输入错误次数已设置为" + ChatColor.RESET +
                        ChatColor.AQUA + " " + args[1] + " " + ChatColor.GREEN + ChatColor.UNDERLINE + "次");
                return true;
            }
            default:
                sender.sendMessage(ChatColor.DARK_RED + "没有这个命令");
        }
        return false;
    }

    //补全指令信息
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1){
            if(args[0].isEmpty()){
                return firstTips;
            }else{
                for(String temp : firstTips){
                    if(temp.startsWith(args[0].toLowerCase())){
                        return Collections.singletonList(temp);
                    }
                }
            }
        }
        if(args.length == 2 && args[0].equals("sound")){
            if(args[1].isEmpty()){
                return soundEffect;
            }else {
                for(String temp : soundEffect){
                    if(temp.startsWith(args[1].toLowerCase())){
                        return Collections.singletonList(temp);
                    }
                }
            }
        }else if(args.length == 2 && args[0].equals("message")){
            if(args[1].isEmpty()){
                return messageTips;
            }else {
                for(String temp : messageTips){
                    if(temp.startsWith(args[1].toLowerCase())){
                        return Collections.singletonList(temp);
                    }
                }
            }
        }
        return null;
    }

    //返回一个背包组，里面是写着玩家信息的人头
    public Inventory[] showPlayerOnline(){
        ArrayList<String> arrayList = new ArrayList<>(LoginMethod.getPlayerList().keySet());
        Collections.sort(arrayList);
        int pageNum = (arrayList.size()/36) + 1;
        Inventory[] temp = new Inventory[pageNum];

        //功能按钮
        ItemStack nextButton = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = nextButton.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "下一页");
        nextButton.setItemMeta(itemMeta);
        ItemStack previousButton = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        itemMeta.setDisplayName(ChatColor.GREEN + "上一页");
        previousButton.setItemMeta(itemMeta);
        ItemStack paper = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta paperMeta = paper.getItemMeta();

        for(int i = 0, j = 0; i < pageNum; i++){
            temp[i] = Bukkit.createInventory(new Menu(), 54, ChatColor.RED + "" + ChatColor.BOLD + "目前已注册的玩家有:");
            temp[i].setItem(45,previousButton);
            temp[i].setItem(53,nextButton);
            paperMeta.setDisplayName(ChatColor.RESET + "" + (i + 1) + "");//显示页数
            paper.setItemMeta(paperMeta);
            temp[i].setItem(49,paper);
            for(int count = 0; j < arrayList.size(); j++,count++){
                if(count > 35) {
                    break;
                }
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta data = (SkullMeta) itemStack.getItemMeta();
                if(data != null)
                data.setDisplayName(ChatColor.AQUA + arrayList.get(j));
                if(Constant.isSkullShow()){//第一次加载头颅皮肤可能会导致卡顿
                    data.setOwningPlayer(Bukkit.getOfflinePlayer(arrayList.get(j)));
                }
                ArrayList<String> lores = new ArrayList<>();
                lores.add("" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.UNDERLINE + "上次登录时间:");
                LogoutDate date;
                if((date = LoginMethod.getLogoutTime(arrayList.get(j))) != null){
                    lores.add(ChatColor.YELLOW + "" + date);
                } else{
                  lores.add(ChatColor.YELLOW + "在线");
                }
                data.setLore(lores);
                itemStack.setItemMeta(data);
                temp[i].setItem(count,itemStack);
            }
        }
        return temp;
    }

    //监听点击菜单事件
    @EventHandler
    public void moveItemOnInventory(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof Menu){
            String name = e.getInventory().getItem(49).getItemMeta().getDisplayName();
            int a = Integer.parseInt(name);
            if(e.getSlot() == 45){//上一页
                if(a - 2 >= 0){
                    e.getWhoClicked().openInventory(showPlayerOnline()[a - 2]);
                }
            } else if(e.getSlot() == 53){//下一页
                Inventory[] inventory = showPlayerOnline();
                if(a < inventory.length){
                    e.getWhoClicked().openInventory(inventory[a]);
                }
            }
            e.setCancelled(true);
        }
    }

}
