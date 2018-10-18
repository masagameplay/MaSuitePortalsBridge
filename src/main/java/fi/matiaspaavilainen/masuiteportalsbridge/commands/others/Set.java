package fi.matiaspaavilainen.masuiteportalsbridge.commands.others;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.worldedit.bukkit.selections.Selection;
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
            Selection selection = plugin.we.getSelection(p);
            if (selection != null) {
                try {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("MaSuitePortals");
                    out.writeUTF("SetPortal");
                    out.writeUTF(p.getName()); // Creator's name
                    out.writeUTF(args[0]); // Portal name
                    out.writeUTF(args[1]); // Portal type
                    out.writeUTF(args[2]); // Portal destination
                    out.writeUTF(args[3]); // Portal fill type
                    out.writeUTF(p.getWorld().getName() + ":" + selection.getMinimumPoint().getX() + ":" + selection.getMinimumPoint().getY() + ":" + selection.getMinimumPoint().getZ()); // Portal min loc
                    out.writeUTF(p.getWorld().getName() + ":" + selection.getMaximumPoint().getX() + ":" + selection.getMaximumPoint().getY() + ":" + selection.getMaximumPoint().getZ()); // Portal max loc
                    Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                } catch (Exception e) {
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
