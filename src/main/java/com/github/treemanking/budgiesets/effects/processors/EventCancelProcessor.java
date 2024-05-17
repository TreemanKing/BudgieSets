package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class EventCancelProcessor implements EffectProcessor {

    private final String BOOLEAN_KEY = "Boolean";

    @Override
    public void processEffect(List<?> eventCancels, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {

        if (!(event instanceof Cancellable cancellableEvent)) {
            BudgieSets.getBudgieSets().getLogger().warning("Event not cancellable: " + event.getEventName());
            return;
        }

        for (Object eventCancel : eventCancels) {
            if (eventCancel instanceof Map<?, ?> eventCancelMap) {
                if (validateEventCancelConifg(eventCancelMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    boolean eventCancelStatus = (boolean) eventCancelMap.get(BOOLEAN_KEY);
                    List<String> conditions = (List<String>) eventCancelMap.get("Conditions");

                    if (checkConditions(conditions, player) && eventCancelStatus) {
                        applyEventCancel(cancellableEvent);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid Cancel Event configuration: " + eventCancelMap);
                }
            }
        }
    }

    private void applyEventCancel(@NotNull Cancellable event) {
        event.setCancelled(true);
    }

    private boolean validateEventCancelConifg(Map<?, ?> eventCancelMap) {
        return eventCancelMap.containsKey(BOOLEAN_KEY) && (boolean) eventCancelMap.get(BOOLEAN_KEY);
    }

}
