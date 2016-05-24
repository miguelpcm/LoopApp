package loop.airsign.loop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.json.JSONObject;

import airsign.loopAPI.AsyncServerRequest;
import airsign.loopAPI.AsyncServerRequest.AsyncServerResponseListener;
import airsign.loopAPI.GenericParser;
import airsign.loopAPI.LoopAuthServices;
import airsign.loopAPI.LoopClientDetails;
import airsign.loopAPI.LoopServices;
import airsign.persistance.Preferences;
import airsign.utils.DialogFactory;
import airsign.utils.NetworkStatus;

public class WelcomeActivity extends Activity
{

	private Preferences prefs;
	private ViewGroup enterButtons;

	private View.OnClickListener
		oclbLogin = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = LoopAuthServices.getLoginIntent(LoopClientDetails.redirectUri);
				startActivity(intent);
			}
		},

		oclbRegister = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = LoopAuthServices.getRegistrationIntent(LoopClientDetails.redirectUri);
				startActivity(intent);
			}
		};

	private AsyncServerResponseListener onResponseListener = new AsyncServerResponseListener()
	{
		@Override
		public void onReceivedData(String res)
		{
			try
			{
				GenericParser.ErrorResponse resp = GenericParser.getError(new JSONObject(res));
				if (resp.isError()) showEnter();
				else startApp();
			}
			catch (Exception e) { e.printStackTrace(); showEnter(); }
		}

		@Override
		public void onError() { showEnter(); }
	};

	private void showEnter()
	{
		prefs.setAuth(null);
		enterButtons.setVisibility(View.VISIBLE);
	}

	private void startApp()
	{
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onNewIntent(Intent newIntent)
	{
		super.onNewIntent(newIntent);
		String s = LoopAuthServices.parseAuthToken(newIntent);
		prefs.setAuth(s);
		startApp();
	}

	private void testAuthToken()
	{
		String auth = prefs.getAuth();
		if (auth!=null)
		{
			AsyncServerRequest request = new AsyncServerRequest(getApplicationContext(), onResponseListener);
			request.execute(LoopServices.getMyProfile(auth));
		}
		else showEnter();
	}

	private void lauchNetDialog()
	{
		DialogInterface.OnClickListener
			yes = new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					Intent intent = NetworkStatus.getActivateNetFromSettingsIntent();
					startActivity(intent);
				}
			},

			no = new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {}
			};

		AlertDialog ad = DialogFactory.getYesNoDialog(WelcomeActivity.this,
				R.string.no_network, R.string.activate_network, yes, no);
		ad.show();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (NetworkStatus.networkEnabled(getApplicationContext()))
			testAuthToken();
		else lauchNetDialog();
	}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		prefs = new Preferences(getApplicationContext());
		enterButtons = (ViewGroup)findViewById(R.id.enterButtons);
		Button
				bLogin = (Button)findViewById(R.id.bLogin),
				bRegister = (Button)findViewById(R.id.bRegister);
		bLogin.setOnClickListener(oclbLogin);
		bRegister.setOnClickListener(oclbRegister);
    }
}