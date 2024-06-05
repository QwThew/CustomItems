package dev.thew.customitems.talismans.model;

import dev.thew.customitems.talismans.service.TalismanService;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Getter
@Builder
public class Talisman {
    final String nbt;
    final List<PotionEffect> effects;

    public void give(final Player player) {
        player.addPotionEffects(effects);
        TalismanService.addCache(player, this);
    }

    public void remove(final Player player) {
        effects.forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        TalismanService.removeCache(player);
    }
}
