package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.graphics.Bitmap;

/**
 * Created by admin on 23/09/2016.
 */
public class Cache

{

    private static Bitmap cache_bitmap;

    public static byte[] getData() {
        return data;
    }

    public static void setData(byte[] data) {
        Cache.data = data;
    }

    private static byte[] data;

    public static int getOrientation() {
        return orientation;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Cache.height = height;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Cache.width = width;
    }

    public static int width = 0;
    public static int height = 0;


    public static void setOrientation(int orientation) {
        Cache.orientation = orientation;
    }

    private static int orientation;


    public static String getCache_file_name() {
        return cache_file_name;
    }

    public static void setCache_file_name(String cache_file_name) {
        Cache.cache_file_name = cache_file_name;
    }

    private static String cache_file_name=null;

    public static Bitmap getCache_bitmap() {
        return cache_bitmap;
    }

    public static void setCache_bitmap(Bitmap cache_bitmap) {
        Cache.cache_bitmap = cache_bitmap;
    }




}
