package dev.thew.customitems.talismans.service;

import dev.thew.customitems.CustomItems;
import dev.thew.customitems.talismans.model.Talisman;
import dev.thew.customitems.talismans.types.ExplotionDefender;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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

    @Getter
    static NamespacedKey namespacedKey;

    public static Talisman getTalisman(final Player player){
        if(cache.containsKey(player)) return cache.get(player);
        return null;
    }

    public static void update(final Player player,final ItemStack offhand) {
        Talisman cacheTalisman = getTalisman(player);
        Talisman handTalisman = getTalisman(offhand);

        if(cacheTalisman == null && handTalisman != null) handTalisman.give(player);
        else if(cacheTalisman != null && handTalisman == null) cacheTalisman.remove(player);
    }

    public static void loadCustomEvents(){
        Bukkit.getPluginManager().registerEvents(new ExplotionDefender(), CustomItems.getInstance());
    }

    public static void loadConfiguration(FileConfiguration config) {
        namespacedKey = new NamespacedKey(CustomItems.getInstance(), "talisman");

        ConfigurationSection section = config.getConfigurationSection("talismans");
        Validate.notNull(section, "talismans section is null");

        for (String talismanId : section.getKeys(false)) {
            ConfigurationSection talismanSection = section.getConfigurationSection(talismanId);
            Validate.notNull(talismanSection, "talisman section is null");

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
                    .nbt(id)
                    .build()
            );
        }
    }

    public static Talisman getTalisman(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        String talismanId = container.get(namespacedKey, PersistentDataType.STRING);

        for (Talisman talisman : talismans)
            if (talisman.getNbt().equalsIgnoreCase(talismanId)) return talisman;

        return null;
    }

    public static void removeCache(final Player player){
        cache.remove(player);
    }

    public static void addCache(final Player player, final Talisman talisman){
        cache.put(player, talisman);
    }

    public static void shutDown(){
        cache.forEach((player, talisman) -> talisman.remove(player));
    }
}
