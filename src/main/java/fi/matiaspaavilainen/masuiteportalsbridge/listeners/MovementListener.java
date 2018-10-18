package fi.matiaspaavilainen.masuiteportalsbridge.listeners;

import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import fi.matiaspaavilainen.masuiteportalsbridge.PortalManager;
import fi.matiaspaavilainen.masuiteportalsbridge.PortalRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MovementListener implements Listener {

    private MaSuitePortalsBridge plugin;

    public MovementListener(MaSuitePortalsBridge p) {
        plugin = p;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Block t = e.getTo().getBlock();
        Block f = e.getFrom().getBlock();
        if(f.equals(t)){
            return;
        }
        if(!PortalManager.portals.containsKey(p.getWorld())){
            return;
        }
        PortalManager.portals.get(p.getWorld()).forEach(portal -> {
            Location corner1 = new Location(p.getWorld(), portal.getMinLoc().getX(), portal.getMinLoc().getY(), portal.getMinLoc().getZ());
            Location corner2 = new Location(p.getWorld(), portal.getMaxLoc().getX(), portal.getMaxLoc().getY(), portal.getMaxLoc().getZ());
            PortalRegion pr = new PortalRegion(corner1, corner2);
            if(pr.isIn(p)){
                Vector unitVector = e.getFrom().toVector().subtract(e.getTo().toVector()).normalize();
                Location l = e.getPlayer().getLocation();
                l.setYaw(l.getYaw()+180);
                e.getPlayer().teleport(l);
                e.getPlayer().setVelocity(unitVector.multiply(0.3));
                portal.send(p, plugin);
            }
        });
    }
}
