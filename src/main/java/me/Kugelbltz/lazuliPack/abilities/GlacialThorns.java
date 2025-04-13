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
import org.bukkit.Particle;
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

    Location location,origin;

    @Attribute(Attribute.WIDTH)
    double width;
    @Attribute(Attribute.COOLDOWN)
    long cooldown;
    @Attribute(Attribute.RANGE)
    double range;
    @Attribute(Attribute.DAMAGE)
    double damage;
    @Attribute(Attribute.DURATION)
    long maxDuration;

    Random random = new Random();
    int height;
    int revertTime;
    float increase;
    double iceFrequency;
    double frequency;
    boolean enabled;



    public GlacialThorns(Player player) {
        super(player);

        setFields();
        if (!bPlayer.isOnCooldown(this)) {
            if (!bPlayer.isChiBlocked()) {

                for (Block block : GeneralMethods.getBlocksAroundPoint(player.getLocation(), 3)) {
                    if (this.isWaterbendable(block)) {
                        increase=0;
                        start();
                        break;
                    }
                }

            }
        }
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


    //Don't stare at it too long, you'll go mad
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

            //Honestly, the below is really old code and I forgot what does what and I'm too lazy to rework it.
            if (this.isWaterbendable(loopLocation.getBlock()) && chance <= frequency) {
                for(Block innerLoopBlock : GeneralMethods.getBlocksAroundPoint(loopLocation,1.5)){
                    if(innerLoopBlock.getType() == Material.WATER){
                        new TempBlock(innerLoopBlock, Material.ICE.createBlockData(), revertTime);
                    }
                    innerLoopBlock.getWorld().spawnParticle(Particle.CLOUD,innerLoopBlock.getLocation().add(0,1,0),5,1,1,1,0);
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
                        if (entity != player) {
                            DamageHandler.damageEntity(entity, damage, this);
                            entity.setVelocity(new Vector(0, 0.75, 0));
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
        return plugin.getConfig().getString("Strings.GlacialThorns.Description");
    }

    @Override
    public String getInstructions() {
        return plugin.getConfig().getString("Strings.GlacialThorns.Instructions");
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
