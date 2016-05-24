package airsign.loopAPI;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.client.methods.HttpUriRequest;

public class AsyncServerRequest extends AsyncTask<HttpUriRequest, Void, String>
{

	private ServerRequester sr;
	private AsyncServerResponseListener asrl;

	// Only for SSL, need access to resources to grab the keystore
	private Context ctx;

	public interface AsyncServerResponseListener
	{
		void onReceivedData(String res);
		void onError();
	}

	public AsyncServerRequest(Context context, AsyncServerResponseListener listener)
	{
		ctx = context;
		asrl = listener;
	}
	
	@Override
	protected String doInBackground(HttpUriRequest... req)
	{
		sr = new ServerRequester(ctx);
		return sr.doRequest(req[0]);
	}

	@Override
	protected void onPostExecute(String res)
	{
        if (asrl!=null)
        	if (sr.getError()) asrl.onError();
        	else asrl.onReceivedData(res);
    }

	public void execute(HttpUriRequest request)
	{
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
	}

} // AsyncConnection