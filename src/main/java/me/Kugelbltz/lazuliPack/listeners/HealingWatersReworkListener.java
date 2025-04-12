package me.Kugelbltz.lazuliPack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import me.Kugelbltz.lazuliPack.abilities.HealingWatersRework;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static me.Kugelbltz.lazuliPack.abilities.HealingWatersRework.isCurrentlyHealing;


public class HealingWatersReworkListener implements Listener {

    @EventHandler
    private void onClick(PlayerAnimationEvent event) {

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HealingWaters")) {
            new HealingWatersRework(player, false);
        }
    }


    @EventHandler
    private void onSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            Player player = event.getPlayer();
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
            if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HealingWaters")) {
                if (isCurrentlyHealing.get(player) == null) {
                    new HealingWatersRework(player, true);
                }

            }
        }
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity attacker = event.getDamager();

        if (attacker instanceof Player) {
            if (isCurrentlyHealing.get((Player) attacker) != null) {
                HealingWatersRework.healingPlayer.put((Player) attacker, (LivingEntity) entity);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void blockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
        if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("HealingWaters")) {
            if (bendingPlayer.canBend(CoreAbility.getAbility("HealingWaters"))) {
                event.setCancelled(true);
            }
        }
    }

}
