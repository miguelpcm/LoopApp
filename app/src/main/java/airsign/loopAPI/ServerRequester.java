package airsign.loopAPI;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.util.EntityUtils;

import loop.airsign.loop.R;

public class ServerRequester
{
//	private HttpClient client;
	private SecHTTPClient client;
	private boolean error = false;

	public ServerRequester(Context ctx)
	{
		client = new SecHTTPClient(ctx, SecHTTPClient.keyStoreType,
				R.raw.loopks, "mypassword",
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		final int timeout = 12;
		client.setTimeout(timeout);
	}

	public boolean getError() { return error; }

	private String getEntityContent(HttpEntity entity)
	{
		String res = null;

		try { if (entity!=null) res = EntityUtils.toString(entity); }
        catch (Exception e)
        {
        	e.printStackTrace();
        	error = true;
        }
		return res;
	}

    public String doRequest(HttpUriRequest req)
    {
        HttpResponse resp;
        try { resp = client.execute(req); }
        catch (Exception e)
        {
            e.printStackTrace();
            error = true;
            return null;
        }
        HttpEntity entity = resp.getEntity();
        return getEntityContent(entity);
    }

} // ServerRequester
