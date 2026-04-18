package com.github.Syaaddd.oceanMinerGrow;

import com.github.Syaaddd.oceanMinerGrow.setup.ItemSetup;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;

public final class OceanMinerGrow extends JavaPlugin implements SlimefunAddon {

    private static OceanMinerGrow instance;

    @Override
    public void onEnable() {
        instance = this;
        new ItemSetup(this).register();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "";
    }

    public static OceanMinerGrow getInstance() {
        return instance;
    }
}
