package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Portal {

    private String name;
    private String type;
    private String destination;
    private Location minLoc, maxLoc;
    private String fillType;

    public Portal() {
    }

    /**
     * Constructor for Portal
     *
     * @param name        portal's name
     * @param type        portal's type (warp/server)
     * @param destination portal's destination (server or warp name)
     * @param fillType    what {@link Material} will be used to fill the portal
     * @param minLoc      the first corner of portal
     * @param maxLoc      the second corner of portal
     */
    public Portal(String name, String type, String destination, String fillType, Location minLoc, Location maxLoc) {
        this.name = name;
        this.type = type;
        this.destination = destination;
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.fillType = fillType;
    }

    /**
     * Sends player trough portal
     *
     * @param player to send
     * @param plugin to use in plugin messages
     */
    public void send(Player player, MaSuitePortalsBridge plugin) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            if (getType().equals("server")) {

                out.writeUTF("ConnectOther");
                out.writeUTF(player.getName());
                out.writeUTF(getDestination());
                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } else if (getType().equals("warp")) {

                out.writeUTF("WarpPlayerCommand");
                out.writeUTF(player.getName());
                out.writeUTF("console");
                out.writeUTF(getDestination());

                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Fills portal
     */
    public void fillPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (block.getType().equals(Material.AIR)) {

                // Portal manipulation if type is nether_portal
                if (getFillType().toLowerCase().contains("nether_portal")) {
                    block.setType(Material.NETHER_PORTAL);
                    Orientable orientable = (Orientable) block.getBlockData();
                    switch (getFillType().toLowerCase()) {
                        case ("nether_portal_north"):
                            orientable.setAxis(Axis.X);
                            break;
                        case ("nether_portal_east"):
                            orientable.setAxis(Axis.Z);
                            break;
                        case ("nether_portal_south"):
                            orientable.setAxis(Axis.X);
                            break;
                        case ("nether_portal_west"):
                            orientable.setAxis(Axis.Z);
                            break;
                    }
                    block.setBlockData(orientable);

                } else {
                    block.setType(Material.valueOf(getFillType().toUpperCase()));
                    if (getFillType().equals("water")) {
                        Levelled levelledData = (Levelled) block.getState().getBlockData();
                        levelledData.setLevel(0);
                        block.getState().setBlockData(levelledData);
                    }
                }


            }
        });
    }

    /**
     * Sets portal material to {@link Material#AIR}
     */
    public void clearPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> block.setType(Material.AIR));
    }

    /**
     * Returns portal info as string
     *
     * @return portal info
     */
    public String toString() {
        String minLoc = getMinLoc().getWorld() + ":" + getMinLoc().getX() + ":" + getMinLoc().getY() + ":" + getMinLoc().getZ();
        String maxLoc = getMaxLoc().getX() + ":" + getMaxLoc().getY() + ":" + getMaxLoc().getZ();
        return getName() + ":" + getType() + ":" + getDestination() + ":" + getFillType() + ":" + minLoc + ":" + maxLoc;
    }

    public Location getMinLoc() {
        return minLoc;
    }

    public void setMinLoc(Location minLoc) {
        this.minLoc = minLoc;
    }

    public Location getMaxLoc() {
        return maxLoc;
    }

    public void setMaxLoc(Location maxLoc) {
        this.maxLoc = maxLoc;
    }

    public String getFillType() {
        return fillType;
    }

    public void setFillType(String fillType) {
        this.fillType = fillType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
