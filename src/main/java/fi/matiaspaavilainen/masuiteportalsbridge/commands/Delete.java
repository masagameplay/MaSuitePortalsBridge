package fi.matiaspaavilainen.masuiteportalsbridge.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge.colorize;

public class Delete implements CommandExecutor {

    private MaSuitePortalsBridge plugin;

    public Delete(MaSuitePortalsBridge p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length != 1) {
            p.sendMessage(colorize(plugin.config.getSyntaxes().getString("portal.delete")));
            return false;
        }

        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF("MaSuitePortals");
            out.writeUTF("DelPortal");
            out.writeUTF(p.getName()); // Creator's name
            out.writeUTF(args[0]); // Portal name
            p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.getStackTrace();
        }

        return false;
    }
}
