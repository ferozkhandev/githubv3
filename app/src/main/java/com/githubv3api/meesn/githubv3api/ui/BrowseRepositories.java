package com.githubv3api.meesn.githubv3api.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.Adapter.OtherRecyclerAdapter;
import com.githubv3api.meesn.githubv3api.Adapter.UserRecyclerAdapter;
import com.githubv3api.meesn.githubv3api.HomePage;
import com.githubv3api.meesn.githubv3api.InternetCheck;
import com.githubv3api.meesn.githubv3api.Login;
import com.githubv3api.meesn.githubv3api.R;
import com.githubv3api.meesn.githubv3api.SplashActivity;
import com.githubv3api.meesn.githubv3api.database.Repository;
import com.githubv3api.meesn.githubv3api.model.OtherUsers;
import com.githubv3api.meesn.githubv3api.model.User;
import com.githubv3api.meesn.githubv3api.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

public class BrowseRepositories extends Fragment {
    private RecyclerView recyclerView;
    private String baseUrl = "https://api.github.com";
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrolledOutItems;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private ImageView imageView;
    private AppViewModel appViewModel;
    private Executor executor = Executors.newSingleThreadExecutor();
    final OtherRecyclerAdapter otherRecyclerAdapter = new OtherRecyclerAdapter();
    private List<OtherUsers> loadedUsers;
    private String username;
    private InternetCheck internetCheck;
    private TextView noDataTitle, noDataDescription;
    private boolean isLoaded=false;
    private int i = 0;

    private OnFragmentInteractionListener mListener;

    public BrowseRepositories() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_my_repositories, container, false);

        // Inflate the layout for this fragment
        linearLayoutManager = new LinearLayoutManager(getContext());
        progressBar = rootView.findViewById(R.id.loadmore1);
        imageView = rootView.findViewById(R.id.bin1);
        internetCheck = new InternetCheck(rootView.getContext());

        noDataTitle = rootView.findViewById(R.id.noDataTitle);
        noDataDescription = rootView.findViewById(R.id.noDataDescription);

        //Recycler View
        recyclerView = rootView.findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(otherRecyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    //Data Fetch
                    isScrolling = false;
                    if (loadedUsers!=null && !loadedUsers.isEmpty())
                    {
                        if (i<loadedUsers.size()-1)
                        {
                            i++;
                            loadData(rootView);
                        }
                    }
                    fetchData();
                    Log.d("Fetch", "Fetching");
                }
            }
        });

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appViewModel.loadUsers(getContext());
        appViewModel.getLoadedUsers().observe(this, new Observer<List<OtherUsers>>() {
            @Override
            public void onChanged(@Nullable List<OtherUsers> otherUsers) {
                loadedUsers = otherUsers;
                if (loadedUsers!=null)
                {
                    isLoaded = true;
                }
            }
        });
        loadData(rootView);
        //checkLoaded(rootView);
        return rootView;
    }


    private synchronized void checkLoaded(final View rootView)
    {
        if (isLoaded)
        {
            loadData(rootView);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData(rootView);
                }}, 4000);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private synchronized void loadData(final View rootView) {
        if (loadedUsers != null && !loadedUsers.isEmpty()) {
            username = loadedUsers.get(i).getLogin();

        } else {
            username = "mojombo";
        }
        Context context = getContext();
        appViewModel.loadRepositories(username, context);
        if (appViewModel.getRepositories() != null) {
            appViewModel.getRepositories("other").observe(this, new Observer<List<Repository>>() {
                @Override
                public void onChanged(@Nullable List<Repository> repositories) {
                    if (repositories != null && !repositories.isEmpty()) {
                        Log.d("DataloadedCheck", "Loaded but not displayed");
                        Log.d(TAG, "onChanged:data " + repositories.get(0).getName());
                        recyclerView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        otherRecyclerAdapter.setUsers(repositories);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        Log.d("DataloadedCheck", "null");
                        isNetIssue(rootView);
                    }
                }
            });
        } else {
            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Log.d("DataloadedCheck", "failed");
            isNetIssue(rootView);
        }
    }

    private void isNetIssue(final View rootView)
    {
        if (!internetCheck.netCheck())
        {
            Snackbar snackbar = Snackbar
                    .make(rootView, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isNetIssue(v);
                        }
                    });
            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    isNetIssue(rootView);

                }}, 5000);
        }
        else
        {
            loadData(rootView);
        }
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
