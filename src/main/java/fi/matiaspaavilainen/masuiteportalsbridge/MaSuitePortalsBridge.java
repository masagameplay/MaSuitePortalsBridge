package fi.matiaspaavilainen.masuiteportalsbridge;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Delete;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.List;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Set;
import fi.matiaspaavilainen.masuiteportalsbridge.listeners.MovementListener;
import fi.matiaspaavilainen.masuiteportalsbridge.listeners.PhysicsListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public final class MaSuitePortalsBridge extends JavaPlugin implements Listener {

    public WorldEditPlugin we = null;

    public Config config = new Config(this);

    @Override
    public void onEnable() {
        // Load WorldEdit
        we = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        if (we == null) {
            System.out.println("[MaSuitePortals] WorldEdit not detected. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Register and load everything
        registerCommands();
        registerListener();
        initLists();
        //reguestPortals();


        // Create configs
        config.createConfigs();
    }

    @Override
    public void onDisable() {
        PortalManager.portals.forEach(((world, portals) -> portals.forEach(Portal::clearPortal)));
    }

    /**
     * Register commands
     */
    private void registerCommands() {
        getCommand("setportal").setExecutor(new Set(this));
        getCommand("delportal").setExecutor(new Delete(this));
        getCommand("portals").setExecutor(new List(this));
    }

    /**
     * Register listener
     */
    private void registerListener() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PortalsMessageListener(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new PhysicsListener(), this);
        getServer().getPluginManager().registerEvents(this, this);

    }

    /**
     * Initialize lists for Portals
     */
    private void initLists() {
        getServer().getWorlds().forEach(world -> PortalManager.portals.put(world, new ArrayList<Portal>()));
    }

    /**
     * Send request to get portals from Bungee
     */
    private void reguestPortals() {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF("MaSuitePortals");
            out.writeUTF("RequestPortals");
            getServer().getScheduler().runTaskTimerAsynchronously(this, () -> getServer().sendPluginMessage(this, "BungeeCord", b.toByteArray()), 0, 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // If list is empty when player joins, request portals
        if (PortalManager.portalNames.isEmpty()) {
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuitePortals");
                out.writeUTF("RequestPortals");
                getServer().getScheduler().runTaskLaterAsynchronously(this, () -> e.getPlayer().sendPluginMessage(this, "BungeeCord", b.toByteArray()), 100);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Colorize string
     *
     * @param s string to colorize
     * @return colorized string
     */
    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
