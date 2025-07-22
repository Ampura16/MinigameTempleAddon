package top.brmc.ampura16.minigamestemplateaddon.mobs;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThunderEnder implements Listener {

    private static final Map<UUID, Integer> attackCountMap = new HashMap<>();
    private static JavaPlugin plugin;

    public static void initialize(JavaPlugin plugin) {
        ThunderEnder.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new ThunderEnder(), plugin);
    }

    public static Zombie spawn(Location loc) {
        try {
            Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            zombie.setBaby(false);
            zombie.setCanPickupItems(false);

            configureAttributes(zombie);
            equipZombie(zombie);
            addVisualEffects(zombie);

            // 初始化攻击计数器
            attackCountMap.put(zombie.getUniqueId(), 0);

            return zombie;
        } catch (Exception e) {
            throw new RuntimeException("生成ThunderEnder失败", e);
        }
    }

    private static void configureAttributes(Zombie zombie) {
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0);
        zombie.setHealth(100.0);
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
        zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8.0);
        zombie.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8);
    }

    private static void equipZombie(Zombie zombie) {
        Color armorColor = Color.fromRGB(128, 0, 128);

        ItemStack helmet = createArmorPiece(Material.LEATHER_HELMET, armorColor);
        ItemStack chestplate = createArmorPiece(Material.LEATHER_CHESTPLATE, armorColor);
        ItemStack leggings = createArmorPiece(Material.LEATHER_LEGGINGS, armorColor);
        ItemStack boots = createArmorPiece(Material.LEATHER_BOOTS, armorColor);

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 1);
        sword.addEnchantment(Enchantment.KNOCKBACK, 1);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chestplate);
        zombie.getEquipment().setLeggings(leggings);
        zombie.getEquipment().setBoots(boots);
        zombie.getEquipment().setItemInMainHand(sword);

        zombie.getEquipment().setHelmetDropChance(0.0f);
        zombie.getEquipment().setChestplateDropChance(0.0f);
        zombie.getEquipment().setLeggingsDropChance(0.0f);
        zombie.getEquipment().setBootsDropChance(0.0f);
        zombie.getEquipment().setItemInMainHandDropChance(0.1f);
    }

    private static ItemStack createArmorPiece(Material material, Color color) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        meta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private static void addVisualEffects(Zombie zombie) {
        zombie.addPotionEffect(new PotionEffect(
                PotionEffectType.FIRE_RESISTANCE,
                Integer.MAX_VALUE,
                0,
                false,
                false
        ));
        zombie.setCustomName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "ThunderEnder");
        zombie.setCustomNameVisible(true);
        zombie.setRemoveWhenFarAway(false);
    }

    public static boolean isThunderEnder(Zombie zombie) {
        return zombie != null
                && zombie.isValid()
                && !zombie.isBaby()
                && "ThunderEnder".equals(ChatColor.stripColor(zombie.getCustomName()))
                && zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() >= 100.0
                && zombie.getEquipment().getHelmet() != null;
    }
}
