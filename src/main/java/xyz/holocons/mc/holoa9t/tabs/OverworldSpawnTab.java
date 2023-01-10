package xyz.holocons.mc.holoa9t.tabs;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.adv.SpawnCocoFindAdv;
import xyz.holocons.mc.holoa9t.adv.SpawnYagooFindAdv;
import xyz.holocons.mc.holoa9t.baseadv.HoloRailsCartRideAdv;

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
        var HoloRailsCartRideDisplay = new AdvancementDisplay(
                Material.MINECART,
                "Use Spawn Station",
                AdvancementFrameType.TASK,
                true, true,
                1, 0,
                "Take a ride from Spawn station. When on max speed, leaving will break the cart!");
        new HoloRailsCartRideAdv(namespace, HoloRailsCartRideDisplay, root).register(children);

        new SpawnYagooFindAdv(namespace, "yagoo_location",
                "Greet Yagoo",
                "Found the truly best girl",
                1, 1, root).register(children);

        new SpawnCocoFindAdv(namespace, "dragon_location",
                "Greet the OG",
                "Good morning, Weather Hackers",
                1, 2, root).register(children);

        super.register();
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        boolean loaded = super.loadConfigValues(cfg);



        return loaded;
    }
}
