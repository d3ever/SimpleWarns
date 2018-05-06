package sexy.criss.simplewars.sql;

import org.bukkit.configuration.ConfigurationSection;
import sexy.criss.simplewars.Main;

public class SQL {
	private static ConfigurationSection sec = Main.getInstance().getConfig().getConfigurationSection( "settings.sql" );
	public static String TABLE         = sec.getString( "table" );
	public static String HOST          = sec.getString( "host" );
	public static String PORT          = sec.getString( "port" );
	public static String DBNAME      = sec.getString( "database" );
	public static String USERNAME   = sec.getString( "user" );
	public static String PASSWORD   = sec.getString( "password" );
	
	public static boolean SQL_USAGE = Main.getInstance().getConfig().getConfigurationSection( "settings" ).getBoolean( "mysql_enabled" );
}
