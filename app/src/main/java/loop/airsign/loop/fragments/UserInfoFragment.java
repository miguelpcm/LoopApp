package loop.airsign.loop.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import airsign.loopAPI.AsyncServerRequest;
import airsign.loopAPI.AsyncServerRequest.AsyncServerResponseListener;
import airsign.loopAPI.LoopServices;
import airsign.loopAPI.loopAPI.data.User;
import airsign.loopAPI.loopAPI.parsers.UserParser;
import airsign.styles.Themes;
import airsign.utils.ImgLoaderFactory;
import airsign.views.CustomFragment;
import loop.airsign.loop.R;

public class UserInfoFragment extends CustomFragment
{
    public static final String kFragmentID = "userInfo";
    private String userID = null;
    private ArrayList<String> follows;
    private boolean isMe, following = false;
    private TextView
        tvBioTitle, tvDetailsTitle, bStar,
        tvName, tvProvenance, tvDegree, tvAffiliation, tvJob, tvBio;
    private View bPeople, bArticles;
    private ImageView imgUser;
    private ImageLoader imgLoader;
    private User user;

    private View.OnClickListener
        oclStar = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playSound();
                if (following) requestUnfollowUser();
                else requestFollowUser();
            }
        },

        oclTabSwitcher = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (user!=null)
                {
                    Bundle b = new Bundle();
                    b.putString(kID, user.id);
                    if (view==bPeople)
                    {
                        playSound();
                        b.putString(kInfo, UserContactsFragment.kFragmentID);
                        sendMessage(b);
                    }
                    else if (view==bArticles)
                    {
                        playSound();
                        b.putString(kInfo, UserArticlesFragment.kFragmentID);
                        b.putInt(kComingFrom, UserArticlesFragment.kUser);
                        sendMessage(b);
                    }
                }
            }
        };

    private AsyncServerResponseListener
        onUserReceived = new AsyncServerResponseListener()
        {
            @Override
            public void onReceivedData(String res)
            {
                user = UserParser.parseUser(res);
                if (isMe) prefs.setUserID(user.id);
                showInfo();
            }
            @Override public void onError() { hideLoading(); sessionExpired(); }
        },

        onFollowChangeCompleted = new AsyncServerResponseListener()
        {
            @Override
            public void onReceivedData(String res)
            {
                following = !following;
                if (following) follows.add(userID);
                else follows.remove(userID);
                prefs.saveFollowing(follows);
                updateStar();
                hideLoading();
            }
            @Override public void onError() {
                hideLoading(); }
        };

    public UserInfoFragment() {}
    public static UserInfoFragment create() { return new UserInfoFragment(); }

    private void checkIsFollowed()
    {
        if (!isMe && user.isPublic)
        {
            following = follows.contains(user.id);
            updateStar();
        }
    }

    private void updateStar()
    {
        if (following)
        {
            bStar.setText(R.string.following);
            bStar.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        }
        else
        {
            bStar.setText(R.string.follow);
            bStar.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.empty_star, 0, 0, 0);
        }
        bStar.setVisibility(View.VISIBLE);
    }

    private void requestUserInfo()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onUserReceived);
        if (isMe) request.execute(LoopServices.getMyProfile(auth));
        else request.execute(LoopServices.getUserProfile(auth, userID));
    }

    private void requestFollowUser()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onFollowChangeCompleted);
        request.execute(LoopServices.postFollowUser(auth, userID));
    }

    private void requestUnfollowUser()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onFollowChangeCompleted);
        request.execute(LoopServices.postUnfollowUser(auth, userID));
    }

    private void showInfo()
    {
        if (user==null) requestUserInfo();
        else
        {
            if (!imgLoader.isInited()) imgLoader = ImgLoaderFactory.getInstance(getActivity().getApplicationContext());
            imgLoader.displayImage(user.imgUrl, imgUser);
            tvName.setText(user.getName());
            boolean showDetails = (user.degree!=null || user.affiliation!=null || user.jobTitle!=null);
            if (showDetails)
            {
                String bulletPoint = "\u2022 "; // "\u25cf"
                tvDetailsTitle.setVisibility(View.VISIBLE);
                if (user.degree!=null && !user.degree.equals(""))
                    tvDegree.setText(bulletPoint + user.degree);
                else tvDegree.setVisibility(View.GONE);
                if (user.affiliation!=null && !user.affiliation.equals(""))
                    tvAffiliation.setText(bulletPoint + user.affiliation);
                else tvAffiliation.setVisibility(View.GONE);
                if (user.jobTitle!=null && !user.jobTitle.equals(""))
                    tvJob.setText(bulletPoint + user.jobTitle);
                else tvJob.setVisibility(View.GONE);
            }
            if (user.bio!=null)
            {
                tvBio.setText(user.bio);
                tvBioTitle.setVisibility(View.VISIBLE);
            }
            if (!user.isPublic)
            {
                bPeople.setVisibility(View.GONE);
                bArticles.setVisibility(View.GONE);
            }
            String provenance = user.getProvenance();
            if (provenance!=null) tvProvenance.setText(provenance);
            checkIsFollowed();
            hideLoading();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (isMe) changeActivityTitle(R.string.me);
        else
        {
            changeActivityTitle(R.string.user_profile);
            follows = prefs.getFollowing();
        }
        setBackgroundStyle();
        setFontColors();
        showInfo();
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        userID = (b!=null)? b.getString(kID):null;
        isMe = (userID==null);
	}

    private void setFontColors()
    {
        int color = prefs.getStyle();
        if (color==Themes.kWhite)
        {
            tvAffiliation.setTextColor(Color.BLACK);
            tvBioTitle.setTextColor(Color.BLACK);
            tvBio.setTextColor(Color.BLACK);
            tvDegree.setTextColor(Color.BLACK);
            tvDetailsTitle.setTextColor(Color.BLACK);
            tvJob.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvProvenance.setTextColor(Color.BLACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle b)
    {
        imgLoader = ImgLoaderFactory.getInstance(getActivity().getApplicationContext());
        ViewGroup v = (ViewGroup)li.inflate(R.layout.user_info_fragment, container, false);
        back = v.findViewById(R.id.back);

        // Load common elements of fragment
        bPeople = v.findViewById(R.id.bPeople);
        bPeople.setOnClickListener(oclTabSwitcher);
        bArticles = v.findViewById(R.id.bArticles);
        bArticles.setOnClickListener(oclTabSwitcher);

        // Load Info layout
        tvName = (TextView)v.findViewById(R.id.txtName);
        tvProvenance = (TextView)v.findViewById(R.id.txtProvenance);
        tvDegree = (TextView)v.findViewById(R.id.txtDegree);
        tvAffiliation = (TextView)v.findViewById(R.id.txtAffiliation);
        tvJob = (TextView)v.findViewById(R.id.txtJob);
        tvBio = (TextView)v.findViewById(R.id.txtBio);
        tvBioTitle = (TextView)v.findViewById(R.id.txtBioTitle);
        tvDetailsTitle = (TextView)v.findViewById(R.id.txtDetails);
        imgUser = (ImageView)v.findViewById(R.id.imgUser);
        bStar = (TextView)v.findViewById(R.id.bStar);
        bStar.setOnClickListener(oclStar);
        return v;
    }
}
