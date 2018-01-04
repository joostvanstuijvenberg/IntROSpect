package nl.joostvanstuijvenberg.introspect;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.viewmodel.MasterListViewModel;

/**
 * Created by Joost van Stuijvenberg.
 *
 * This file is part of IntROSpect. IntROSpect is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * IntROSpect is distributed in the hope that it will be useful but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with IntROSpect.  If
 * not, see <http://www.gnu.org/licenses/>.
 */

public class MasterListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Button.OnClickListener {

    private static final int ACTIVITY_RESULT_ADD_NEW_CONNECTION = 0;
    private static final int ACTIVITY_RESULT_SCAN_FOR_ROS_MASTERS = 1;

    private Toolbar toolbar;
    private ArrayList<String> mRecentMasters;
    private ListView mRecentMastersListView;
    private ArrayAdapter<String> mRecentMastersAdapter;
    private MasterListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the user interface.
        setContentView(R.layout.activity_select_master);
        toolbar = (Toolbar) findViewById(R.id.selectMasterToolbar);
        setSupportActionBar(toolbar);

        // List of recent masters. Clicking one will open a connection.
        mRecentMasters = new ArrayList<>();
        mRecentMastersAdapter = new ArrayAdapter<String>(this, R.layout.activity_select_row, mRecentMasters);
        mRecentMastersListView = (ListView) findViewById(R.id.recentMastersListView);
        mRecentMastersListView.setAdapter(mRecentMastersAdapter);
        mRecentMastersListView.setOnItemClickListener(this);

        // Obtain data from the view model.
        mViewModel = ViewModelProviders.of(this).get(MasterListViewModel.class);
        final Observer<List<Master>> recentMastersObserver = new Observer<List<Master>>() {
            @Override
            public void onChanged(@Nullable List<Master> recentMasters) {
                if (recentMasters != null) {
                    mRecentMasters.clear();
                    for (Master m : recentMasters)
                        mRecentMasters.add(m.toString());
                    mRecentMastersAdapter.notifyDataSetChanged();
                }
            }
        };
        mViewModel.getRecentMasters().observe(this, recentMastersObserver);

        // Clicking the 'Add new connection' button lets us enter the connection details of a new master to connect to.
        findViewById(R.id.buttonAddNewConnection).setOnClickListener(this);

        // Clicking the 'Scan for ROS masters' lets the app scan for ROS masters.
        findViewById(R.id.buttonScanMasters).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                mViewModel.clearRecentMasters();
                return true;
            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Master m = new Master((String) adapterView.getItemAtPosition(position));
        Intent i = new Intent(getApplicationContext(), MasterDetailsActivity.class);
        i.putExtra(Constants.PARCEL_KEY_MASTER, m);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Master master = null;
        switch (requestCode) {
            case ACTIVITY_RESULT_ADD_NEW_CONNECTION:
            case ACTIVITY_RESULT_SCAN_FOR_ROS_MASTERS:
                if (resultCode == RESULT_OK) {
                    master = data.getParcelableExtra(Constants.PARCEL_KEY_MASTER);
                    mViewModel.addRecentMaster(master);
                    Intent i = new Intent(getApplicationContext(), MasterDetailsActivity.class);
                    i.putExtra(Constants.PARCEL_KEY_MASTER, master);
                    startActivity(i);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.buttonAddNewConnection:
                    startActivityForResult(new Intent(getApplicationContext(), MasterNewActivity.class), ACTIVITY_RESULT_ADD_NEW_CONNECTION);
                    break;
                case R.id.buttonScanMasters:
                    startActivityForResult(new Intent(getApplicationContext(), MasterScanActivity.class), ACTIVITY_RESULT_SCAN_FOR_ROS_MASTERS);
                    break;
            }
        }
    }
}
