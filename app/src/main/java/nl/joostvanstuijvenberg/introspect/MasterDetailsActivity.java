package nl.joostvanstuijvenberg.introspect;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.viewmodel.MasterDetailsViewModel;

/**
 * Created by Joost van Stuijvenberg on 23/10/2017.
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

public class MasterDetailsActivity extends AppCompatActivity implements Button.OnClickListener {

    private Master mMaster;
    private MasterDetailsViewModel mViewModel;
    private TextView mNumberOfNodes;
    private TextView mNumberOfTopics;
    private TextView mNumberOfServices;
    private Button mNodesButton;
    private Button mTopicsButton;
    private Button mServicesButton;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaster = getIntent().getExtras().getParcelable(Constants.PARCEL_KEY_MASTER);
        mViewModel = ViewModelProviders.of(this, new MasterDetailsViewModel.Factory(mMaster)).get(MasterDetailsViewModel.class);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String ris = sp.getString("pref_refresh_interval", "10");
        int refreshInterval = Integer.parseInt(ris);
        mViewModel.setRefreshInterval(refreshInterval * 1000);

        // Inflate the user interface.
        setContentView(R.layout.activity_show_master);
        mNumberOfNodes = (TextView) findViewById(R.id.nodeCount);
        mNumberOfTopics = (TextView) findViewById(R.id.topicCount);
        mNumberOfServices = (TextView) findViewById(R.id.serviceCount);
        mNodesButton = (Button) findViewById(R.id.buttonNodes);
        mNodesButton.setOnClickListener(this);
        mTopicsButton = (Button) findViewById(R.id.buttonTopics);
        mTopicsButton.setOnClickListener(this);
        mServicesButton = (Button) findViewById(R.id.buttonServices);
        mServicesButton.setOnClickListener(this);
        ((TextView) findViewById(R.id.masterHost)).setText(mMaster.getHost());
        ((TextView) findViewById(R.id.masterPort)).setText(Integer.valueOf(mMaster.getPort()).toString());

        // Toasts are those small popups that are quite useful to reflect the status of an app.
        Toast loadingToast = Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT);
        Toast timeoutToast = Toast.makeText(getApplicationContext(), "Timeout occurred while contacting master.", Toast.LENGTH_LONG);
        Toast errorToast = Toast.makeText(getApplicationContext(), "Error occurred while loading data.", Toast.LENGTH_LONG);

        // Make sure changes in the ROS graph are reflected in the user interface.
        mViewModel.masterViewModel.observe(this, master -> {
            if (master != null) {
                mNumberOfNodes.setText(Integer.toString(master.numberOfNodes));
                mNumberOfTopics.setText(Integer.toString(master.numberOfTopics));
                mNumberOfServices.setText(Integer.toString(master.numberOfServices));

                if (master.showLoading)
                    loadingToast.show();
                else
                    loadingToast.cancel();

                if (master.showTimeout)
                    timeoutToast.show();
                else
                    timeoutToast.cancel();

                if (master.showError) {
                    errorToast.show();
                } else
                    errorToast.cancel();

                mNodesButton.setEnabled(master.enableButtons);
                mTopicsButton.setEnabled(master.enableButtons);
                mServicesButton.setEnabled(master.enableButtons);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.buttonNodes:
                i = new Intent(getApplicationContext(), NodeListActivity.class);
                break;
            case R.id.buttonTopics:
                i = new Intent(getApplicationContext(), TopicListActivity.class);
                break;
            case R.id.buttonServices:
                i = new Intent(getApplicationContext(), ServiceListActivity.class);
                break;
            case R.id.buttonGraph:
                break;
        }

        if (i != null) {
            i.putExtra(Constants.PARCEL_KEY_MASTER, mMaster);
            startActivity(i);
        }
    }

}
