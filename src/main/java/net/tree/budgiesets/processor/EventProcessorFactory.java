package net.tree.budgiesets.processor;

import net.tree.budgiesets.processor.events.EffectStaticProcessor;

public class EventProcessorFactory {
    public static EventProcessor createProcessor(String eventType) {
        switch (eventType.toUpperCase()) {
            case "EFFECT_STATIC":
                return new EffectStaticProcessor();
            // Add more cases for other event types as needed
            default:
                return null;
        }
    }
}


