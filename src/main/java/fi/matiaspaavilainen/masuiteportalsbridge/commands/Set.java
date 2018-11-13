package fi.matiaspaavilainen.masuiteportalsbridge.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.regions.Region;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        Player p = (Player) sender;
        if (args.length == 4) {
            BukkitPlayer bp = BukkitAdapter.adapt(p);
            LocalSession localSession = plugin.we.getWorldEdit().getSessionManager().get(BukkitAdapter.adapt(p));
            if (localSession != null) {
                try {
                    Region rg = localSession.getSelection(bp.getWorld());
                    if(rg == null){
                        p.sendMessage(ChatColor.RED + "No selected area");
                        return false;
                    }
                    try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                         DataOutputStream out = new DataOutputStream(b)) {
                        out.writeUTF("MaSuitePortals");
                        out.writeUTF("SetPortal");
                        out.writeUTF(p.getName()); // Creator's name
                        out.writeUTF(args[0]); // Portal name
                        out.writeUTF(args[1]); // Portal type
                        out.writeUTF(args[2]); // Portal destination
                        out.writeUTF(args[3]); // Portal fill type
                        out.writeUTF(p.getWorld().getName() + ":" + rg.getMinimumPoint().getX() + ":" + rg.getMinimumPoint().getY() + ":" + rg.getMinimumPoint().getZ()); // Portal min loc
                        out.writeUTF(p.getWorld().getName() + ":" + rg.getMaximumPoint().getX() + ":" + rg.getMaximumPoint().getY() + ":" + rg.getMaximumPoint().getZ()); // Portal max loc
                        p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                    } catch (IOException e){
                        e.getStackTrace();
                    }
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "No selected area");
                return false;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Correct syntax: /setportal <name> <warp/server> <target> <fill type>");
            return false;
        }

    }
}
