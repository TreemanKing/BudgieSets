package com.github.treemanking.budgiesets.effects.processors;

import com.destroystokyo.paper.ParticleBuilder;
import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

public class ParticleProcessor implements EffectProcessor {

    private final String PARTICLE_KEY = "Particle";
    private final String COLOR_KEY = "RGB";
    private final String COUNT_KEY = "Count";
    private final String DATA_KEY = "Data";

    @Override
    public void processEffect(List<?> particles, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object particle : particles) {
            if (particle instanceof Map<?, ?> particleMap) {
                if (validateParticleConfig(particleMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    Particle particleType = Enum.valueOf(Particle.class, (String) particleMap.get(PARTICLE_KEY));
                    int rgb = (int) particleMap.get(COLOR_KEY);
                    int count = (int) particleMap.get(COUNT_KEY);
                    System.out.println(particleMap.get(DATA_KEY));
                    Class <?> particleDataType = particleType.getDataType();

                    List<String> conditions = (List<String>) particleMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        // TODO: Verify and check against the data field for each type of datafield
                        spawnParticle(player, particleType, rgb, count, null);
                    }

                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid particle configuration: " + particleMap);
                }
            }
        }
    }

    private void spawnParticle(Player player, Particle particle, int color, int count, Object data) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        particleBuilder.location(player.getLocation()).count(count).color(Color.fromRGB(color));
    }

    private boolean validateParticleConfig(Map<?, ?> particleMap) {
        return particleMap.containsKey(PARTICLE_KEY)
                && particleMap.containsKey(COUNT_KEY)
                && isValidParticleEnum((String) particleMap.get(PARTICLE_KEY))
                && particleMap.get(COLOR_KEY) instanceof Integer
                && particleMap.get(COUNT_KEY) instanceof Integer;
    }

    private boolean isValidParticleEnum(String type) {
        try {
            Enum.valueOf(Particle.class, type);
            return true;
        } catch (IllegalArgumentException exception) {
            BudgieSets.getBudgieSets().getLogger().warning(type + "Not a valid sound.");
        }
        return false;
    }
}
