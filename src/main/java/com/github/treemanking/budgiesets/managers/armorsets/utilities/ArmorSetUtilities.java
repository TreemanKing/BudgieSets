package com.github.treemanking.budgiesets.managers.armorsets.utilities;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ArmorSetUtilities {

    /**
     * Checks if the player is wearing a complete set of a specified armor.
     * <p>
     * This method retrieves the armor contents from the player's inventory and checks
     * if all four armor slots are occupied and if each piece has the required NBT tag
     * that matches the specified armor set name.
     * </p>
     * @param player the player whose armor is being checked
     * @param armorSetName the name of the armor set to check against
     * @return true if the player is wearing the full armor set with the specified NBT tag, false otherwise
     */
    default boolean isWearingFullSet(Player player, String armorSetName) {
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        // Check if the player is actually wearing the armor
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) {
                return false;
            }
        }

        // Check if all four armor pieces have the specified NBT tag
        boolean hasNbtTag = true;
        for (ItemStack armorPiece : armorContents) {
            if (!hasCustomNbtTag(armorPiece, armorSetName)) {
                // At least one armor piece does not have the required NBT tag
                hasNbtTag = false;
                break;
            }
        }
        return hasNbtTag;
    }

    /**
     * Checks if an ItemStack has a custom NBT tag matching a specified armor set name.
     *
     * <p>This method uses the NBT API to check if the item has an NBT tag with the key "armorSet"
     * and if the value of this tag matches the provided armor set name.</p>
     *
     * @param itemStack the item to check for the custom NBT tag
     * @param armorSetName the name of the armor set to check for in the NBT tag
     * @return true if the item has the NBT tag with the specified armor set name, false otherwise
     */
    default boolean hasCustomNbtTag(ItemStack itemStack, String armorSetName) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasTag("armorSet") && nbtItem.getString("armorSet").equalsIgnoreCase(armorSetName);
    }

    /**
     * Enumeration representing the equipment status of a player.
     * <ul>
     * This enum defines three possible statuses:
     * <li> EQUIPPED: The player is wearing the full armor set. </li>
     * <li> NOT_EQUIPPED: The player is not wearing the full armor set. </li>
     * <li> NULL: The player's equipment status is unknown, typically used when a player first joins. </li>
     * </ul>
     */
    enum EquipStatus {
        EQUIPPED,
        NOT_EQUIPPED,
        /**
         * For when someone joins, give them a status of "nothing"
         */
        NULL
    }
}
