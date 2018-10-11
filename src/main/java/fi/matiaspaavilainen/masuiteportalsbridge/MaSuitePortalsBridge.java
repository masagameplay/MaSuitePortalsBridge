package fi.matiaspaavilainen.masuiteportalsbridge;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Delete;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.List;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Set;
import fi.matiaspaavilainen.masuiteportalsbridge.listeners.MovementListener;
import fi.matiaspaavilainen.masuiteportalsbridge.listeners.PhysicsListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class MaSuitePortalsBridge extends JavaPlugin {

    public WorldEditPlugin we = null;
    @Override
    public void onEnable() {
        we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if(we == null){
            System.out.println("[MaSuitePortals] WorldEdit not detected. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        registerCommands();
        registerListener();
        //reguestPortals();

        World world = getServer().getWorld("world");
        PortalManager.portals.put(world, new ArrayList<>());
        PortalManager.portals.get(getServer().getWorld("world")).add(new Portal("test", "warp", "isoportaali", new Location(world, 6, 103, 22), new Location(world, 8, 98, 22), "water"));
        PortalManager.loadPortals();
    }

    @Override
    public void onDisable() {
        PortalManager.portals.forEach(((world, portals) -> portals.forEach(Portal::clearPortal)));
    }

    private void registerCommands(){

        // Check if server version
        if(getServer().getVersion().contains("13")){
            // If version is 1.13, use new WE
            getCommand("setportal").setExecutor(new Set(this));
        }else{
            // If version is not 1.13, use old WE
        }
        getCommand("delportal").setExecutor(new Delete(this));
        getCommand("portals").setExecutor(new List(this));
    }

    private void registerListener(){
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        //getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PortalsMessageListener(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new PhysicsListener(), this);
    }

    private void reguestPortals() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MaSuitePortals");
        out.writeUTF("RequestPortals");
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            System.out.println("[MaSuite] [Portals] Requesting list of portals");
            getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
        }, 0, 3000);
    }
}
