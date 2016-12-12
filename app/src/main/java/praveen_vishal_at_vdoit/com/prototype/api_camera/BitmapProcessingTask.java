package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class BitmapProcessingTask extends AsyncTask<Void,Void,Bitmap> {


    private byte[] data;
    private int width ,height,orientation;
    private BitmapProcessedCallback bitmapProcessedCallback;

    public BitmapProcessingTask(byte[]data,int orientation,int height,int width,BitmapProcessedCallback bitmapProcessedCallback)

    {
        this.data=data;
        this.orientation=orientation;
        this.width=width;
        this.height=height;
        this.bitmapProcessedCallback=bitmapProcessedCallback;
    }

    @Override
    protected Bitmap doInBackground(Void... params)
    {

        return decodeSampledBitmapFromResource(data,width,height);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        super.onPostExecute(bitmap);
        bitmapProcessedCallback.processed(bitmap,orientation);


    }

    public static Bitmap decodeSampledBitmapFromResource(byte[]data, int reqWidth, int reqHeight)
    {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data,0,data.length,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data,0,data.length,options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
