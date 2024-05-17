package com.github.treemanking.budgiesets.effects;

import com.github.treemanking.budgiesets.effects.processors.*;

/**
 * The EffectProcessorFactory class is responsible for creating instances of
 * EffectProcessor based on the specified effect type.
 */
public class EffectProcessorFactory {
    /**
     * Creates an EffectProcessor instance based on the given effect type.
     *
     * @param effectType the type of effect processor to create
     * @return an EffectProcessor instance corresponding to the event type, or null if the event type is not recognized
     */
    public static EffectProcessor createProcessor(String effectType) {
        return switch (effectType.toUpperCase()) {
            case "PERM_POTION" -> new PermPotionProcessor();
            case "ACTION_BAR" -> new ActionBarProcessor();
            case "HUNGER" -> new HungerProcessor();
            case "HEALTH" -> new HealthProcessor();
            case "BURN" -> new BurnProcessor();
            case "EXP_LEVEL" -> new ExpLevelProcessor();
            case "CANCEL_EVENT" -> new EventCancelProcessor();
            case "POTION" -> new PotionProcessor();
            case "PLAY_SOUND" -> new PlaySoundProcessor();
            case "PARTICLE" -> new ParticleProcessor();
            default -> null;
        };
    }
}
