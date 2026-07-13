package cn.lunadeer.dominion.v1_20_1.events.player.PVP;

import cn.lunadeer.dominion.api.dtos.flag.Flags;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

import static cn.lunadeer.dominion.misc.Others.checkPrivilegeFlagSilence;

public class Piston implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void handler(PotionSplashEvent event) {
        if (!(event.getPotion().getShooter() instanceof Player attacker)) {
            return;
        }

        for (LivingEntity entity : event.getAffectedEntities()) {
            if (entity instanceof Player victim) {
                if (victim == attacker) continue;
                if (!checkPrivilegeFlagSilence(victim.getLocation(), Flags.PVP, attacker, null)
                        || !checkPrivilegeFlagSilence(victim.getLocation(), Flags.PVP, victim, null)) {
                    event.setIntensity(victim, 0);
                }
            } else if (entity instanceof Animals) {
                if (!checkPrivilegeFlagSilence(entity.getLocation(), Flags.ANIMAL_KILLING, attacker, null)) {
                    event.setIntensity(entity, 0);
                }
            } else if (entity instanceof Villager) {
                if (!checkPrivilegeFlagSilence(entity.getLocation(), Flags.VILLAGER_KILLING, attacker, null)) {
                    event.setIntensity(entity, 0);
                }
            } else if (entity instanceof Monster) {
                if (!checkPrivilegeFlagSilence(entity.getLocation(), Flags.MONSTER_KILLING, attacker, null)) {
                    event.setIntensity(entity, 0);
                }
            }
        }
    }
}
