package loop.airsign.loop.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import airsign.persistance.Preferences;
import airsign.styles.Themes;
import airsign.views.CustomFragment;
import loop.airsign.loop.R;

public class SearchFragment extends CustomFragment
{
    private View s0, s1, s2, s3, s4;
    private ImageView imgLogo;
    private Button bTitle, bAuthor, bAbstract, bKeyword, bSearch;
    private EditText etSearch;
    private int searchFlag;
    public static final int
        kByTitle = 0,
        kByAbstract = 1,
        kByKeyword = 2,
        kByAuthor = 3;

    private View.OnClickListener
        oclSearchType = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playSound();
                bTitle.setSelected(false);
                bAuthor.setSelected(false);
                bAbstract.setSelected(false);
                bKeyword.setSelected(false);
                v.setSelected(true);
                if (v==bTitle) searchFlag = kByTitle;
                else if (v==bAbstract) searchFlag = kByAbstract;
                else if (v==bAuthor) searchFlag = kByAuthor;
                else if (v==bKeyword) searchFlag = kByKeyword;
            }
        },

        oclbSearch = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playSound();
                String toSearch = etSearch.getText().toString();
                if (toSearch==null || toSearch.equals("")) etSearch.setHint(R.string.empty_field);
                else
                {
                    Bundle b = new Bundle();
                    b.putString(kInfo, UserArticlesFragment.kFragmentID);
                    b.putInt(kComingFrom, UserArticlesFragment.kSearch);
                    b.putString(UserArticlesFragment.kSearchField, toSearch);
                    b.putInt(UserArticlesFragment.kSearchBy, searchFlag);
                    sendMessage(b);
                }
            }
        };

    public SearchFragment() {}
    public static SearchFragment create() { return new SearchFragment(); }

    private void setGuiStyle()
    {
        int color = prefs.getStyle();
        if (color==Themes.kWhite)
        {
            int nColor = getResources().getColor(R.color.grey_dark);
            etSearch.setTextColor(Color.BLACK);
            etSearch.setHintTextColor(getResources().getColor(R.color.grey_dark_translucent));
            etSearch.setBackgroundColor(getResources().getColor(R.color.grey_translucent));
            bSearch.setTextColor(Color.BLACK);
            bSearch.setBackgroundResource(R.drawable.button_drafted_black);
            bTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bmagnifier, 0, 0, 0);
            bTitle.setTextColor(Color.BLACK);
            bAuthor.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bmagnifier, 0, 0, 0);
            bAuthor.setTextColor(Color.BLACK);
            bAbstract.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bmagnifier, 0, 0, 0);
            bAbstract.setTextColor(Color.BLACK);
            bKeyword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.bmagnifier, 0, 0, 0);
            bKeyword.setTextColor(Color.BLACK);
            s0.setBackgroundColor(nColor);
            s1.setBackgroundColor(nColor);
            s2.setBackgroundColor(nColor);
            s3.setBackgroundColor(nColor);
            s4.setBackgroundColor(nColor);
            imgLogo.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        changeActivityTitle(R.string.search_article);
        setBackgroundStyle();
        setGuiStyle();
        bTitle.setSelected(true);
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        prefs = new Preferences(getActivity().getApplicationContext());
        searchFlag = kByTitle;
	}

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle b)
    {
        ViewGroup v = (ViewGroup)li.inflate(R.layout.search_fragment, container, false);
        back = v.findViewById(R.id.back);
        bTitle = (Button)v.findViewById(R.id.bTitle);
        bTitle.setOnClickListener(oclSearchType);
        bAbstract = (Button)v.findViewById(R.id.bAbstract);
        bAbstract.setOnClickListener(oclSearchType);
        bAuthor = (Button)v.findViewById(R.id.bAuthor);
        bAuthor.setOnClickListener(oclSearchType);
        bKeyword = (Button)v.findViewById(R.id.bKeyword);
        bKeyword.setOnClickListener(oclSearchType);
        bSearch = (Button)v.findViewById(R.id.bSearch);
        bSearch.setOnClickListener(oclbSearch);
        etSearch = (EditText)v.findViewById(R.id.etSearch);
        s0 = v.findViewById(R.id.s0);
        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        s4 = v.findViewById(R.id.s4);
        imgLogo = (ImageView)v.findViewById(R.id.imgLogo);
        return v;
    }

}
