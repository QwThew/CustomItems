package dev.thew.customitems.talismans.service;

import dev.thew.customitems.talismans.model.Talisman;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TalismanService {

    private TalismanService(){
        throw new IllegalStateException("TalismanService class");
    }

    @Getter
    static List<Talisman> talismans = new ArrayList<>();

    @Getter
    static Map<Player, Talisman> cache = new HashMap<>();

    private static Talisman getTalisman(final Player player){
        if(cache.containsKey(player)) return cache.get(player);
        return null;
    }

    public static void update(final Player player, ItemStack offhand){
        Talisman cacheTalisman = getTalisman(player);
        Talisman handTalisman = getTalisman(offhand);


        if(cacheTalisman == null && handTalisman != null) handTalisman.give(player);
        else if(cacheTalisman != null && handTalisman == null) cacheTalisman.remove(player);
    }

    public static void loadConfiguration(FileConfiguration config){
        ConfigurationSection section = config.getConfigurationSection("talismans");
        Validate.notNull(section, "talismans section is null");

        for (String talismanId : section.getKeys(false)) {
            ConfigurationSection talismanSection = section.getConfigurationSection(talismanId);
            Validate.notNull(talismanSection, "talisman section is null");

            String materialName = talismanSection.getString("material");

            Material material = Material.valueOf(materialName);
            Validate.notNull(material, "material " + materialName + " is not found");

            String id = talismanSection.getString("id");
            Validate.notNull(id, "id is null");

            List<PotionEffect> effects = new ArrayList<>();

            ConfigurationSection effectsSection = talismanSection.getConfigurationSection("effects");
            if (effectsSection != null && !effectsSection.getKeys(false).isEmpty())
                for (String effectId : effectsSection.getKeys(false)) {

                    ConfigurationSection effectSection = effectsSection.getConfigurationSection(effectId);
                    Validate.notNull(effectSection, "effects section is null");

                    String effectName = effectSection.getString("effect");
                    Validate.notNull(effectName, "effect name is null");

                    PotionEffectType potionEffectType = PotionEffectType.getByName(effectName);
                    Validate.notNull(potionEffectType, "effect type " + effectName + " is not found");

                    int power = effectSection.getInt("power");

                    effects.add(new PotionEffect(potionEffectType, 99999, power));
                }

            talismans.add(Talisman.builder()
                    .effects(effects)
                    .material(material)
                    .nbt(id)
                    .build()
            );
        }
    }

    public static Talisman getTalisman(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        for (Talisman talisman : talismans)
            for (NamespacedKey key : container.getKeys())
                if (key.getKey().equalsIgnoreCase(talisman.getNbt())) return talisman;

        return null;
    }

    public static void shutDown(){
        cache.forEach((player, talisman) -> talisman.remove(player));
    }
}
