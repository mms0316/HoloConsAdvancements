package xyz.holocons.mc.holoa9t.baseadv;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.file.FileDisableable;
import xyz.holocons.mc.holoa9t.util.CuboidIntXYZ;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CuboidEnterAdv extends BaseAdvancement implements FileDisableable {
    String sectionName;

    // Configs of this advancement
    boolean isDisabled;
    Set<CuboidIntXYZ> cuboids;

    public CuboidEnterAdv(@NotNull String namespace, String key, AdvancementDisplay display, @NotNull Advancement parent) {
        super(namespace + "." + key,
                display,
                parent);
        sectionName = namespace + "." + key;
    }

    void registerEvents() {
        // Register the event via the tab's EventManager
        // This event might take a toll on the server, as PlayerMoveEvent is used a lot
        registerEvent(PlayerMoveEvent.class, e -> {
            // Global configuration
            if (isDisabled) return;
            if (cuboids == null) return;

            // Player configuration
            var player = e.getPlayer();

            var loc = player.getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            for (var cuboid : cuboids) {
                if (cuboid.includes(x, y, z)) {
                    incrementProgression(player);
                    break;
                }
            }
        });
    }

    public void register(Set<BaseAdvancement> advancements) {
        registerEvents();
        advancements.add(this);
    }

    @Override
    public String getSectionName() {
        return sectionName;
    }

    @Override
    public boolean getDisabled() {
        return isDisabled;
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean setInitialConfigValues(FileConfiguration cfg) {
        var changed = FileDisableable.super.setInitialConfigValues(cfg);

        var path = sectionName + getConfigPathSeparator(cfg) + "cuboid";
        if (cfg.contains(path)) {
            return changed;
        }

        cfg.set(path, List.of("(Change these to coordinates) " + CuboidIntXYZ.getPlaceholder()));
        return true;
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        if (!FileDisableable.super.loadConfigValues(cfg)) return false;

        if (cuboids == null) {
            cuboids = new HashSet<>();
        } else {
            cuboids.clear();
        }

        final var sep = getConfigPathSeparator(cfg);
        final var path = sectionName + sep + "cuboid";

        for (var cuboid : cfg.getStringList(path)) {
            var cub = CuboidIntXYZ.from(cuboid);
            if (cub == null) {
                HoloAdvancements.getInstance().getLogger().warning("Wrong HoloRailsCartAdv cuboid " + cuboid + " at " + path);
                return false;
            }

            cuboids.add(cub);
        }

        return true;
    }
}
