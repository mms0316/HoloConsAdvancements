package xyz.holocons.mc.holoa9t.tabs;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.display.DisplayBuilder;
import xyz.holocons.mc.holoa9t.file.FileConfigurable;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseTab implements FileConfigurable {
    static final String TAB_SECTION = "tab";
    static final String BACKGROUND_TEXTURE = "background_texture";
    protected String backgroundTexture;

    protected DisplayBuilder rootDisplayBuilder;

    protected String sectionName;
    protected boolean grantRootAdvancement = false;

    private AdvancementTab tab = null;
    private RootAdvancement root = null;
    private HashSet<BaseAdvancement> children;

    public BaseTab(String sectionName) {
        this.sectionName = sectionName;

        rootDisplayBuilder = new DisplayBuilder("display" + "." + sectionName);
    }

    public AdvancementTab getTab() {
        if (tab == null)
            tab = HoloAdvancements.getInstance().getAPI().createAdvancementTab(getSectionName());
        return tab;
    }

    protected RootAdvancement getRootAdvancement() {
        if (root == null) {
            var display = rootDisplayBuilder.build();
            if (display != null) {
                root = new RootAdvancement(getTab(), getSectionName(),
                        display,
                        backgroundTexture);
            }
        }
        return root;
    }

    protected Set<BaseAdvancement> getChildren() {
        if (children == null)
            children = new HashSet<>();
        return children;
    }

    public void register() {
        var tab = getTab();

        tab.registerAdvancements(getRootAdvancement(), getChildren());

        var eventManager = tab.getEventManager();
        if (grantRootAdvancement) {
            eventManager.register(tab, PlayerLoadingCompletedEvent.class, event -> {
                tab.showTab(event.getPlayer());
                tab.grantRootAdvancement(event.getPlayer()); //show this adv as soon as possible
            });
        }
    }

    @Override
    public String getSectionName() {
        return sectionName;
    }

    @Override
    public boolean setInitialConfigValues(FileConfiguration cfg) {
        boolean changed = rootDisplayBuilder.setInitialConfigValues(cfg);

        final String basePath = getBasePath();
        String path;

        path = basePath + BACKGROUND_TEXTURE;
        if (cfg.get(path) == null) {
            cfg.set(path, "Use a minecraft texture like textures/block/tnt_top.png");
            changed = true;
        }

        for (var adv : getChildren()) {
            if (adv instanceof FileConfigurable fileConfigurable) {
                if (fileConfigurable.setInitialConfigValues(cfg)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean loadConfigValues(FileConfiguration cfg) {
        boolean fail = !rootDisplayBuilder.loadConfigValues(cfg);

        backgroundTexture = loadBackgroundTexture(cfg, getBasePath());
        if (backgroundTexture == null)
            fail = true;

        for (var adv : getChildren()) {
            if (adv instanceof FileConfigurable fileConfigurable) {
                if (!fileConfigurable.loadConfigValues(cfg)) {
                    fail = true;
                }
            }
        }

        return !fail;
    }

    protected String loadBackgroundTexture(FileConfiguration cfg, String basePath) {
        var value = cfg.getString(basePath + BACKGROUND_TEXTURE);
        if (value != null && !value.matches("[a-z0-9/._-]+"))
            value = null;

        if (value == null) {
            value = ""; // This will create the infamous black & magenta texture: https://minecraft.fandom.com/wiki/Missing_textures_and_models
        }

        return value;
    }

    private String getBasePath() {
        return TAB_SECTION + "." + getSectionName() + ".";
    }
}
