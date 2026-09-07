package com.github.treemanking.budgiesets.utilities;

/**
 * Holder for the configuration key names used throughout the effect and event processors.
 *
 * <p>This class is not meant to be instantiated or subclassed. Import the constants
 * where they are needed:</p>
 *
 * <pre>{@code
 * import static com.github.treemanking.budgiesets.utilities.ProcessorKeys.*;
 * }</pre>
 */
public final class ProcessorKeys {

    public static final String TYPE_KEY = "Type";
    public static final String AMPLIFIER_KEY = "Amplifier";
    public static final String AMBIENT_KEY = "Ambient";
    public static final String PARTICLES_KEY = "Particles";
    public static final String DURATION_KEY = "Duration";
    public static final String PITCH_KEY = "Pitch";
    public static final String TIME_KEY = "Time";
    public static final String VOLUME_KEY = "Volume";
    public static final String SOUND_KEY = "Sound";
    public static final String BOOLEAN_KEY = "Boolean";
    public static final String AMOUNT_KEY = "Amount";
    public static final String PARTICLE_KEY = "Particle";
    public static final String COLOR_KEY = "Hex-Color";
    public static final String COUNT_KEY = "Count";
    public static final String DATA_KEY = "Data";
    public static final String MATERIAL_KEY = "Material";
    public static final String INT_KEY = "Int";
    public static final String FLOAT_KEY = "Float";
    public static final String ARRIVAL_TIME_KEY = "Arrival-Time";
    public static final String FROM_COLOR_KEY = "From-Hex";
    public static final String TO_COLOR_KEY = "To-Hex";
    public static final String SIZE_KEY = "Size";
    public static final String OFFSET_KEY = "Offset";
    public static final String ACTION_TYPE_KEY = "Action-Type";
    public static final String TEXT_KEY = "Text";
    public static final String EFFECT_TARGET = "Effect-Target";
    public static final String ATTRIBUTE_KEY = "Attribute-Type";
    public static final String OPERATION_KEY = "Operation";
    public static final String CONDITIONS_KEY = "Conditions";
    public static final String CHANCE_KEY = "Chance";
    public static final String COOLDOWN_KEY = "Cooldown";

    private ProcessorKeys() {
        throw new AssertionError("ProcessorKeys is a constants holder and must not be instantiated");
    }
}
