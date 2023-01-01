package xyz.holocons.mc.holoa9t;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingFailedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.holocons.mc.holoa9t.tabs.BaseTab;
import xyz.holocons.mc.holoa9t.tabs.OverworldSpawnTab;

import java.util.HashSet;
import java.util.Set;

public class HoloAdvancements extends JavaPlugin implements Listener {
    static HoloAdvancements instance;
    public static HoloAdvancements getInstance() { return instance; }

    UltimateAdvancementAPI api;
    public UltimateAdvancementAPI getAPI() { return api; }

    Set<BaseTab> tabs;
    private Set<BaseTab> getTabs() {
        if (tabs == null) {
            tabs = new HashSet<>();

            //Put new tabs here
            tabs.add(new OverworldSpawnTab(true));
        }

        return tabs;
    }

    @Override
    public void onEnable() {
        instance = this;
        api = UltimateAdvancementAPI.getInstance(this);
        loadInitialConfigFile();

        getLogger().info("Loaded");
    }

    private void loadInitialConfigFile() {
        var cfg = this.getConfig();

        var changed = false;

        //Initial configs here
        if (cfg.get("reloadPeriod") == null) {
            cfg.set("reloadPeriod", 5 * 60);
            changed = true;
        }

        //Initial configs per advancement tab
        for (var tab : getTabs()) {
            if (tab.setInitialConfigValues(cfg))
                changed = true;

            tab.loadConfigValues(cfg);
        }

        if (changed) {
            this.saveConfig();
        }

        //Automatic file reload
        var reloadPeriod = cfg.getInt("reloadPeriod");
        if (reloadPeriod > 0) {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, this::loadNewConfigFile, reloadPeriod, reloadPeriod);
        }
    }

    void loadNewConfigFile() {
        this.reloadConfig();

        var cfg = this.getConfig();

        for (var tab : getTabs()) {
            tab.loadConfigValues(cfg);
        }

        getLogger().info("Reloaded config file");
    }

    @EventHandler
    public void onLoadFail(PlayerLoadingFailedEvent event) {
        getLogger().warning("Unable to load achievements for " + event.getPlayer().getDisplayName());
    }

    /*
    public static class HolorailsCartAdv extends BaseAdvancement {

        public HolorailsCartAdv(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent, @Range(from = 1L, to = 2147483647L) int maxProgression) {
            super(key, display, parent, maxProgression);

            // Register the event via the tab's EventManager
            registerEvent(BlockBreakEvent.class, e -> {
                Player player = e.getPlayer();
                if (isVisible(player) && e.getBlock().getType() == Material.STONE) {
                    incrementProgression(player);
                }
            });
        }

        @Override
        public void giveReward(@NotNull Player player) {
            // This code is called when the advancement is granted
            player.getInventory().addItem(new ItemStack(Material.STONE_BRICKS));
        }
    }
    */
}
