package xyz.holocons.mc.holoa9t.display;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.file.FileConfigurable;
import xyz.holocons.mc.holoa9t.icon.PlayerHead;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DisplayBuilder implements FileConfigurable {
    static final String ICON = "icon";
    static final String TITLE = "title";
    static final String FRAME_TYPE = "frame_type";
    static final String DESCRIPTION = "description";
    static final String SHOW_TOAST = "show_toast";
    static final String ANNOUNCE_CHAT = "announce_chat";
    static final String X = "x";
    static final String Y = "y";

    String section;
    ItemStack icon;
    String title;
    AdvancementFrameType frameType;
    List<String> description;
    boolean showToast;
    boolean announceChat;
    Float x;
    Float y;

    public DisplayBuilder(String section) {
        this.section = section;
    }

    public AdvancementDisplay build() {
        if (icon == null || title == null || description == null) return null;

        return new AdvancementDisplay(
                icon,
                title,
                frameType != null ? frameType : AdvancementFrameType.TASK,
                showToast, announceChat,
                x, y,
                description);
    }

    @Override
    public String getSectionName() {
        return section;
    }

    @Override
    public boolean setInitialConfigValues(FileConfiguration cfg) {
        boolean changed = false;
        final String basePath = getSectionName() + getConfigPathSeparator(cfg);
        String path;

        path = basePath + ICON;
        if (cfg.get(path) == null) {
            cfg.set(path, "Use a Material name or Identifier (see Bukkit Material#matchMaterial) or a texture URL from http://textures.minecraft.net/texture/");
            changed = true;
        }

        path = basePath + TITLE;
        if (cfg.get(path) == null) {
            cfg.set(path, "Example title");
            changed = true;
        }

        path = basePath + FRAME_TYPE;
        if (cfg.get(path) == null) {
            cfg.set(path, "Use TASK (default) / GOAL / CHALLENGE");
            changed = true;
        }

        path = basePath + DESCRIPTION;
        if (cfg.get(path) == null) {
            cfg.set(path, List.of("Use list of multiple elements", "or a single element"));
            changed = true;
        }

        path = basePath + SHOW_TOAST;
        if (cfg.get(path) == null) {
            cfg.set(path, "Use true (default) or false");
            changed = true;
        }

        path = basePath + ANNOUNCE_CHAT;
        if (cfg.get(path) == null) {
            cfg.set(path, "Use true (default) or false");
            changed = true;
        }

        path = basePath + X;
        if (cfg.get(path) == null) {
            cfg.set(path, "X coordinate offset, accepts decimal point.");
            changed = true;
        }

        path = basePath + Y;
        if (cfg.get(path) == null) {
            cfg.set(path, "Y coordinate offset, accepts decimal point.");
            changed = true;
        }

        return changed;
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        final String basePath = getSectionName() + getConfigPathSeparator(cfg);

        icon = loadIcon(cfg, basePath);
        if (icon == null) return false;

        title = loadTitle(cfg, basePath);
        if (title == null) return false;

        frameType = loadFrameType(cfg, basePath);

        description = loadDescription(cfg, basePath);
        if (description == null) return false;

        showToast = loadShowToast(cfg, basePath);
        announceChat = loadAnnounceChat(cfg, basePath);

        x = loadX(cfg, basePath);
        if (x == null) return false;
        y = loadY(cfg, basePath);
        if (y == null) return false;

        return true;
    }

    private ItemStack loadIcon(FileConfiguration cfg, String basePath) {
        var iconString = cfg.getString(basePath + "icon");
        if (iconString == null) return null;

        if (iconString.startsWith("http://textures.minecraft.net/texture/")) {
            try {
                var url = new URL(iconString);
                return PlayerHead.make(url);
            } catch (MalformedURLException e) {
                HoloAdvancements.getInstance().getLogger().warning("Invalid icon URL [" + iconString + "] at " + section);
                return null;
            }
        } else if (iconString.contains("://")) {
            HoloAdvancements.getInstance().getLogger().warning("Unsupported icon URL [" + iconString + "] at " + section);
            return null;
        } else {
            var material = Material.matchMaterial(iconString);
            if (material == null) {
                HoloAdvancements.getInstance().getLogger().warning("Invalid icon name [" + iconString + "] at " + section);
                return null;
            }

            return new ItemStack(material);
        }
    }

    private String loadTitle(FileConfiguration cfg, String basePath) {
        return cfg.getString(basePath + TITLE);
    }

    private AdvancementFrameType loadFrameType(FileConfiguration cfg, String basePath) {
        var frameTypeString = cfg.getString(basePath + FRAME_TYPE);
        if (frameTypeString == null) return null;

        return AdvancementFrameType.valueOf(frameTypeString.toUpperCase());
    }

    private List<String> loadDescription(FileConfiguration cfg, String basePath) {
        final var path = basePath + DESCRIPTION;
        var obj = cfg.get(path);
        if (obj == null) return null;

        if (obj instanceof List) {
            return cfg.getStringList(path);
        } else {
            return List.of(obj.toString());
        }
    }

    private boolean loadShowToast(FileConfiguration cfg, String basePath) {
        return cfg.getBoolean(basePath + SHOW_TOAST, true);
    }

    private boolean loadAnnounceChat(FileConfiguration cfg, String basePath) {
        return cfg.getBoolean(basePath + ANNOUNCE_CHAT, true);
    }

    private Float loadX(FileConfiguration cfg, String basePath) {
        var obj = cfg.get(basePath + X);
        if (obj instanceof Number number) {
            return NumberConversions.toFloat(number);
        } else {
            return null;
        }
    }

    private Float loadY(FileConfiguration cfg, String basePath) {
        var obj = cfg.get(basePath + Y);
        if (obj instanceof Number number) {
            return NumberConversions.toFloat(number);
        } else {
            return null;
        }
    }
}
