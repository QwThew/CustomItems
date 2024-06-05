package dev.thew.customitems;

import dev.thew.customitems.rune.executor.RuneExecutor;
import dev.thew.customitems.rune.service.RuneService;
import dev.thew.customitems.talismans.executor.TalismanExecutor;
import dev.thew.customitems.talismans.service.TalismanObserver;
import dev.thew.customitems.talismans.service.TalismanService;
import dev.thew.customitems.utils.Utils;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CustomItems extends JavaPlugin {

    @Getter
    private static CustomItems instance = null;

    @Override
    public void onEnable() {
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();

        FileConfiguration config = getConfig();

        RuneService.loadConfiguration(config);
        TalismanService.loadConfiguration(config);
        TalismanService.loadCustomEvents();

        RuneExecutor runeExecutor = new RuneExecutor();
        Utils.hookCommand("rune", runeExecutor, runeExecutor);

        TalismanExecutor talismanExecutor = new TalismanExecutor();
        Utils.hookCommand("talisman", talismanExecutor, null);

        TalismanObserver talismanObserver = new TalismanObserver();
        getServer().getPluginManager().registerEvents(talismanObserver, this);
    }

    @Override
    public void onDisable() {
        TalismanService.shutDown();
    }
}
