package cn.lunadeer.dominion.v1_20_1.events.player.PVP;

import cn.lunadeer.dominion.api.dtos.flag.Flags;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

import static cn.lunadeer.dominion.misc.Others.checkPrivilegeFlagSilence;

public class CloudEffect implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void handler(AreaEffectCloudApplyEvent event) {
        if (!(event.getEntity().getSource() instanceof Player attacker)) {
            return;
        }

        event.getAffectedEntities().removeIf(entity -> {
            if (entity instanceof Player victim) {
                if (victim == attacker) return false;
                return !checkPrivilegeFlagSilence(victim.getLocation(), Flags.PVP, attacker, null)
                        || !checkPrivilegeFlagSilence(victim.getLocation(), Flags.PVP, victim, null);
            }
            if (entity instanceof Animals) {
                return !checkPrivilegeFlagSilence(entity.getLocation(), Flags.ANIMAL_KILLING, attacker, null);
            }
            if (entity instanceof Villager) {
                return !checkPrivilegeFlagSilence(entity.getLocation(), Flags.VILLAGER_KILLING, attacker, null);
            }
            if (entity instanceof Monster) {
                return !checkPrivilegeFlagSilence(entity.getLocation(), Flags.MONSTER_KILLING, attacker, null);
            }
            return false;
        });
    }
}
