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
    String nbt;
    List<PotionEffect> effects;

    public void give(Player player) {
        player.addPotionEffects(effects);
        TalismanService.getCache().put(player, this);
    }

    public void remove(Player player) {
        effects.forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        TalismanService.getCache().remove(player);
    }
}
