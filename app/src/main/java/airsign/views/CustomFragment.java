package airsign.views;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import airsign.persistance.Preferences;
import airsign.styles.Themes;
import airsign.utils.SoundPlayer;
import loop.airsign.loop.R;

public class CustomFragment extends Fragment
{
    public static final String
        kID = "id",
        kInfo = "info",
        kComingFrom = "coming",
        kTitle = "title",
        kColor = "color",
        kPopStack = "pop", // If OnFragmentMessage includes this flag, then pop the fragments stack.
        kSessionExpired = "end";

    protected int color;
    protected Preferences prefs;
    protected OnFragmentMessage ofm;
    protected View back;
    protected Dialog dialog;
    private SoundPlayer soundPlayer;

    public interface OnFragmentMessage { void onMessageFromFragment(Bundle b); }
    public void setReportListener(OnFragmentMessage listener)
    {
        ofm = listener;
    }
    protected void sendMessage(Bundle b)
    {
        if (ofm!=null) ofm.onMessageFromFragment(b);
    }

    protected void setBackgroundStyle()
    {
        int gradient = Themes.getBackgroundGradient(color);
        back.setBackgroundResource(gradient);
    }

    protected void sessionExpired()
    {
        Bundle b = new Bundle();
        b.putBoolean(kSessionExpired, true);
        if (ofm!=null) sendMessage(b);
    }

    protected void changeActivityTitle(int resID)
    {
        Bundle b = new Bundle();
        b.putInt(kTitle, resID);
        if (ofm!=null) sendMessage(b);
    }

    protected void playSound()
    {
        soundPlayer.play(getActivity().getApplicationContext(), R.raw.click);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        color = prefs.getStyle();
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        prefs = new Preferences(getActivity().getApplicationContext());
        soundPlayer = new SoundPlayer();
        createLoadingDialog();
	}

    private void createLoadingDialog()
    {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grey_dark_translucent)));
    }

    protected void showLoading() { dialog.show(); }
    protected void hideLoading() { dialog.dismiss(); }

}
