
package praveen_vishal_at_vdoit.com.prototype.api_camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.support.v4.util.SparseArrayCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@SuppressWarnings("deprecation")
class Camera1 extends CameraViewImpl{

    private static final int INVALID_CAMERA_ID = -1;

    private static final SparseArrayCompat<String> FLASH_MODES = new SparseArrayCompat<>();

    static {
        FLASH_MODES.put(Constants.FLASH_OFF, Camera.Parameters.FLASH_MODE_OFF);
        FLASH_MODES.put(Constants.FLASH_ON, Camera.Parameters.FLASH_MODE_ON);
        FLASH_MODES.put(Constants.FLASH_TORCH, Camera.Parameters.FLASH_MODE_TORCH);
        FLASH_MODES.put(Constants.FLASH_AUTO, Camera.Parameters.FLASH_MODE_AUTO);
        FLASH_MODES.put(Constants.FLASH_RED_EYE, Camera.Parameters.FLASH_MODE_RED_EYE);
    }

    private int mCameraId;

    private Camera mCamera;

    private Camera.Parameters mCameraParameters;

    private final Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();

    private final SurfaceInfo mSurfaceInfo = new SurfaceInfo();

    private final SizeMap mPreviewSizes = new SizeMap();

    private final SizeMap mPictureSizes = new SizeMap();

    private AspectRatio mAspectRatio;

    private boolean mShowingPreview;

    private boolean mAutoFocus;

    private int mFacing;

    private int mFlash;

    private int mDisplayOrientation;

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        private void reconfigurePreview(SurfaceTexture surface, int width, int height) {
            mSurfaceInfo.configure(surface, width, height);

            if (mCamera != null) {




                setUpPreview();

                adjustCameraParameters();
            }
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
        {
            reconfigurePreview(surface, width, height);


        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
        {
            reconfigurePreview(surface, width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
        {
            releaseCamera(); // Safe guard
            return true;
        }


        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface)
        {
        }


    };

    public Camera1(Callback callback) {
        super(callback);
    }

    @Override
    TextureView.SurfaceTextureListener getSurfaceTextureListener()
    {
        return mSurfaceTextureListener;
    }

    @Override
    void start() {
        chooseCamera();
        openCamera();
        if (mSurfaceInfo.surface != null) {

            setUpPreview();
        }
        mShowingPreview = true;
        mCamera.startPreview();
    }

    @Override
    void stop() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        mShowingPreview = false;
        releaseCamera();
    }


    private void setUpPreview() {
        try
        {
            byte[] buffer;
            buffer = previewBuffer();
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(previewCallback);
            mCamera.stopPreview();
            mCamera.setPreviewTexture(mSurfaceInfo.surface);

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.d("Function", "onPreviewFrame iniciado");
            //Convert to jpg
            Camera.Size previewSize = camera.getParameters().getPreviewSize();
            Log.d("Function", "onPreviewFrame: preview size=" + previewSize.height + " " + previewSize.width);
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);
            byte jpgData[] = baos.toByteArray();
            mCamera.addCallbackBuffer(previewBuffer());
        }
    };
    private byte[] previewBuffer() {
        Log.d("Function", "previewBuffer iniciado");
        int bufferSize;
        byte buffer[];
        int bitsPerPixel;

        Camera.Parameters mParams = mCamera.getParameters();
        Camera.Size mSize = mParams.getPreviewSize();
        Log.d("Function", "previewBuffer: preview size=" + mSize.height + " " + mSize.width);
        int mImageFormat = mParams.getPreviewFormat();

        if (mImageFormat == ImageFormat.YV12) {
            int yStride = (int) Math.ceil(mSize.width / 16.0) * 16;
            int uvStride = (int) Math.ceil((yStride / 2) / 16.0) * 16;
            int ySize = yStride * mSize.height;
            int uvSize = uvStride * mSize.height / 2;
            bufferSize = ySize + uvSize * 2;
            buffer = new byte[bufferSize];
            Log.d("Function", "previewBuffer: buffer size=" + Integer.toString(bufferSize));
            return buffer;
        }

        bitsPerPixel = ImageFormat.getBitsPerPixel(mImageFormat);
        bufferSize = (int) (mSize.height * mSize.width * ((bitsPerPixel / (float) 8)));
        buffer = new byte[bufferSize];
        Log.d("Function", "previewBuffer: buffer size=" + Integer.toString(bufferSize));
        return buffer;
    }
    @Override
    boolean isCameraOpened() {
        return mCamera != null;
    }

    @Override
    void setFacing(int facing) {
        if (mFacing == facing) {
            return;
        }
        mFacing = facing;
        if (isCameraOpened()) {
            stop();
            start();
        }
    }
 
    @Override
    int getFacing() {
        return mFacing;
    }

    @Override
    Set<AspectRatio> getSupportedAspectRatios() {
        return mPreviewSizes.ratios();
    }

    @Override
    void setAspectRatio(AspectRatio ratio) {
        if (mAspectRatio == null || !isCameraOpened()) {
            // Handle this later when camera is opened
            mAspectRatio = ratio;
        } else if (!mAspectRatio.equals(ratio)) {
            final Set<Size> sizes = mPreviewSizes.sizes(ratio);
            if (sizes == null) {
                throw new UnsupportedOperationException(ratio + " is not supported");
            } else {
                mAspectRatio = ratio;
                adjustCameraParameters();
            }
        }
    }

    @Override
    AspectRatio getAspectRatio() {
        return mAspectRatio;
    }



    @Override
    void setFlash(int flash) {
        if (flash == mFlash) {
            return;
        }
        if (setFlashInternal(flash)) {
            mCamera.setParameters(mCameraParameters);
        }
    }

    @Override
    int getFlash() {
        return mFlash;
    }

    @Override
    void takePicture() {
        if (!isCameraOpened()) {
            throw new IllegalStateException("Camera is not ready. Call start() before takePicture().");
        }
          takePictureInternal();
    }

    private void takePictureInternal() {
        mCamera.takePicture(null, null, null, new Camera.PictureCallback() {


            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mCallback.onPictureTaken(data);
                camera.startPreview();
            }
        });
    }

    @Override
    void setDisplayOrientation(int displayOrientation) {
        mDisplayOrientation = displayOrientation;
        if (isCameraOpened()) {
            int cameraRotation = calcCameraRotation(displayOrientation);
            mCameraParameters.setRotation(cameraRotation);
            mCamera.setParameters(mCameraParameters);
            mCamera.setDisplayOrientation(cameraRotation);
        }
    }

    /**
     * This rewrites {@link #mCameraId} and {@link #mCameraInfo}.
     */
    private void chooseCamera() {
        for (int i = 0, count = Camera.getNumberOfCameras(); i < count; i++) {
            Camera.getCameraInfo(i, mCameraInfo);
            if (mCameraInfo.facing == mFacing) {

                mCameraId = i;
                return;
            }
        }
        mCameraId = INVALID_CAMERA_ID;
    }

    private void openCamera() {
        if (mCamera != null) {
            releaseCamera();
        }
        mCamera = Camera.open(mCameraId);
        mCameraParameters = mCamera.getParameters();
        // Supported preview sizes
        mPreviewSizes.clear();
        for (Camera.Size size : mCameraParameters.getSupportedPreviewSizes()) {
            mPreviewSizes.add(new Size(size.width, size.height));
        }
        // Supported picture sizes;
        mPictureSizes.clear();
        for (Camera.Size size : mCameraParameters.getSupportedPictureSizes()) {
            mPictureSizes.add(new Size(size.width, size.height));
        }
        // AspectRatio
        if (mAspectRatio == null) {
            mAspectRatio = Constants.DEFAULT_ASPECT_RATIO;
        }
        adjustCameraParameters();
        mCamera.setDisplayOrientation(calcCameraRotation(mDisplayOrientation));
        mCallback.onCameraOpened();
    }

    private AspectRatio chooseAspectRatio() {
        AspectRatio r = null;
        for (AspectRatio ratio : mPreviewSizes.ratios()) {
            r = ratio;
            if (ratio.equals(Constants.DEFAULT_ASPECT_RATIO)) {
                return ratio;
            }
        }
        return Constants.DEFAULT_ASPECT_RATIO;
    }
    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    private void adjustCameraParameters() {
        final SortedSet<Size> sizes = mPreviewSizes.sizes(mAspectRatio);
        if (sizes == null) { // Not supported
            mAspectRatio = Constants.DEFAULT_ASPECT_RATIO;
        }
        Size size = chooseOptimalSize(sizes);
        final Camera.Size currentSize = mCameraParameters.getPictureSize();
        if (currentSize.width != size.getWidth() || currentSize.height != size.getHeight()) {
            // Largest picture size in this ratio
            final Size pictureSize = mPictureSizes.sizes(mAspectRatio).last();
            if (mShowingPreview) {
                mCamera.stopPreview();
            }
            List<Camera.Size> sizes_changed = mCameraParameters.getSupportedPreviewSizes();
            Camera.Size optimal_size = optimal_preview_size(sizes_changed, Cache.getHeight(),Cache.getWidth());

            mCameraParameters.setPreviewSize(optimal_size.width, optimal_size.height);
            mCameraParameters.setPictureSize(optimal_size.width, optimal_size.height);
            mCameraParameters.setRotation(calcCameraRotation(mDisplayOrientation));
            setAutoFocusInternal(mAutoFocus);
            setFlashInternal(mFlash);
            mCamera.setParameters(mCameraParameters);
            if (mShowingPreview) {
                mCamera.startPreview();
            }
        }
    }


    private Camera.Size optimal_preview_size(List<Camera.Size> sizes, int w, int h)
    {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;

        if(sizes == null)
            return null;

        Camera.Size optimal_size = null;

        double min_diff = Double.MAX_VALUE;

        int target_height = h;

        for(Camera.Size size : sizes)
        {
            double ratio = (double) size.width / size.height;
            if(Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if(Math.abs(size.height - target_height) < min_diff)
            {
                optimal_size = size;
                min_diff = Math.abs(size.height - target_height);
            }
        }
        if(optimal_size == null)
        {
            min_diff = Double.MAX_VALUE;
            for(Camera.Size size : sizes)
            {
                if(Math.abs(size.height - target_height) < min_diff)
                {
                    optimal_size = size;
                    min_diff = Math.abs(size.height - target_height);
                }
            }
        }
        return optimal_size;
    }
    @SuppressWarnings("SuspiciousNameCombination")
    private Size chooseOptimalSize(SortedSet<Size> sizes) {
        if (mSurfaceInfo.width == 0 || mSurfaceInfo.height == 0) { // Not yet laid out
            return sizes.first(); // Return the smallest size
        }
        int desiredWidth;
        int desiredHeight;
        if (mDisplayOrientation == 90 || mDisplayOrientation == 270) {
            desiredWidth = mSurfaceInfo.height;
            desiredHeight = mSurfaceInfo.width;
        } else {
            desiredWidth = mSurfaceInfo.width;
            desiredHeight = mSurfaceInfo.height;
        }
        Size result = null;
        for (Size size : sizes) { // Iterate from small to large
            if (desiredWidth <= size.getWidth() && desiredHeight <= size.getHeight()) {
                return size;

            }
            result = size;
        }
        return result;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
            mCallback.onCameraClosed();
        }
    }

    private int calcCameraRotation(int rotation) {
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (360 - (mCameraInfo.orientation + rotation) % 360) % 360;
        } else {  // back-facing
            return (mCameraInfo.orientation - rotation + 360) % 360;
        }
    }

    /**
     * @return {@code true} if {@link #mCameraParameters} was modified.
     */
    private boolean setAutoFocusInternal(boolean autoFocus) {
        mAutoFocus = autoFocus;
        if (isCameraOpened()) {
            final List<String> modes = mCameraParameters.getSupportedFocusModes();
            if (autoFocus && modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
                mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
            } else if (modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
            } else {
                mCameraParameters.setFocusMode(modes.get(0));
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return {@code true} if {@link #mCameraParameters} was modified.
     */
    private boolean setFlashInternal(int flash) {
        if (isCameraOpened()) {
            List<String> modes = mCameraParameters.getSupportedFlashModes();
            String mode = FLASH_MODES.get(flash);
            if (modes!= null && modes.contains(mode)) {
                mCameraParameters.setFlashMode(mode);
                mFlash = flash;
                return true;
            }
            String currentMode = FLASH_MODES.get(mFlash);
            if (modes == null || !modes.contains(currentMode)) {
                mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mFlash = Constants.FLASH_OFF;
                return true;
            }
            return false;
        } else {
            mFlash = flash;
            return false;
        }
    }

}
