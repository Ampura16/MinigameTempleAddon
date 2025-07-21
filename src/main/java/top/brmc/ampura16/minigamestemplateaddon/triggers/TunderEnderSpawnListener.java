package top.brmc.ampura16.minigamestemplateaddon.triggers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import top.brmc.ampura16.minigamestemplateaddon.MinigamesTemplateAddonMain;
import top.brmc.ampura16.minigamestemplateaddon.event.TunderEnderSpawnEvent;

import java.util.HashSet;
import java.util.Objects;

public class TunderEnderSpawnListener implements Listener {

    private final World targetWorld = JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class).getServer().getWorld("world");
    private final Location targetTunderEnderBlock1 = new Location(targetWorld, 221, 73, -277);
    private final Location targetTunderEnderBlock2 = new Location(targetWorld, 221, 73, -281); // 目标方块2坐标
    private final HashSet<Location> brokenBlocks = new HashSet<>(); // 记录被破坏的方块位置
    private boolean lightningReleased = false; // 用于跟踪闪电是否已释放

    @EventHandler
    public void onBlockBreak(BlockBreakEvent bpe) {
        Block block = bpe.getBlock();
        Location brokenLocation = block.getLocation();

        // 检查是否破坏目标方块1或目标方块2
        if (brokenLocation.equals(targetTunderEnderBlock1)
                || brokenLocation.equals(targetTunderEnderBlock2)) {
            // 将被破坏方块的位置添加到集合中
            brokenBlocks.add(brokenLocation);

            // 发送消息给玩家
            Player player = bpe.getPlayer();
            player.sendMessage(ChatColor.DARK_RED + ""
                    + ChatColor.ITALIC + "有一个目标方块被破坏了... "
                    + ChatColor.RED + "(" + brokenBlocks.size()
                    + "/2)");

            // 检查是否两个目标方块都被破坏
            if (brokenBlocks.contains(targetTunderEnderBlock1)
                    && brokenBlocks.contains(targetTunderEnderBlock2)
                    && !lightningReleased) {
                player.sendMessage(ChatColor.DARK_RED + ""
                        + ChatColor.ITALIC
                        + "所有目标方块都被破坏...");

                TunderEnderSpawnEvent tesEvent = new TunderEnderSpawnEvent(targetWorld);
                Bukkit.getPluginManager().callEvent(tesEvent); // 触发事件

                // 现在等待5秒，然后释放闪电
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        releaseLightningAt(new Location(targetWorld, 221, 73, -279), 2);

                        lightningReleased = true;
                        // 清除已破坏的方块状态
                        brokenBlocks.clear();
                        lightningReleased = false;
                    }
                }.runTaskLater(JavaPlugin.getPlugin(
                        MinigamesTemplateAddonMain.class), 100); // 100 ticks = 5 seconds
            }
        } else {
            // 如果破坏的方块不是目标方块之一，则清除记录
            brokenBlocks.clear();
            lightningReleased = false; // 重置闪电释放状态
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location placedLocation = block.getLocation();

        // 检查放置的方块是否是目标方块1或目标方块2
        if (placedLocation.equals(targetTunderEnderBlock1)
                || placedLocation.equals(targetTunderEnderBlock2)) {
            // 如果放回的是目标方块1，并且集合中包含该方块，移除记录
            if (placedLocation.equals(targetTunderEnderBlock1)
                    && brokenBlocks.contains(targetTunderEnderBlock1)) {
                brokenBlocks.remove(targetTunderEnderBlock1);
            }
            // 如果放回的是目标方块2，并且集合中包含该方块，移除记录
            if (placedLocation.equals(targetTunderEnderBlock2)
                    && brokenBlocks.contains(targetTunderEnderBlock2)) {
                brokenBlocks.remove(targetTunderEnderBlock2);
            }
        }
    }

    private void releaseLightningAt(Location location, int times) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < times) {
                    // 生成闪电效果
                    Objects.requireNonNull(location.getWorld()).strikeLightningEffect(location);
                    count++;
                } else {
                    // 取消任务
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class), 0, 5);
    }
}
