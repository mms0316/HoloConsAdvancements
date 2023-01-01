package xyz.holocons.mc.holoa9t.tabs;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.holocons.mc.holoa9t.HoloAdvancements;
import xyz.holocons.mc.holoa9t.file.FileConfigurable;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseTab {
    protected String namespace;
    protected String key;
    protected String title;
    protected String description;
    protected String backgroundTexture;
    protected Material icon;
    protected AdvancementFrameType frame = AdvancementFrameType.TASK;
    protected boolean showToast = true;
    protected boolean announceChat = true;
    protected float x = 0;
    protected float y = 0;
    protected boolean grantRootAdvancement = false;

    private AdvancementTab tab = null;
    private RootAdvancement root = null;
    private HashSet<BaseAdvancement> children;

    public BaseTab() {
    }

    public AdvancementTab getTab() {
        if (tab == null)
            tab = HoloAdvancements.getInstance().getAPI().createAdvancementTab(namespace);
        return tab;
    }

    protected RootAdvancement getRootAdvancement() {
        if (root == null)
            root = new RootAdvancement(getTab(), namespace + "." + key,
                new AdvancementDisplay(icon, title, frame, showToast, announceChat, x, y, description),
                backgroundTexture);
        return root;
    }

    protected Set<BaseAdvancement> getChildren() {
        if (children == null)
            children = new HashSet<>();
        return children;
    }

    protected void register() {
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

    public boolean setInitialConfigValues(FileConfiguration cfg) {
        boolean changed = false;
        for (var adv : getChildren()) {
            if (adv instanceof FileConfigurable fileConfigurable) {
                changed = changed || fileConfigurable.setInitialConfigValues(cfg);
            }
        }
        return changed;
    }

    public void loadConfigValues(FileConfiguration cfg) {
        for (var adv : getChildren()) {
            if (adv instanceof FileConfigurable fileConfigurable) {
                fileConfigurable.loadConfigValues(cfg);
            }
        }
    }
}
