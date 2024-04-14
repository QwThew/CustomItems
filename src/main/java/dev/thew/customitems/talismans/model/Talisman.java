package dev.thew.customitems.talismans.model;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Getter
@Builder
public class Talisman {
    String nbt;

    Material material;

    List<PotionEffect> effects;

    public void give(Player player) {
        player.addPotionEffects(effects);
    }

    public void remove(Player player) {
        effects.forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }
}
