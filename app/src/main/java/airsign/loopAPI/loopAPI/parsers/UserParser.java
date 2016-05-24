package airsign.loopAPI.loopAPI.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import airsign.loopAPI.loopAPI.data.User;
import airsign.loopAPI.loopAPI.data.UserList;

public class UserParser
{

    private static final String
        kID = "id",
        kFirstName = "firstName",
        kMiddleName = "middleName",
        kLastName = "lastName",
        kCity = "cityName",
        kCountry = "countryName",
        kBio = "bio",
        kPublic = "isPublic",
        kAffiliation = "affiliation",
        kDegree = "degree",
        kJob = "jobTitle",
        kValue = "value",
        kCount = "@odata.count";

    private static User parseUser(JSONObject json)
    {
        String
            id = json.optString(kID),
            firstName = json.isNull(kFirstName)? null:json.optString(kFirstName),
            middleName = json.isNull(kMiddleName)? null:json.optString(kMiddleName),
            lastName = json.isNull(kLastName)? null:json.optString(kLastName),
            city = json.isNull(kCity)? null:json.optString(kCity),
            country = json.isNull(kCountry)? null:json.optString(kCountry),
            bio = json.isNull(kBio)? null:json.optString(kBio),
            affiliation = json.isNull(kAffiliation)? null:json.optString(kAffiliation),
            degree = json.isNull(kDegree)? null:json.optString(kDegree),
            jobTitle = json.isNull(kJob)? null:json.optString(kJob),
            imgUrl = "http://www.frontiersin.org/files/Profile%20Library/"+id+"/Thumb_203_203.jpg";

        Boolean isPublic = json.optString(kPublic).equals("true");

        return new User(id, firstName, middleName, lastName, city, country, degree, jobTitle,
                bio, imgUrl, affiliation, isPublic);
    }

    public static User parseUser(String info)
    {
        User res = null;
        if (info!=null)
        {
            try { res = parseUser(new JSONObject(info)); }
            catch (Exception e) { e.printStackTrace(); }
        }
        return res;
    }

    public static UserList parseUsers(String info)
    {
        int count = 0;
        ArrayList<User> list = new ArrayList<User>();
        if (info!=null)
        {
            try
            {
                JSONObject json = new JSONObject(info);
                count = json.optInt(kCount);
                JSONArray jArray = json.optJSONArray(kValue);
                for (int i=0; i<jArray.length(); i++)
                {
                    JSONObject entry = jArray.getJSONObject(i);
                    User user = parseUser(entry);
                    list.add(user);
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        return new UserList(count, list);
    }
}
