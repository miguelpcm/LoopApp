package airsign.persistance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import airsign.styles.Themes;

public class Preferences
{

	private SharedPreferences sp;
	private Editor spe;

    // Keys for the queries
    private final String
        kUserID = "kUserID",
        kFollows = "kFollows",
        kStyle = "kStyle",
        kAuth = "kAuth";

	public Preferences(Context c)
    {
        final String appTag = "Loop";
        sp = c.getSharedPreferences(appTag, Activity.MODE_PRIVATE);
    }


    // -- Styles
    public int getStyle() { return sp.getInt(kStyle, Themes.kWhite); }
    public void setStyle(int styleID)
    {
        spe = sp.edit();
            spe.putInt(kStyle, styleID);
        spe.commit();
    }

    // -- User ID
    public String getUserID() { return sp.getString(kUserID, null); }
    public void setUserID(String userID)
    {
        spe = sp.edit();
            spe.putString(kUserID, userID);
        spe.commit();
    }

    // -- Users Im Following
    public ArrayList<String> getFollowing()
    {
        ArrayList<String> list = new ArrayList<String>();
        String s = sp.getString(kFollows, null);
        String[] ids = s.split(",");
        for (int i=0; i<ids.length; i++) list.add(ids[i]);
        return list;
    }

    public void saveFollowing(ArrayList<String> list)
    {
        String ids = "";
        for (String s:list) ids += ","+s;
        spe = sp.edit();
            spe.putString(kFollows, ids);
        spe.commit();
    }

    public void saveFollowing(String ids)
    {
        spe = sp.edit();
            spe.putString(kFollows, ids);
        spe.commit();
    }

    // -- Auth token
    public String getAuth() { return sp.getString(kAuth, null); }
    public void setAuth(String auth)
    {
        spe = sp.edit();
            spe.putString(kAuth, auth);
        spe.commit();
    }

} // Preferences
