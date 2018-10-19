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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class MaSuitePortalsBridge extends JavaPlugin {

    public WorldEditPlugin we = null;

    private Config config = new Config(this);

    @Override
    public void onEnable() {
        we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if(we == null){
            System.out.println("[MaSuitePortals] WorldEdit not detected. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Register and load everything
        registerCommands();
        registerListener();
        initLists();
        reguestPortals();

        // Create configs
        config.createConfigs();
    }

    @Override
    public void onDisable() {
        PortalManager.portals.forEach(((world, portals) -> portals.forEach(Portal::clearPortal)));
    }

    private void registerCommands(){

        // Check if server version
        getCommand("setportal").setExecutor(new Set(this));
        getCommand("delportal").setExecutor(new Delete(this));
        getCommand("portals").setExecutor(new List(this));
    }

    private void registerListener(){
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PortalsMessageListener(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        // Check if server version
        getServer().getPluginManager().registerEvents(new PhysicsListener(), this);

    }

    private void initLists(){
        getServer().getWorlds().forEach(world -> PortalManager.portals.put(world, new ArrayList<Portal>()));
    }
    private void reguestPortals() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MaSuitePortals");
        out.writeUTF("RequestPortals");
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray()), 0, 30000);
    }
}
