package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.Location;

import java.util.UUID;

public class PortalRegion {

    private UUID worldUniqueId;

    private double maxX, maxY, maxZ, minX, minY, minZ;

    public PortalRegion(Location firstPoint, Location secondPoint) {
        worldUniqueId = firstPoint.getWorld().getUID();

        maxX = Math.max(firstPoint.getX(), secondPoint.getX());
        maxY = Math.max(firstPoint.getY(), secondPoint.getY());
        maxZ = Math.max(firstPoint.getZ(), secondPoint.getZ());

        minX = Math.min(firstPoint.getX(), secondPoint.getX());
        minY = Math.min(firstPoint.getY(), secondPoint.getY());
        minZ = Math.min(firstPoint.getZ(), secondPoint.getZ());
    }

    public boolean isInPortal(Location loc) {
        return (minX <= loc.getX()
                && minY <= loc.getY()
                && minZ <= loc.getZ()
                && maxX >= loc.getX()
                && maxY >= loc.getY()
                && maxZ >= loc.getZ());
    }

}
