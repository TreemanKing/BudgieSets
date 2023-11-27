package net.tree.budgiesets.eventlisteners;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ArmorSetEquip implements Listener {

    private final String armorSetName;
    private final ConfigurationSection armorSetConfig;

    public ArmorSetEquip(String armorSetName, ConfigurationSection armorSetConfig) {
        this.armorSetName = armorSetName;
        this.armorSetConfig = armorSetConfig;
    }

    @EventHandler
    public void onPlayerEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        if (isWearingFullSet(player)) {
            player.sendMessage(ChatColor.GREEN + "You are now wearing the " + armorSetName + " set");
            // CHECK ARMOR SET EVENTS AND EFFECTS

        }
    }

    private boolean isWearingFullSet(Player player) {
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        // Check if all four armor pieces have the specified NBT tag
        boolean hasNbtTag = true;
        for (ItemStack armorPiece : armorContents) {
            if (!hasCustomNbtTag(armorPiece, "armorSet:"+armorSetName)) {
                hasNbtTag = false;
                break;
            }
        }

        // Check if the player is actually wearing the armor
        boolean isWearingArmor = false;
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece.getType() != Material.AIR) {
                isWearingArmor = true;
                break;
            }
        }

        return hasNbtTag && isWearingArmor;
    }

    private boolean hasCustomNbtTag(ItemStack itemStack, String name) {
        ReadWriteNBT nbt = NBT.itemStackToNBT(itemStack);
        return nbt.hasTag(name);
    }
}
