package xyz.holocons.mc.holoa9t.icon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ZombieIcon {
    static ItemStack icon;
    public static ItemStack getIcon() {
        if (icon == null) {
            icon = new ItemStack(Material.ZOMBIE_HEAD);
        }
        return icon;
    }
}
