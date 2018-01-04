package nl.joostvanstuijvenberg.introspect;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.viewmodel.MasterNewViewModel;

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

public class MasterNewActivity extends AppCompatActivity implements Observer<MasterNewViewModel.State>, Button.OnClickListener {

    private MasterNewViewModel mViewModel;
    private LiveData<MasterNewViewModel.State> mState;

    private EditText mHostEditText;
    private TextView mHostErrorTextView;
    private EditText mPortEditText;
    private TextView mPortErrorTextView;
    private Button mConnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_master);
        mHostEditText = findViewById(R.id.hostEditText);
        mHostErrorTextView = findViewById(R.id.hostError);
        mPortEditText = findViewById(R.id.portEditText);
        mPortErrorTextView = findViewById(R.id.portError);
        mConnectButton = findViewById(R.id.buttonConnect);
        mConnectButton.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(MasterNewViewModel.class);
        mState = mViewModel.getState();
        mState.observe(this, this);
    }

    @Override
    public void onChanged(@Nullable MasterNewViewModel.State state) {
        if (state != null) {
            switch (state) {
                case EMPTY:
                    getData();
                    mConnectButton.setEnabled(true);
                    break;
                case CHECKING:
                    getData();
                    mConnectButton.setEnabled(false);
                    break;
                case ERROR:
                    getData();
                    if (!mViewModel.hostError.isEmpty())
                        mHostEditText.requestFocus();
                    else if (!mViewModel.portError.isEmpty())
                        mPortEditText.requestFocus();
                    mConnectButton.setEnabled(true);
                    break;
                case CHECKED:
                    Intent i = new Intent();
                    Master m = new Master(mViewModel.host, mViewModel.port);
                    i.putExtra(Constants.PARCEL_KEY_MASTER, m);
                    setResult(RESULT_OK, i);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonConnect) {
            setData();
            mViewModel.connect();
        }
    }

    private void getData() {
        mHostEditText.setText(mViewModel.host);
        mHostErrorTextView.setText(mViewModel.hostError);
        mPortEditText.setText(Integer.toString(mViewModel.port));
        mPortErrorTextView.setText(mViewModel.portError);
    }

    private void setData() {
        mViewModel.host = mHostEditText.getText().toString();
        mViewModel.port = mPortEditText.getText().toString().isEmpty() ? 0 : Integer.valueOf(mPortEditText.getText().toString());
    }

}
