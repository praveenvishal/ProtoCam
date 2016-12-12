package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import praveen_vishal_at_vdoit.com.prototype.LauncherActivity;
import praveen_vishal_at_vdoit.com.prototype.R;

public class PreviewActivity extends Activity

{

    private Bitmap bitmap,rotated;
    private SubsamplingScaleImageView imageView;
    private int orientation;
    private static Cache cache;
    private ImageView preview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bitmap=Cache.getCache_bitmap();
        orientation = Cache.getOrientation();
        setContentView(R.layout.preview_container);
        preview = (ImageView)findViewById(R.id.preview_photo);
        load(bitmap,orientation);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void load(Bitmap bitmap,int orientation)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        if(orientation>100) {
            matrix.postRotate(180);
            matrix.postScale(-1, 1);
        }
        rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        preview.setImageBitmap(rotated);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PreviewActivity.this,LauncherActivity.class);
        startActivity(i);
    }
}
