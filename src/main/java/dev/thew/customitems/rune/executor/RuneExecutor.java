package dev.thew.customitems.rune.executor;

import dev.thew.customitems.rune.model.Rune;
import dev.thew.customitems.rune.service.RuneService;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class RuneExecutor implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NonNull CommandSender sender,@NonNull Command command,@NonNull String s,@NonNull String[] args) {

        if (!(sender instanceof Player player)){
            if (args.length == 2) {
                String runeName = args[0];
                String playerName = args[1];

                Rune rune = RuneService.getRuneById(runeName);
                if (rune == null) {
                    sender.sendMessage(" Такого айтема не существует!");
                    return false;
                }

                final Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) {
                    sender.sendMessage(" Игрок не в сети!");
                    return false;
                }

                final Inventory inventory = player.getInventory();
                inventory.addItem(rune.getItemStack()).forEach((i, is) -> player.getWorld().dropItemNaturally(player.getLocation(), is));
            }
            return true;
        }

        if (args.length == 1) {
            String runeName = args[0];
            Rune rune = RuneService.getRuneById(runeName);
            if (rune == null) {
                player.sendMessage(" Такого айтема не существует!");
                return false;
            }

            final Inventory inventory = player.getInventory();
            inventory.addItem(rune.getItemStack()).forEach((i, is) -> player.getWorld().dropItemNaturally(player.getLocation(), is));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender,@NonNull Command command,@NonNull String s, String[] args) {
        if (args.length == 1) return RuneService.getRunesId();
        return Collections.emptyList();
    }
}
