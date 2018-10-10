package fi.matiaspaavilainen.masuiteportalsbridge.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import fi.matiaspaavilainen.masuiteportalsbridge.Portal;
import fi.matiaspaavilainen.masuiteportalsbridge.PortalManager;
import fi.matiaspaavilainen.masuiteportalsbridge.PortalRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class MovementListener implements Listener {

    private MaSuitePortalsBridge plugin;

    public MovementListener(MaSuitePortalsBridge p) {
        plugin = p;
    }

    private HashMap<UUID, Long> inPortal = new HashMap<>();

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
                p.sendMessage("portal");
                try {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("MaSuitePortals");
                    out.writeUTF("SendPlayer");
                    out.writeUTF(p.getName());
                    out.writeUTF(portal.getName());
                    // Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
                Vector unitVector = e.getFrom().toVector().subtract(e.getTo().toVector()).normalize();
                Location l = e.getPlayer().getLocation();
                l.setYaw(l.getYaw()+180);
                e.getPlayer().teleport(l);
                e.getPlayer().setVelocity(unitVector.multiply(0.3));
            }
        });
        /*
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {

            if (pr.isIn(p.getLocation())) {
                if (!inPortal.containsKey(p.getUniqueId())) {
                    inPortal.put(p.getUniqueId(), System.currentTimeMillis());
                    e.setCancelled(true);
                    try {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("ConnectOther");
                        out.writeUTF(p.getName());
                        out.writeUTF("Freebuild");
                        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                } else {
                       if ((System.currentTimeMillis() - inPortal.get(p.getUniqueId())) / 1000 > 3) {
                        inPortal.remove(p.getUniqueId());
                    }
                }
            }

        }*/
    }
}
