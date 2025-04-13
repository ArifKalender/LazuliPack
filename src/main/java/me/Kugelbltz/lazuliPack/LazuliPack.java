package me.Kugelbltz.lazuliPack;

import com.projectkorra.projectkorra.ability.CoreAbility;
import me.Kugelbltz.lazuliPack.listeners.HealingWatersReworkListener;
import me.Kugelbltz.lazuliPack.listeners.PKReloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.plaf.PanelUI;
import java.util.Random;

public final class LazuliPack extends JavaPlugin {

    public static Plugin plugin;
    public static Random random;
    public static String version = "§x§0§8§4§C§F§B‧§x§1§5§5§9§F§B⁺§x§2§1§6§6§F§B˚§x§2§E§7§3§F§B༓§x§3§B§7§F§F§CL§x§4§7§8§C§F§Ca§x§5§4§9§9§F§Cz§x§6§1§A§6§F§Cu§x§6§E§B§3§F§Cl§x§7§A§C§0§F§Ci§x§8§7§C§C§F§DP§x§9§4§D§9§F§Da§x§A§0§E§6§F§Dc§x§A§D§F§3§F§Dk 0.1";
    @Override
    public void onEnable() {
        plugin=this;
        saveDefaultConfig();
        registerListeners();
        this.getConfig().options().copyDefaults(true);
        CoreAbility.registerPluginAbilities(this,"me.Kugelbltz.lazuliPack.abilities");
    }

    private void registerListeners(){

        getServer().getPluginManager().registerEvents(new HealingWatersReworkListener(), this);
        getServer().getPluginManager().registerEvents(new PKReloadEvent(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
