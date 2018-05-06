package sexy.criss.simplewars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sexy.criss.simplewars.commands.UnWarnCommand;
import sexy.criss.simplewars.commands.WarnCommand;
import sexy.criss.simplewars.player.SexyPlayer;
import sexy.criss.simplewars.sql.MySQL_EXP;
import sexy.criss.simplewars.sql.SQL;
import sexy.criss.simplewars.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class Main extends JavaPlugin {
	private static Main instance;
	public static FileConfiguration players_storage;
	
	
	public static Main getInstance() {
		return instance;
	}

	@Override public void onEnable() {
		// Plugin startup logic
		instance = this;

		this.getConfig().options().copyDefaults( true );
		this.saveDefaultConfig();
		this.saveConfig();
		
		if( SQL.SQL_USAGE) {
			MySQL_EXP.connect();
			MySQL_EXP.createTable( new String[]{"NAME VARCHAR(100)", "WARNS int"} );
		} else {
			players_storage = Utils.loadConfig( new File( getDataFolder(), "players_storage.yml" ) );
		}
		
		new WarnCommand().register();
		new UnWarnCommand().register();

		Bukkit.getConsoleSender().sendMessage( Utils.f(ChatColor.GREEN + this.getDescription().getName() + " has been enabled." )) ;
		
	}

	@Override public void onDisable() {
		// Plugin shutdown logic
		for( SexyPlayer sexyPlayer : SexyPlayer.sexyPlayers.values() ) {
			sexyPlayer.save();
		}
		save( players_storage, "players_storage.yml" );
		
		Bukkit.getConsoleSender().sendMessage( Utils.f( ChatColor.DARK_RED + this.getDescription().getName() + " has been disabled." )) ;
		
	}
	
	
	public void save( FileConfiguration configuration, String name ) {
		try {
			configuration.save( new File( getDataFolder(), name ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
}
