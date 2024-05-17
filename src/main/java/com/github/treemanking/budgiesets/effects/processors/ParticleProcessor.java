package com.github.treemanking.budgiesets.effects.processors;

import com.destroystokyo.paper.ParticleBuilder;
import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.effects.EffectProcessorKeys;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

public class ParticleProcessor implements EffectProcessor, EffectProcessorKeys {

    @Override
    public void processEffect(List<?> particles, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object particle : particles) {
            if (particle instanceof Map<?, ?> particleMap) {
                if (validateParticleConfig(particleMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    Particle particleType = Enum.valueOf(Particle.class, (String) particleMap.get(PARTICLE_KEY));
                    String hex = (String) particleMap.get(COLOR_KEY);
                    int count = (int) particleMap.get(COUNT_KEY);
                    double offset = (double) particleMap.get(OFFSET_KEY);
                    Map<?, ?> particleDataMap = (Map<?, ?>) particleMap.get(DATA_KEY);

                    List<String> conditions = (List<String>) particleMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        spawnParticle(player, particleType, count, offset, particleDataMap);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid particle configuration: " + particleMap);
                }
            }
        }
    }

    private void spawnParticle(Player player, Particle particle, int count, double offset, Map<?, ?> dataMap) {
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);

        if (dataMap == null || dataMap.isEmpty()) {
            particleBuilder.location(player.getLocation())
                    .count(count)
                    .offset(offset, offset, offset)
                    .spawn();
        } else {
            particleBuilder.location(player.getLocation())
                    .count(count)
                    .data(convertData(particle.getDataType(), dataMap, player))
                    .offset(offset, offset, offset)
                    .spawn();
        }
    }

    private boolean validateParticleConfig(Map<?, ?> particleMap) {
        return particleMap.containsKey(PARTICLE_KEY)
                && particleMap.containsKey(COUNT_KEY)
                && particleMap.containsKey(OFFSET_KEY)
                && isValidParticleEnum((String) particleMap.get(PARTICLE_KEY))
                && particleMap.get(COUNT_KEY) instanceof Integer
                && particleMap.get(OFFSET_KEY) instanceof Double;
    }

    private boolean isValidParticleEnum(String type) {
        try {
            Enum.valueOf(Particle.class, type);
            return true;
        } catch (IllegalArgumentException exception) {
            BudgieSets.getBudgieSets().getLogger().warning(type + "Not a valid particle.");
        }
        return false;
    }

    private Object convertData(Class<?> particleDataTypeClass, Map<?, ?> dataMap, Player player) {
        if(dataMap == null) return null;

        if (particleDataTypeClass.equals(MaterialData.class)) {
            if (validateMaterialKey(dataMap)) {
                return Material.getMaterial(dataMap.get(MATERIAL_KEY).toString().toUpperCase());
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(BlockData.class)) {
            if (validateMaterialKey(dataMap)) {
                return Material.getMaterial(dataMap.get(MATERIAL_KEY).toString().toUpperCase()).createBlockData();
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(Integer.class)) {
            if (validateIntKey(dataMap)) {
                return dataMap.get(INT_KEY);
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(Float.class)) {
            if (validateFloatKey(dataMap)) {
                return (float) ((double) dataMap.get(FLOAT_KEY));
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(Vibration.class)) {
            if (validateArrivalTimeKey(dataMap)) {
                return new Vibration(new Vibration.Destination.EntityDestination(player), (int) dataMap.get(ARRIVAL_TIME_KEY));
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(Particle.DustTransition.class)) {
            int[] fromColor = convertHexToRGB((String) dataMap.get(FROM_COLOR_KEY));
            int[] toColor = convertHexToRGB((String) dataMap.get(TO_COLOR_KEY));
            if (validateHexKey(dataMap)) {
                return new Particle.DustTransition(
                        Color.fromRGB(fromColor[0], fromColor[1], fromColor[2]),
                        Color.fromRGB(toColor[0], toColor[1], toColor[2]),
                        (float) dataMap.get(SIZE_KEY)
                );
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(ItemStack.class)) {
            if (validateMaterialKey(dataMap)) {
                return new ItemStack(Material.getMaterial(dataMap.get(MATERIAL_KEY).toString().toUpperCase()));
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        } else if (particleDataTypeClass.equals(Particle.DustOptions.class)) {
            if (validateSizeKey(dataMap) && validateColorKey(dataMap)) {
                int[] color = convertHexToRGB((String) dataMap.get(COLOR_KEY));
                return new Particle.DustOptions(Color.fromRGB(color[0], color[1], color[2]), (float) ((double)dataMap.get(SIZE_KEY)));
            }
            BudgieSets.getBudgieSets().getLogger().warning("Invalid configuration. Please see the wiki on particle data.");
            return null;
        }
        return null;
    }


    private boolean validateMaterialKey(Map<?, ?> map) {
        return map.containsKey(MATERIAL_KEY)
                && Material.getMaterial(map.get(MATERIAL_KEY).toString().toUpperCase()) != null;
    }

    private boolean validateFloatKey(Map<?, ?> map) {
        return map.containsKey(FLOAT_KEY)
                && map.get(FLOAT_KEY) instanceof Double;
    }

    private boolean validateIntKey(Map<?, ?> map) {
        return map.containsKey(INT_KEY)
                && map.get(INT_KEY) instanceof Integer;
    }

    private boolean validateArrivalTimeKey(Map<?, ?> map) {
        return map.containsKey(ARRIVAL_TIME_KEY)
                && map.get(ARRIVAL_TIME_KEY) instanceof Integer;
    }

    private boolean validateHexKey(Map<?, ?> map) {
        return map.containsKey(FROM_COLOR_KEY)
                && map.containsKey(TO_COLOR_KEY)
                && map.get(FROM_COLOR_KEY) instanceof String
                && map.get(TO_COLOR_KEY) instanceof String
                && validateSizeKey(map);
    }

    private boolean validateSizeKey(Map<?, ?> map) {
        return map.containsKey(SIZE_KEY);
    }

    private boolean validateColorKey(Map<?, ?> map) {
        return map.containsKey(COLOR_KEY)
                && map.get(COLOR_KEY) instanceof String;
    }
}
