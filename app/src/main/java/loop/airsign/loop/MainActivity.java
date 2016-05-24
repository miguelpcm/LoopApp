package loop.airsign.loop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import airsign.persistance.Preferences;
import airsign.views.CustomFragment;
import airsign.views.SlidingActivity;
import loop.airsign.loop.fragments.PublicationFragment;
import loop.airsign.loop.fragments.SearchFragment;
import loop.airsign.loop.fragments.UserArticlesFragment;
import loop.airsign.loop.fragments.UserContactsFragment;
import loop.airsign.loop.fragments.ThemesFragment;
import loop.airsign.loop.fragments.UserInfoFragment;

public class MainActivity extends SlidingActivity
{

    private View bUsersMe, bArticlesSearch, bArticlesLike, bThemes, bLogout;
    private CustomFragment.OnFragmentMessage
        ofm = new CustomFragment.OnFragmentMessage()
        {
            @Override
            public void onMessageFromFragment(Bundle b)
            {
                boolean end = b.getBoolean(CustomFragment.kSessionExpired, false);
                if (end)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.end_session), Toast.LENGTH_LONG).show();
                    endSession();
                }
                else
                {
                    int title = b.getInt(CustomFragment.kTitle);
                    if (title!=0) setTitle(title);
                    String id = b.getString(CustomFragment.kID);
                    String fragmentID = b.getString(CustomFragment.kInfo);
                    boolean pop = b.getBoolean(CustomFragment.kPopStack, false);
                    if (pop) popFragmentStack();
                    if (fragmentID!=null)
                    {
                        if (fragmentID.equals(UserInfoFragment.kFragmentID)) showUserFragment(id);
                        else if (fragmentID.equals(UserContactsFragment.kFragmentID)) showUserContactsFragment(id);
                        else if (fragmentID.equals(PublicationFragment.kFragmentID)) showPublicationFragment(id);
                        else if (fragmentID.equals(UserArticlesFragment.kFragmentID))
                        {
                            int comingFrom = b.getInt(CustomFragment.kComingFrom);
                            if (comingFrom==UserArticlesFragment.kUser)
                                showUserArticlesFragment(id);
                            else
                            {
                                String toSearch = b.getString(UserArticlesFragment.kSearchField);
                                int searchBy = b.getInt(UserArticlesFragment.kSearchBy);
                                showSearchArticleResults(toSearch, searchBy);
                            }
                        }
                    }
                }
            }
        },

        ofmTitleTheme = new CustomFragment.OnFragmentMessage()
        {
            @Override
            public void onMessageFromFragment(Bundle b)
            {
                int title = b.getInt(CustomFragment.kTitle);
                int color = b.getInt(ThemesFragment.kColor);
                if (title!=0) setTitle(title);
                if (color!=0) setStyle(color);
            }
        };

    private View.OnClickListener oclbPanel = new View.OnClickListener()
    {
        @Override public void onClick(View v)
        {
            if (v==bUsersMe) showMeFragment();
            else if (v==bArticlesLike)
            {
                popStackExceptBottom();
                showLikedArticlesFragment();
            }
            else if (v==bArticlesSearch)
            {
                popStackExceptBottom();
                showSearchFragment();
            }
            else if (v==bThemes) showThemesFragment();
            else if (v==bLogout) endSession();
        }
    };

    private void showThemesFragment()
    {
        pane.closePane();
        ThemesFragment f = ThemesFragment.create();
        f.setReportListener(ofmTitleTheme);
        addToStackFragment(f);
    }

    private void showSearchFragment()
    {
        pane.closePane();
        SearchFragment f = SearchFragment.create();
        f.setReportListener(ofm);
        addToStackFragment(f);
    }

    private void showUserFragment(String userID)
    {
        pane.closePane();
        UserInfoFragment f = UserInfoFragment.create();
        Bundle b = new Bundle();
        b.putString(CustomFragment.kID, userID);
        f.setReportListener(ofm);
        f.setArguments(b);
        addToStackFragment(f);
    }

    private void showUserContactsFragment(String userID)
    {
        pane.closePane();
        UserContactsFragment f = UserContactsFragment.create();
        Bundle b = new Bundle();
        b.putString(CustomFragment.kID, userID);
        f.setReportListener(ofm);
        f.setArguments(b);
        addToStackFragment(f);
    }

    private void showUserArticlesFragment(String userID)
    {
        pane.closePane();
        UserArticlesFragment f = UserArticlesFragment.create();
        Bundle b = new Bundle();
        b.putInt(CustomFragment.kComingFrom, UserArticlesFragment.kUser);
        b.putString(CustomFragment.kID, userID);
        f.setReportListener(ofm);
        f.setArguments(b);
        addToStackFragment(f);
    }

    private void showLikedArticlesFragment()
    {
        pane.closePane();
        UserArticlesFragment f = UserArticlesFragment.create();
        Bundle b = new Bundle();
        b.putInt(CustomFragment.kComingFrom, UserArticlesFragment.kDB);
        f.setReportListener(ofm);
        f.setArguments(b);
        addToStackFragment(f);
    }

    private void showSearchArticleResults(String toSearch, int searchBy)
    {
        pane.closePane();
        UserArticlesFragment f = UserArticlesFragment.create();
        Bundle b = new Bundle();
        b.putString(UserArticlesFragment.kSearchField, toSearch);
        b.putInt(CustomFragment.kComingFrom, UserArticlesFragment.kSearch);
        b.putInt(UserArticlesFragment.kSearchBy, searchBy);
        f.setReportListener(ofm);
        f.setArguments(b);
        addToStackFragment(f);
    }

    private void showPublicationFragment(String articleID)
    {
        pane.closePane();
        PublicationFragment f = PublicationFragment.create();
        Bundle b = new Bundle();
        b.putString(CustomFragment.kID, articleID);
        f.setReportListener(ofm);
        f.setArguments(b);
        addToStackFragment(f);
    }

    private void showMeFragment()
    {
        pane.closePane();
        UserInfoFragment f = UserInfoFragment.create();
        f.setReportListener(ofm);
        setFragment(f);
    }

    private void endSession()
    {
        Preferences prefs = new Preferences(getApplicationContext());
        prefs.setAuth(null);
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int setLayout() { return R.layout.sliding_pane; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        bThemes = findViewById(R.id.bSettingsThemes);
        bThemes.setOnClickListener(oclbPanel);
        bUsersMe = findViewById(R.id.bUsersMe);
        bUsersMe.setOnClickListener(oclbPanel);
        bArticlesLike = findViewById(R.id.bArticlesLikes);
        bArticlesLike.setOnClickListener(oclbPanel);
        bArticlesSearch = findViewById(R.id.bArticlesSearch);
        bArticlesSearch.setOnClickListener(oclbPanel);
        bLogout = findViewById(R.id.bLogout);
        bLogout.setOnClickListener(oclbPanel);
        showMeFragment();
    }
}
