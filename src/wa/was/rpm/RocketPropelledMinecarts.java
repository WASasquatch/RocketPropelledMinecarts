package wa.was.rpm;

//Import Bukkit API & Utils
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

import wa.was.rpm.commands.DebugCommand;
import wa.was.rpm.events.MinecartEvent;

public class RocketPropelledMinecarts extends JavaPlugin
{
    @Override
    public void onEnable() {
    	createConfig();
    	getServer().getPluginManager().registerEvents(new MinecartEvent(this), this);
    	this.getCommand("rpmdebug").setExecutor(new DebugCommand());
    }
    
    private void createConfig() {
        try {
            if ( ! ( getDataFolder().exists() ) ) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if ( ! ( file.exists() ) ) {
                getLogger().info("Config.yml not found, creating it for you!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
}