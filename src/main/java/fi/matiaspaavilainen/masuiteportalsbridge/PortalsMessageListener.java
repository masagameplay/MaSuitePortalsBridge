package fi.matiaspaavilainen.masuiteportalsbridge;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PortalsMessageListener implements PluginMessageListener {

    private MaSuitePortalsBridge plugin;
    public PortalsMessageListener(MaSuitePortalsBridge p){
        plugin = p;
    }

    /**
     * A method that will be thrown when a PluginMessageSource sends a plugin
     * message on a registered channel.
     *
     * @param channel Channel that the message was sent through.
     * @param player  Source of the message.
     * @param message The raw message that was sent.
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")){
            return;
        }
    }
}
