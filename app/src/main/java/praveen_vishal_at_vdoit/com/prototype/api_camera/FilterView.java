package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.graphics.SurfaceTexture;
import android.os.Handler;

/**
 * Created by admin on 14/10/2016.
 */
public class FilterView extends SurfaceTexture {




    public FilterView(int texName) {
        super(texName);
    }



    @Override
    public void setOnFrameAvailableListener(OnFrameAvailableListener listener) {
        super.setOnFrameAvailableListener(listener);

    }

    @Override
    public void setOnFrameAvailableListener(OnFrameAvailableListener listener, Handler handler) {
        super.setOnFrameAvailableListener(listener, handler);
    }

    @Override
    public void setDefaultBufferSize(int width, int height) {
        super.setDefaultBufferSize(width, height);
    }

    @Override
    public void updateTexImage() {
        super.updateTexImage();
    }

    @Override
    public void releaseTexImage() {
        super.releaseTexImage();
    }

    @Override
    public void detachFromGLContext() {
        super.detachFromGLContext();
    }

    @Override
    public void attachToGLContext(int texName) {
        super.attachToGLContext(texName);
    }

    @Override
    public void getTransformMatrix(float[] mtx) {
        super.getTransformMatrix(mtx);
    }

    @Override
    public long getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
