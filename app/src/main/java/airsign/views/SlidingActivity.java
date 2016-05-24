
package airsign.views;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import airsign.persistance.Preferences;
import airsign.styles.Themes;
import loop.airsign.loop.R;


public abstract class SlidingActivity extends FragmentActivity
{
	private View actionBar;
	private TextView tvTitle;
	private int savedTitleID;
    protected SlidingPaneLayout pane;
    private static final int parallaxDistance = 200;
    private int fragmentContainerID = R.id.fragmentContainer;

	private OnClickListener oclIbPanel = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (pane.isOpen()) pane.closePane();
			else pane.openPane();
		}
	};

    private PanelSlideListener psl = new PanelSlideListener()
    {
		@Override public void onPanelSlide(View v, float arg1) {}
		@Override public void onPanelOpened(View v) { tvTitle.setText(R.string.app_name); }
		@Override public void onPanelClosed(View v) { tvTitle.setText(savedTitleID); }
	};

	private void clearFragmentStack()
	{
		int stackLength = getSupportFragmentManager().getBackStackEntryCount();
		for (int i=0; i<stackLength; ++i)
			getSupportFragmentManager().popBackStackImmediate();
	}

	protected void popStackExceptBottom()
	{
		int stackLength = getSupportFragmentManager().getBackStackEntryCount();
		for (int i=0; i<stackLength-1; ++i)
			getSupportFragmentManager().popBackStackImmediate();
	}

	protected void setFragment(Fragment f)
	{
		clearFragmentStack();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContainerID, f).commit();
	}

	protected void addToStackFragment(Fragment f)
	{
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(fragmentContainerID, f).addToBackStack(null).commit();
	}

	protected void popFragmentStack()
	{
		getSupportFragmentManager().popBackStack();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			if (pane.isOpen()) pane.closePane();
			else pane.openPane();
			return true;
		}
		else return super.onOptionsItemSelected(item);
	}

    @Override
    public void onBackPressed()
    {
        if (pane.isOpen()) pane.closePane();
        else if (!getSupportFragmentManager().popBackStackImmediate())
			super.onBackPressed();
    }

	@Override
	public void setTitle(int title)
	{
		savedTitleID = title;
		tvTitle.setText(title);
	}

	protected void setStyle(int theme)
	{
		int color = Themes.getActionBarColor(theme);
		actionBar.setBackgroundResource(color);
}
	private void setStyle()
	{
		Preferences prefs = new Preferences(getApplicationContext());
		int theme = prefs.getStyle();
		setStyle(theme);
	}

	protected abstract int setLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
        setContentView(setLayout());

        pane = (SlidingPaneLayout)findViewById(R.id.slidingPane);
        pane.setParallaxDistance(parallaxDistance);
        pane.setPanelSlideListener(psl);

        // Custom actionbar
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar);
		actionBar = findViewById(R.id.actionBar);
        View bPanel = findViewById(R.id.ibPanel);
		bPanel.setOnClickListener(oclIbPanel);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setOnClickListener(oclIbPanel);
		setStyle();
    }

}
