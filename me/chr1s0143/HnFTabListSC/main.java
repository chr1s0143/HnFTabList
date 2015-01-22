package HnFTabList.me.chr1s0143.HnFTabListSC;

/**
 * Created by chris on 21/01/2015.
 */
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.minecraft.server.v1_8_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
    private ProtocolManager protocolManager;
    private String fixColors(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable() {
        saveDefaultConfig();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (cmd.getName().equalsIgnoreCase("hnftablist")) {
                    if (args.length == 0)
                        player.sendMessage(ChatColor.DARK_GREEN + "try doing " + ChatColor.GOLD + "/hnf reload ");
                    else if (args[0].equalsIgnoreCase("reload")) {
                        try {
                            reloadConfig();
                            player.sendMessage(ChatColor.GREEN + "Config File has been reloaded!");
                        } catch (Exception exc) {
                            exc.printStackTrace();
                            player.sendMessage(ChatColor.RED + "Config file reload encounter a problem. See Console for more info");
                        }
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "HnFTabList commands:");
                        player.sendMessage(ChatColor.GOLD + "/hnf reload" + ChatColor.GRAY + ChatColor.BOLD + " - " + ChatColor.RESET
                                + ChatColor.GREEN + "reload the config file");
                    }
                }
            } else {
                player.sendMessage("Only OP's can use this command!");
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PacketContainer packetcontainer = this.protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        packetcontainer.getChatComponents()
                .write(0, WrappedChatComponent.fromText(fixColors(getConfig().getString("header", ""))))
                .write(1, WrappedChatComponent.fromText(fixColors(getConfig().getString("footer", ""))));
        try
        {
            this.protocolManager.sendServerPacket(event.getPlayer(), packetcontainer);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
