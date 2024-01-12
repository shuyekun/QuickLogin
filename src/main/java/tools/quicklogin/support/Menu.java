package tools.quicklogin.support;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Menu implements InventoryHolder {
    //菜单类，用作判断应不应该取消点击事件
    @Override
    public Inventory getInventory() {
        return (Inventory) this;
    }
}
