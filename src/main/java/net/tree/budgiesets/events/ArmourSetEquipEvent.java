package net.tree.budgiesets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class ArmourSetEquipEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final EquipStatus equipStatus;

    public ArmourSetEquipEvent (@NotNull Player player, EquipStatus equipStatus) {
        super(player);
        this.equipStatus = equipStatus;
    }

    /**
     * Gets whether the player has equipped or unequipped the armor set
     * @return Equip Status of the Armor Set
     */
    @NotNull
    public EquipStatus getEquipStatus() {
        return this.equipStatus;
    }

    @NotNull
    public HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public enum EquipStatus {
        EQUIPPED,
        UNEQUIPPED
    }
}
