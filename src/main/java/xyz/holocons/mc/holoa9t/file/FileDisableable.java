package xyz.holocons.mc.holoa9t.file;

import org.bukkit.configuration.file.FileConfiguration;

public interface FileDisableable extends FileConfigurable {
    boolean getDisabled();
    void setDisabled(boolean isDisabled);

    default boolean setInitialConfigValues(FileConfiguration cfg) {
        final var sectionName = getSectionName();
        if (cfg.contains(sectionName)) return false;

        var cfgSection = cfg.createSection(sectionName);
        cfgSection.set("disabled", false);

        return true;
    }

    default boolean loadConfigValues(FileConfiguration cfg) {
        final var sectionName = getSectionName();

        final var pathSep = cfg.options().pathSeparator();

        var isDisabled = cfg.getBoolean(sectionName + pathSep + "disabled", false);
        setDisabled(isDisabled);
        return true;
    }
}
