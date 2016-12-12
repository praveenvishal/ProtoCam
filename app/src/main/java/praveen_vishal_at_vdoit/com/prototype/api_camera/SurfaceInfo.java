
package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.graphics.SurfaceTexture;


/**
 * Stores information about the {@link SurfaceTexture} showing camera preview.
 */
class SurfaceInfo {

    SurfaceTexture surface;
    int width;
    int height;

    void configure(SurfaceTexture s, int w, int h) {
        surface = s;
        width = w;
        height = h;
    }

}
