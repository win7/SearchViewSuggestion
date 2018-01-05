package com.festeam.win7.searchviewsuggestion;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String[] SUGGESTIONS = {
            "Cusco", "Lima", "Quito", "Bogota", "Buenos Aires",
            "DF. Mexíco", "Los Angeles", "California", "Santiago",
            "Brazilia", "Caracas", "Cali"
    };
    SimpleCursorAdapter mAdapter;
    MatrixCursor mc;

    final String TAG = "XXX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView tw_city = findViewById(R.id.textView_city);

        final String[] from = new String[] {"city_name"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        /* This replace searchView.setOnQueryTextListener(new .... */
        /* mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return populateAdapter((String) constraint);
            }
        }); */

        SearchView searchView = findViewById(R.id.searchView_search);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);
        searchView.setActivated(true);
        searchView.setQueryHint("search city");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                mc.moveToPosition(position);
                long id = mc.getLong(mc.getColumnIndex(BaseColumns._ID));
                String city = mc.getString(mc.getColumnIndex("city_name"));
                // long dataId =  cursor.getLong(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));
                mc.close();
                Log.e(TAG,"onSuggestionClick " + city);
                Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();
                tw_city.setText(city);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                Log.e(TAG, "onSuggestionSelect " + SUGGESTIONS[position]);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e(TAG, "onQueryTextSubmit " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e(TAG, "onQueryTextChange " + s);
                FilterAdapter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* You must implements your logic to get data using OrmLite */
    private Cursor FilterAdapter(String query) {
        mc = new MatrixCursor(new String[]{ BaseColumns._ID, "city_name" });
        for (int i=0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase())) {
                mc.addRow(new Object[]{i, SUGGESTIONS[i]});
            }
        }
        mAdapter.changeCursor(mc);
        return mc;
    }
}
