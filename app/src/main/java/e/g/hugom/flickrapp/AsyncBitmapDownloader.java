package e.g.hugom.flickrapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncBitmapDownloader extends AsyncTask<String,Void, Bitmap> {

    private MainActivity activity;

    //prend en parametre l'activite pour avoir acces a l'ui thread apres lexecution
    public AsyncBitmapDownloader(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String link = strings[0];
        URL url = null;
        try{
            url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try{
                //telecharge l'image a partir de lurl passe en parametre
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(is);
                return bm;
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //Une fois limage charge laffiche dans l'image view
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivBitmap = activity.findViewById(R.id.ivBitmap);
                ivBitmap.setImageBitmap(bitmap);
            }
        });
    }
    
}
