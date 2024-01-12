package tools.quicklogin.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import tools.quicklogin.support.LoginMethod;
import tools.quicklogin.support.LogoutDate;

import java.net.InetAddress;

import static tools.quicklogin.support.LoginMethod.*;
import static tools.quicklogin.support.Constant.*;

public class LoginEventListener implements Listener {

    //防止同名抢占
    @EventHandler
    public void playerLogin(AsyncPlayerPreLoginEvent e){
        if(isOnline(e.getName())){
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "您已在线");
        }
    }


    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        Player p = e.getPlayer();
        String playerName = p.getName();
        if(isUnLogin(playerName)){//修复玩家在登录阶段服务器重启导致的问题
            p.getInventory().setContents(getTemporaryPacket(playerName));
            System.out.println("已恢复 " + playerName + " 的物品");
        }
        if(p.getAddress()!=null && p.getAddress().getAddress().equals(lastLoginAddress(playerName))){//自动登录
            if((System.currentTimeMillis() - lastLoginTime(playerName).getTime()) <= 1000 * getAutoLoginTime()){
                setLogoutTime(p.getName());
                p.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE  + "已为您自动登录");
                return;
            }
        }

        //暂存背包物品
        ItemStack[] item = p.getInventory().getContents();
        savePacket(p.getName(), item);
        p.setInvulnerable(true);
        p.setInvisible(true);
        p.getInventory().clear();

        if (!isRegister(playerName)) {
            p.sendTitle("","请注册: /register <密码> <确认密码>",10,80,20);
        } else {
            p.sendTitle("",ChatColor.RED + "请登录: /login <密码>",10,80,20);
        }

        //登录超时踢出
        if (getKickTime() != 0) {
            Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                if (isUnLogin(playerName)) {
                    p.kickPlayer(ChatColor.DARK_RED + "您已登录超时");
                }
            }, getKickTime() * 20L);
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        e.setQuitMessage("");
        if(isUnLogin(e.getPlayer().getName())){
            Player p = e.getPlayer();
            String playerName = p.getName();
            p.getInventory().setContents(getTemporaryPacket(playerName));
            removeTemporaryPacket(playerName);
        } else {
            String name = e.getPlayer().getName();
            setLogoutTime(name,new LogoutDate());
            InetAddress address = e.getPlayer().getAddress().getAddress();
            setLoginAddress(name,address);
            removeFromOnlineList(name);
            LoginMethod.leaveMessage(name);
        }
    }

    @EventHandler
    public void playerSendCommand(PlayerCommandPreprocessEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            String command = e.getMessage().split(" ")[0];
            if(!command.equals("/login") && !command.equals("/l") && !command.equals("/register")
                    && !command.equals("/reg")){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerSendChat(AsyncPlayerChatEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerDrop(PlayerDropItemEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerOpenInventory(InventoryOpenEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerBreakBlock(BlockBreakEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerMovement(PlayerMoveEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
            if (!isRegister(e.getPlayer().getName())) {
                e.getPlayer().sendTitle("","请注册: /register <密码> <确认密码>",0,40,5);
            } else {
                e.getPlayer().sendTitle("",ChatColor.RED + "请登录: /login <密码>",0,40,5);
            }
        }
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteractArmorStand(PlayerArmorStandManipulateEvent e){
        if(isUnLogin(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerPickupItem(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player){
            if(isUnLogin(e.getEntity().getName())){
                e.setCancelled(true);
            }
        }
    }
}
