package dev.thew.customitems.rune.service;

import dev.thew.customitems.rune.model.Rune;
import dev.thew.customitems.utils.Utils;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RuneService {

    private RuneService(){
        throw new IllegalStateException("RuneService class");
    }

    @Getter
    static List<Rune> runes = new ArrayList<>();

    public static void loadConfiguration(FileConfiguration config){
        ConfigurationSection runesSection = config.getConfigurationSection("runes");
        Validate.notNull(runesSection, "runes section is null");

        for (String runeId : runesSection.getKeys(false)) {
            ConfigurationSection runeSection = runesSection.getConfigurationSection(runeId);
            Validate.notNull(runeSection, "rune section is null");

            String materialName = runeSection.getString("material");
            Material material = Material.valueOf(materialName);

            String itemName = runeSection.getString("name");
            List<String> lore = runeSection.getStringList("lore");

            List<String> flagsStrings = runeSection.getStringList("flags");
            List<ItemFlag> flags = Utils.getItemFlags(flagsStrings);

            ItemStack itemRune = new ItemStack(material);

            ItemMeta meta = itemRune.getItemMeta();
            assert meta != null;

            meta.setDisplayName(itemName);
            meta.setLore(lore);

            if (!flags.isEmpty()) for (ItemFlag flag : flags) meta.addItemFlags(flag);

            ConfigurationSection enchantsSection = runeSection.getConfigurationSection("enchants");
            if (enchantsSection != null && !enchantsSection.getKeys(false).isEmpty())
                for (String enchantId : enchantsSection.getKeys(false)) {

                    ConfigurationSection enchantSection = enchantsSection.getConfigurationSection(enchantId);
                    Validate.notNull(enchantSection, "Enchant " + enchantId + " not found");

                    String enchantName = enchantSection.getString("enchant");
                    Validate.notNull(enchantName, "enchant name cannot be null");

                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantName));
                    Validate.notNull(enchantment, "Invalid enchantment: " + enchantName);

                    int level = enchantSection.getInt("level");
                    boolean ignoreLevelRestriction = enchantSection.getBoolean("iLR");

                    meta.addEnchant(enchantment, level, ignoreLevelRestriction);
                }

            ConfigurationSection attributesSection = runeSection.getConfigurationSection("attributes");
            if (attributesSection != null && !attributesSection.getKeys(false).isEmpty())
                for (String attributeId : attributesSection.getKeys(false)) {

                    ConfigurationSection attributeSection = attributesSection.getConfigurationSection(attributeId);
                    Validate.notNull(attributeSection, "Attribute " + attributeId + " is null");

                    String attributeName = attributeSection.getString("attribute");
                    Attribute attribute = Attribute.valueOf(attributeName);

                    String attributeModifierName = attributeSection.getString("attributename");
                    Validate.notNull(attributeModifierName, "Attribute modifier name cannot be null");

                    int amount = attributeSection.getInt("amount");

                    String operationName = attributeSection.getString("operation");
                    AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(operationName);

                    String equipmentName = attributeSection.getString("equipmentslot");
                    EquipmentSlot equipmentSlot = EquipmentSlot.valueOf(equipmentName);

                    meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attributeModifierName, amount, operation, equipmentSlot));
                }

            itemRune.setItemMeta(meta);

            runes.add(Rune.builder()
                    .id(runeId)
                    .itemStack(itemRune)
                    .build()
            );
        }
    }

    public static Rune getRuneById(String id){
        for (Rune rune : runes)
            if (rune.getId().equalsIgnoreCase(id)) return rune;

        return null;
    }

    public static List<String> getRunesId(){
        List<String> runesId = new ArrayList<>();
        runes.forEach(rune -> runesId.add(rune.getId()));

        return runesId;
    }
}
