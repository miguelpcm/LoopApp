package loop.airsign.loop.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import airsign.persistance.Preferences;
import airsign.styles.Themes;
import airsign.views.CustomFragment;
import loop.airsign.loop.R;

public class ThemesFragment extends CustomFragment
{
    private View s0, s1, s2, s3, s4, s5, s6;
    private TextView tvTitle;
    private Button bWhite, bRed, bBlue, bGreen, bPurple, bGrey, bBlack;
    private boolean changeContrastTheme; // Flag for making changes in black/white views

    private View.OnClickListener oclTheme = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            playSound();
            int oldColor = color;
            color = Themes.kGrey;
            if (v==bRed) color = Themes.kRed;
            else if (v==bBlue) color = Themes.kBlue;
            else if (v==bGreen) color = Themes.kGreen;
            else if (v==bPurple) color = Themes.kPurple;
            else if (v==bBlack) color = Themes.kBlack;
            else if (v==bWhite) color = Themes.kWhite;
            if (oldColor!=color)
            {
                changeContrastTheme = (oldColor==Themes.kWhite || color==Themes.kWhite);
                prefs.setStyle(color);
                Bundle b = new Bundle();
                b.putInt(kColor, color);
                sendMessage(b);
                setStyle();
            }
        }
    };

    public ThemesFragment() {}
    public static ThemesFragment create()
    {
        return new ThemesFragment();
    }

    private void setLayoutStyle()
    {
        int nColor;
        if (color==Themes.kWhite)
        {
            nColor = getResources().getColor(R.color.grey_dark);
            bWhite.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
            bBlack.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
            bBlue.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
            bGrey.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
            bGreen.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
            bPurple.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
            bRed.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bbrush, 0, 0, 0);
        }
        else
        {
            nColor = getResources().getColor(android.R.color.white);
            bWhite.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
            bBlack.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
            bBlue.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
            bGrey.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
            bGreen.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
            bPurple.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
            bRed.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.brush, 0, 0, 0);
        }
        tvTitle.setTextColor(nColor);
        bWhite.setTextColor(nColor);
        bBlack.setTextColor(nColor);
        bBlue.setTextColor(nColor);
        bGrey.setTextColor(nColor);
        bGreen.setTextColor(nColor);
        bPurple.setTextColor(nColor);
        bRed.setTextColor(nColor);
        s0.setBackgroundColor(nColor);
        s1.setBackgroundColor(nColor);
        s2.setBackgroundColor(nColor);
        s3.setBackgroundColor(nColor);
        s4.setBackgroundColor(nColor);
        s5.setBackgroundColor(nColor);
        s6.setBackgroundColor(nColor);
    }

    public void setStyle()
    {
        setBackgroundStyle();
        if (changeContrastTheme) setLayoutStyle();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        changeActivityTitle(R.string.themes);
        setStyle();
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        prefs = new Preferences(getActivity().getApplicationContext());
	}

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle b)
    {
        ViewGroup v = (ViewGroup)li.inflate(R.layout.settings_themes_fragment, container, false);
        back = v.findViewById(R.id.back);
        tvTitle = (TextView)v.findViewById(R.id.tvTitle);
        bGrey = (Button)v.findViewById(R.id.bThGrey);
        bGrey.setOnClickListener(oclTheme);
        bRed = (Button)v.findViewById(R.id.bThRed);
        bRed.setOnClickListener(oclTheme);
        bGreen = (Button)v.findViewById(R.id.bThGreen);
        bGreen.setOnClickListener(oclTheme);
        bBlue = (Button)v.findViewById(R.id.bThBlue);
        bBlue.setOnClickListener(oclTheme);
        bPurple = (Button)v.findViewById(R.id.bThPurple);
        bPurple.setOnClickListener(oclTheme);
        bBlack = (Button)v.findViewById(R.id.bThBlack);
        bBlack.setOnClickListener(oclTheme);
        bWhite = (Button)v.findViewById(R.id.bThWhite);
        bWhite.setOnClickListener(oclTheme);
        s0 = v.findViewById(R.id.s0);
        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        s4 = v.findViewById(R.id.s4);
        s5 = v.findViewById(R.id.s5);
        s6 = v.findViewById(R.id.s6);
        changeContrastTheme = true;
        return v;
    }

}
