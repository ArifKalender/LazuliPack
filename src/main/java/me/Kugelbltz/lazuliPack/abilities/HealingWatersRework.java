package me.Kugelbltz.lazuliPack.abilities;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.*;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.waterbending.util.WaterReturn;
import me.Kugelbltz.lazuliPack.LazuliPack;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;

import static me.Kugelbltz.lazuliPack.LazuliPack.plugin;
import static me.Kugelbltz.lazuliPack.LazuliPack.random;

public class HealingWatersRework extends HealingAbility implements AddonAbility, SubAbility {

    @com.projectkorra.projectkorra.attribute.Attribute(com.projectkorra.projectkorra.attribute.Attribute.RANGE)
    int range;
    @com.projectkorra.projectkorra.attribute.Attribute(com.projectkorra.projectkorra.attribute.Attribute.COOLDOWN)
    long cooldown;
    short power;
    @com.projectkorra.projectkorra.attribute.Attribute("MaxHeal")
    int maxHeal;

    String description, instructions;

    boolean started;
    boolean enabled;
    int increment;
    Block prepareBlock;
    Vector vector;
    Location abilLocation;
    public static HashMap<Player, LivingEntity> healingPlayer = new HashMap<Player, LivingEntity>();
    public static HashMap<Player, Boolean> isCurrentlyHealing = new HashMap<Player, Boolean>();

    public HealingWatersRework(Player player, boolean startedWithSneak) {
        super(player);

        setFields();
        if (bPlayer.canBend(this)) {

            if (!bPlayer.isChiBlocked()) {
                if (startedWithSneak) {
                    if (WaterReturn.hasWaterBottle(player)) {
                        abilLocation = player.getLocation();
                        WaterReturn.emptyWaterBottle(player);
                        isCurrentlyHealing.put(player, true);
                        start();
                    }
                } else if (prepare()) {
                    isCurrentlyHealing.put(player, true);
                    start();
                }
            }
        }
    }

    private void setFields() {
        range = plugin.getConfig().getInt("Abilities.HealingWaters.SelectRange");
        cooldown = plugin.getConfig().getLong("Abilities.HealingWaters.Cooldown");
        power = (short) plugin.getConfig().getInt("Abilities.HealingWaters.HealFrequency");
        maxHeal = plugin.getConfig().getInt("Abilities.HealingWaters.MaxHeal");

        description = plugin.getConfig().getString("Strings.HealingWaters.Description");
        instructions = plugin.getConfig().getString("Strings.HealingWaters.Instructions");
        enabled = plugin.getConfig().getBoolean("Abilities.HealingWaters.Enabled");
        increment = 0;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    private static void cancelPrevious(Player player) {
        Collection<HealingWatersRework> hw = getAbilities(player, HealingWatersRework.class);

        for (HealingWatersRework oldhw : hw) {
            if (oldhw != null && !oldhw.started) {
                oldhw.remove();
            }
        }

    }

    private boolean prepare() {
        if (isCurrentlyHealing.get(player) == null) {
            this.prepareBlock = BlockSource.getWaterSourceBlock(player, range, ClickType.LEFT_CLICK, true, true, bPlayer.canPlantbend());

            if (this.prepareBlock != null && this.isWaterbendable(prepareBlock) || this.isWaterbendable(player.getEyeLocation().getBlock())) {
                this.abilLocation = this.prepareBlock.getLocation();
                cancelPrevious(player);
                return true;
            }
        }
        return false;
    }

    double theta = 0;

    private void genCircle(Location centre, double radius) {
        double x = centre.getX();
        double y = centre.getY();
        double z = centre.getZ();
        double lincrement = (Math.PI * 2) / 15;
        theta += lincrement;
        double offsetX = radius * Math.cos(theta);
        double offsetZ = radius * Math.sin(theta);
        Location particleLocation = new Location(centre.getWorld(), x + offsetX, y, z + offsetZ);
        if (healingPlayer.get(player) == null) {
            if (increment >= 70) {
                GeneralMethods.displayColoredParticle("#a2befc", particleLocation, 3, 0, 0, 0);
                if (random.nextInt(0, 10) <= 2)
                    particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 1, 0.05, 0.05, 0.05, 0.05, Material.LIGHT_BLUE_GLAZED_TERRACOTTA.createBlockData());
            } else if (increment >= 35) {
                GeneralMethods.displayColoredParticle("#77a1fc", particleLocation, 3, 0, 0, 0);

                if (random.nextInt(0, 10) <= 2)
                    particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 1, 0.05, 0.05, 0.05, 0.05, Material.LIGHT_BLUE_GLAZED_TERRACOTTA.createBlockData());
            } else {
                GeneralMethods.displayColoredParticle("#4f87ff", particleLocation, 3, 0, 0, 0);

                if (random.nextInt(0, 10) <= 2)
                    particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 2, 0.05, 0.05, 0.05, 0.05, Material.BLUE_WOOL.createBlockData());
            }
        } else {
            if (increment >= 70) {
                GeneralMethods.displayColoredParticle("#a2befc", particleLocation, 3, 0, 0.3, 0);
                //ParticleEffect.FALLING_DUST.display(particleLocation, 1, 0.05F, 0.05F, 0.05F, 0.05F, Material.WHITE_CONCRETE.createBlockData());
                if (random.nextInt(0, 10) <= 2)
                    particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 1, 0.05, 0.3, 0.05, 0.05, Material.LIGHT_BLUE_GLAZED_TERRACOTTA.createBlockData());
            } else if (increment >= 35) {
                GeneralMethods.displayColoredParticle("#77a1fc", particleLocation, 3, 0, 0.3, 0);

                if (random.nextInt(0, 10) <= 2)
                    particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 1, 0.05, 0.3, 0.05, 0.05, Material.LIGHT_BLUE_GLAZED_TERRACOTTA.createBlockData());
            } else {
                GeneralMethods.displayColoredParticle("#4f87ff", particleLocation, 3, 0, 0.3, 0);

                if (random.nextInt(0, 10) <= 2)
                    particleLocation.getWorld().spawnParticle(Particle.FALLING_DUST, particleLocation, 2, 0.05, 0.3, 0.05, 0.05, Material.BLUE_WOOL.createBlockData());
            }
        }
    }


    private Location getHandLocation(LivingEntity player, boolean healingElse) {
        if (!healingElse) {//yok ya cok zor bişeye benzemiyor evt
            Location temp;
            temp = player.getLocation();
            temp.setY(player.getLocation().getY() + 0.5);
            temp.add(player.getLocation().getDirection().multiply(-0.25));
            return GeneralMethods.getRightSide(temp, 0.4);
        } else {
            Location temp;
            temp = player.getLocation();
            temp.setY(player.getLocation().getY() + 0.7);
            return temp;
        }
    }


    @Override
    public void progress() {
        if (!started) {
            if (player.isSneaking()) {
                started = true;
            } else {
                abilLocation.getWorld().spawnParticle(Particle.FALLING_DUST, abilLocation, 2, 0.1, 0.1, 0.1, 0.05, Material.BLUE_WOOL.createBlockData());
                abilLocation.getWorld().spawnParticle(Particle.FALLING_DRIPSTONE_WATER, abilLocation, 2, 0.1, 0.1, 0.1, 0.05);
            }
        }
        if ((started && !player.isSneaking()) || player.isDead() ||  maxHeal <= 0 || getHandLocation(player, false).distance(abilLocation) > range || player.isDead() || !player.isOnline() && !bPlayer.canBend(this) || bPlayer.isChiBlocked() || !bPlayer.getBoundAbilityName().equalsIgnoreCase("HealingWaters")) {
            remove();
            isCurrentlyHealing.remove(player);
            healingPlayer.remove(player);
            bPlayer.addCooldown(this);
            return;
        }

        if (started) {


            if (healingPlayer.get(player) == null) {

                String msg = Element.HEALING.getColor()+"Your health: §f"+(int)player.getHealth()+Element.HEALING.getColor()+"/§f"+(int)player.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
                TextComponent textComponent = new TextComponent(msg);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);


                vector = getHandLocation(player, false).toVector().subtract(abilLocation.toVector()).normalize().multiply(0.35);
                abilLocation.add(vector);
                abilLocation.getWorld().playSound(abilLocation, Sound.BLOCK_BEACON_AMBIENT, 0.3f, 2f);

                if (abilLocation.distance(getHandLocation(player, false)) <= 0.5) {

                    increment++;
                    genCircle(getHandLocation(player, false), 0.2);

                    if (abilLocation.getBlock().getType() == Material.AIR || abilLocation.getBlock().getType() == Material.LIGHT) {
                        new TempBlock(abilLocation.getBlock(), Material.LIGHT.createBlockData(), 500, this);
                    }

                    if (increment % power == 0) {
                        if (player.getHealth() < player.getAttribute(Attribute.MAX_HEALTH).getBaseValue()) {
                            //player.setHealth(player.getHealth() + 0.25);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
                            maxHeal--;
                        }
                    }
                } else {
                    GeneralMethods.displayColoredParticle("0099FF", abilLocation, 2, 0.1, 0.1, 0.1);
                }
            } else {

                String msg = Element.HEALING.getColor()+healingPlayer.get(player).getName()+"'s Health: §f"+(int)healingPlayer.get(player).getHealth()+Element.HEALING.getColor()+"/§f"+(int)healingPlayer.get(player).getAttribute(Attribute.MAX_HEALTH).getBaseValue();
                TextComponent textComponent = new TextComponent(msg);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);

                vector = getHandLocation(healingPlayer.get(player), true).toVector().subtract(abilLocation.toVector()).normalize().multiply(0.35);
                abilLocation.add(vector);
                abilLocation.getWorld().playSound(abilLocation, Sound.BLOCK_BEACON_AMBIENT, 0.3f, 2f);

                if (abilLocation.distance(getHandLocation(healingPlayer.get(player), true)) <= 0.5) {

                    increment++;
                    genCircle(getHandLocation(healingPlayer.get(player), true), 0.4);

                    if (abilLocation.getBlock().isPassable()) {
                        new TempBlock(abilLocation.getBlock(), Material.LIGHT.createBlockData(), 500, this);
                    }

                    if (abilLocation.distance(healingPlayer.get(player).getLocation()) > range) {
                        remove();
                        isCurrentlyHealing.remove(player);
                        healingPlayer.remove(player);
                        bPlayer.addCooldown(this);
                        return;
                    }

                    if (increment % power == 0) {
                        if (healingPlayer.get(player).getHealth() < healingPlayer.get(player).getAttribute(Attribute.MAX_HEALTH).getBaseValue()) {
                            healingPlayer.get(player).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
                            // healingPlayer.get(player).setHealth(healingPlayer.get(player).getHealth() + 0.25);
                            maxHeal--;
                        }
                    }
                } else {
                    GeneralMethods.displayColoredParticle("0099FF", abilLocation, 2, 0.1, 0.1, 0.1);
                }
            }
        }

    }

    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public String getName() {
        return "HealingWaters";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getInstructions() {
        return instructions;
    }


    @Override
    public Location getLocation() {
        return abilLocation;
    }

    @Override
    public void load() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getAuthor() {
        return "Kugelbltz";
    }

    @Override
    public String getVersion() {
        return LazuliPack.version;
    }
}
