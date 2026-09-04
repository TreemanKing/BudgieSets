package com.github.treemanking.budgiesets.utilities;

/**
 * Enumeration representing the equipment status of a player with respect to an armor set.
 * <ul>
 * <li> EQUIPPED: The player is wearing the full armor set. </li>
 * <li> NOT_EQUIPPED: The player is not wearing the full armor set. </li>
 * <li> NULL: The player's equipment status is unknown, typically used when a player first joins. </li>
 * </ul>
 */
public enum EquipStatus {
    EQUIPPED,
    NOT_EQUIPPED,
    /**
     * For when someone joins, give them a status of "nothing"
     */
    NULL
}

