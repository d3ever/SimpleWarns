package sexy.criss.simplewars.sql;

import org.bukkit.Bukkit;
import sexy.criss.simplewars.Main;
import sexy.criss.simplewars.utils.Utils;

import java.sql.*;

public class MySQL_EXP {

	public static Connection con;
	
	public static void connect() {
		if (!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + SQL.HOST + ":" + SQL.PORT + "/" + SQL.DBNAME, SQL.USERNAME, SQL.PASSWORD);
				Bukkit.getConsoleSender().sendMessage( Utils.f("&aMySQL Connected.") );
			} catch (SQLException var1) {
				var1.printStackTrace();
			}
		}

	}

	public static void close() {
		if (isConnected()) {
			try {
				con.close();
				Bukkit.getConsoleSender().sendMessage( Utils.f( "&cMySQL Disconnected." ) );
			} catch (SQLException var1) {
				var1.printStackTrace();
			}
		}

	}

	public static boolean isConnected() {
		return con != null;
	}

	public static void createTable(String[] keys) {
		String keysKey = String.join(", ", keys);
		if(isConnected()) {
			try {
				{
					con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + SQL.TABLE + " (" + keysKey + ")");
					Bukkit.getConsoleSender().sendMessage( Utils.f( "&a%s table has been initialized.", SQL.TABLE ) );
				}
			}catch(SQLException ex) {
				ex.printStackTrace();
				Bukkit.shutdown();
			}
		}
	}

	public static void update(String qry) {
		if (isConnected()) {
			try {
				con.createStatement().executeUpdate(qry);
			} catch (SQLException var2) {
				var2.printStackTrace();
			}
		}

	}

	public static ResultSet getResult(String qry) {
		ResultSet rs = null;

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(qry);
		} catch (SQLException var3) {
			connect();
			System.err.println(var3);
		}

		return rs;
	}
}
