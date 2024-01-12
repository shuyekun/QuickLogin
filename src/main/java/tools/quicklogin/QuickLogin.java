package tools.quicklogin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import tools.quicklogin.command.*;
import tools.quicklogin.listener.LoginEventListener;
import tools.quicklogin.support.Constant;
import tools.quicklogin.support.LoginMethod;

public final class QuickLogin extends JavaPlugin {
    private static String loginSound = null;
    private static float loginVolume = 0.5f;
    private static String joinMessage = null;
    private static int kickTime = 0;
    private static boolean skullState = false;
    private static long autoLoginTime = 0;

    @Override
    public void onEnable() {
        System.out.println("这是一款轻量化的登录插件--QuickLogin V1.9");
        saveDefaultConfig();//没有config.yml则生成一个
        LoginMethod.setPlugins(this);
        Constant.loadConfig();
        QuickLoginCommand quickLogin = new QuickLoginCommand();//防止重复生成
        Bukkit.getPluginCommand("ql").setExecutor(quickLogin);
        Bukkit.getPluginManager().registerEvents(quickLogin,this);
        Bukkit.getPluginManager().registerEvents(new LoginEventListener(),this);
        Bukkit.getPluginCommand("login").setExecutor(new LoginCommand());
        Bukkit.getPluginCommand("register").setExecutor(new RegisterCommand());
        Bukkit.getPluginCommand("exchangepassword").setExecutor(new ChangePasswordCommand());
        Bukkit.getPluginCommand("opexchange").setExecutor(new OpExchangeCommand());
}

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
