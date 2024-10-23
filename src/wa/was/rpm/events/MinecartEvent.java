package wa.was.rpm.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import wa.was.rpm.RocketPropelledMinecarts;
import wa.was.rpm.tasks.SlowMinecarts;

public class MinecartEvent implements Listener
{
	private JavaPlugin plugin;
	private double boostFactor;
	private double maxSpeed;
	private SlowMinecarts slowCarts;
	
	// Setup environment
	public MinecartEvent(RocketPropelledMinecarts plugin) {
		this.plugin = plugin;
		boostFactor = plugin.getConfig().getDouble("boost-factor");
		maxSpeed = plugin.getConfig().getDouble("maximum-speed");
		if ( maxSpeed > 0.6 ) 
			maxSpeed = 0.6;
		slowCarts = new SlowMinecarts();
		slowCarts.runTaskTimer(plugin, 0L, 10L);
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event) {
	    Player p = event.getPlayer();
	    ItemStack i = p.getInventory().getItemInMainHand();
	    
	    // User has no permission for this
	    if ( ! ( p.hasPermission("rpm.boost") ) ) {
	    	p.sendMessage(ChatColor.RED + plugin.getConfig().getString("no-permissions"));
	    }
	    
	    // Is player in vehicle, and is that vehicle a minecart, and is the item held a basic firework rocket
	    if ( ! ( p.isInsideVehicle() ) ) return;
    	if ( ! ( p.getVehicle() instanceof Minecart ) ) return;
		if ( i.getType() != Material.FIREWORK ) return;
		
		FireworkMeta firework = (FireworkMeta) i.getItemMeta();   			
		if ( firework.hasEffects() ) return;
		
		// Get the Minecart in question
		Minecart cart = (Minecart) p.getVehicle();
		
		// Is this the first run? If so get base speed... (Unreliable) as well as set max speed
		if ( ! ( slowCarts.containsCart(cart) ) ) {
			slowCarts.addMinecart(cart);
			cart.setMaxSpeed(maxSpeed);
		}
		
		// Set new speed
		cart.setVelocity(cart.getVelocity().multiply(boostFactor));
		
		// Play rocket sound
		p.playSound(p.getLocation(),Sound.ENTITY_FIREWORK_SHOOT, 1, 1);

		// Remove Item for non-creative user
		if ( p.getGameMode() != GameMode.CREATIVE ) {
			incrementalRemoveMainHand(p);
		}
		
		// Cancel normal firework launch
		event.setCancelled(true);
		event.setUseItemInHand(Result.DENY);
	}
	
	// Incrementally Remove from main hand
	public void incrementalRemoveMainHand(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if ( item.getAmount() > 1 ) {
			item.setAmount(item.getAmount() - 1);
		} else {
			item = null;
		}
		player.getInventory().setItemInMainHand(item);
	}
	
}
