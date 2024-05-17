package com.github.treemanking.budgiesets.events;

import com.github.treemanking.budgiesets.events.processors.*;

/**
 * The EventProcessorFactory class is responsible for creating instances of
 * EventProcessor based on the specified event type.
 */
public class EventProcessorFactory {

    /**
     * Creates an EventProcessor instance based on the given event type.
     *
     * @param eventType the type of event processor to create
     * @return an EventProcessor instance corresponding to the event type, or null if the event type is not recognized
     */
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
