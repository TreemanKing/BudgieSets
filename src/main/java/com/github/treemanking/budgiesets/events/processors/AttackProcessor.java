package com.github.treemanking.budgiesets.events.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.utilities.Processor;
import com.github.treemanking.budgiesets.utilities.ProcessorKeys;
import com.github.treemanking.budgiesets.events.EventProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.managers.configuration.EffectsManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AttackProcessor implements EventProcessor {

    @Override
    public void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new AttackProcessor.AttackListener(armorSetName, effectsMap, playerEquipStatusHashMap), plugin);
    }

    private class AttackListener implements Listener, ProcessorKeys {

        private final Map<?, ?> effectsMap;
        private final Map<UUID, Long> cooldownMap = new HashMap<>();
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;
        private final String armorSetName;
        private String eventType;

        public AttackListener(String armorSetName, Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
            this.armorSetName = armorSetName;

            if (event.containsKey(TYPE_KEY) && event.get(TYPE_KEY) instanceof String) {
                this.eventType = (String) event.get(TYPE_KEY);
            }
        }

        @EventHandler
        private void onPlayerAttack(EntityDamageByEntityEvent event) {
            Player player = null;
            Entity attackingEnemy = event.getDamager();
            Entity damagedEntity = event.getEntity();
            EntityDamageEvent.DamageCause damageCause = event.getCause();

            switch (eventType) {
                case "COMBAT_PLAYER":
                    if (damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                            && damagedEntity.getType().equals(EntityType.PLAYER)) {
                        player = (Player) event.getDamager();
                    }
                    break;
                case "COMBAT_PLAYER_PROJECTILE":
                    if (damageCause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                        if (!damagedEntity.getType().equals(EntityType.PLAYER)) break;
                        if (attackingEnemy.getType().equals(EntityType.ARROW)) {
                            Arrow arrow = (Arrow) attackingEnemy;
                            if (!(arrow.getShooter() instanceof Player)) break;
                            player = (Player) arrow.getShooter();
                        } else if (attackingEnemy.getType().equals(EntityType.SPECTRAL_ARROW)) {
                            SpectralArrow arrow = (SpectralArrow) attackingEnemy;
                            if (!(arrow.getShooter() instanceof Player)) break;
                            player = (Player) arrow.getShooter();
                        }
                    }
                    break;
                case "COMBAT_MOB":
                    if (damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                            && damagedEntity instanceof Mob) {
                        player = (Player) attackingEnemy;
                    }
                    break;
                case "COMBAT_MOB_PROJECTILE":
                    if (!(damagedEntity instanceof Mob)) break;
                    if (damageCause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                        if (attackingEnemy.getType().equals(EntityType.ARROW)) {
                            Arrow arrow = (Arrow) attackingEnemy;
                            player = (Player) arrow.getShooter();
                            break;
                        } else if (attackingEnemy.getType().equals(EntityType.SPECTRAL_ARROW)) {
                            SpectralArrow arrow = (SpectralArrow) attackingEnemy;
                            if (!(arrow.getShooter() instanceof Player)) break;
                            if (!damagedEntity.getType().equals(EntityType.PLAYER)) break;
                            player = (Player) arrow.getShooter();
                        }
                    }
                    break;
                default:
                    if (damageCause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                        if (attackingEnemy.getType().equals(EntityType.ARROW)) {
                            Arrow arrow = (Arrow) attackingEnemy;
                            player = (Player) arrow.getShooter();
                            break;
                        }
                    } else if (damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                        player = (Player) attackingEnemy;
                    }
            }
            if (player == null) return;
            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;

            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            if (checkMap(effectsMap, player, cooldownMap)) {
                new EffectsManager(effectsMap, player, currentStatus, event);
            }
        }

        @Override
        public int hashCode() {
            return armorSetName.hashCode();
        }
    }
}
