package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class PortalsMessageListener implements PluginMessageListener {

    private MaSuitePortalsBridge plugin;

    public PortalsMessageListener(MaSuitePortalsBridge p) {
        plugin = p;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = null;
        try {
            subchannel = in.readUTF();
            if (subchannel.equals("MaSuitePortals")) {
                String childchannel = in.readUTF();

                // If channel is CreatePortal
                if (childchannel.equals("CreatePortal")) {
                    // Split portal information
                    String[] p = in.readUTF().toLowerCase().split(":");

                    // If portal's world is not null
                    if (Bukkit.getWorld(p[4]) != null) {
                        Portal portal = null;
                        // If portal exists
                        if (PortalManager.portalNames.contains(p[0])) {
                            // Load the portal
                            for (List<Portal> portals : PortalManager.portals.values()) {
                                for (Portal por : portals) {
                                    if (por.getName().equalsIgnoreCase(p[0])) {
                                        portal = por;
                                        //Remove it
                                        PortalManager.removePortal(portal);

                                        // Bind new values
                                        portal.setName(p[0]);
                                        portal.setType(p[1]);
                                        portal.setDestination(p[2]);
                                        portal.setFillType(p[3]);
                                        portal.setMinLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])));
                                        portal.setMaxLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));

                                        // Save the updated one to list
                                        PortalManager.portals.get(plugin.getServer().getWorld(p[4])).add(portal);
                                        return;
                                    }
                                }

                            }

                        } else {
                            // Create portal from values
                            portal = new Portal(p[0], p[1], p[2], p[3],
                                    new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])),
                                    new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));

                            // Save the updated one to list
                            PortalManager.portals.get(plugin.getServer().getWorld(p[4])).add(portal);
                            PortalManager.portalNames.add(p[0]);
                        }
                        if (portal != null) {
                            portal.fillPortal();
                        }
                    } else {
                        // Return warning message
                        System.out.println("[MaSuite] [Portals] [Portal=" + p[0] + "] [World=" + p[4] + "] World not found");
                    }
                }

                // If channel is DeletePortal
                if (childchannel.equals("DeletePortal")) {
                    // Read portal's name
                    String p = in.readUTF();
                    //Check if list contains the name
                    if (PortalManager.portalNames.contains(p)) {
                        // Load portal
                        for (List<Portal> portals : PortalManager.portals.values()) {
                            for (Portal portal : portals) {
                                if (portal.getName().equalsIgnoreCase(p)) {
                                    PortalManager.removePortal(portal);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}