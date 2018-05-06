package sexy.criss.simplewars.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simplewars.Main;
import sexy.criss.simplewars.commands.manager.SexyCommand;
import sexy.criss.simplewars.player.SexyPlayer;
import sexy.criss.simplewars.sql.HorrorSQL;
import sexy.criss.simplewars.utils.Utils;

import java.util.Arrays;

public class UnWarnCommand extends SexyCommand {

	public UnWarnCommand() {
		super( "unwarn" );
	}

	@Override public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args ) {
		String pit = (sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE";
		if((sender instanceof Player) && !((Player) sender).hasPermission( Utils.getString( Utils.MessageType.PERMISSIONS, "unwarn_use" ) )) {
			((Player) sender).sendMessage( Utils.f( Utils.getString( Utils.MessageType.MESSAGES, "permError" ) ) );
			return true;
		}
		
		switch ( args.length ) {
			case 0:
				this.help( sender );
				return true;
			default:
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				SexyPlayer sexyPlayer = SexyPlayer.get( target.getName() );
				int warns = sexyPlayer.getCurrentWarns();
				if(warns <= 0) {
					sender.sendMessage( Utils.f( hold( Utils.getString( Utils.MessageType.MESSAGES, "userNotHaveWarns"), pit, target.getName() ) ) );
					return true;
				}
				sexyPlayer.removeWarn(1);
				sender.sendMessage( Utils.f( hold( Utils.getString( Utils.MessageType.MESSAGES, "warnRemoved" ), pit, target.getName() ) ));
				if(target.isOnline()) {
					target.getPlayer().sendMessage( Utils.f( hold( Utils.getString( Utils.MessageType.MESSAGES, "yourWarnRemoved" ), pit, target.getName() ) ));
				}
				return true;
		}
	}

	public void help(CommandSender player) {
		String[] msg = Utils.getStrings( Utils.MessageType.MESSAGES, "usage" );
		player.sendMessage( Utils.f( Arrays.asList(msg) ).toArray(new String[0]) );
	}
	
	public String hold(String input, String own, String target) {
		String out = input;
		out = out.replace( "${OWN}", own );
		out = out.replace( "${TARGET}", target );
		
		return out;
	}
	
}
