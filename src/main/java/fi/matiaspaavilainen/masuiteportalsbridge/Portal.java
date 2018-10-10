package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;

public class Portal {

    private String name;
    private String type;
    private String destination;
    private Location minLoc, maxLoc;
    private String filltype;

    public Portal(){}

    public Portal(String name, String destination, String type, Location minLoc, Location maxLoc, String filltype) {
        this.name = name;
        this.type = type;
        this.destination = destination;
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.filltype = filltype;
    }


    public void fillPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (getFilltype().equals("water")) {
                if (block.getType().equals(Material.AIR)) {
                    block.setType(Material.WATER);
                    Levelled levelledData = (Levelled) block.getState().getBlockData();
                    levelledData.setLevel(6);
                    block.getState().setBlockData(levelledData);
                }
            }
        });
    }

    public void clearPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (block.getType().equals(Material.WATER) || block.getType().equals(Material.LAVA) || block.getType().equals(Material.COBWEB) || block.getType().equals(Material.NETHER_PORTAL)) {
                block.setType(Material.AIR);
            }
        });
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

    public String getFilltype() {
        return filltype;
    }

    public void setFilltype(String filltype) {
        this.filltype = filltype;
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
