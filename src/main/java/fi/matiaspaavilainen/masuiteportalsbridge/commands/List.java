package fi.matiaspaavilainen.masuiteportalsbridge.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuiteportalsbridge.MaSuitePortalsBridge;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class List implements CommandExecutor {

    private MaSuitePortalsBridge plugin;
    public List(MaSuitePortalsBridge p){
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MaSuitePortals");
        out.writeUTF("List");
        out.writeUTF(sender.getName());
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        return false;
    }
}
