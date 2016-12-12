package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SurfaceViewAdapter extends BaseAdapter {
    private Context mContext;

    // Constructor
    public SurfaceViewAdapter(Context c) {
        mContext = c;
    }


    @Override
    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        CameraView cameraView ;

        if (convertView == null) {
            cameraView = new CameraView(mContext);
            cameraView.setLayoutParams(new GridView.LayoutParams(85, 85));
           // cameraView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cameraView.setPadding(8, 8, 8, 8);
        }
        else
        {
            cameraView = (CameraView) convertView;
        }
       // imageView.setImageResource(mThumbIds[position]);
        return cameraView;
    }

    // Keep all Images in array

}