package sexy.criss.simplewars.player;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simplewars.Main;
import sexy.criss.simplewars.sql.HorrorSQL;
import sexy.criss.simplewars.sql.SQL;

import java.util.Map;

public class SexyPlayer implements ISexyPlayer {

	public static Map<String, SexyPlayer> sexyPlayers = Maps.newHashMap();

	private String name;
	private int currentWarns;
	
	public SexyPlayer( String name ) {
		this.name = name;
		this.currentWarns = 0;

		sexyPlayers.put( name, this );
	}
	
	public SexyPlayer( String name, int currentWarns ) {
		this.name = name;
		this.currentWarns = currentWarns;
		
		sexyPlayers.put( name, this );
	}

	public SexyPlayer( String name, ConfigurationSection sec ) {
		this.name = name;
		this.currentWarns = sec.getInt( "WARNS" );

		sexyPlayers.put( name, this );
	}

	public static SexyPlayer get(String name) {
		if(sexyPlayers.containsKey( name )) {
			return sexyPlayers.get( name );
		} else {
			if(SQL.SQL_USAGE) {
				new SexyPlayer( name, Integer.valueOf( HorrorSQL.getValue( name ).toString() ).intValue() );
			} else {
				if(Main.players_storage.contains( name )) {
					SexyPlayer sexyPlayer = new SexyPlayer( name, Main.players_storage.getConfigurationSection( name ) );
					
					sexyPlayer.save();
				} else {
					SexyPlayer sexyPlayer = new SexyPlayer( name );
					
					sexyPlayer.save();
				}
			}
			return get( name );
		}
		
	}

	@Override public int getCurrentWarns() {
		return this.currentWarns;
	}

	@Override public void setWarns(int warns) {
		this.currentWarns = warns;
	}

	@Override public void addWarn(int warns) {
		this.currentWarns += warns;
	}

	@Override public void removeWarn(int warns) {
		this.currentWarns -= warns;
	}

	@Override public void save() {
		FileConfiguration conf = Main.players_storage;
		ConfigurationSection sec = conf.contains( this.name ) ? conf.getConfigurationSection( this.name ) : conf.createSection( this.name );

		sec.set( "WARNS", String.valueOf( this.currentWarns ) );

	}
}
