package net.tree.budgiesets.processor.factory;

import net.tree.budgiesets.processor.events.*;
import net.tree.budgiesets.processor.interfaces.EventProcessor;

public class EventProcessorFactory {
    public static EventProcessor createProcessor(String eventType) {
        switch (eventType.toUpperCase()) {
            case "EFFECT_STATIC":
                return new EffectStaticProcessor();
            case "CONSUME":
                return new ConsumeProcessor();
            case "JUMP":
                return new JumpProcessor();
            case "HOOK":
                return new HookProcessor();
            case "PLAYER_ELYTRA_BOOST":
                return new ElytraBoostProcessor();
                /*
            case "ATTACK":
                return new AttackProcessor();
                 */
            default:
                return null;
        }
    }
}


