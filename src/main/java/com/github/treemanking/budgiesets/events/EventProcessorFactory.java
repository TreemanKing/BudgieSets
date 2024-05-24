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
        return switch (eventType.toUpperCase()) {
            case "ON_EQUIP" -> new OnEquipProcessor();
            case "CONSUME" -> new ConsumeProcessor();
            case "JUMP" -> new JumpProcessor();
            case "HOOK" -> new HookProcessor();
            case "ELYTRA_BOOST" -> new ElytraBoostProcessor();
            case "ATTACK" -> new AttackProcessor();
            case "DAMAGED" -> new DamagedProcessor();
            case "ON_POTION_EFFECT" -> new OnPotionEffectProcessor();
            case "SNEAK" -> new SneakProcessor();
            case "SPRINT" -> new SprintProcessor();
            case "ON_RIPTIDE" -> new RiptideProcessor();
            default -> null;
        };
    }
}
