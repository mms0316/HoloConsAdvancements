package xyz.holocons.mc.holoa9t.icon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.net.URL;
import java.util.UUID;

public class PlayerHead {
    public static ItemStack make(URL url) {
        var itemStack = new ItemStack(Material.PLAYER_HEAD);
        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta) {
            var profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            profile.getTextures().setSkin(url);
            skullMeta.setOwnerProfile(profile);

            //This is required, as getItemMeta() returns a new SkullMeta but keeps ItemMeta as null
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }
}
