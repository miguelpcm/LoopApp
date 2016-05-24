package loop.airsign.loop.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import airsign.loopAPI.AsyncServerRequest;
import airsign.loopAPI.AsyncServerRequest.AsyncServerResponseListener;
import airsign.loopAPI.LoopServices;
import airsign.loopAPI.loopAPI.data.Article;
import airsign.loopAPI.loopAPI.parsers.ArticlesParser;
import airsign.persistance.DBInterface;
import airsign.styles.Themes;
import airsign.views.CustomFragment;
import loop.airsign.loop.R;

public class UserArticlesFragment extends CustomFragment
{
    public static final String kFragmentID = "userArticles";
    private String userID = null;
    private boolean isMe;
    private ListView lvArticles;
    private ArticlesAdapter articlesAdapter;
    private ArrayList<Article> articleList;
    private View bUser, bPeople, articlesSeparator;
    private TextView tvTitle;
    private ViewGroup layoutBottom;
    private DBInterface db;
    private int comingFrom;
    public static final String
            kSearchBy = "searchby",
            kSearchField = "searchfield";
    private int searchBy;
    private String searchField;
    public static final int
        kUser = 1,
        kDB = 2,
        kSearch = 3;

    private View.OnClickListener
        oclTabSwitcher = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle b = new Bundle();
                b.putBoolean(kPopStack, true);
                if (view==bUser) { playSound(); sendMessage(b); }
                else if (view==bPeople)
                {
                    playSound();
                    b.putString(kID, userID);
                    b.putString(kInfo, UserContactsFragment.kFragmentID);
                    sendMessage(b);
                }
            }
        };

    private AdapterView.OnItemClickListener oiclArticles = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l)
        {
            playSound();
            Article article = articleList.get(pos);
            Bundle b = new Bundle();
            b.putString(kInfo, PublicationFragment.kFragmentID);
            b.putString(CustomFragment.kID, article.id);
            sendMessage(b);
        }
    };


    private AsyncServerResponseListener onArticlesReceived = new AsyncServerResponseListener()
    {
        @Override
        public void onReceivedData(String res)
        {
            articleList = ArticlesParser.parseArticlesSimple(res);
            showArticles();
            hideLoading();
        }
        @Override  public void onError() { hideLoading(); sessionExpired(); }
    };

    public UserArticlesFragment() {}
    public static UserArticlesFragment create() { return new UserArticlesFragment(); }

    private void showArticles()
    {
        if (articleList==null)
        {
            if (comingFrom==kUser) requestUserArticles();
            else if (comingFrom==kDB) loadArticlesFromDB();
            else if (comingFrom==kSearch) searchArticles();
        }
        else
        {
            articlesAdapter = new ArticlesAdapter(getActivity(), articleList);
            lvArticles.setAdapter(articlesAdapter);
            String title = getString(R.string.publications) + "  -  " + articleList.size();
            tvTitle.setText(title);
        }
    }

    private void requestUserArticles()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onArticlesReceived);
        if (isMe) request.execute(LoopServices.getMyArticles(auth));
        else request.execute(LoopServices.getUserArticles(auth, userID));
    }

    private void searchArticles()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onArticlesReceived);
        if (searchBy==SearchFragment.kByTitle) request.execute(LoopServices.searchArticleByTitle(auth, searchField));
        else if (searchBy==SearchFragment.kByAbstract) request.execute(LoopServices.searchArticleByAbstract(auth, searchField));
        else if (searchBy==SearchFragment.kByAuthor) request.execute(LoopServices.searchArticleByAuthor(auth, searchField));
        else if (searchBy==SearchFragment.kByKeyword) request.execute(LoopServices.searchArticleByKeyword(auth, searchField));
    }

    private void loadArticlesFromDB()
    {
        articleList = db.getAllLikedArticles();
        showArticles();
        hideLoading();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (comingFrom==kUser)
        {
            if (isMe) changeActivityTitle(R.string.me);
            else changeActivityTitle(R.string.user_profile);
        }
        else if (comingFrom==kDB)
        {
            articleList = null;
            changeActivityTitle(R.string.my_liked_articles);
        }
        else if (comingFrom==kSearch) changeActivityTitle(R.string.search_results);

        setBackgroundStyle();
        setFontColors();
        showArticles();
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        comingFrom = b.getInt(kComingFrom);
        switch (comingFrom)
        {
            case kUser:
            {
                userID = b.getString(kID);
                isMe = prefs.getUserID().equals(userID);
                break;
            }
            case kSearch:
            {
                searchBy = b.getInt(kSearchBy);
                searchField = b.getString(kSearchField);
                searchField = searchField.replace(" ", "%20");
                break;
            }
            case kDB:
            {
                db = new DBInterface(getActivity().getApplicationContext());
                break;
            }
        }
	}

    private void setFontColors()
    {
        int color = prefs.getStyle();
        if (color==Themes.kWhite)
        {
            tvTitle.setTextColor(Color.BLACK);
            articlesSeparator.setBackgroundColor(getResources().getColor(R.color.grey_dark_translucent));
            lvArticles.setDivider(new ColorDrawable(getResources().getColor(R.color.grey_dark_translucent)));
            lvArticles.setDividerHeight(1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle b)
    {
        ViewGroup v = (ViewGroup)li.inflate(R.layout.user_articles_fragment, container, false);
        back = v.findViewById(R.id.back);

        // Load common elements of fragment
        bUser = v.findViewById(R.id.bInfo);
        bUser.setOnClickListener(oclTabSwitcher);
        bPeople = v.findViewById(R.id.bPeople);
        bPeople.setOnClickListener(oclTabSwitcher);

        // Load Articles layout
        tvTitle = (TextView)v.findViewById(R.id.txtTitleArticles);
        layoutBottom = (ViewGroup)v.findViewById(R.id.layoutBottom);
        if (comingFrom!=kUser) layoutBottom.setVisibility(View.GONE);
        lvArticles = (ListView)v.findViewById(R.id.listArticles);
        lvArticles.setOnItemClickListener(oiclArticles);
        articlesSeparator = v.findViewById(R.id.sepArticles);
        return v;
    }

    private class ArticlesAdapter extends ArrayAdapter<Article>
    {
        private static final int layoutID = R.layout.row_article;
        private LayoutInflater li;

        public ArticlesAdapter(Context ctx, ArrayList<Article> list)
        {
            super(ctx, layoutID, list);
            li = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent)
        {
            View v;
            if (convertView==null) v = li.inflate(layoutID, null);
            else v = convertView;
            TextView
                h0 = (TextView)v.findViewById(R.id.tvDate),
                h1 = (TextView)v.findViewById(R.id.tvName),
                h2 = (TextView)v.findViewById(R.id.tvJournal);
            Article article = articleList.get(pos);
            if (prefs.getStyle()==Themes.kWhite)
            {
                h0.setTextColor(Color.BLACK);
                h1.setTextColor(Color.BLACK);
                h2.setTextColor(Color.BLACK);
            }
            if (article.date!=null) h0.setText(article.date);
            h1.setText(article.title);
            if (article.journal!=null) h2.setText(article.journal);
            return v;
        }
    }

}
