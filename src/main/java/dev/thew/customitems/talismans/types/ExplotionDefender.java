package dev.thew.customitems.talismans.types;

//import dev.thew.customitems.talismans.model.Talisman;
//import dev.thew.customitems.talismans.service.TalismanService;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.player.PlayerTeleportEvent;
//import org.bukkit.inventory.ItemStack;

public class ExplotionDefender implements Listener {

//    @EventHandler(priority = EventPriority.LOWEST)
//    public void damage(EntityDamageEvent event) {
//        if (!(event.getEntity() instanceof Player player)) return;
//
//        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
//            Talisman talisman = TalismanService.getTalisman(player);
//            if (talisman == null) return;
//
//            if (!talisman.getNbt().equalsIgnoreCase("hasteexp")) return;
//
//            event.setDamage(event.getDamage() / 2);
//        }
//
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void enderPearlThrown(PlayerTeleportEvent event) {
//        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
//        final Player player = event.getPlayer();
//
//        Talisman talisman = TalismanService.getTalisman(player);
//        if (talisman == null) return;
//
//        String id = "heavenly";
//        if (!talisman.getNbt().equalsIgnoreCase(id)) return;
//
//        event.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
//    }
}
