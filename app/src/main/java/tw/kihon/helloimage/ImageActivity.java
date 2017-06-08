package tw.kihon.helloimage;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.kihon.helloimage.api.Api;

/**
 * Created by kihon on 2017/06/07.
 */

public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    SubsamplingScaleImageView mImageView;
    private Target mTarget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

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
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.with(this).load(imageData.webformatURL).into(mTarget);
    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(mTarget);
        super.onDestroy();
    }
}
