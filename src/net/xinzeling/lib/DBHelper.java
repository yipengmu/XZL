package net.xinzeling.lib;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private Context context;

	public DBHelper(Context context, String dbName) {
		super(context, dbName, null, 1);
		this.context = context;
	}

	public void onCreate(SQLiteDatabase database) {
		executeSQLScript(database, "create.sql");
		executeSQLScript(database, "lunar.sql");
	}

	public void executeSQLScript(SQLiteDatabase database, String sqlFile) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open(sqlFile);
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
			String[] createScript = outputStream.toString().split(";");
			for (int i = 0; i < createScript.length; i++) {
				String sqlStatement = createScript[i].trim();
				// TODO You may want to parse out comments here
				if (sqlStatement.length() > 0) {
					database.execSQL(sqlStatement + ";");
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {
	}

}
