package sexy.criss.simplewars.commands;

import com.google.common.collect.Lists;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simplewars.Main;
import sexy.criss.simplewars.commands.manager.SexyCommand;
import sexy.criss.simplewars.player.SexyPlayer;
import sexy.criss.simplewars.sql.HorrorSQL;
import sexy.criss.simplewars.utils.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WarnCommand extends SexyCommand {

	public WarnCommand() {
		super( "warn" );
	}

	@Override public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args ) {

		if((sender instanceof Player) && !(((Player) sender).hasPermission( Utils.getString( Utils.MessageType.PERMISSIONS, "warn_use" ) ))) {
			((Player) sender).sendMessage( Utils.f( Utils.getString( Utils.MessageType.MESSAGES, "permError" ) ) );
			return true;
		}

		switch ( args.length ) {
			case 0:
				this.help( sender );
				return true;
			default:
				OfflinePlayer target = Bukkit.getOfflinePlayer( args[0] );
				SexyPlayer sexyPlayer = SexyPlayer.get( target.getName() );
				String pit = (sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE";
				int yCount = sexyPlayer.getCurrentWarns();


				if(yCount >= Integer.valueOf( Utils.getObject( "settings.warn_limit" ) ).intValue()) {

					String reason = Utils.f( Utils.getString( Utils.MessageType.MESSAGES, "warnsMaxed" ).replace( "${TARGET}" ,target.getName() ) );
					sexyPlayer.setWarns( 0 );
					if(target.isOnline()) {
						target.getPlayer().kickPlayer( reason );
					}
					Bukkit.getBanList( BanList.Type.NAME ).addBan( target.getName(), reason, getBanTime(), pit);
					Bukkit.broadcastMessage( reason );
					return true;
				}
				String stringReason;
				if(args.length == 1) {
					stringReason = Utils.getString( Utils.MessageType.MESSAGES, "def_reason" ).replace( "${TARGET}" ,target.getName() );
				} else {
					List<String> reason = Lists.newArrayList();
					for(int i = 1; i < args.length; i++) {
						reason.add( args[i] );
					}
					stringReason = String.join( " ", reason );
				}
				sexyPlayer.addWarn(1);
				String info = hold( Utils.getString( Utils.MessageType.MESSAGES, "bcInfo" ), pit, target.getName(), stringReason, String.valueOf( sexyPlayer.getCurrentWarns() ) );
				String announcer = Utils.f( info );
				
				if(target.isOnline()) {
					String timed = hold( Utils.getString( Utils.MessageType.MESSAGES, "yourWarned"), pit, target.getName(), stringReason, String.valueOf( sexyPlayer.getCurrentWarns() ) );
					target.getPlayer().sendMessage( Utils.f( timed ) );
				}
				sender.sendMessage( Utils.f( hold( Utils.getString( Utils.MessageType.MESSAGES, "youWarnedPlayer" ), pit, target.getName(), stringReason, String.valueOf( sexyPlayer.getCurrentWarns() ) ) ));
				Bukkit.broadcastMessage( announcer );
				return true;
		}
	}

	public void help(CommandSender player) {
		String[] msg = Utils.getStrings( Utils.MessageType.MESSAGES, "usage" );
		player.sendMessage( Utils.f( Arrays.asList(msg) ).toArray(new String[0]) );
	}
	
	public String hold(String input, String own, String target, String reason, String warns) {
		String out = input;
		out = out.replace( "${OWN}", own );
		out = out.replace( "${TARGET}", target );
		out = out.replace( "${REASON}", reason );
		out = out.replace( "${WARNS}", warns );
		out = out.replace( "${MAX_WARNS}", String.valueOf( Integer.valueOf( Utils.getObject( "settings.warn_limit" ) ).intValue() ));
		
		return out;
	}
	
	public Date getBanTime() {
		Date date = new Date();
		if(!Main.getInstance().getConfig().contains( "settings.ban_time" )) {
			date.setYear( 100 );
			return date;
		}
		String format = Utils.getObject( "settings.ban_time" );
		format = format.toLowerCase();
		char type = format.toCharArray()[format.toCharArray().length - 1];
		int dCount = Integer.valueOf( format.replaceAll("\\D+", "") ).intValue();
		switch ( type ) {
			case 'y':
				date.setYear( dCount );
				break;
			case 'd':
				date.setHours( 24 * dCount );
				break;
			default:
				date.setYear( dCount );
				break;
		}
		
		return date;
	}
	
}
