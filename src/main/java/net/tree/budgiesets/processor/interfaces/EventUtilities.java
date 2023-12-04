package net.tree.budgiesets.processor.interfaces;

import java.util.Map;

public interface EventUtilities {

    default boolean checkCancelled(Map<?, ?> effectsMap) {
        final String CANCELLED_KEY = "Cancelled";
        if (effectsMap == null) return false;
        if (!effectsMap.containsKey(CANCELLED_KEY)) return false;
        if (!(effectsMap.get(CANCELLED_KEY) instanceof Boolean)) return false;
        return (boolean) effectsMap.get(CANCELLED_KEY);
    }
}
