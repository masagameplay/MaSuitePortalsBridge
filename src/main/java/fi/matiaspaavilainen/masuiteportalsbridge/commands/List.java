package fi.matiaspaavilainen.masuiteportalsbridge.commands;

import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class List implements CommandExecutor {

    private MaSuitePortalsBridge plugin;

    public List(MaSuitePortalsBridge p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF("MaSuitePortals");
            out.writeUTF("List");
            out.writeUTF(sender.getName());
            Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
