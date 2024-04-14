package dev.thew.customitems.rune.model;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@Builder
public class Rune {
    String id;
    ItemStack itemStack;
}
