package fi.matiaspaavilainen.masuiteportalsbridge;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;

public class Portal {

    private String name;
    private String type;
    private String destination;
    private Location minLoc, maxLoc;
    private String fillType;

    public Portal() {
    }

    public Portal(String name, String type, String destination, Location minLoc, Location maxLoc, String fillType) {
        this.name = name;
        this.type = type;
        this.destination = destination;
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.fillType = fillType;
    }


    public void send(Player p, MaSuitePortalsBridge plugin) {
        if (getType().equals("server")) {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("ConnectOther");
                out.writeUTF(p.getName());
                out.writeUTF(getDestination());
                Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        } else if (getType().equals("warp")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("WarpPlayerCommand");
            out.writeUTF(p.getName());
            out.writeUTF("console");
            out.writeUTF(getDestination());

            Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }
    }

    public void fillPortal() {
        System.out.println(getFillType());
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (block.getType().equals(Material.AIR)) {
                if (getFillType().equals("water")) {
                    Levelled levelledData = (Levelled) block.getState().getBlockData();
                    levelledData.setLevel(10);
                    block.getState().setBlockData(levelledData);
                }
                block.setType(Material.valueOf(getFillType().toUpperCase()));
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
