package top.brmc.ampura16.minigamestemplateaddon.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ThunderEnderSpawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final World targetWorld;
    private final Location spawnLocation;

    public ThunderEnderSpawnEvent(World targetWorld, Location spawnLocation) {
        this.targetWorld = targetWorld;
        this.spawnLocation = spawnLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public World getTargetWorld() {
        return targetWorld;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
