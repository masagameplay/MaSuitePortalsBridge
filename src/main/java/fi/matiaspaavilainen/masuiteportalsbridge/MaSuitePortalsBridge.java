package fi.matiaspaavilainen.masuiteportalsbridge;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Delete;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.List;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Set;
import fi.matiaspaavilainen.masuiteportalsbridge.listeners.MovementListener;
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

        getPortals();

    }

    private void registerCommands(){
        getCommand("setportal").setExecutor(new Set(this));
        getCommand("delportal").setExecutor(new Delete(this));
        getCommand("portals").setExecutor(new List(this));
    }

    private void registerListener(){
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PortalsMessageListener(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
    }

    private void getPortals(){
        World world = getServer().getWorld("world");
        PortalManager.portals.put(world, new ArrayList<>());
        PortalManager.portals.get(getServer().getWorld("world")).add(new Portal("test", new Location(world, -2, 102, 26), new Location(world, 3, 99, 25), "air"));
    }
    @Override
    public void onDisable() {
    }
}
