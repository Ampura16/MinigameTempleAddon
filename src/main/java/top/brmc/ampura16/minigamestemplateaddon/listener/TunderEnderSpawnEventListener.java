package top.brmc.ampura16.minigamestemplateaddon.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.brmc.ampura16.minigamestemplateaddon.event.TunderEnderSpawnEvent;

public class TunderEnderSpawnEventListener implements Listener {

    String listenerName = ChatColor.LIGHT_PURPLE + "[TunderEnderSpawnEvent] ";

    @EventHandler
    public void onTunderEnderSpawn(TunderEnderSpawnEvent event) {
        // 向所有在线玩家广播消息
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(listenerName + ChatColor.GOLD + "已调用.")
        );
    }
}
