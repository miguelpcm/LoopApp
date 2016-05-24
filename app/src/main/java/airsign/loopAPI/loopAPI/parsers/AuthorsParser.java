package airsign.loopAPI.loopAPI.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import airsign.loopAPI.loopAPI.data.Author;
import airsign.loopAPI.loopAPI.data.User;
import airsign.loopAPI.loopAPI.data.UserList;

public class AuthorsParser
{

    private static final String
        kID = "userIds",
        kName = "fullName";

    public static ArrayList<Author> parse(JSONArray json)
    {
        ArrayList<Author> authors = new ArrayList<Author>();
        try
        {
            for (int i = 0; i < json.length(); i++)
            {
                JSONObject jobj = json.getJSONObject(i);
                String name = jobj.isNull(kName)? null:jobj.optString(kName);
                JSONArray jID = jobj.optJSONArray(kID);
                String id = (jID.length()>0)? jID.getString(0):null;
                Author author = new Author(id, name);
                authors.add(author);
            }
        }
        catch (Exception e) {}
        return authors;
    }
}
