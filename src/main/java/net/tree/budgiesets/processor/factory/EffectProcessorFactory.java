package net.tree.budgiesets.processor.factory;

import net.tree.budgiesets.processor.effects.*;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;

public class EffectProcessorFactory {
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
            default:
                return null;
        }
    }
}
