package xyz.holocons.mc.holoa9t.baseadv;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.util.PositionIntXYZ;
import xyz.holocons.mc.holoa9t.file.FileDisableable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HoloRailsCartRideAdv extends BaseAdvancement implements FileDisableable {
    final static String key = "holorails_cart_ride";
    String sectionName;

    // Configs of this advancement
    boolean isDisabled;
    Set<PositionIntXYZ> positions;

    public HoloRailsCartRideAdv(@NotNull String namespace, @NotNull AdvancementDisplay display, @NotNull Advancement parent) {
        super(namespace + "." + key, display, parent);
        sectionName = namespace + "." + key;
    }

    void registerEvents() {
        // Register the event via the tab's EventManager
        registerEvent(PlayerInteractEvent.class, e -> {
            // Global configuration
            if (isDisabled) return;
            if (positions == null) return;

            // Player configuration
            var player = e.getPlayer();
            if (!isVisible(player)) return;

            // Event handling
            var block = e.getClickedBlock();
            if (block == null) return;

            //Player has to trigger a tripwire while being in a vehicle (cart)
            if (!player.isInsideVehicle()) return;
            if (block.getType() != Material.TRIPWIRE) return;

            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            for (var position : positions) {
                if (x == position.x() && y == position.y() && z == position.z()) {
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

        var path = sectionName + getConfigPathSeparator(cfg) + "tripwires";
        if (cfg.contains(path)) {
            return changed;
        }

        cfg.set(path, List.of("(Change these to tripwire coordinates) " + PositionIntXYZ.getPlaceholder()));
        return true;
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        if (!FileDisableable.super.loadConfigValues(cfg)) return false;

        if (positions == null) {
            positions = new HashSet<>();
        } else {
            positions.clear();
        }

        final var sep = getConfigPathSeparator(cfg);
        final var path = sectionName + sep + "tripwires";

        for (var position : cfg.getStringList(path)) {
            var pos = PositionIntXYZ.from(position);
            if (pos == null) {
                HoloAdvancements.getInstance().getLogger().warning("Wrong HoloRailsCartAdv coordinate " + position + " at " + path);
                return false;
            }

            positions.add(pos);
        }

        return true;
    }
}
