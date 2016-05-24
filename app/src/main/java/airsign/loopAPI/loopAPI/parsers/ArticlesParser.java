package airsign.loopAPI.loopAPI.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import airsign.loopAPI.loopAPI.data.Affiliation;
import airsign.loopAPI.loopAPI.data.Article;
import airsign.loopAPI.loopAPI.data.Author;

public class ArticlesParser
{

    private static final String
        kValue = "value",
        kID = "id",
        kTitle = "title",
        kAbstract = "abstract",
        kDate = "publicationDate",
        kAffiliations = "affiliations",
        kKeywords = "keywords",
        kAuthors = "authors",
        kJournal = "journal",
            kName = "name",
        kUrl = "loopUrl";

    private static ArrayList<String> getKeywords(JSONArray json)
    {
        ArrayList<String> keywords = new ArrayList<String>();
        if (json!=null) for (int i=0; i<json.length(); i++) keywords.add(json.optString(i));
        return keywords;
    }

    private static Article parseArticle(JSONObject json, boolean simplify)
    {
        String
            id = json.optString(kID),
            title = json.isNull(kTitle)? null:json.optString(kTitle),
            date = json.isNull(kDate)? null:json.optString(kDate),
            journal = json.optJSONObject(kJournal).optString(kName);
        date = date.replace("-", "/");
        if (simplify) return new Article(id, title, null, journal, date, null, null, null, null);
        else
        {
            String
                url = json.isNull(kUrl)? null:json.optString(kUrl),
                abstrakt = json.isNull(kAbstract)? null:json.optString(kAbstract);
            ArrayList<Author> authors = AuthorsParser.parse(json.optJSONArray(kAuthors));
            ArrayList<String> keywords = getKeywords(json.optJSONArray(kKeywords));
            ArrayList<Affiliation> affiliations = AffiliationsParser.parse(json.optJSONArray(kAffiliations));
            return new Article(id, title, abstrakt, journal, date, url, keywords, affiliations, authors);
        }
    }

    public static Article parseArticle(String info) { return parseArticle(info, false); }
    public static Article parseArticle(String info, boolean simplify)
    {
        Article res = null;
        if (info!=null)
        {
            try { res = parseArticle(new JSONObject(info), simplify); }
            catch (Exception e) { e.printStackTrace(); }
        }
        return res;
    }

    public static ArrayList<Article> parseArticlesSimple(String info)
    {
        ArrayList<Article> list = new ArrayList<Article>();
        if (info!=null)
        {
            try
            {
                JSONObject json = new JSONObject(info);
                JSONArray jArray = json.optJSONArray(kValue);
                for (int i=0; i<jArray.length(); i++)
                {
                    JSONObject entry = jArray.getJSONObject(i);
                    Article article = parseArticle(entry, true);
                    list.add(article);
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        return list;
    }
}
