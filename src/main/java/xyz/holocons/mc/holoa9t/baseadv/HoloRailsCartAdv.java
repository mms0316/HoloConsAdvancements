package xyz.holocons.mc.holoa9t.baseadv;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.util.PositionIntXYZ;
import xyz.holocons.mc.holoa9t.file.FileConfigurable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HoloRailsCartAdv extends BaseAdvancement implements FileConfigurable {
    final static String key = "cart_ride";
    final static Material icon = Material.MINECART;
    final static AdvancementFrameType frame = AdvancementFrameType.TASK;
    final static boolean showToast = true;
    final static boolean announceChat = true;

    String sectionName;

    // Configs of this advancement
    boolean isDisabled;
    Set<PositionIntXYZ> positions;

    public HoloRailsCartAdv(@NotNull String namespace, String title, String description, float x, float y, @NotNull Advancement parent) {
        super(namespace + "." + key,
                new AdvancementDisplay(icon, title, frame, showToast, announceChat, x, y, description),
                parent);
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
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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
        var changed = FileConfigurable.super.setInitialConfigValues(cfg);

        var path = sectionName + getConfigPathSeparator(cfg) + "tripwires";
        if (cfg.contains(path)) {
            return changed;
        }

        cfg.set(path, List.of("(Change these to tripwire coordinates) 0, 0, 0"));
        return true;
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        if (!FileConfigurable.super.loadConfigValues(cfg)) return false;

        if (positions == null) {
            positions = new HashSet<>();
        } else {
            positions.clear();
        }

        final var sep = getConfigPathSeparator(cfg);
        final var path = sectionName + sep + "tripwires";

        for (var position : cfg.getStringList(path)) {
            var coordsString = position.split(",", 3 + 1);
            if (coordsString.length != 3) {
                HoloAdvancements.getInstance().getLogger().warning("Wrong HoloRailsCartAdv coordinate " + position + " at " + path);
                return false;
            }

            try {
                var x = Integer.parseInt(coordsString[0].trim());
                var y = Integer.parseInt(coordsString[1].trim());
                var z = Integer.parseInt(coordsString[2].trim());

                positions.add(new PositionIntXYZ(x, y, z));
            } catch (NumberFormatException ignored) {
                HoloAdvancements.getInstance().getLogger().warning("Wrong HoloRailsCartAdv coordinate " + position + " at " + path);
                return false;
            }
        }

        return true;
    }
}
