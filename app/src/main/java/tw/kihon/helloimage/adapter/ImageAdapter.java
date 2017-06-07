package tw.kihon.helloimage.adapter;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.kihon.helloimage.R;
import tw.kihon.helloimage.api.Api;
import tw.kihon.helloimage.util.Utils;

/**
 * Created by kihon on 2017/06/07.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final WeakReference<Context> mContext;
    private final LayoutInflater mLayoutInflater;
    private List<Api.Response.SearchImages.Hits> mData;
    private RecyclerView mRecyclerView;

    public ImageAdapter(Context context, List<Api.Response.SearchImages.Hits> data) {
        mContext = new WeakReference<>(context);
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.list_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            final ItemViewHolder imageViewHolder = (ItemViewHolder) viewHolder;
            Api.Response.SearchImages.Hits item = mData.get(position);
            int displayWidth = Utils.displayWidth;
            if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                displayWidth = (int) (Utils.displayWidth / 2 - Utils.convertDpToPixel(6f));
            }
            double aspectRatio = (item.previewHeight) / (item.previewWidth * 1.0);
            int displayHeight = (int) (displayWidth * aspectRatio);
            imageViewHolder.imageView.getLayoutParams().height = displayHeight;
            imageViewHolder.imageView.getLayoutParams().width = displayWidth;
            Picasso.with(mContext.get()).load(item.previewURL).into(imageViewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
