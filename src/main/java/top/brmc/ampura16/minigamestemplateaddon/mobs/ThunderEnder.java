package top.brmc.ampura16.minigamestemplateaddon.mobs;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ThunderEnder {

    public static Zombie spawn(Location loc) {
        try {
            // 使用纯Bukkit API创建僵尸
            Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);

            // 禁止生成小僵尸
            zombie.setBaby(false);
            zombie.setCanPickupItems(false); // 防止捡起物品改变装备

            // 设置属性
            configureAttributes(zombie);
            // 添加装备
            equipZombie(zombie);
            // 添加视觉效果
            addVisualEffects(zombie);

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
        zombie.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8); // 增加击退抗性
    }

    private static void equipZombie(Zombie zombie) {
        // 紫色皮革盔甲
        Color armorColor = Color.fromRGB(128, 0, 128); // 深紫色

        // 头盔
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(armorColor);
        helmetMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
        helmetMeta.setUnbreakable(true);
        helmet.setItemMeta(helmetMeta);

        // 胸甲
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setColor(armorColor);
        chestplateMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
        chestplateMeta.setUnbreakable(true);
        chestplate.setItemMeta(chestplateMeta);

        // 护腿
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(armorColor);
        leggingsMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
        leggingsMeta.setUnbreakable(true);
        leggings.setItemMeta(leggingsMeta);

        // 靴子
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(armorColor);
        bootsMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
        bootsMeta.setUnbreakable(true);
        boots.setItemMeta(bootsMeta);

        // 武器
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 1);
        sword.addEnchantment(Enchantment.KNOCKBACK, 1);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

        // 设置装备
        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chestplate);
        zombie.getEquipment().setLeggings(leggings);
        zombie.getEquipment().setBoots(boots);
        zombie.getEquipment().setItemInMainHand(sword);

        // 设置装备掉落率
        zombie.getEquipment().setHelmetDropChance(0.0f);
        zombie.getEquipment().setChestplateDropChance(0.0f);
        zombie.getEquipment().setLeggingsDropChance(0.0f);
        zombie.getEquipment().setBootsDropChance(0.0f);
        zombie.getEquipment().setItemInMainHandDropChance(0.1f); // 只有武器有10%几率掉落
    }

    private static void addVisualEffects(Zombie zombie) {

        // 抗火效果
        zombie.addPotionEffect(new PotionEffect(
                PotionEffectType.FIRE_RESISTANCE,
                Integer.MAX_VALUE,
                0,
                false,
                false
        ));

        // 设置名称和属性
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
                && zombie.getEquipment().getHelmet() != null; // 通过装备进一步验证
    }
}
