package airsign.loopAPI;

import android.content.Intent;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class LoopAuthServices
{
    private static final String
            authUrl = "https://registration.frontiersin.org/oauth2/auth?response_type=token",
                clientID = "&client_id=" + LoopClientDetails.clientID,
                redirectUri = "&redirect_uri=",
                scope = "&scope=openid%20profile_read%20profile_management%20publications_read",
                op = "&state=dummy&op=",
                    login = "login",
                    register = "register";

    private static String composeEndpoint(String redirUrl)
    {
        try { redirUrl = URLEncoder.encode(redirUrl, "utf-8"); }
        catch (UnsupportedEncodingException e) {}
        return authUrl + clientID + redirectUri + redirUrl + scope + op;
    }

    public static String parseAuthToken(Intent intent)
    {
        Uri uri = intent.getData();
        if (uri != null)
        {
            try
            {
                String decoded = URLDecoder.decode(uri.toString(), "utf-8");
                String[] splits = decoded.split("access_token=");
                splits = splits[1].split("&state");
                return splits[0];
            }
            catch (Exception e) {}
        }
        return null;
    }

    public static Intent getLoginIntent(String redirUrl)
    {
        String endpoint = composeEndpoint(redirUrl) + login;
        return new Intent(Intent.ACTION_VIEW, Uri.parse(endpoint));
    }

    public static Intent getRegistrationIntent(String redirUrl)
    {
        String endpoint = composeEndpoint(redirUrl) + register;
        return new Intent(Intent.ACTION_VIEW, Uri.parse(endpoint));
    }
}