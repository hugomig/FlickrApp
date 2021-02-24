package e.g.hugom.flickrapp;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncFlickrJSONData extends AsyncTask<String,Void, JSONObject> {

    private MainActivity activity;

    public AsyncFlickrJSONData(MainActivity activity){
        super();
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String urlStr = strings[0];
        Log.i("Hugo",urlStr);

        URL url = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String s = readStream(in);
                try {
                    s = s.substring(15,s.length()-1);
                    JSONObject jsonRes = new JSONObject(s);
                    return jsonRes;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if(jsonObject != null) {
            Log.i("Hugo", jsonObject.toString());
            try {
                String url = jsonObject.getJSONArray("items").getJSONObject(1).getJSONObject("media").getString("m");
                Log.i("Hugo",url);

                AsyncBitmapDownloader async = new AsyncBitmapDownloader(activity);
                async.execute(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i("Hugo","NULL JSONObject");
        }
    }


    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
