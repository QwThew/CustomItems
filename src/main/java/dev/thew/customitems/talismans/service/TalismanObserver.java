package dev.thew.customitems.talismans.service;

import dev.thew.customitems.CustomItems;
import dev.thew.customitems.talismans.model.Talisman;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TalismanObserver implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();
        final ItemStack offHandItem = event.getOffHandItem();
        if (offHandItem == null) return;

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
    public void check(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();

        if (!item.hasItemMeta()) return;
        if (!item.getType().equals(Material.PLAYER_HEAD)) return;

        Talisman talisman = TalismanService.getTalisman(item);
        if (talisman == null) return;

        e.setCancelled(true);
        e.getPlayer().setNoDamageTicks(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getInventory().getType().equals(InventoryType.CRAFTING)) return;

        Bukkit.getScheduler().runTaskLater(CustomItems.getInstance(), () -> {
            final PlayerInventory inventory = player.getInventory();
            final ItemStack offhandItem = inventory.getItemInOffHand();

            TalismanService.update(player, offhandItem);
        }, 2L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        final PlayerInventory inventory = player.getInventory();
        final ItemStack offhandItem = inventory.getItemInOffHand();

        TalismanService.update(player, offhandItem);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void check(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final PlayerInventory inventory = player.getInventory();
        final ItemStack offhandItem = inventory.getItemInOffHand();

        Talisman talisman = TalismanService.getTalisman(offhandItem);
        if (talisman == null) return;

        talisman.remove(player);
    }

}
