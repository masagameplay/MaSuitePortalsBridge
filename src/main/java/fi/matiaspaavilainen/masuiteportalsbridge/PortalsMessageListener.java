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
                    Portal portal = new Portal();
                    portal.setName(p[0]);
                    portal.setType(p[1]);
                    portal.setDestination(p[2]);
                    portal.setFillType(p[3]);
                    portal.setMinLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]), Double.parseDouble(p[7])));
                    portal.setMaxLoc(new Location(Bukkit.getWorld(p[4]), Double.parseDouble(p[8]), Double.parseDouble(p[9]), Double.parseDouble(p[10])));
                    if (Bukkit.getWorld(p[4]) != null) {
                        if (PortalManager.portalNames.contains(portal.getName())) {
                            return;
                        }
                        PortalManager.portals.get(Bukkit.getWorld(p[4])).add(portal);
                        PortalManager.loadPortals();
                    } else {
                        System.out.println("World not found");
                    }
                }
                if (childchannel.equals("DeletePortal")) {
                    String p = in.readUTF();
                    if (PortalManager.portalNames.contains(p)) {
                        PortalManager.portals.forEach((world, portals) -> portals.stream().filter(portal -> portal.getName().equals(p)).filter(PortalManager::removePortal));
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}