package net.tree.budgiesets.processor.factory;

import net.tree.budgiesets.processor.effects.ActionBarProcessor;
import net.tree.budgiesets.processor.effects.HungerProcessor;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;

public class EffectProcessorFactory {
    public static EffectProcessor createProcessor(String effectType) {
        switch (effectType.toUpperCase()) {
            case "ACTION_BAR":
                return new ActionBarProcessor();
            case "HUNGER":
                return new HungerProcessor();
            default:
                return null;
        }
    }
}
