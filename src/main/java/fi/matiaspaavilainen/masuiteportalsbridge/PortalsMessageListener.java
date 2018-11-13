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
                if (childchannel.equals("CreatePortal")) {
                    String[] p = in.readUTF().toLowerCase().split(":");
                    if (Bukkit.getWorld(p[4]) != null) {
                        if (PortalManager.portalNames.contains(p[0])) {
                            if (PortalManager.portals.values().stream().map(portals1 -> portals1.stream().filter(portal -> portal.getName().equalsIgnoreCase(p[0])).findFirst().get()).findFirst().isPresent()) {
                                Portal por = PortalManager.portals.values().stream().map(portals1 -> portals1.stream().filter(portal -> portal.getName().equalsIgnoreCase(p[0])).findFirst().get()).findFirst().get();
                                PortalManager.removePortal(por);
                                por.setType(p[1]);
                                por.setDestination(p[2]);
                                por.setFillType(p[3]);
                                por.setMinLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])));
                                por.setMaxLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));
                                PortalManager.portals.get(Bukkit.getWorld(p[4])).add(por);
                            }
                        } else {
                            Portal portal = new Portal(p[0], p[1], p[2], p[3], new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])), new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));
                            PortalManager.portals.get(Bukkit.getWorld(p[4])).add(portal);
                        }
                        PortalManager.portalNames.add(p[0]);
                        PortalManager.loadPortals();
                    } else {
                        System.out.println("World not found");
                    }
                }
                if (childchannel.equals("DeletePortal")) {
                    System.out.println("Received");
                    String p = in.readUTF();
                    System.out.println(p);
                    if (PortalManager.portalNames.contains(p)) {
                        System.out.println(p);
                        Portal por = PortalManager.portals.values().stream().map(portals1 -> portals1.stream().filter(portal -> portal.getName().equalsIgnoreCase(p)).findFirst().get()).findFirst().get();
                        PortalManager.removePortal(por);
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}