package sexy.criss.simplewars.sql;

import com.google.common.collect.Lists;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class HorrorSQL {

	public static boolean playerExists(String name) {
		try {
			{
				ResultSet set = MySQL_EXP.getResult("SELECT * FROM " + SQL.TABLE + " WHERE NAME='" + name + "'");

				if(set.next())return set.getString("NAME") != null;
				return false;
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static void createPlayer(String uuid, String[] keys, String[] values) {
		List<String> valuesArray=Lists.newArrayList();
		Arrays.asList(values).forEach(s->valuesArray.add("'"+s+"'"));
		if(!playerExists(uuid))MySQL_EXP.update("INSERT INTO "+SQL.TABLE+" ("+String.join(", ",keys)+") VALUES ("+String.join(", ",valuesArray)+");");
	}

	public static void setValue(String uuid, Object value) {
		MySQL_EXP.update( "UPDATE " + SQL.TABLE + " SET \t" + "WARNS" + "='" + value + "' WHERE NAME='" + uuid + "'" );
	}

	public static Object getValue(String name) {
		if(!playerExists( name )) {
			createPlayer( name, new String[]{"NAME, WARNS"}, new String[]{name, "0"} );
		}
		try {
			{
				ResultSet set = MySQL_EXP.getResult("SELECT * FROM " + SQL.TABLE + " WHERE NAME='" + name + "'");
				if(set.next() && set.getObject("WARNS") == null);
				return set.getObject("WARNS");
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
