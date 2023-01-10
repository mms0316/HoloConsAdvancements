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
    protected DisplayBuilder rootDisplayBuilder;

    protected String sectionName;
    protected String backgroundTexture;
    protected boolean grantRootAdvancement = false;

    private AdvancementTab tab = null;
    private RootAdvancement root = null;
    private HashSet<BaseAdvancement> children;

    public BaseTab(String sectionName) {
        this.sectionName = sectionName;

        rootDisplayBuilder = new DisplayBuilder(sectionName);
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

        for (var adv : getChildren()) {
            if (adv instanceof FileConfigurable fileConfigurable) {
                if (!fileConfigurable.loadConfigValues(cfg)) {
                    fail = true;
                }
            }
        }

        return !fail;
    }
}
