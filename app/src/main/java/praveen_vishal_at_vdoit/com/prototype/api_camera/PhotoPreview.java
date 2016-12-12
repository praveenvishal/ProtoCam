package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import praveen_vishal_at_vdoit.com.prototype.R;


public class PhotoPreview extends Fragment {

    private ImageView photo;

    public PhotoPreview()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_photo_preview2, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        photo = (ImageView)view.findViewById(R.id.photo_preview);

        Glide.with(getContext())
                .load(Cache.getData())
                .asBitmap()
                .into(photo);



    }
}
