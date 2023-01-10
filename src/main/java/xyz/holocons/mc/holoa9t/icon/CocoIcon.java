package xyz.holocons.mc.holoa9t.icon;

import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;

public class CocoIcon {
    static ItemStack icon;
    public static ItemStack getIcon() {
        if (icon == null) {
            try {
                icon = PlayerHead.make(new URL("http://textures.minecraft.net/texture/efe873d27cc5c39880b94dd2dbf45b9c75789a18ea442fbee1fa28ab87ac1981"));
            } catch (MalformedURLException ignored) { }
        }
        return icon;
    }
}
