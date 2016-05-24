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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import airsign.loopAPI.AsyncServerRequest;
import airsign.loopAPI.AsyncServerRequest.AsyncServerResponseListener;
import airsign.loopAPI.LoopServices;
import airsign.loopAPI.loopAPI.data.User;
import airsign.loopAPI.loopAPI.data.UserList;
import airsign.loopAPI.loopAPI.parsers.UserParser;
import airsign.styles.Themes;
import airsign.utils.ImgLoaderFactory;
import airsign.views.CustomFragment;
import loop.airsign.loop.R;

public class UserContactsFragment extends CustomFragment
{
    public static final String kFragmentID = "UserContacts";
    private String userID = null;
    private boolean isMe;
    private final int
        kFollowers = 1,
        kFollowing = 2,
        kCoauthors = 3;
    private View peopleSeparator;
    private ViewGroup layoutTitlePeople, layoutSelectionPeople;
    private ListView lvPeople;
    private PeopleAdapter peopleAdapter;
    private ArrayList<User> peopleList, followersList, followingList, coauthorsList;
    private int followersCount, followingCount, coauthorsCount;
    private TextView tvPeople, tvFollowing, tvFollowers, tvCoauthors;
    private View bInfo, bArticles;
    private ImageView imgDropdownPeople;
    private ImageLoader imgLoader;
    private int showing;

    private View.OnClickListener
        oclTabSwitcher = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle b = new Bundle();
                b.putBoolean(kPopStack, true);
                if (view==bInfo) { playSound(); sendMessage(b); }
                else if (view==bArticles)
                {
                    playSound();
                    b.putString(kID, userID);
                    b.putString(kInfo, UserArticlesFragment.kFragmentID);
                    b.putInt(kComingFrom, UserArticlesFragment.kUser);
                    sendMessage(b);
                }
            }
        },

        oclPeopleSelection = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playSound();
                showing = kFollowers;
                if (view==tvFollowing) showing = kFollowing;
                else if (view==tvCoauthors) showing = kCoauthors;
                loadTargetGui();
            }
        },

        oclDropdownPeople = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playSound();
                layoutTitlePeople.setVisibility(View.GONE);
                layoutSelectionPeople.setVisibility(View.VISIBLE);
            }
        };

    private AdapterView.OnItemClickListener oiclPeople = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l)
        {
            playSound();
            User user = peopleList.get(pos);
            if (!prefs.getUserID().equals(user.id)) // Don't load our user again
            {
                Bundle b = new Bundle();
                b.putString(kInfo, UserInfoFragment.kFragmentID);
                b.putString(CustomFragment.kID, user.id);
                sendMessage(b);
            }
        }
    };

    private AsyncServerResponseListener
        onUserListReceived = new AsyncServerResponseListener()
        {
            @Override
            public void onReceivedData(String res)
            {
                UserList ul = UserParser.parseUsers(res);
                switch(showing)
                {
                    case kFollowers:
                    {
                        followersList = ul.list;
                        followersCount = ul.count;
                        showFollowers();
                        break;
                    }
                    case kFollowing:
                    {
                        followingList = ul.list;
                        followingCount = ul.count;
                        showFollowing();
                        if (isMe) storeFollowing();
                        break;
                    }
                    case kCoauthors:
                    {
                        coauthorsList = ul.list;
                        coauthorsCount = ul.count;
                        showCoauthors();
                        break;
                    }
                }
                hideLoading();
            }
            @Override  public void onError() { hideLoading(); sessionExpired(); }
        };

    public UserContactsFragment() {}
    public static UserContactsFragment create() { return new UserContactsFragment(); }

    private void storeFollowing()
    {
        String ids = "";
        for (User u:peopleList) ids += "," + u.id;
        prefs.saveFollowing(ids);
    }

    private void requestFollowers()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onUserListReceived);
        if (isMe) request.execute(LoopServices.getMyFollowers(auth));
        else request.execute(LoopServices.getUserFollowers(auth, userID));
    }

    private void requestFollows()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onUserListReceived);
        if (isMe) request.execute(LoopServices.getMyFollows(auth));
        else request.execute(LoopServices.getUserFollows(auth, userID));
    }

    private void requestCoauthors()
    {
        showLoading();
        Context ctx = getActivity().getApplicationContext();
        String auth = prefs.getAuth();
        AsyncServerRequest request = new AsyncServerRequest(ctx, onUserListReceived);
        if (isMe) request.execute(LoopServices.getMyCoauthors(auth));
        else request.execute(LoopServices.getUserCoauthors(auth, userID));
    }

    private void showFollowers()
    {
        tvFollowers.setSelected(true);
        tvFollowing.setSelected(false);
        tvCoauthors.setSelected(false);
        if (followersList==null) requestFollowers();
        else
        {
            peopleList.clear();
            peopleList.addAll(followersList);
            peopleAdapter = new PeopleAdapter(getActivity(), peopleList);
            lvPeople.setAdapter(peopleAdapter);
            String title = getString(R.string.followers) + "  -  " + followersCount;
            tvPeople.setText(title);
        }
    }

    private void showFollowing()
    {
        tvFollowers.setSelected(false);
        tvFollowing.setSelected(true);
        tvCoauthors.setSelected(false);
        if (followingList == null) requestFollows();
        else
        {
            peopleList.clear();
            peopleList.addAll(followingList);
            peopleAdapter = new PeopleAdapter(getActivity(), peopleList);
            lvPeople.setAdapter(peopleAdapter);
            String title = getString(R.string.following) + "  -  " + followingCount;
            tvPeople.setText(title);
        }
    }

    private void showCoauthors()
    {
        tvFollowers.setSelected(false);
        tvFollowing.setSelected(false);
        tvCoauthors.setSelected(true);
        if (coauthorsList==null) requestCoauthors();
        else
        {
            peopleList.clear();
            peopleList.addAll(coauthorsList);
            peopleAdapter = new PeopleAdapter(getActivity(), peopleList);
            lvPeople.setAdapter(peopleAdapter);
            String title = getString(R.string.coauthors) + "  -  " + coauthorsCount;
            tvPeople.setText(title);
        }
    }

    private void loadTargetGui()
    {
        layoutTitlePeople.setVisibility(View.VISIBLE);
        layoutSelectionPeople.setVisibility(View.GONE);
        switch(showing)
        {
            case kFollowers: showFollowers(); break;
            case kFollowing: showFollowing(); break;
            case kCoauthors: showCoauthors(); break;
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (isMe)
        {
            changeActivityTitle(R.string.me);
            followingList = null;
        }
        else changeActivityTitle(R.string.user_profile);
        setBackgroundStyle();
        setFontColors();
        loadTargetGui();
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        userID = b.getString(kID);
        isMe = prefs.getUserID().equals(userID);
        showing = kFollowing;
	}

    private void setFontColors()
    {
        int color = prefs.getStyle();
        if (color==Themes.kWhite)
        {
            tvPeople.setTextColor(Color.BLACK);
            peopleSeparator.setBackgroundColor(getResources().getColor(R.color.grey_dark_translucent));
            tvFollowing.setTextColor(Color.BLACK);
            tvFollowing.setBackgroundResource(R.drawable.button_drafted_black);
            tvFollowers.setTextColor(Color.BLACK);
            tvFollowers.setBackgroundResource(R.drawable.button_drafted_black);
            tvCoauthors.setTextColor(Color.BLACK);
            tvCoauthors.setBackgroundResource(R.drawable.button_drafted_black);
            imgDropdownPeople.setImageResource(R.drawable.bdropdown);
            lvPeople.setDivider(new ColorDrawable(getResources().getColor(R.color.grey_dark_translucent)));
            lvPeople.setDividerHeight(1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle b)
    {
        imgLoader = ImgLoaderFactory.getInstance(getActivity().getApplicationContext());
        ViewGroup v = (ViewGroup)li.inflate(R.layout.user_contacts_fragment, container, false);
        back = v.findViewById(R.id.back);
        // Load common elements of fragment
        bInfo = v.findViewById(R.id.bInfo);
        bInfo.setOnClickListener(oclTabSwitcher);
        bArticles = v.findViewById(R.id.bArticles);
        bArticles.setOnClickListener(oclTabSwitcher);

        // Load People layout
        layoutTitlePeople = (ViewGroup)v.findViewById(R.id.layoutTitlePeople);
        layoutSelectionPeople = (ViewGroup)v.findViewById(R.id.layoutSelectionPeople);
        tvPeople = (TextView)v.findViewById(R.id.txtPeople);
        tvFollowing = (TextView)v.findViewById(R.id.txtFollowing);
        tvFollowing.setOnClickListener(oclPeopleSelection);
        tvFollowers = (TextView)v.findViewById(R.id.txtFollowers);
        tvFollowers.setOnClickListener(oclPeopleSelection);
        tvCoauthors = (TextView)v.findViewById(R.id.txtCoauthors);
        tvCoauthors.setOnClickListener(oclPeopleSelection);
        imgDropdownPeople = (ImageView)v.findViewById(R.id.imgDropdownPeople);
        imgDropdownPeople.setOnClickListener(oclDropdownPeople);
        lvPeople = (ListView)v.findViewById(R.id.listPeople);
        lvPeople.setOnItemClickListener(oiclPeople);
        peopleSeparator = v.findViewById(R.id.sepPeople);
        peopleList = new ArrayList<User>();

        return v;
    }

    private class PeopleAdapter extends ArrayAdapter<User>
    {
        private static final int layoutID = R.layout.row_people;
        private LayoutInflater li;

        public PeopleAdapter(Context ctx, ArrayList<User> list)
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
            final ImageView imgUser = (ImageView)v.findViewById(R.id.imgUser);
            TextView
                h1 = (TextView)v.findViewById(R.id.tvName),
                h2 = (TextView)v.findViewById(R.id.tvAffiliation);
            User user = peopleList.get(pos);
            if (!imgLoader.isInited()) imgLoader = ImgLoaderFactory.getInstance(getActivity().getApplicationContext());
            imgLoader.displayImage(user.imgUrl, imgUser);
            if (prefs.getStyle()==Themes.kWhite)
            {
                h1.setTextColor(Color.BLACK);
                h2.setTextColor(Color.BLACK);
            }
            h1.setText(user.getName());
            h2.setText(user.affiliation);
            return v;
        }
    }

}
