package top.brmc.ampura16.minigamestemplateaddon.event;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TunderEnderSpawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final World targetWorld;

    public TunderEnderSpawnEvent(World targetWorld) {
        this.targetWorld = targetWorld;
        targetWorld.setStorm(true);
        targetWorld.setThundering(true);
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
}
