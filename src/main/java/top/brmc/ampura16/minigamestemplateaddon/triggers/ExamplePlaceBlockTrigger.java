package top.brmc.ampura16.minigamestemplateaddon.triggers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import top.brmc.ampura16.minigamestemplateaddon.MinigamesTemplateAddonMain;

public class ExamplePlaceBlockTrigger implements Listener {

    // 设定特定位置
    private final Location targetLocation1 = new Location(JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class).getServer().getWorld("world"), 358, 64, 107); // 请根据实际世界名称更改为实际世界的Location
    private final Location targetLocation2 = new Location(JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class).getServer().getWorld("world"), 356, 64, 105); // 请根据实际世界名称更改为实际世界的Location

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location placedLocation = event.getBlock().getLocation();

        if (event.getBlock().getType() == Material.EMERALD_BLOCK) {
            // 检查放置的位置是否是目标位置
            if (placedLocation.equals(targetLocation1)) {
                // 发送提升或者其他逻辑
                player.sendMessage(ChatColor.GREEN + "你在该特定位置放了一个 " + Material.EMERALD_BLOCK);
                player.sendMessage(ChatColor.AQUA + "这里可以改成一些剧情(?");
                player.sendMessage(ChatColor.AQUA + "添加更多剧情或者联动检测条件和触发什么其他东西...");
                // 这里可以添加更多的逻辑，如给予玩家物品、经验等
            }
        }
        else if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
            if (placedLocation.equals(targetLocation2)) {
                // 发送提升或者其他逻辑
                player.sendMessage(ChatColor.GREEN + "你在该特定位置放了一个 " + Material.DIAMOND_BLOCK);
                player.sendMessage(ChatColor.AQUA + "这里可以改成一些剧情(?");
                player.sendMessage(ChatColor.AQUA + "添加更多剧情或者联动检测条件和触发什么其他东西...");
                // 这里可以添加更多的逻辑，如给予玩家物品、经验等
            }
        }
    }
}
