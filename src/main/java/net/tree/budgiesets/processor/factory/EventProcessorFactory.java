package net.tree.budgiesets.processor.factory;

import net.tree.budgiesets.processor.interfaces.EventProcessor;
import net.tree.budgiesets.processor.events.EatProcessor;
import net.tree.budgiesets.processor.events.EffectStaticProcessor;

public class EventProcessorFactory {
    public static EventProcessor createProcessor(String eventType) {
        switch (eventType.toUpperCase()) {
            case "EFFECT_STATIC":
                return new EffectStaticProcessor();
            case "EAT":
                return new EatProcessor();
            default:
                return null;
        }
    }
}


