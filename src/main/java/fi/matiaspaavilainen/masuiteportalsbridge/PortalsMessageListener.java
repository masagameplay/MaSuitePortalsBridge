package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

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
                System.out.println(in.readUTF());
                if (childchannel.equals("PortalList")) {
                    System.out.println("Received list of portals");
                    // Split all portal data to separate portals
                    String[] p = in.readUTF().toLowerCase().split("|");
                    //System.out.println("All: " + Arrays.toString(p));
                    // Loop them
                    for (String po : p) {
                        // Split all info of portal to strings
                        String[] portalList = po.split(":");
                        System.out.println("Portals: " + Arrays.toString(portalList));
                        Location minLoc, maxLoc;
                        minLoc = new Location(Bukkit.getWorld(portalList[4]), Double.parseDouble(portalList[5]), Double.parseDouble(portalList[6]), Double.parseDouble(portalList[7]));
                        maxLoc = new Location(Bukkit.getWorld(portalList[4]), Double.parseDouble(portalList[8]), Double.parseDouble(portalList[9]), Double.parseDouble(portalList[10]));

                        Portal portal = new Portal();
                        portal.setName(portalList[0]);
                        portal.setType(portalList[1]);
                        portal.setDestination(portalList[2]);
                        portal.setFilltype(portalList[3]);
                        portal.setMinLoc(minLoc);
                        portal.setMaxLoc(maxLoc);
                        // Build location from those

                        if (minLoc.getWorld() != null) {
                            PortalManager.portals.get(minLoc.getWorld()).add(portal);
                        }
                    }
                    PortalManager.loadPortals();

                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}