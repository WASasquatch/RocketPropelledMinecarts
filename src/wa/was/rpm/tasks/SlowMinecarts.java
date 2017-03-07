package wa.was.rpm.tasks;

import wa.was.rpm.commands.DebugCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Minecart;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SlowMinecarts extends BukkitRunnable
{
	private ArrayList<Minecart> minecarts;
	private HashMap<UUID, Vector> baseVectors;
	
	// Setup environment
	public SlowMinecarts() {
		this.minecarts = new ArrayList<>();
		this.baseVectors = new HashMap<>();
	}

	// Slow minecart task
    @Override
    public void run() {
    	for ( int i = 0; i < minecarts.size(); i++ ) {
    		Minecart cart = minecarts.get(i);
    		Vector cancelThreshold = baseVectors.get(cart.getUniqueId());
    		if( cancelThreshold.lengthSquared() <= .000324 ) {
        		if( cart.getVelocity().lengthSquared() <= .000324) cart.setVelocity(cancelThreshold );
        	}
        	if( ! ( cart.isValid() ) || cart.getVelocity().lengthSquared() <= cancelThreshold.lengthSquared() ) {
        		removeMinecart(cart);
        		i--;
        		continue;
        	}
        	Vector cv = cart.getVelocity();
        	if ( DebugCommand.debugEnabled ) {
        		System.out.print("----[RPM Debug]----");
        		System.out.print("Minecart: "+ cart.getUniqueId());
        		System.out.print("Velocity: "+ cv.toString() +" | Length: "+ cv.length());
        		System.out.print("BaseVector: "+ cancelThreshold.toString());
        	}
        	Vector nv = new Vector(
        					(Math.abs(cv.getX()) > cancelThreshold.getX() ? cv.getX() * .875 : cancelThreshold.getX()),
        					(Math.abs(cv.getY()) > cancelThreshold.getY() ? cv.getY() * .875 : cancelThreshold.getY()),
        					(Math.abs(cv.getZ()) > cancelThreshold.getZ() ? cv.getZ() * .875 : cancelThreshold.getZ())
        			);
        	if ( DebugCommand.debugEnabled ) {
        		System.out.print("Setting minecart velocity to: "+nv);
        		System.out.print("-------------------");
        	}
        	cart.setVelocity(nv);
    	}
	}
    
    public void removeMinecart(Minecart cart) {
    	minecarts.remove(cart);
    	baseVectors.remove(cart.getUniqueId());
    	if ( DebugCommand.debugEnabled ) {
	    	System.out.print("----[RPM Debug]----");
	    	System.out.println("Clearing Minecart: "+ cart.getUniqueId());
			System.out.print("-------------------");
    	}
    }
    
    public boolean withinThreshold(Vector cartV) {
    	return Math.abs(cartV.lengthSquared()) < .00000625;
    }
    
    public void addMinecart(Minecart cart) {
    	minecarts.add(cart);
    	baseVectors.put(cart.getUniqueId(), cart.getVelocity());
    }
    
    public boolean containsCart(Minecart cart) {
    	return baseVectors.containsKey(cart.getUniqueId());
    }
}