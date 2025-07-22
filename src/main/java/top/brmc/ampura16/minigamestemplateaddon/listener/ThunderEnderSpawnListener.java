package top.brmc.ampura16.minigamestemplateaddon.listener;

import org.bukkit.*;
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
import java.util.Set;

public class ThunderEnderSpawnListener implements Listener {

    String listenerName = ChatColor.LIGHT_PURPLE + "[ThunderEnderSpawnEvent] ";
    private final World targetWorld;
    private final Location targetBlock1;
    private final Location targetBlock2;
    private final Location spawnLocation;
    private final Set<Location> brokenBlocks = new HashSet<>();
    private boolean isSequenceActive = false;
    private long lastBreakTime = 0;
    private static final long COOLDOWN_MS = 5000;

    public ThunderEnderSpawnListener(MinigamesTemplateAddonMain plugin) {
        this.targetWorld = plugin.getServer().getWorld("world");
        this.targetBlock1 = new Location(targetWorld, 341, 63, -244);
        this.targetBlock2 = new Location(targetWorld, 341, 63, -240);
        this.spawnLocation = new Location(targetWorld, 341, 64, -242);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isSequenceActive) return;

        Location brokenLoc = event.getBlock().getLocation();
        if (!isTargetBlock(brokenLoc)) return;

        if (System.currentTimeMillis() - lastBreakTime > COOLDOWN_MS) {
            brokenBlocks.clear();
        }
        lastBreakTime = System.currentTimeMillis();

        brokenBlocks.add(brokenLoc);
        event.getPlayer().sendMessage(ChatColor.RED + "(" + brokenBlocks.size() + "/2)");
        checkBothBlocksBroken(event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location placedLoc = event.getBlock().getLocation();
        if (isTargetBlock(placedLoc)) {
            brokenBlocks.remove(placedLoc);
            isSequenceActive = false;
        }
    }

    private boolean isTargetBlock(Location loc) {
        return loc.equals(targetBlock1) || loc.equals(targetBlock2);
    }

    private void checkBothBlocksBroken(Player player) {
        if (brokenBlocks.contains(targetBlock1) &&
                brokenBlocks.contains(targetBlock2) &&
                System.currentTimeMillis() - lastBreakTime <= COOLDOWN_MS) {
            startSummoningSequence(player);
        }
    }

    private void startSummoningSequence(Player player) {
        isSequenceActive = true;
        setThunderWeather();

        // 保留事件调用提示
        ThunderEnderSpawnEvent event = new ThunderEnderSpawnEvent(targetWorld, spawnLocation.clone());
        Bukkit.getPluginManager().callEvent(event);
        player.sendMessage(listenerName + ChatColor.GOLD + "已调用.");

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
        targetWorld.setWeatherDuration(20 * 60 * 5);
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
                    brokenBlocks.clear();
                    isSequenceActive = false;
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(MinigamesTemplateAddonMain.class), 0, 20);
    }
}
