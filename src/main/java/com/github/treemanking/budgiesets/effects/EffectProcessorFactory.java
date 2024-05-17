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
        switch (effectType.toUpperCase()) {
            case "PERM_POTION":
                return new PermPotionProcessor();
            case "ACTION_BAR":
                return new ActionBarProcessor();
            case "HUNGER":
                return new HungerProcessor();
            case "HEALTH":
                return new HealthProcessor();
            case "BURN":
                return new BurnProcessor();
            case "EXP_LEVEL":
                return new ExpLevelProcessor();
            case "CANCEL_EVENT":
                return new EventCancelProcessor();
            case "POTION":
                return new PotionProcessor();
            case "PLAY_SOUND":
                return new PlaySoundProcessor();
            case "PARTICLE":
                return new ParticleProcessor();
            default:
                return null;
        }
    }
}
