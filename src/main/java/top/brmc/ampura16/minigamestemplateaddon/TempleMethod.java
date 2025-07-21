package top.brmc.ampura16.minigamestemplateaddon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TempleMethod implements Listener {

    String templeToolName = ChatColor.AQUA + "[测试工具] ";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String adminName = player.getName();

        // 检查玩家的名字是否在特定名单中
        if (adminName.equals("V_I0")
                || adminName.equals("MeTerminator")
                || adminName.equals("Ampura16")) {
            printNotice(player);
        }
    }

    void printNotice(Player player) {
        player.sendMessage(templeToolName + ChatColor.GREEN + "燧石 = goBrush");
        player.sendMessage(templeToolName + ChatColor.GREEN + "羽毛 = goPaint");
        player.sendMessage(templeToolName + ChatColor.AQUA + "这两个工具联动了FAWE,可以快速刷地图");
        player.sendMessage(templeToolName + ChatColor.GOLD + "具体用法有待研究...");
    }
}
