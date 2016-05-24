package airsign.loopAPI;

import android.content.Context;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.security.KeyStore;

public class SecHTTPClient extends DefaultHttpClient
{

	private final int
		portHTTP = 80,
		portHTTPS = 443;
	private final String
		HTTP = "http",
		HTTPS = "https";
	
	public static final String keyStoreType = "BKS";
	
	private static PlainSocketFactory sf;
	private static SSLSocketFactory ssf;


	// Example:
	//   keyStoreType = "BKS";
	//   keyStore = R.raw.certks;
	//   flag = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
	public SecHTTPClient(Context ctx, String ksType, int ks,
						 String pass, X509HostnameVerifier flag)
	{
		sf = PlainSocketFactory.getSocketFactory();
		ssf = newSSLSocket(ctx, ksType, ks, pass, flag);
	}
	
	@Override
	protected ClientConnectionManager createClientConnectionManager()
	{
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme(HTTP, sf, portHTTP));
		registry.register(new Scheme(HTTPS, ssf, portHTTPS));
		return new SingleClientConnManager(getParams(), registry);
	}

	public void setTimeout(int secs)
	{
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, secs * 1000);
		HttpConnectionParams.setSoTimeout(params, secs * 1000);
		setParams(params);
	}

	public SSLSocketFactory newSSLSocket(Context ctx, String keyStoreType,
			int keyStore, String password, X509HostnameVerifier flag)
	{
		try
		{
			KeyStore trusted = KeyStore.getInstance(keyStoreType);
			InputStream in = ctx.getResources().openRawResource(keyStore);
			char[] pass = password.toCharArray();
			trusted.load(in, pass);
			SSLSocketFactory sslsf = new SSLSocketFactory(trusted);
			sslsf.setHostnameVerifier(flag);
			in.close();
			return sslsf;
		}
			catch (Exception e) { return null; }
	}
	
} // SecHTTPClient
