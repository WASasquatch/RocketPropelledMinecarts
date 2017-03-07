package wa.was.rpm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DebugCommand implements CommandExecutor {

	public static boolean debugEnabled = false;
	
	// Execute Lurker Tracking Command
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {

		if ( args.length > 0  ) return false;
		if ( ! ( sender instanceof ConsoleCommandSender ) ) return false;
		
		if ( debugEnabled ) {
			debugEnabled = false;
			System.out.print("[RPM] Debug Disabled");
		} else {
			debugEnabled = true;
			System.out.print("[RPM] Debug Enabled");
		}
			
		return true;
	}

}
