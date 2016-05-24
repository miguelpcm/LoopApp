package airsign.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus
{

	public static Intent getActivateNetFromSettingsIntent()
	{
		return new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	}

	public static boolean networkEnabled(Intent intent)
	{
		boolean havingNetwork =
			intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        return !havingNetwork;
	}

	public static boolean networkEnabled(Context ctx)
	{
		ConnectivityManager cm =
			(ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni!=null && ni.isAvailable() && ni.isConnected());
	}
	
} // NetworkStatus
