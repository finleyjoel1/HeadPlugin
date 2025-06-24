package dev.finley;

import dev.finley.commands.HeadCommand;
import dev.finley.language.LanguageManager;
import dev.finley.listener.ChatListener;
import dev.finley.listener.InvClickListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class HeadPlugin extends JavaPlugin {

    @Getter
    private static HeadPlugin instance;

    private String prefix = "";

    private LanguageManager languageManager;

    private FileConfiguration messageFile;

    private HashMap<UUID, String> changePlayers = new HashMap<>();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        File messageF = new File(getDataFolder(), "messages.yml");
        messageFile = YamlConfiguration.loadConfiguration(messageF);
    }

    @Override
    public void onEnable() {
        instance = this;

        // ---> setup <---

        //manager
        languageManager = new LanguageManager();

        //set data from file
        languageManager.setSelectedLanguage(getConfig().getString("settings.language"));
        prefix = getConfig().getString("settings.prefix");

        //register command
        Objects.requireNonNull(getCommand("head")).setExecutor(new HeadCommand());

        List.of(new InvClickListener(), new ChatListener(changePlayers)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, getInstance()));

        // ---> setup <---
    }

    @Override
    public void onDisable() {

    }
}
