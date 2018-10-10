package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Location;

public class Portal {

    private String name;
    private Location minLoc, maxLoc;
    private String filltype;


    public Portal(String name, Location minLoc, Location maxLoc, String filltype) {
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.filltype = filltype;
        this.name = name;
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
}
