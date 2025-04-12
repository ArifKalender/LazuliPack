package me.Kugelbltz.lazuliPack.listeners;

import com.projectkorra.projectkorra.event.BendingReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.Kugelbltz.lazuliPack.LazuliPack.plugin;

public class PKReloadEvent implements Listener {

    @EventHandler
    private void onReload(BendingReloadEvent event){
        plugin.reloadConfig();
        event.getSender().sendMessage("§x§0§8§4§C§F§BL§x§0§F§5§3§F§Ba§x§1§6§5§B§F§Bz§x§1§E§6§2§F§Bu§x§2§5§6§9§F§Bl§x§2§C§7§0§F§Bi§x§3§3§7§8§F§CP§x§3§A§7§F§F§Ca§x§4§1§8§6§F§Cc§x§4§9§8§D§F§Ck §x§5§7§9§C§F§Cw§x§5§E§A§3§F§Ca§x§6§5§A§A§F§Cs §x§7§4§B§9§F§Cr§x§7§B§C§0§F§Ce§x§8§2§C§7§F§Cl§x§8§9§C§F§F§Do§x§9§0§D§6§F§Da§x§9§7§D§D§F§Dd§x§9§F§E§4§F§De§x§A§6§E§C§F§Dd§x§A§D§F§3§F§D.");

    }

}
