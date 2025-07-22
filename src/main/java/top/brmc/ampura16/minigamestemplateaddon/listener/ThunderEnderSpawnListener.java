package top.brmc.ampura16.minigamestemplateaddon.listener;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import top.brmc.ampura16.minigamestemplateaddon.event.ThunderEnderSpawnEvent;
import top.brmc.ampura16.minigamestemplateaddon.mobs.ThunderEnder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ThunderEnderSpawnListener implements Listener {

    private final Set<Location> targetBlocks = new HashSet<>();
    private final Set<Location> brokenBlocks = new HashSet<>();
    private final JavaPlugin plugin;
    private boolean isSequenceActive = false;
    String listenerName = ChatColor.LIGHT_PURPLE + "[TunderEnderSpawnEvent] ";

    public ThunderEnderSpawnListener(JavaPlugin plugin) {
        this.plugin = plugin;
        World world = plugin.getServer().getWorld("world");
        if (world != null) {
            targetBlocks.add(new Location(world, 221, 73, -277));
            targetBlocks.add(new Location(world, 221, 73, -281));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location brokenLoc = event.getBlock().getLocation();

        if (!targetBlocks.contains(brokenLoc)) {
            brokenBlocks.clear();
            return;
        }

        if (isSequenceActive) {
            return;
        }

        brokenBlocks.add(brokenLoc);
        Player player = event.getPlayer();

        player.sendMessage(ChatColor.DARK_RED + "⚡ 目标方块被破坏 " + brokenBlocks.size() + "/2");

        if (brokenBlocks.size() == targetBlocks.size()) {
            // player.sendMessage(ChatColor.DARK_RED + "警告!雷霆之力正在聚集...");
            isSequenceActive = true;

            Location spawnLoc = new Location(brokenLoc.getWorld(), 221, 73, -279);
            setThunderWeather(spawnLoc);

            // 5秒后开始闪电序列
            new BukkitRunnable() {
                @Override
                public void run() {
                    startLightningSequence(spawnLoc);
                }
            }.runTaskLater(plugin, 100); // 5秒 = 100 ticks
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location placedLoc = event.getBlock().getLocation();
        if (targetBlocks.contains(placedLoc)) {
            brokenBlocks.remove(placedLoc);
            isSequenceActive = false;
            // 移除了天气恢复代码
        }
    }

    private void setThunderWeather(Location location) {
        World world = location.getWorld();
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(20 * 60 * 5); // 5分钟雷暴
        world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
    }

    private void startLightningSequence(Location location) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < 3) { // 3道闪电
                    Objects.requireNonNull(location.getWorld()).strikeLightningEffect(location);
                    count++;
                } else {
                    // 触发事件
                    ThunderEnderSpawnEvent tesEvent = new ThunderEnderSpawnEvent(
                            location.getWorld(),
                            location.clone()
                    );
                    Bukkit.getPluginManager().callEvent(tesEvent);
                    // 生成Boss
                    ThunderEnder.spawn(location);

                    // 事件调用提示
                    Bukkit.getOnlinePlayers().forEach(player ->
                            player.sendMessage(listenerName + ChatColor.GOLD + "已调用.")
                    );

                    // 发送提示
                    location.getWorld().getPlayers().forEach(p -> {
                        if (p.getLocation().distance(location) < 50) {
                            /**
                            p.sendTitle(
                                    "§5⚡ 雷霆终结者 ⚡",
                                    "§c已在雷暴中降临！",
                                    10, 70, 20
                            );
                             */
                        }
                    });
                    brokenBlocks.clear();
                    isSequenceActive = false;
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20); // 每1秒一次
    }
}
