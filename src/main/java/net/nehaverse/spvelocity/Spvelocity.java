package net.nehaverse.spvelocity;

import org.bukkit.plugin.java.JavaPlugin;

public final class Spvelocity extends JavaPlugin {

    private SpvelocityManager manager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        manager = new SpvelocityManager(this);
        getCommand("proxy").setExecutor(new SpvelocityCommand(manager));
        getLogger().info("Spvelocity enabled.");
    }

    @Override
    public void onDisable() {
        if (manager != null) manager.shutdown();
        getLogger().info("Spvelocity disabled.");
    }
}