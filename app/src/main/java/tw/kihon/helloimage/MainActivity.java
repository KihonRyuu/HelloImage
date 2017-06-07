package tw.kihon.helloimage;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import tw.kihon.helloimage.adapter.ImageAdapter;
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
    private List<Api.Response.SearchImages.Hits> mData;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Utils.getDefaultDisplay(this);
        setSupportActionBar(mToolbar);

        configSearchView();
        configRecyclerView();
        setListeners();

        Api.RequestBody.SearchImages params = new Api.RequestBody.SearchImages();
        params.setQuery("Taiwan Street");
        ApiRequest.searchImages(params, new Api.Callback<Api.Response.SearchImages>() {
            @Override
            public void onResponse(Call<Api.Response.SearchImages> call, Response<Api.Response.SearchImages> response) {
                super.onResponse(call, response);
                if (response.body() == null || response.body().hits == null || response.body().hits.size() == 0) {
                    return;
                }
                mData = response.body().hits;
                mImageAdapter = new ImageAdapter(MainActivity.this, response.body().hits);
                mRecyclerView.setAdapter(mImageAdapter);
            }
        });
    }

    private void setListeners() {
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public synchronized void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setSharedElementEnterTransition(new ChangeImageTransform(MainActivity.this, null));
                }*/
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                intent.putExtra("data", mData.get(position));
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, "profile");
//                startActivity(intent, options.toBundle());
                startActivity(intent);

            }
        });
    }

    private void configRecyclerView() {
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        int spacing = (int) Utils.convertDpToPixel(6);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacing));
        mRecyclerView.setLayoutManager(createStaggeredGridLayoutManager());
    }

    private void configSearchView() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        MenuItem item = mMenu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_switch:
                if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    if (mMenu != null) {
                        mMenu.findItem(R.id.action_switch).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_view_compact_24dp));
                    }
                    mRecyclerView.setLayoutManager(createLinearLayoutManager());
                } else if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    if (mMenu != null) {
                        mMenu.findItem(R.id.action_switch).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_view_list_24dp));
                    }
                    mRecyclerView.setLayoutManager(createStaggeredGridLayoutManager());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView.LayoutManager createStaggeredGridLayoutManager() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        return layoutManager;
    }

    private RecyclerView.LayoutManager createLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
