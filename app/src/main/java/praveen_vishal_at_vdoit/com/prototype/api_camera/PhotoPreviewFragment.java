
package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import praveen_vishal_at_vdoit.com.prototype.R;


public class PhotoPreviewFragment extends Fragment  {

    private Bitmap bitmap,rotated;
    private SubsamplingScaleImageView imageView;
    private int orientation;
    private ImageButton time_picker,send_button,picture_save_pic;
    private static final String IMG_PREFIX = "IMG_";
    private static final String IMG_POSTFIX = ".jpg";
    private static final String TIME_FORMAT = "yyyyMMdd_HHmmss";
    private static Cache cache;
    private int height;
    private int width;


    public static PhotoPreviewFragment newInstance(Bitmap bitmap,int orientation)
    {
        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.bitmap = bitmap;
        fragment.orientation=orientation;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("I m in PhotoFragment","Yes");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_photo_preview, container, false);


    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        imageView = (SubsamplingScaleImageView) view.findViewById(R.id.photo);

        load();


    }



    public void load()
    {

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        if(orientation>90)
        {
        matrix.postScale(-1, 1);
        }

        rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        ImageSource imagesource = ImageSource.bitmap(rotated);
        imageView.setImage(imagesource);


    }


}
