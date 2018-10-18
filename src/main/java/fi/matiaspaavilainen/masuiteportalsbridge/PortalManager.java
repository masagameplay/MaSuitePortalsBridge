package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortalManager {

    public static HashMap<World, List<Portal>> portals = new HashMap<>();
    public static List<String> portalNames = new ArrayList<>();

    public static void loadPortals(){
        PortalManager.portals.forEach((world, portals) -> portals.forEach(Portal::fillPortal));
    }
}
