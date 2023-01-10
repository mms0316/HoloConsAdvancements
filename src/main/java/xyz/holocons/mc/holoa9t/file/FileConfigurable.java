package xyz.holocons.mc.holoa9t.file;

import org.bukkit.configuration.file.FileConfiguration;

public interface FileConfigurable {
    String getSectionName();

    boolean setInitialConfigValues(FileConfiguration cfg);

    boolean loadConfigValues(FileConfiguration cfg);

    //helper for returning "." (YAML) or "/" (file path in case this ever happens)
    default char getConfigPathSeparator(FileConfiguration cfg) {
        return cfg.options().pathSeparator();
    }
}
