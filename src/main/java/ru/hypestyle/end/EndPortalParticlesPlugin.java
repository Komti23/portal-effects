package ru.hypestyle.end;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.block.Action;

public class EndPortalParticlesPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("EndPortalParticlesPlugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EndPortalParticlesPlugin has been disabled.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.END_PORTAL_FRAME) {
                EndPortalFrame frame = (EndPortalFrame) block.getBlockData();
                if (!frame.hasEye() && event.getItem() != null && event.getItem().getType() == Material.ENDER_EYE) {
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        EndPortalFrame updatedFrame = (EndPortalFrame) block.getBlockData();
                        if (updatedFrame.hasEye()) {
                            getLogger().info("Ender Eye inserted. Starting particle column...");
                            startParticleColumn(block);
                        } else {
                            getLogger().info("Ender Eye not inserted.");
                        }
                    }, 1L);
                }
            }
        }
    }

    private void startParticleColumn(Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                EndPortalFrame frame = (EndPortalFrame) block.getBlockData();
                if (frame.hasEye()) {
                    Location loc = block.getLocation().add(0.5, 0.5, 0.5);
                    // Создание столба частиц
                    for (int i = 0; i <= 10; i++) {
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.clone().add(0, i * 0.2, 0), 1, 0, 0, 0, 0.1);
                    }
                } else {
                    getLogger().info("Ender Eye removed, stopping particle column.");
                    cancel();
                }
            }
        }.runTaskTimer(this, 0L, 10L);
    }
}
