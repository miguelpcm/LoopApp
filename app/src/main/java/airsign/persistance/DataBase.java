package airsign.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import airsign.loopAPI.loopAPI.data.Article;

public class DataBase
{

	// Database name and version
	private static final String NameDB = "Loop.db";
	private static final int VersionDB = 1;
	
	// Adapter resources
	private SQLiteDatabase db;
	private DataBaseAsistant dba;

	// SQLite tokens
	@SuppressWarnings("unused")
	private static final String
		OPEN = " (",
		CLOSE = ")",
		SEP = ",",
		QUOTE = "'",
		SEP_STAMENT = ";",
		CREATE = "CREATE ",
		SELCOUNT = " SELECT COUNT(*) FROM ",
		
		// Tables
		TABLE = "TABLE ",
		TEXT_PK = " text primary key,",
		TEXT = " text",
		INTEGER = " integer",
		INTEGER_PK = " integer primary key,",
		INTEGER_PK_AI = " integer primary key autoincrement,",
		NOTNULL = " not null ",
		
		// Triggers
		AFTER = " AFTER",
		DELETE = " DELETE",
		FROM = " FROM ",
		ON = " ON ",
		TRIGGER = "TRIGGER ",
		WHEN = " WHEN",
		BEGIN = " BEGIN ",
		END = " END",

		
		// DB Management
		DROP = "DROP TABLE IF EXISTS ",
		SEQUENCE = "sqlite_sequence WHERE name=",
		
		// Order tokens
		ASC = " ASC",
		DESC = " DESC",
		
		// Ops
		MAX = "MAX", // MAX(int_value_in_table)

		// Comparision tokens
		EQZERO = "==0",
		EQ = " = '",
		ENDEQ = "'",
		EQ_SAFE = "=?";

	// Tables
	public static final String
		LikedArticlesTable = "LikedArticles",
			ID = "id",
			DATE = "date",
			TITLE = "title",
			JOURNAL = "journal";

// -- Helper class -----------------------------------------------------------
	
	private static class DataBaseAsistant extends SQLiteOpenHelper
	{
		
		private static final String
		
		// Liked articles
			createLikedArticlesTable =
				CREATE + TABLE + LikedArticlesTable +
				OPEN +
					ID + TEXT_PK + // Id of the article
					TITLE + TEXT + SEP + // Title
					JOURNAL + TEXT + SEP + // Title
					DATE + TEXT + // Publication date
				CLOSE + SEP_STAMENT;
		

		public DataBaseAsistant(Context c, String BDName, CursorFactory cf, int ver)
		{
			super(c, BDName, cf, ver);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(createLikedArticlesTable);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldV, int newV)
		{
			db.execSQL(DROP + LikedArticlesTable);
			onCreate(db);
		}
		
	} // DataBaseAsistant


// -- DataBase explotation ---------------------------------------------------

	public DataBase(Context c)
	{
		dba = new DataBaseAsistant(c, NameDB, null, VersionDB);
	}

	public boolean open()
	{
		try
		{
			db = dba.getWritableDatabase();
			return true;
		}
		catch (SQLiteException e)
		{
			db = dba.getReadableDatabase();
			return false;
		}
	}

	public void close()
	{
		db.close();
		dba.close();
	}
	
// Alarms
	public void addLikedArticle(Article a)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID, a.id);
		cv.put(TITLE, a.title);
		cv.put(JOURNAL, a.journal);
		cv.put(DATE, a.date);
		db.insert(LikedArticlesTable, null, cv);
	}

	public boolean existsLikedArticle(String id)
	{
		String where = ID + EQ + id + ENDEQ;
		Cursor cursor = db.query(true, LikedArticlesTable, null, where,
				null, null, null, null, null);
		return (cursor.getCount()>0);
	}
	
	public void deleteLikedArticle(String id)
	{
		String where = ID + EQ + id + ENDEQ;
		db.delete(LikedArticlesTable, where, null);
	}
	
	public Cursor getAllLikedArticles()
	{
		Cursor cursor = db.query(true, LikedArticlesTable, null, null,
				null, null, null, null, null);
		return cursor;
	}

} // DataBase
