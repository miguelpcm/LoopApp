package airsign.loopAPI;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class LoopServices
{
    private static final String
        baseUrl = "https://api.frontiersin.org/v2/",
            header = "Authorization",
                headerKeyword = "Bearer ",
            key = "?key=",
            me = "/me",
            users = "/users",
            actFollow = "/action.followuser",
            actUnfollow = "/action.unfollowuser",
                userId = "userId",
            publications = "/publications",
                search = "/search.",
                    byTitle = "bytitle(title='",
                    byKeyword = "bykeyword(keyword='",
                    byAbstract = "byabstract(abstract='",
                    byAuthor = "byauthorname(fullname='",
        // People
            followers = "/followers",
            following = "/following",
            coauthors = "/coauthors";


    public static HttpGet getMyProfile(String authToken)
    {
        HttpGet get = new HttpGet(baseUrl + me + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getUserProfile(String authToken, String userID)
    {
        HttpGet get = new HttpGet(baseUrl + users + "("+userID+")" + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getArticle(String authToken, String articleID)
    {
        HttpGet get = new HttpGet(baseUrl + publications + "("+articleID+")" + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet searchArticleByTitle(String authToken, String s)
    {
        String url = baseUrl + publications + search + byTitle + s + "')" + key + LoopClientDetails.apiKey;
        return searchArticle(authToken, url);
    }

    public static HttpGet searchArticleByAbstract(String authToken, String s)
    {
        String url = baseUrl + publications + search + byAbstract + s + "')" + key + LoopClientDetails.apiKey;
        return searchArticle(authToken, url);
    }

    public static HttpGet searchArticleByAuthor(String authToken, String s)
    {
        String url = baseUrl + publications + search + byAuthor + s + "')" + key + LoopClientDetails.apiKey;
        return searchArticle(authToken, url);
    }

    public static HttpGet searchArticleByKeyword(String authToken, String s)
    {
        String url = baseUrl + publications + search + byKeyword + s + "')" + key + LoopClientDetails.apiKey;
        return searchArticle(authToken, url);
    }

    private static HttpGet searchArticle(String authToken, String url)
    {
        HttpGet get = new HttpGet(url);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getMyArticles(String authToken)
    {
        HttpGet get = new HttpGet(baseUrl + me + publications + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getUserArticles(String authToken, String userID)
    {
        HttpGet get = new HttpGet(baseUrl + users + "("+userID+")" + publications + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getMyFollowers(String authToken)
    {
        HttpGet get = new HttpGet(baseUrl + me + followers + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getUserFollowers(String authToken, String userID)
    {
        HttpGet get = new HttpGet(baseUrl + users + "("+userID+")" + followers + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getMyFollows(String authToken)
    {
        HttpGet get = new HttpGet(baseUrl + me + following + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getUserFollows(String authToken, String userID)
    {
        HttpGet get = new HttpGet(baseUrl + users + "("+userID+")" + following + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getMyCoauthors(String authToken)
    {
        HttpGet get = new HttpGet(baseUrl + me + coauthors + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpGet getUserCoauthors(String authToken, String userID)
    {
        HttpGet get = new HttpGet(baseUrl + users + "("+userID+")" + coauthors + key + LoopClientDetails.apiKey);
        get.setHeader(header, headerKeyword + authToken);
        return get;
    }

    public static HttpPost postFollowUser(String authToken, String id)
    {
        HttpPost post = new HttpPost(baseUrl + me + actFollow + key + LoopClientDetails.apiKey);
        post.setHeader(header, headerKeyword + authToken);
        post.setHeader("Content-type", "application/json");
        try
        {
            JSONObject json = new JSONObject();
            json.put(userId, id);
            StringEntity se = new StringEntity(json.toString());
            post.setEntity(se);
        }
        catch (Exception e) { e.printStackTrace(); }
        return post;
    }

    public static HttpPost postUnfollowUser(String authToken, String id)
    {
        HttpPost post = new HttpPost(baseUrl + me + actUnfollow + key + LoopClientDetails.apiKey);
        post.setHeader(header, headerKeyword + authToken);
        post.setHeader("Content-type", "application/json");
        try
        {
            JSONObject json = new JSONObject();
            json.put(userId, id);
            StringEntity se = new StringEntity(json.toString());
            post.setEntity(se);
        }
        catch (Exception e) { e.printStackTrace(); }
        return post;
    }
}