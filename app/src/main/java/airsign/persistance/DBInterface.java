package airsign.persistance;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import airsign.loopAPI.loopAPI.data.Article;

public class DBInterface
{
	private DataBase db;
	public DBInterface(Context ctx) { db = new DataBase(ctx); }
	
	public void addLikedArticle(Article a)
	{
		db.open();
			db.addLikedArticle(a);
		db.close();
	}

	public void removeLikedArticle(String id)
	{
		db.open();
			db.deleteLikedArticle(id);
		db.close();
	}

	public boolean isArticleLiked(String id)
	{
		db.open();
			boolean res = db.existsLikedArticle(id);
		db.close();
		return res;
	}
	
	public ArrayList<Article> getAllLikedArticles()
	{
		db.open();
			ArrayList<Article> list = new ArrayList<Article>();
			Cursor c = db.getAllLikedArticles();
			if (c.moveToFirst()) do
			{
				String
					id = c.getString(c.getColumnIndex(DataBase.ID)),
					title = c.getString(c.getColumnIndex(DataBase.TITLE)),
					journal = c.getString(c.getColumnIndex(DataBase.JOURNAL)),
					date = c.getString(c.getColumnIndex(DataBase.DATE));
				Article a = new Article(id, title, null, journal, date, null, null, null, null);
				list.add(a);
			}
			while (c.moveToNext());
		db.close();
		return list;
	}

}
