package xyz.holocons.mc.holoa9t.tabs;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import xyz.holocons.mc.holoa9t.baseadv.HoloRailsCartAdv;

public class OverworldSpawnTab extends BaseTab {
    public OverworldSpawnTab(boolean grantRootAdvancement) {
        super();
        this.grantRootAdvancement = grantRootAdvancement;

        namespace = "overworld_spawn";
        key = "root";
        title = "Spawn Area";
        description = "Explore the Spawn area";
        backgroundTexture = "textures/block/quartz_bricks.png";
        icon = Material.SMOOTH_QUARTZ;
        frame = AdvancementFrameType.TASK;
        showToast = true;
        announceChat = true;
        x = 0;
        y = 0;

        var root = getRootAdvancement();
        var children = getChildren();

        // Put new children here
        new HoloRailsCartAdv(namespace,
                "Use Spawn Station",
                "Take a ride from Spawn station. When on max speed, leaving will break the cart!",
                x + 1, y, root).register(children);

        super.register();
    }
}
