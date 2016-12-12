
package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.graphics.Matrix;
import android.view.TextureView;

import java.util.Set;

abstract class CameraViewImpl {

    protected final Callback mCallback;

    public CameraViewImpl(Callback callback) {
        mCallback = callback;
    }

    abstract TextureView.SurfaceTextureListener getSurfaceTextureListener();

    abstract void start();

    abstract void stop();
    //abstract void release_camera();

    abstract boolean isCameraOpened();

    abstract void setFacing(int facing);

    abstract int getFacing();

    abstract Set<AspectRatio> getSupportedAspectRatios();

    abstract void setAspectRatio(AspectRatio ratio);

    abstract AspectRatio getAspectRatio();





    abstract void setFlash(int flash);

    abstract int getFlash();

    abstract void takePicture();

    abstract void setDisplayOrientation(int displayOrientation);

    interface Callback {

        void onCameraOpened();

        void onCameraClosed();

        void onPictureTaken(byte[] data);

        void onTransformUpdated(Matrix matrix);

    }

}
