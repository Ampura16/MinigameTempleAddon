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
import top.brmc.ampura16.minigamestemplateaddon.event.ThunderEnderSpawnEvent;
import top.brmc.ampura16.minigamestemplateaddon.mobs.ThunderEnder;

import java.util.HashSet;

public class ThunderEnderSpawnListener implements Listener {

    private final World targetWorld;
    private final Location targetBlock1;
    private final Location targetBlock2;
    private final Location spawnLocation;
    private final HashSet<Location> brokenBlocks = new HashSet<>();
    private boolean isSequenceActive = false;

    public ThunderEnderSpawnListener() {
        this.targetWorld = JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class).getServer().getWorld("world");
        this.targetBlock1 = new Location(targetWorld, 221, 73, -277);
        this.targetBlock2 = new Location(targetWorld, 221, 73, -281);
        this.spawnLocation = new Location(targetWorld, 221, 73, -279);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location brokenLoc = block.getLocation();

        if (!isTargetBlock(brokenLoc)) {
            resetState();
            return;
        }

        if (isSequenceActive) {
            return;
        }

        brokenBlocks.add(brokenLoc);
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC +
                "目标方块被破坏... " + ChatColor.RED + "(" + brokenBlocks.size() + "/2)");

        if (brokenBlocks.size() == 2) {
            startSummoningSequence(player);
        }
    }

    private boolean isTargetBlock(Location loc) {
        return loc.equals(targetBlock1) || loc.equals(targetBlock2);
    }

    private void resetState() {
        brokenBlocks.clear();
        isSequenceActive = false;
    }

    private void startSummoningSequence(Player player) {
        isSequenceActive = true;
        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC +
                "所有目标方块都被破坏...");

        // 设置雷暴天气
        setThunderWeather();

        // 触发事件
        ThunderEnderSpawnEvent event = new ThunderEnderSpawnEvent(targetWorld, spawnLocation.clone());
        Bukkit.getPluginManager().callEvent(event);

        // 5秒后开始闪电序列
        new BukkitRunnable() {
            @Override
            public void run() {
                startLightningSequence();
            }
        }.runTaskLater(JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class), 100);
    }

    private void setThunderWeather() {
        targetWorld.setStorm(true);
        targetWorld.setThundering(true);
        targetWorld.setWeatherDuration(20 * 60 * 5); // 5分钟
        targetWorld.playSound(spawnLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
    }

    private void startLightningSequence() {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < 3) {
                    targetWorld.strikeLightningEffect(spawnLocation);
                    count++;
                } else {
                    ThunderEnder.spawn(spawnLocation);
                    resetState();
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class), 0, 20);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location placedLoc = event.getBlock().getLocation();
        if (isTargetBlock(placedLoc) && brokenBlocks.contains(placedLoc)) {
            brokenBlocks.remove(placedLoc);
            isSequenceActive = false;
        }
    }
}
