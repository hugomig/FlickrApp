package e.g.hugom.flickrapp;

import android.view.View;

public class GetImageOnClickListener implements View.OnClickListener {

    MainActivity activity;

    public GetImageOnClickListener(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        AsyncFlickrJSONData async = new AsyncFlickrJSONData(activity);
        async.execute(MainActivity.getUrl(v.getContext(),activity));
    }
}
