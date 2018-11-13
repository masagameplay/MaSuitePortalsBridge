package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Portal {

    private String name;
    private String type;
    private String destination;
    private Location minLoc, maxLoc;
    private String fillType;

    public Portal() {
    }

    public Portal(String name, String type, String destination, String fillType, Location minLoc, Location maxLoc) {
        this.name = name;
        this.type = type;
        this.destination = destination;
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.fillType = fillType;
    }


    public void send(Player p, MaSuitePortalsBridge plugin) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            if (getType().equals("server")) {

                out.writeUTF("ConnectOther");
                out.writeUTF(p.getName());
                out.writeUTF(getDestination());
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } else if (getType().equals("warp")) {

                out.writeUTF("WarpPlayerCommand");
                out.writeUTF(p.getName());
                out.writeUTF("console");
                out.writeUTF(getDestination());

                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void fillPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (block.getType().equals(Material.AIR)) {
                block.setType(Material.valueOf(getFillType().toUpperCase()));
                if (getFillType().equals("water")) {
                    Levelled levelledData = (Levelled) block.getState().getBlockData();
                    levelledData.setLevel(0);
                    block.getState().setBlockData(levelledData);
                }

            }
        });
    }

    public void clearPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> block.setType(Material.AIR));
    }

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
