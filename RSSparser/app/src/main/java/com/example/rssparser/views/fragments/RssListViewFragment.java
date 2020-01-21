package com.example.rssparser.views.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rssparser.R;
import com.example.rssparser.models.entities.Feed;
import com.example.rssparser.services.NetworkIntentService;
import com.example.rssparser.services.RssParserService;
import com.example.rssparser.viewModels.FeedViewModel;
import com.example.rssparser.views.adapters.RssListViewAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RssListViewFragment extends Fragment {
    private SearchView searchView;
    private TextView networkTextView;
    private ProgressBar progressBar;

    private static FeedViewModel feedViewModel;
    private GetRssFeedTask task;

    public RssListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rss_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        networkTextView = view.findViewById(R.id.internet);
        searchView = view.findViewById(R.id.search);
        searchView.setQuery("https://www.wired.com/feed/category/gear/latest/rss",
                false);
        progressBar = view.findViewById(R.id.progressBar);

        Handler handler = new Handler((Message msg) -> {
            Bundle data = msg.getData();
            boolean isConnected = data.getBoolean("isConnected");
            if(isConnected)
                networkTextView.setText("Connected");
            else
                networkTextView.setText("Disconnected");
            return true;
        });

        Intent intent = new Intent(getActivity(), NetworkIntentService.class);
        intent.putExtra("messenger", new Messenger(handler));
        getActivity().startService(intent);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        RssListViewAdapter adapter = new RssListViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        feedViewModel = new ViewModelProvider(getActivity()).get(FeedViewModel.class);
        feedViewModel.isLoading().observe(getViewLifecycleOwner(), (Boolean isLoading) -> {
            if(isLoading) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        feedViewModel.getAll().observe(getViewLifecycleOwner(), (List<Feed> feed) ->
                adapter.setFeed(feed));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isNetworkAvailable(getContext())) {
                    task = new GetRssFeedTask(getContext());
                    task.execute(query);
                }
                else {
                    List<Feed> feeds = feedViewModel.selectAll(query);
                    feedViewModel.setAllFeed(feedViewModel.selectAll(query));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private static class GetRssFeedTask extends AsyncTask<String, Void, AsyncTaskResult<Boolean>> {
        private List<Feed> allFeed = new ArrayList<>();
        private String feedUrl;
        private Context context;

        public GetRssFeedTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            feedViewModel.setIsLoading(true);
        }

        @Override
        protected AsyncTaskResult<Boolean> doInBackground(String... params) {
            feedUrl = params[0];
            Boolean flag = false;
            try {
                RssParserService.ParseRss(allFeed, feedUrl);
                flag = true;
                return new AsyncTaskResult<Boolean>(flag);
            }
            catch (ParseException e) {
                return new AsyncTaskResult<Boolean>(e);
            } catch (XmlPullParserException e) {
                return new AsyncTaskResult<Boolean>(e);
            } catch (IOException e) {
                return new AsyncTaskResult<Boolean>(e);
            }
            catch (Exception e) {
                return new AsyncTaskResult<Boolean>(e);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<Boolean> result) {
            super.onPostExecute(result);

            if (result.getError() != null) {
                feedViewModel.setIsLoading(false);
                Toast toast = Toast.makeText(context, result.getError().toString(), Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            feedViewModel.setAllFeed(allFeed);
            feedViewModel.deleteAll(feedUrl);

            int i = 1;
            for(Feed feed: allFeed) {
                feedViewModel.saveFeed(feed);
                if(i == RssParserService.MAX_CACHED_FEED) {
                    break;
                }
                i++;
            }

            feedViewModel.setIsLoading(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(task != null) {
            task.cancel(false);
        }
        Intent service = new Intent(getActivity(), NetworkIntentService.class);
        getActivity().stopService(service);
    }


}
