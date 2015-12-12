package com.ahs.udacity.popularmovies.activity.listener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.ahs.udacity.popularmovies.network.MoviesService;
import com.ahs.udacity.popularmovies.service.DownloadService;

/**
 * Created by shetty on 08/12/15.
 */
public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = EndlessScrollListener.class.getName();
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleItemCount;
    private StaggeredGridLayoutManager mLayoutManager;
    private int totalItemCount;
    private int[] pastVisiblesItems;
    private int[] firstVisibleItem = null;
    private LoadMore loadMore;
    private int count=1;

    public EndlessScrollListener(StaggeredGridLayoutManager layoutManager,LoadMore loadMore,int visibleThreshold,int span_count) {
        this.mLayoutManager = layoutManager;
        this.loadMore = loadMore;
        this.visibleThreshold = visibleThreshold;
        this.firstVisibleItem = new int[span_count];
    }
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        if(totalItemCount<20)
            count =1;
        else {
            count = totalItemCount / 20 + 1;
        }
        mLayoutManager.findFirstVisibleItemPositions(firstVisibleItem);
        Log.d(TAG, "visibleItemCount:" + visibleItemCount + " totalItemCount:" + totalItemCount + " previousItemCount:" + previousTotal + " totalItemCount-visibleItemCount:" + (totalItemCount - visibleItemCount) + " (firstVisibleItem[0] + visibleThreshold):" + (firstVisibleItem[0] + visibleThreshold));
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                loadMore.stopLoading();
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem[0] + visibleThreshold)) {
            // End has been reached

            if(loadMore!=null) {
                loadMore.loadMore(count);
            }

            loadMore.loading();
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
    }

    public interface LoadMore{
        void loadMore(int count);
        void loading();

        void stopLoading();
    }



}
