package tw.kihon.helloimage;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import tw.kihon.helloimage.api.Api;
import tw.kihon.helloimage.api.ApiRequest;
import tw.kihon.helloimage.util.Utils;

public class MainActivity extends AppCompatActivity
        implements MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    private ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Utils.getDefaultDisplay(this);

        setSupportActionBar(mToolbar);
//        mToolbar.setTitleTextColor(Color.WHITE);

        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        int spacing = (int) Utils.convertDpToPixel(6);
//        mRecyclerView.addItemDecoration(new SpacingDecorator(spacing, spacing, true));
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);

        Api.RequestBody.SearchImages params = new Api.RequestBody.SearchImages();
        params.setQuery("Taiwan Street");
        ApiRequest.searchImages(params, new Api.Callback<Api.Response.SearchImages>() {
            @Override
            public void onResponse(Call<Api.Response.SearchImages> call, Response<Api.Response.SearchImages> response) {
                super.onResponse(call, response);
                if (response.body() == null || response.body().hits == null || response.body().hits.size() == 0) {
                    return;
                }
                mImageAdapter = new ImageAdapter(MainActivity.this, response.body().hits);
                mRecyclerView.setAdapter(mImageAdapter);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {

    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
