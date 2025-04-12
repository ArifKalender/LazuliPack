package me.Kugelbltz.lazuliPack;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.plaf.PanelUI;
import java.util.Random;

public final class LazuliPack extends JavaPlugin {

    public static Plugin plugin;
    public static Random random;
    public static String version = "§x§0§8§4§C§F§B‧§x§1§5§5§9§F§B⁺§x§2§1§6§6§F§B˚§x§2§E§7§3§F§B༓§x§3§B§7§F§F§CL§x§4§7§8§C§F§Ca§x§5§4§9§9§F§Cz§x§6§1§A§6§F§Cu§x§6§E§B§3§F§Cl§x§7§A§C§0§F§Ci§x§8§7§C§C§F§DP§x§9§4§D§9§F§Da§x§A§0§E§6§F§Dc§x§A§D§F§3§F§Dk";
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin=this;
    }

    private void registerListeners(){

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
