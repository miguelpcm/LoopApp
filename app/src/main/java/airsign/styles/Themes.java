package airsign.styles;

import loop.airsign.loop.R;

public class Themes
{
    public static final int
        kGrey   =   1,
        kGreen  =   2,
        kRed    =   3,
        kBlue   =   4,
        kPurple =   5,
        kWhite  =   6,
        kBlack  =   7;

    public static class ColorTheme
    {
        public int actionBarColor, backgroundGradient;
        public ColorTheme(int actionBar, int background)
        {
            actionBarColor = actionBar;
            backgroundGradient = background;
        }
    }

    public static int getActionBarColor(int id)
    {
        switch (id)
        {
            case kWhite: return R.color.white_actionbar;
            case kGreen: return R.color.green_actionbar;
            case kRed: return R.color.red_actionbar;
            case kBlue: return R.color.blue_actionbar;
            case kPurple: return R.color.purple_actionbar;
            case kBlack: return R.color.black_actionbar;
            default: return R.color.grey_actionbar;
        }
    }

    public static int getBackgroundGradient(int id)
    {
        switch (id)
        {
            case kWhite: return R.drawable.gradient_background_white;
            case kGreen: return R.drawable.gradient_background_green;
            case kRed: return R.drawable.gradient_background_red;
            case kBlue: return R.drawable.gradient_background_blue;
            case kPurple: return R.drawable.gradient_background_purple;
            case kBlack: return R.drawable.gradient_background_black;
            default: return R.drawable.gradient_background_grey;
        }
    }

    public static ColorTheme getStyle(int id)
    {
        switch (id)
        {
            case kWhite: return new ColorTheme(R.color.white_actionbar, R.drawable.gradient_background_white);
            case kGreen: return new ColorTheme(R.color.green_actionbar, R.drawable.gradient_background_green);
            case kRed: return new ColorTheme(R.color.red_actionbar, R.drawable.gradient_background_red);
            case kBlue: return new ColorTheme(R.color.blue_actionbar, R.drawable.gradient_background_blue);
            case kPurple: return new ColorTheme(R.color.purple_actionbar, R.drawable.gradient_background_purple);
            case kBlack: return new ColorTheme(R.color.black_actionbar, R.drawable.gradient_background_black);
            default: return new ColorTheme(R.color.grey_actionbar, R.drawable.gradient_background_grey);
        }
    }
}
