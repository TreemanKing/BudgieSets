package net.tree.budgiesets.processor.factory;

import net.tree.budgiesets.processor.events.AttackProcessor;
import net.tree.budgiesets.processor.events.JumpProcessor;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import net.tree.budgiesets.processor.events.ConsumeProcessor;
import net.tree.budgiesets.processor.events.EffectStaticProcessor;

public class EventProcessorFactory {
    public static EventProcessor createProcessor(String eventType) {
        switch (eventType.toUpperCase()) {
            case "EFFECT_STATIC":
                return new EffectStaticProcessor();
            case "CONSUME":
                return new ConsumeProcessor();
            case "JUMP":
                return new JumpProcessor();
            case "ATTACK":
                return new AttackProcessor();
            default:
                return null;
        }
    }
}


