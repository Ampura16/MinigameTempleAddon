package top.brmc.ampura16.minigamestemplateaddon;

import org.bukkit.plugin.java.JavaPlugin;
import top.brmc.ampura16.minigamestemplateaddon.listener.ThunderEnderSpawnListener;
import top.brmc.ampura16.minigamestemplateaddon.triggers.ExamplePlaceBlockTrigger;

public final class MinigamesTemplateAddonMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerEvents();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ExamplePlaceBlockTrigger(), this);
        getServer().getPluginManager().registerEvents(new TempleMethod(), this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ThunderEnderSpawnListener(this), this);
    }
}
