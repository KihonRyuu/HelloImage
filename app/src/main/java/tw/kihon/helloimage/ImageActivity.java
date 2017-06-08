package tw.kihon.helloimage;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.klinker.android.sliding.SlidingActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.kihon.helloimage.api.Api;
import tw.kihon.helloimage.widget.ProgressController;

/**
 * Created by kihon on 2017/06/07.
 */

public class ImageActivity extends SlidingActivity {

    @BindView(R.id.main)
    ViewGroup mMainLayout;
    @BindView(R.id.imageView)
    SubsamplingScaleImageView mImageView;

    private Target mTarget;
    private ProgressController mProgressController;

    @Override
    public void init(Bundle bundle) {
        setContent(R.layout.activity_image);
        ButterKnife.bind(this);
        disableHeader();
        enableFullscreen();
        View progressLayout = LayoutInflater.from(this).inflate(R.layout.progress_view, mMainLayout, false);
        mMainLayout.addView(progressLayout);
        View progressView = progressLayout.findViewById(R.id.progressView);
        mProgressController = new ProgressController(mMainLayout, progressView);

        if (getIntent() == null || getIntent().getParcelableExtra("data") == null) {
            Toast.makeText(this, R.string.image_err_msg, Toast.LENGTH_SHORT).show();
            finish();
        }

        Api.Response.SearchImages.Hits imageData = getIntent().getParcelableExtra("data");
        mImageView.setMaxScale(10f);
        mImageView.setDoubleTapZoomScale(5f);
        mImageView.setDoubleTapZoomDuration(250);
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mImageView.setImage(ImageSource.bitmap(bitmap));
                mProgressController.setLoading(false);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mProgressController.setLoading(true);
            }
        };
        Picasso.with(this).load(imageData.largeImageURL).priority(Picasso.Priority.HIGH).into(mTarget);
    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(mTarget);
        super.onDestroy();
    }
}
