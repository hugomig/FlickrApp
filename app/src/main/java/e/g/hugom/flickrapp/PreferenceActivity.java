package e.g.hugom.flickrapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    public final static String keySearch = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ouvre le fragment preference qui va etre initialise grace au fichier xml
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            //Prend le fichier xml pour afficher linterface utilisateur
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}