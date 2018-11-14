package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

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
                        // If portal exists
                        if (PortalManager.portalNames.contains(p[0])) {
                            // Check if the portal is not null
                            if (PortalManager.portals.values().stream().map(portals1 -> portals1.stream().filter(portal -> portal.getName().equalsIgnoreCase(p[0])).findFirst().get()).findFirst().isPresent()) {
                                // Load the portal
                                Portal por = PortalManager.portals.values().stream().map(portals1 -> portals1.stream().filter(portal -> portal.getName().equalsIgnoreCase(p[0])).findFirst().get()).findFirst().get();
                                //Remove it
                                PortalManager.removePortal(por);

                                // Bind new values
                                por.setType(p[1]);
                                por.setDestination(p[2]);
                                por.setFillType(p[3]);
                                por.setMinLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])));
                                por.setMaxLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));

                                // Save the updated one to list
                                PortalManager.portals.get(Bukkit.getWorld(p[4])).add(por);
                            }
                        } else {
                            // Create portal from values
                            Portal portal = new Portal(p[0], p[1], p[2], p[3], new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])), new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));

                            // Save the updated one to list
                            PortalManager.portals.get(Bukkit.getWorld(p[4])).add(portal);
                        }
                        // Add portal name to list
                        PortalManager.portalNames.add(p[0]);

                        // Refresh portals
                        PortalManager.loadPortals();
                    } else {
                        // Return warning message
                        System.out.println("[MaSuite] [Portals] [Portal="+ p[0]+ "] World not found");
                    }
                }

                // If channel is DeletePortal
                if (childchannel.equals("DeletePortal")) {
                    // Read portal's name
                    String p = in.readUTF();
                    //Check if list contains the name
                    if (PortalManager.portalNames.contains(p)) {
                        // Load portal
                        Portal por = PortalManager.portals.values().stream().map(portals1 -> portals1.stream().filter(portal -> portal.getName().equalsIgnoreCase(p)).findFirst().get()).findFirst().get();
                        // Remove it
                        PortalManager.removePortal(por);
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}