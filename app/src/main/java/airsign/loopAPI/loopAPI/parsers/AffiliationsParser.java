package airsign.loopAPI.loopAPI.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import airsign.loopAPI.loopAPI.data.Affiliation;
import airsign.loopAPI.loopAPI.data.Author;

public class AffiliationsParser
{

    private static final String
        kName = "name",
        kCity = "cityName",
        kCountry = "countryName";

    public static ArrayList<Affiliation> parse(JSONArray json)
    {
        ArrayList<Affiliation> affiliations = new ArrayList<Affiliation>();
        try
        {
            if (json!=null)
            {
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject jobj = json.getJSONObject(i);
                    String
                        name = jobj.isNull(kName) ? null : jobj.optString(kName),
                        city = jobj.isNull(kCity) ? null : jobj.optString(kCity),
                        country = jobj.isNull(kCountry) ? null : jobj.optString(kCountry);
                    Affiliation af = new Affiliation(name, city, country);
                    affiliations.add(af);
                }
            }
        }
        catch (Exception e) {}
        return affiliations;
    }
}
