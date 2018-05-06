package sexy.criss.simplewars.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sexy.criss.simplewars.Main;

import java.io.File;
import java.util.List;

public class Utils {

	public static String f(String str) {
		return ChatColor.translateAlternateColorCodes( '&', str );
	}

	public static String f(String str, Object... objects) {
		return f(String.format( str, objects ));
	}

	public static List<String> f(List<String> lore) {
		List<String> array = Lists.newArrayList();
		lore.forEach( s -> array.add( f(s) ) );
		return array;
	}

	public static String getString(MessageType type, String key) {
		String index = type.equals( MessageType.MESSAGES  ) ? "messages." + key : "permissions." + key;
		String out = key;
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = ( JSONObject ) parser.parse( Main.getInstance().getConfig().getString( index ) );
			out = (String) object.get( "data" );
			return out;
		} catch ( ParseException e ) {
			return out;
		}
	}

	public static String getObject(String key) {
		JSONParser parser = new JSONParser();
		try {
			{
				JSONObject object = ( JSONObject ) parser.parse( Main.getInstance().getConfig().getString( key ) );
				return object.get( "data" ).toString();
			}
		}catch ( ParseException ex ) {
			return null;
		}
	}

	public static String[] getStrings(MessageType type, String key) {
		String index = type.equals( MessageType.MESSAGES  ) ? "messages." + key : "permissions." + key;
		String[] out = {};
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse( Main.getInstance().getConfig().getString( index ) );
			JSONArray array = ( JSONArray ) jsonObject.get( "data" );
			out = ( String[] ) array.toArray( new String[0] );
			return out;
		}catch ( ParseException ex ) {
			return out;
		}
	}

	public static FileConfiguration loadConfig(File file) {
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
		return defConfig;
	}

	public enum MessageType {
		MESSAGES,
		PERMISSIONS;
	}

}
