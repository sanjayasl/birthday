package me.gevorg.birthday.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.gevorg.birthday.R;
import me.gevorg.birthday.widget.WidgetProvider;

/**
 * Birthday activity.
 *
 * @author Gevorg Harutyunyan.
 */
public class BirthdayActivity extends AppCompatActivity {
    /**
     * Create method.
     *
     * @param savedInstanceState saved instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
    }

    /**
     * Update widget.
     */
    @Override
    protected void onPause() {
        super.onPause();

        WidgetProvider.updateAllWidgets(this);
    }

    /**
     * Create menu options.
     *
     * @param menu menu.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_birthday, menu);
        return true;
    }

    /**
     * If menu is selected.
     *
     * @param item item.
     * @return selected or not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Settings menu.
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
