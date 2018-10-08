package fi.matiaspaavilainen.masuiteportalsbridge.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Set implements CommandExecutor {

    private MaSuitePortalsBridge plugin;

    public Set(MaSuitePortalsBridge p) {
        plugin = p;
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if(args.length < 5){
            Player p = (Player) sender;
            WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            Selection selection = worldEdit.getSelection(p);
            if (selection != null) {
                Location min = selection.getMinimumPoint();
                Location max = selection.getMaximumPoint();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("MaSuitePortals");
                out.writeUTF("SetPortal");
                out.writeUTF(p.getName()); // Creator's name
                out.writeUTF(args[0]); // Portal name
                out.writeUTF(args[1]); // Portal type
                out.writeUTF(args[2]); // Portal destination
                out.writeUTF(args[3]); // Portal fill type
                out.writeUTF(min.getWorld().getName() + ":" + min.getX() + ":" + min.getY() + ":" + min.getZ() + ":" + min.getYaw() + ":" + min.getPitch()); // Portal min loc
                out.writeUTF(max.getWorld().getName() + ":" + max.getX() + ":" + max.getY() + ":" + max.getZ() + ":" + max.getYaw() + ":" + max.getPitch()); // Portal max loc
                Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                System.out.println();
                return true;
            } else {
                System.out.println("No selected area");
                // Send no selection
                return false;
            }
        }else{
            // Send correct syntax
            return false;
        }

    }
}
