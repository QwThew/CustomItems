package dev.thew.customitems.talismans.service;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TalismanObserver implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();
        final ItemStack offHandItem = event.getOffHandItem();

        TalismanService.update(player, offHandItem);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack offHandItem = inventory.getItemInOffHand();

        TalismanService.update(player, offHandItem);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();

        if (!(message.equalsIgnoreCase("/ci") || message.equalsIgnoreCase("/clearinventory"))) return;

        final Player player = e.getPlayer();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack offhandItem = inventory.getItemInOffHand();

        TalismanService.update(player, offhandItem);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getInventory().getType().equals(InventoryType.CRAFTING)) return;

        final PlayerInventory inventory = player.getInventory();
        final ItemStack offhandItem = inventory.getItemInOffHand();

        TalismanService.update(player, offhandItem);
    }

}
