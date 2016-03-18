package com.example.lbc.lbc.list_load;

/**
 * Created by lbc on 2016/3/18.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ImageDownloader {
    private static final String TAG = "ImageDownloader";
    private HashMap<String, ImageDownloader.MyAsyncTask> map = new HashMap();
    private Map<String, SoftReference<Bitmap>> imageCaches = new HashMap();

    public ImageDownloader() {
    }

    public void imageDownload(String url, ImageView mImageView, String path, Activity mActivity, OnImageDownload download) {
        SoftReference currBitmap = (SoftReference)this.imageCaches.get(url);
        Bitmap softRefBitmap = null;
        if(currBitmap != null) {
            softRefBitmap = (Bitmap)currBitmap.get();
        }

        String imageName = "";
        if(url != null) {
            imageName = Util.getInstance().getImageName(url);
        }

        if(currBitmap != null && mImageView != null && softRefBitmap != null && url.equals(mImageView.getTag())) {
            System.out.println("从软引用中拿数据--imageName==" + imageName);
            mImageView.setImageBitmap(softRefBitmap);
        } else if(mImageView != null && url.equals(mImageView.getTag())) {
            Bitmap bitmap = this.getBitmapFromFile(mActivity, imageName, path);
            if(bitmap != null) {
                mImageView.setImageBitmap(bitmap);
                this.imageCaches.put(url, new SoftReference(bitmap));
            } else if(url != null && this.needCreateNewTask(mImageView)) {
                ImageDownloader.MyAsyncTask task = new ImageDownloader.MyAsyncTask(url, mImageView, path, mActivity, download);
                if(mImageView != null) {
                    Log.i("ImageDownloader", "执行MyAsyncTask --> " + Util.flag);
                    ++Util.flag;
                    task.execute(new String[0]);
                    this.map.put(url, task);
                }
            }
        }

    }

    private boolean needCreateNewTask(ImageView mImageView) {
        boolean b = true;
        if(mImageView != null) {
            String curr_task_url = (String)mImageView.getTag();
            if(this.isTasksContains(curr_task_url)) {
                b = false;
            }
        }

        return b;
    }

    private boolean isTasksContains(String url) {
        boolean b = false;
        if(this.map != null && this.map.get(url) != null) {
            b = true;
        }

        return b;
    }

    private void removeTaskFormMap(String url) {
        if(url != null && this.map != null && this.map.get(url) != null) {
            this.map.remove(url);
            System.out.println("当前map的大小==" + this.map.size());
        }

    }

    private Bitmap getBitmapFromFile(Activity mActivity, String imageName, String path) {
        Bitmap bitmap = null;
        if(imageName != null) {
            File file = null;
            String real_path = "";

            try {
                if(Util.getInstance().hasSDCard()) {
                    real_path = Util.getInstance().getExtPath() + (path != null && path.startsWith("/")?path:"/" + path);
                } else {
                    real_path = Util.getInstance().getPackagePath(mActivity) + (path != null && path.startsWith("/")?path:"/" + path);
                }

                file = new File(real_path, imageName);
                if(file.exists()) {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                }
            } catch (Exception var8) {
                var8.printStackTrace();
                bitmap = null;
            }
        }

        return bitmap;
    }

    private boolean setBitmapToFile(String path, Activity mActivity, String imageName, Bitmap bitmap) {
        File file = null;
        String real_path = "";

        try {
            if(Util.getInstance().hasSDCard()) {
                real_path = Util.getInstance().getExtPath() + (path != null && path.startsWith("/")?path:"/" + path);
            } else {
                real_path = Util.getInstance().getPackagePath(mActivity) + (path != null && path.startsWith("/")?path:"/" + path);
            }

            file = new File(real_path, imageName);
            File e;
            if(!file.exists()) {
                e = new File(real_path + "/");
                e.mkdirs();
            }

            file.createNewFile();
            e = null;
            FileOutputStream e1;
            if(Util.getInstance().hasSDCard()) {
                e1 = new FileOutputStream(file);
            } else {
                e1 = mActivity.openFileOutput(imageName, 0);
            }

            if(imageName == null || !imageName.contains(".png") && !imageName.contains(".PNG")) {
                bitmap.compress(CompressFormat.JPEG, 90, e1);
            } else {
                bitmap.compress(CompressFormat.PNG, 90, e1);
            }

            e1.flush();
            if(e1 != null) {
                e1.close();
            }

            return true;
        } catch (Exception var8) {
            var8.printStackTrace();
            return false;
        }
    }

    private void removeBitmapFromFile(String path, Activity mActivity, String imageName) {
        File file = null;
        String real_path = "";

        try {
            if(Util.getInstance().hasSDCard()) {
                real_path = Util.getInstance().getExtPath() + (path != null && path.startsWith("/")?path:"/" + path);
            } else {
                real_path = Util.getInstance().getPackagePath(mActivity) + (path != null && path.startsWith("/")?path:"/" + path);
            }

            file = new File(real_path, imageName);
            if(file != null) {
                file.delete();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String url;
        private OnImageDownload download;
        private String path;
        private Activity mActivity;

        public MyAsyncTask(String url, ImageView mImageView, String path, Activity mActivity, OnImageDownload download) {
            this.mImageView = mImageView;
            this.url = url;
            this.path = path;
            this.mActivity = mActivity;
            this.download = download;
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap data = null;
            if(this.url != null) {
                try {
                    URL e = new URL(this.url);
                    InputStream bitmap_data = e.openStream();
                    data = BitmapFactory.decodeStream(bitmap_data);
                    String imageName = Util.getInstance().getImageName(this.url);
                    if(!ImageDownloader.this.setBitmapToFile(this.path, this.mActivity, imageName, data)) {
                        ImageDownloader.this.removeBitmapFromFile(this.path, this.mActivity, imageName);
                    }

                    ImageDownloader.this.imageCaches.put(this.url, new SoftReference(Bitmap.createScaledBitmap(data, 100, 100, true)));
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            return data;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Bitmap result) {
            if(this.download != null) {
                this.download.onDownloadSucc(result, this.url, this.mImageView);
                ImageDownloader.this.removeTaskFormMap(this.url);
            }

            super.onPostExecute(result);
        }
    }
}
