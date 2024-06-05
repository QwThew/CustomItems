package dev.thew.customitems.utils;

import dev.thew.customitems.CustomItems;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static void hookCommand(String command, CommandExecutor executor, TabCompleter completer) {
        PluginCommand pluginCommand = CustomItems.getInstance().getCommand(command);
        assert pluginCommand != null;
        if (executor != null) pluginCommand.setExecutor(executor);
        if (completer != null) pluginCommand.setTabCompleter(completer);
    }

    public static List<ItemFlag> getItemFlags(List<String> args) {
        List<ItemFlag> flags = new ArrayList<>();

        args.forEach(arg -> flags.add(ItemFlag.valueOf(arg)));

        return flags;
    }

}
