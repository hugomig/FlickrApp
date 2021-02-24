package e.g.hugom.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.Vector;

public class ListActivity extends AppCompatActivity {

    ListView lvBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvBitmap = findViewById(R.id.lvBitmap);

        RequestQueue queue = MySingleton.getInstance(lvBitmap.getContext()).getRequestQueue();
        MyAdapter adapter = new MyAdapter(getLayoutInflater(),queue);

        AsyncFlickrJSONDataForList async = new AsyncFlickrJSONDataForList(adapter);
        async.execute(MainActivity.getUrl(getApplicationContext(),this));

        lvBitmap.setAdapter(adapter);
    }
}

class MyAdapter extends BaseAdapter {

    Vector<String> urls;
    LayoutInflater inflater;
    RequestQueue queue;

    public MyAdapter(LayoutInflater inflater, RequestQueue queue){
        urls = new Vector<String>();
        this.inflater = inflater;
        this.queue = queue;
    }

    public void add(String url){
        urls.add(url);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.bitmap_layout,parent,false);
            ImageView imageView = convertView.findViewById(R.id.imageView);

            //creer une requete pour telecharge une image
            ImageRequest imageRequest = new ImageRequest(urls.get(position), new Response.Listener<Bitmap>() {
                //Quand l'image est prete l'affiche dans l'image view
                @Override
                public void onResponse(Bitmap response) {
                    imageView.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("HUGO",error.toString());
                }
            });

            //Ajoute la requete d'image a la file dattente pour gerer toutes les requetes d'images une par une
            queue.add(imageRequest);
        }
        return convertView;
    }
}