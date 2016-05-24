package loop.airsign.loop.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import airsign.loopAPI.AsyncServerRequest;
import airsign.loopAPI.AsyncServerRequest.AsyncServerResponseListener;
import airsign.loopAPI.LoopServices;
import airsign.loopAPI.loopAPI.data.Article;
import airsign.loopAPI.loopAPI.data.Author;
import airsign.loopAPI.loopAPI.parsers.ArticlesParser;
import airsign.persistance.DBInterface;
import airsign.styles.Themes;
import airsign.views.CustomFragment;
import airsign.views.FlowLayout;
import loop.airsign.loop.R;

public class PublicationFragment extends CustomFragment
{
    public static final String kFragmentID = "publication";
    private String articleID = null;
    private Article article;
    private boolean liked = false;
    private TextView tvTitleTitle, tvTitle, tvAbstractTitle, tvAbstract,
            tvAuthorsTitle, tvKeywordsTitle, bHeart;
    private FlowLayout flAuthors, flKeywords;
    private DBInterface db;

    private View.OnClickListener
        oclUser = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playSound();
                String id = (String)view.getTag(R.integer.tag);
                if (!prefs.getUserID().equals(id)) // Don't load our user again
                {
                    Bundle b = new Bundle();
                    b.putString(kInfo, UserInfoFragment.kFragmentID);
                    b.putString(CustomFragment.kID, id);
                    sendMessage(b);
                }
            }
        },

        oclHeart = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playSound();
                if (liked) unlikeArticle();
                else likeArticle();
            }
        };

    private AsyncServerResponseListener
        onArticleReceived = new AsyncServerResponseListener()
        {
            @Override
            public void onReceivedData(String res)
            {
                article = ArticlesParser.parseArticle(res);
                showArticle();
                hideLoading();
            }
            @Override public void onError() { hideLoading(); sessionExpired(); }
        };

    public PublicationFragment() {}
    public static PublicationFragment create() { return new PublicationFragment(); }

    private void checkIsLiked()
    {
        liked = db.isArticleLiked(articleID);
        updateHeart();
    }

    private void updateHeart()
    {
        if (liked)
        {
            bHeart.setText(R.string.liked);
            bHeart.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
        }
        else
        {
            bHeart.setText(R.string.like);
            bHeart.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.empty_heart, 0, 0, 0);
        }
        bHeart.setVisibility(View.VISIBLE);
    }

    private void requestArticle()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onArticleReceived);
        request.execute(LoopServices.getArticle(auth, articleID));
    }

    private void likeArticle()
    {
        db.addLikedArticle(article);
        liked = true;
        updateHeart();
    }

    private void unlikeArticle()
    {
        db.removeLikedArticle(articleID);
        liked = false;
        updateHeart();
    }

    private void showArticle()
    {
        if (article==null) requestArticle();
        else
        {
            tvTitleTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(article.title);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT,
                    FlowLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 12, 14, 0);
            if (article.authors.size()>0)
            {
                tvAuthorsTitle.setVisibility(View.VISIBLE);
                for (Author author : article.authors)
                {
                    View v = View.inflate(getActivity().getApplicationContext(),
                            R.layout.text_flow, null);
                    TextView tvName = (TextView) v.findViewById(R.id.tv);
                    tvName.setText(author.name);
                    if (author.id!=null)
                    {
                        tvName.setPaintFlags(tvName.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                        tvName.setTag(R.integer.tag, author.id);
                        tvName.setOnClickListener(oclUser);
                    }
                    tvName.setLayoutParams(lp);
                    flAuthors.addView(v);
                }
            }
            if (article.abstrakt!=null && !article.abstrakt.equals(""))
            {
                tvAbstractTitle.setVisibility(View.VISIBLE);
                tvAbstract.setVisibility(View.VISIBLE);
                tvAbstract.setText(article.abstrakt);
            }
            if (article.keywords.size()>0)
            {
                tvKeywordsTitle.setVisibility(View.VISIBLE);
                for (String kw : article.keywords)
                {
                    View v = View.inflate(getActivity().getApplicationContext(),
                            R.layout.text_flow, null);
                    TextView tvName = (TextView) v.findViewById(R.id.tv);
                    tvName.setText(kw);
                    tvName.setLayoutParams(lp);
                    flKeywords.addView(v);
                }
            }
            checkIsLiked();
            hideLoading();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        changeActivityTitle(R.string.publication);
        setBackgroundStyle();
        setFontColors();
        showArticle();
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        articleID = b.getString(kID);
        db = new DBInterface(getActivity().getApplicationContext());
	}

    private void setFontColors()
    {
        int color = prefs.getStyle();
        if (color==Themes.kWhite)
        {
            tvTitleTitle.setTextColor(Color.BLACK);
            tvTitle.setTextColor(Color.BLACK);
            tvAbstractTitle.setTextColor(Color.BLACK);
            tvAbstract.setTextColor(Color.BLACK);
            tvAuthorsTitle.setTextColor(Color.BLACK);
            tvKeywordsTitle.setTextColor(Color.BLACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle b)
    {
        ViewGroup v = (ViewGroup)li.inflate(R.layout.publication_fragment, container, false);
        back = v.findViewById(R.id.back);

        tvTitleTitle = (TextView)v.findViewById(R.id.txtTitleTitle);
        tvTitle = (TextView)v.findViewById(R.id.txtTitle);
        tvAbstractTitle = (TextView)v.findViewById(R.id.txtAbstractTitle);
        tvAbstract = (TextView)v.findViewById(R.id.txtAbstract);
        tvAuthorsTitle = (TextView)v.findViewById(R.id.txtAuthorsTitle);
        tvKeywordsTitle = (TextView)v.findViewById(R.id.txtKeywordsTitle);
        bHeart = (TextView)v.findViewById(R.id.bHeart);
        bHeart.setOnClickListener(oclHeart);
        flAuthors = (FlowLayout)v.findViewById(R.id.vAuthors);
        flKeywords = (FlowLayout)v.findViewById(R.id.vKeywords);
        return v;
    }
}
