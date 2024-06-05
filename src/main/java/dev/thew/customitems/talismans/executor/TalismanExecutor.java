package dev.thew.customitems.talismans.executor;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.thew.customitems.talismans.service.TalismanService;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.UUID;

public class TalismanExecutor implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        assert skullMeta != null;
        skullMeta.setDisplayName("§f" + args[0]);

        skullMeta.getPersistentDataContainer().set(TalismanService.getNamespacedKey(), PersistentDataType.STRING, args[0]);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", args[1]));

        Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
        mtd.setAccessible(true);
        mtd.invoke(skullMeta, profile);

        head.setItemMeta(skullMeta);
        player.getInventory().addItem(head);
        return true;
    }
}
