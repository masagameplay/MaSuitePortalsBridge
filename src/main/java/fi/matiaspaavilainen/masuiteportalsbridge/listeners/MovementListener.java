package fi.matiaspaavilainen.masuiteportalsbridge.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import fi.matiaspaavilainen.masuiteportalsbridge.PortalRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        Location corner1 = new Location(p.getWorld(), -2, 102, 27);
        Location corner2 = new Location(p.getWorld(), 3, 99, 26);
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            PortalRegion pr = new PortalRegion(corner1, corner2);
            if (pr.isInPortal(p.getLocation())) {
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

        }
    }
}
