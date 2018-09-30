package fi.matiaspaavilainen.masuiteportalsbridge;

import fi.matiaspaavilainen.masuiteportalsbridge.commands.Delete;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.List;
import fi.matiaspaavilainen.masuiteportalsbridge.commands.Set;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaSuitePortalsBridge extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(getServer().getPluginManager().getPlugin("WorldEdit") == null){
            System.out.println("[MaSuitePortals] WorldEdit not detected. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    private void registerCommands(){
        getCommand("setportal").setExecutor(new Set(this));
        getCommand("delportal").setExecutor(new Delete(this));
        getCommand("portals").setExecutor(new List(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
