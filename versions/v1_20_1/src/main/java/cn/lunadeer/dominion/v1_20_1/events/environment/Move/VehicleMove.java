package cn.lunadeer.dominion.v1_20_1.events.environment.Move;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.cache.CacheManager;
import cn.lunadeer.dominion.configuration.Configuration;
import cn.lunadeer.dominion.events.PaperOnly;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import static cn.lunadeer.dominion.misc.Others.checkEnvironmentFlag;
import static cn.lunadeer.dominion.misc.Others.checkPrivilegeFlag;

@PaperOnly
public class VehicleMove implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void handler(VehicleMoveEvent event) {
        if (!Configuration.vehicleProtection) return;

        Vehicle vehicle = event.getVehicle();
        if (!(vehicle instanceof Boat) && !(vehicle instanceof Minecart)) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        DominionDTO fromDom = CacheManager.instance.getDominion(from);
        DominionDTO toDom = CacheManager.instance.getDominion(to);

        if (fromDom == toDom) return; // both null or same dominion

        boolean hasPlayerPassenger = false;
        for (org.bukkit.entity.Entity passenger : vehicle.getPassengers()) {
            if (passenger instanceof Player player) {
                hasPlayerPassenger = true;
                // Check MOVE at the target location
                if (!checkPrivilegeFlag(to, Flags.MOVE, player, null)) {
                    vehicle.teleport(from);
                    return;
                }
                // Check MOVE at the source location
                if (fromDom != null && !checkPrivilegeFlag(from, Flags.MOVE, player, null)) {
                    vehicle.teleport(from);
                    return;
                }
            }
        }

        if (!hasPlayerPassenger) {
            // Unmanned vehicle - check environment flag
            if (!checkEnvironmentFlag(to, Flags.VEHICLE_MOVE, null)) {
                vehicle.teleport(from);
            } else if (toDom == null && fromDom != null) {
                // Vehicle exiting to wilderness - check source dominion's flag
                if (!checkEnvironmentFlag(from, Flags.VEHICLE_MOVE, null)) {
                    vehicle.teleport(from);
                }
            }
        }
    }
}
