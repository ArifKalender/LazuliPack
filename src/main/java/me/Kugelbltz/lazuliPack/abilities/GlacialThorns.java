package me.Kugelbltz.lazuliPack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.*;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.Kugelbltz.lazuliPack.LazuliPack.plugin;
import static me.Kugelbltz.lazuliPack.LazuliPack.version;


public class GlacialThorns extends IceAbility implements AddonAbility, ComboAbility, SubAbility {

    Location location;
    Location origin;
    double frequency;
    @Attribute(Attribute.WIDTH)
    double width;
    double iceFrequency;
    @Attribute(Attribute.COOLDOWN)
    long cooldown;
    @Attribute(Attribute.RANGE)
    double range;
    @Attribute(Attribute.DAMAGE)
    double damage;
    @Attribute(Attribute.DURATION)
    long maxDuration;
    int revertTime;
    float increase;
    boolean enabled;

    String description, instructions;

    public GlacialThorns(Player player) {
        super(player);

        setFields();
        if (!bPlayer.isOnCooldown(this)) {
            if (!bPlayer.isChiBlocked()) {

                for (Block block : getNearbyBlocks(player.getLocation(), 3)) {
                    if (this.isWaterbendable(block)) {
                        increase=0;
                        start();
                        break;
                    }
                }

            }
        }
    }


    public static List<Block> getNearbyBlocks(Location location, double radius) {
        List<Block> nearbyBlocks = new ArrayList<>();
        World world = location.getWorld();

        if (world == null) {
            return nearbyBlocks;
        }

        int radiusCeil = (int) Math.ceil(radius);

        int startX = location.getBlockX() - radiusCeil;
        int startY = location.getBlockY() - radiusCeil;
        int startZ = location.getBlockZ() - radiusCeil;

        int endX = location.getBlockX() + radiusCeil;
        int endY = location.getBlockY() + radiusCeil;
        int endZ = location.getBlockZ() + radiusCeil;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    Location blockLocation = new Location(world, x, y, z);
                    if (blockLocation.distance(location) <= radius) {
                        Block block = world.getBlockAt(x, y, z);
                        nearbyBlocks.add(block);
                    }
                }
            }
        }

        return nearbyBlocks;
    }


    private void setFields() {
        frequency = plugin.getConfig().getDouble("Abilities.GlacialThorns.Frequency");
        iceFrequency = frequency * 2;
        cooldown = plugin.getConfig().getLong("Abilities.GlacialThorns.Cooldown");
        range = plugin.getConfig().getDouble("Abilities.GlacialThorns.Range");
        damage = plugin.getConfig().getDouble("Abilities.GlacialThorns.Damage");
        revertTime = plugin.getConfig().getInt("Abilities.GlacialThorns.RevertTime");
        width = plugin.getConfig().getInt("Abilities.GlacialThorns.WidthFromOrigin");
        maxDuration = plugin.getConfig().getInt("Abilities.GlacialThorns.MaxDuration");
        enabled = plugin.getConfig().getBoolean("Abilities.GlacialThorns.Enabled");


        location = player.getLocation();
        origin = location.clone();
        location.add(player.getLocation().getDirection().multiply(2));
        location.setY(player.getLocation().getY() - 1);
    }

    Random random = new Random();
    int height;

    private void generateThorn() {
        List<Location> locations = new ArrayList<>();
        for(int i=1;i<=width;i++){
            locations.add(GeneralMethods.getLeftSide(location,i));
            locations.add(GeneralMethods.getRightSide(location,i));
        }

        locations.add(location);

        playIcebendingSound(location);
        for (Location loopLocation : locations) {

            int chance = random.nextInt(0, 100);


            if (this.isWaterbendable(loopLocation.getBlock()) && chance <= frequency) {
                for(Block innerLoopBlock : GeneralMethods.getBlocksAroundPoint(loopLocation,1.5)){
                    if(innerLoopBlock.getType() == Material.WATER){
                        new TempBlock(innerLoopBlock, Material.ICE.createBlockData(), revertTime);
                    }

                }
                height = random.nextInt(3, 5);
                for (int j = 0; j <= height+increase; j++) {
                    loopLocation.setY(origin.getY() + j);
                    TempBlock buz = new TempBlock(loopLocation.getBlock(), Material.ICE.createBlockData(), revertTime);
                    buz.setCanSuffocate(true);
                    bPlayer.addCooldown(this);
                }

                loopLocation.setY(loopLocation.getY() + 1);
                for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 2.5)) {
                    if (entity instanceof LivingEntity) {
                        if (entity != player) {
                            DamageHandler.damageEntity(entity, damage, this);
                            entity.setVelocity(new Vector(0, 0.75, 0));
                        }
                    }
                }
            } else if (this.isIcebendable(loopLocation.getBlock()) && chance <= iceFrequency) {

                height = random.nextInt(3, 5);
                for (int j = 0; j <= height+increase; j++) {
                    loopLocation.setY(origin.getY() + j);
                    TempBlock buz = new TempBlock(loopLocation.getBlock(), Material.ICE.createBlockData(), revertTime);
                    buz.setCanSuffocate(false);
                    bPlayer.addCooldown(this);
                }
                loopLocation.setY(loopLocation.getY() + 1);
                for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 2.5)) {
                    if (entity instanceof LivingEntity) {
                        if (entity != player) {//OFFFFF KAFAM AGRIDI
                            DamageHandler.damageEntity(entity, damage, this);
                            entity.setVelocity(new Vector(0, 0.75, 0));//idk ve umrumda da deigl i
                        }

                    }
                }

            }
        }
    }


    @Override
    public void progress() {
        if(bPlayer.isChiBlocked()){
            remove();
            bPlayer.addCooldown(this);
            return;
        }
        increase=increase+0.25f;
        generateThorn();
        if(maxDuration+this.getStartTime() <= System.currentTimeMillis()){
            remove();
            bPlayer.addCooldown(this);
        }
        location.setY(origin.getY() - 1);
        location.add(location.getDirection());
        if (location.distance(origin) >= range) {
            remove();
            bPlayer.addCooldown(this);
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
        return "GlacialThorns";
    }

    @Override
    public String getDescription() {
        return "The combo made by Eska and Desna. Bend the ice in such a way to make thorns that pierce through your opponents";
    }

    @Override
    public String getInstructions() {
        return "While looking at a large body of water: IceSpike (Tap sneak) > PhaseChange (Left click) > PhaseChange (Left click)";
    }

    @Override
    public Location getLocation() {
        return location;
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
        return version;
    }

    @Override
    public Object createNewComboInstance(Player player) {
        return new GlacialThorns(player);
    }

    @Override
    public ArrayList<ComboManager.AbilityInformation> getCombination() {
        ArrayList<ComboManager.AbilityInformation> combination = new ArrayList<>();
        combination.add(new ComboManager.AbilityInformation("IceSpike", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("IceSpike", ClickType.SHIFT_UP));
        combination.add(new ComboManager.AbilityInformation("PhaseChange", ClickType.LEFT_CLICK));
        combination.add(new ComboManager.AbilityInformation("PhaseChange", ClickType.LEFT_CLICK));
        return combination;
    }
}
