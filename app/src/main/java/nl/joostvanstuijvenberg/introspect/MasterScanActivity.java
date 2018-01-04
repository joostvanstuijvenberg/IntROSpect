package nl.joostvanstuijvenberg.introspect;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.viewmodel.MasterScanViewModel;

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

public class MasterScanActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "MasterScanActivity";

    private EditText mStartIPEditText;
    private TextView mStartIPErrorTextView;
    private EditText mEndIPEditText;
    private TextView mEndIPErrorTextView;
    private ListView mFoundMasters;
    private Button mScanButton;

    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> masters = new ArrayList<>();
    private AlertDialog.Builder mAlertDialogBuilder;
    private MasterScanViewModel mMasterScanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_masters);

        mStartIPEditText = (EditText) findViewById(R.id.startIPEditText);
        mStartIPErrorTextView = (TextView) findViewById(R.id.startIPError);
        mEndIPEditText = (EditText) findViewById(R.id.endIPEditText);
        mEndIPErrorTextView = (TextView) findViewById(R.id.endIPError);
        mScanButton = (Button) findViewById(R.id.buttonScan);
        mScanButton.setOnClickListener(this);
        mFoundMasters = (ListView) findViewById(R.id.foundMasters);
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_scan_row, masters);
        mFoundMasters.setAdapter(mArrayAdapter);
        mFoundMasters.setOnItemClickListener(this);
        mAlertDialogBuilder = new AlertDialog.Builder(this);
        mMasterScanViewModel = ViewModelProviders.of(this).get(MasterScanViewModel.class);

        // Start IP-address.
        mMasterScanViewModel.startIP.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mStartIPEditText.setText(s);
            }
        });

        // Validation errors for the start IP-address.
        mMasterScanViewModel.startIPError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mStartIPErrorTextView.setText(s);
            }
        });

        // End IP-address.
        mMasterScanViewModel.endIP.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mEndIPEditText.setText(s);
            }
        });

        // Validation errors for the end IP-address.
        mMasterScanViewModel.endIPError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mEndIPErrorTextView.setText(s);
            }
        });

        // Found masters.
        mMasterScanViewModel.getFoundMasters().observe(this, new Observer<List<Master>>() {
            @Override
            public void onChanged(@Nullable List<Master> foundMasters) {
                masters.clear();
                for (Master m : foundMasters)
                    masters.add(m.toString());
                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent i = new Intent();
        Master m = new Master((String) adapterView.getItemAtPosition(position));
        i.putExtra(Constants.PARCEL_KEY_MASTER, m);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onClick(View view) {
        mMasterScanViewModel.startIP.setValue(mStartIPEditText.getText().toString());
        mMasterScanViewModel.endIP.setValue(mEndIPEditText.getText().toString());
        if (mMasterScanViewModel.validate())
            mMasterScanViewModel.startScanning();
    }
}
