package fi.matiaspaavilainen.masuiteportalsbridge.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.regions.Region;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Set implements CommandExecutor {

    private MaSuitePortalsBridge plugin;

    public Set(MaSuitePortalsBridge p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length == 4) {
            Player p = (Player) sender;
            BukkitPlayer bp = BukkitAdapter.adapt(p);
            LocalSession localSession = plugin.we.getWorldEdit().getSessionManager().get(BukkitAdapter.adapt(p));
            if (localSession != null) {
                try {
                    Region rg = localSession.getSelection(bp.getWorld());
                    if(rg == null){
                        System.out.println("No selected area");
                        return false;
                    }
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("MaSuitePortals");
                    out.writeUTF("SetPortal");
                    out.writeUTF(p.getName()); // Creator's name
                    out.writeUTF(args[0]); // Portal name
                    out.writeUTF(args[1]); // Portal type
                    out.writeUTF(args[2]); // Portal destination
                    out.writeUTF(args[3]); // Portal fill type
                    out.writeUTF(p.getWorld().getName() + ":" + rg.getMinimumPoint().getX() + ":" + rg.getMinimumPoint().getY() + ":" + rg.getMinimumPoint().getZ()); // Portal min loc
                    out.writeUTF(p.getWorld().getName() + ":" + rg.getMaximumPoint().getX() + ":" + rg.getMaximumPoint().getY() + ":" + rg.getMaximumPoint().getZ()); // Portal max loc
                    Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                System.out.println("No selected area");
                return false;
            }
        } else {
            // Send correct syntax
            return false;
        }

    }
}
