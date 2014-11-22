package barnes.matt.tallpinesrally;

import java.io.BufferedInputStream;
//import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageManager {

    //       private final String PATH = "/data/data/barnes.matt.tallpinesrally.teamimages/";  //put the downloaded file here
    private Context context;
    private DatabaseHelper db;
    private String wifiOnly = "0";

    public ImageManager(Context _context)
    {
        context = _context;
    }


    public String DownloadFromUrl(String imageFileName, String fileName) {  //this is the downloader method
        db = new DatabaseHelper(context);
        wifiOnly = db.getValue("wifiOnly");

        if (wifiOnly == "0")
            return doDownload(imageFileName, fileName);
        else
        {
            if (isWifiConn())
                return doDownload(imageFileName, fileName);
            else
                return "0";

        }
        //return "-1";  //failed for some reason
    }

    private boolean isWifiConn()
    {
        return true;
        //ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //return networkInfo.isConnected();
    }

    private String doDownload(String imageFileName, String fileName)
    {
        try {
            URL url = new URL("http://wedge1-001-site1.mywindowshosting.com/images/" + imageFileName); //you can write here any link
            //  File file = new File(PATH + fileName);

            long startTime = System.currentTimeMillis();
            //Log.d("ImageManager", "download begining");
            //Log.d("ImageManager", "download url:" + url);
            //Log.d("ImageManager", "downloaded file name:" + imageFileName);
                /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

                /*
                 * Define InputStreams to read from the URLConnection.
                 */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
                 */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

                /* Convert the Bytes read to a String. */
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);//new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("ImageManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
            return "1";

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
            return "-1";
        }
    }

    public Drawable fetchDrawable(String fileName) {
        try {
            InputStream is = MainActivity.getInstance().openFileInput(fileName);

            return Drawable.createFromStream(is, fileName);


            //BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            //BitmapFactory.decodeStream(MainActivity.getInstance().openFileInput(fileName), null, options);
            // int imageHeight = options.outHeight;
            // int imageWidth = options.outWidth;
            // String imageType = options.outMimeType;

            //options.inSampleSize = calculateInSampleSize(options, 132, 132);

            //options.inJustDecodeBounds = false;
            //return BitmapFactory.decodeStream(MainActivity.getInstance().openFileInput(fileName), null, options);
            // return
            // Drawable.createFromStream(getApplicationContext().openFileInput(params[0]),
            // params[0]);
        } catch (IOException e) {
            //Log.d("TeamPage", "Image file not found");
            return null;
        }
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
        //if (drawableMap.containsKey(urlString)) {
       //     imageView.setImageDrawable(drawableMap.get(urlString));
        //}

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageView.setImageDrawable((Drawable) message.obj);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                Drawable drawable = fetchDrawable(urlString);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }
}