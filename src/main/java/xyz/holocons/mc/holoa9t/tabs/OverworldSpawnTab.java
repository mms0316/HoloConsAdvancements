package xyz.holocons.mc.holoa9t.tabs;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.baseadv.CuboidEnterAdv;
import xyz.holocons.mc.holoa9t.baseadv.HoloRailsCartRideAdv;
import xyz.holocons.mc.holoa9t.icon.PlayerHead;

public class OverworldSpawnTab extends BaseTab {
    public OverworldSpawnTab(boolean grantRootAdvancement) {
        super("overworld_spawn");
        this.grantRootAdvancement = grantRootAdvancement;

        backgroundTexture = "textures/block/quartz_bricks.png"; //TODO: move to config.yml
    }

    @Override
    public void register() {
        final var namespace = getSectionName();
        var root = getRootAdvancement();
        if (root == null) {
            HoloAdvancements.getInstance().getLogger().warning("Invalid config for " + getSectionName());
            return;
        }

        var children = getChildren();

        // Put new children here

        var display = new AdvancementDisplay(
                Material.MINECART,
                "Use Spawn Station",
                AdvancementFrameType.TASK,
                true, true,
                1, 0,
                "Take a ride from Spawn station.", "When on max speed, leaving will break the cart!");
        new HoloRailsCartRideAdv(namespace, display, root).register(children);

        display = new AdvancementDisplay(
                new ItemStack(Material.ZOMBIE_HEAD),
                "Greet Yagoo",
                AdvancementFrameType.TASK,
                true, true,
                1, 1,
                "Found the truly best girl");
        new CuboidEnterAdv(namespace, "yagoo_location", display, root).register(children);

        display = new AdvancementDisplay(
                PlayerHead.make("http://textures.minecraft.net/texture/efe873d27cc5c39880b94dd2dbf45b9c75789a18ea442fbee1fa28ab87ac1981"),
                "Greet the OG",
                AdvancementFrameType.TASK,
                true, true,
                1, 2,
                "Good morning, Weather Hackers");
        new CuboidEnterAdv(namespace, "dragon_location", display, root).register(children);

        super.register();
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        boolean loaded = super.loadConfigValues(cfg);



        return loaded;
    }
}
