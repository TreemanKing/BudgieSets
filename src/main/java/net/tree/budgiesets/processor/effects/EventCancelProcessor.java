package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class EventCancelProcessor implements EffectProcessor {

    private static final String BOOLEAN_KEY = "Boolean";

    @Override
    public void processEffect(List<?> eventCancels, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {

        if (!(event instanceof Cancellable)) {
            Bukkit.getLogger().warning("Event not cancellable: " + event.getEventName());
            return;
        }
        Cancellable cancellableEvent = (Cancellable) event;

        for (Object eventCancel : eventCancels) {
            if (eventCancel instanceof Map<?, ?>) {
                Map<?, ?> eventCancelMap = (Map<?, ?>) eventCancel;
                if (validateEventCancelConifg(eventCancelMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    boolean eventCancelStatus = (boolean) eventCancelMap.get(BOOLEAN_KEY);
                    List<String> conditions = (List<String>) eventCancelMap.get("Conditions");

                    if (checkConditions(conditions, player) && eventCancelStatus) {
                        applyEventCancel(cancellableEvent);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid Cancel Event configuration: " + eventCancelMap);
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
