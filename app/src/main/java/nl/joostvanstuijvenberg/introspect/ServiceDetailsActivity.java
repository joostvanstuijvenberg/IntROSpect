package nl.joostvanstuijvenberg.introspect;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.data.Node;
import nl.joostvanstuijvenberg.introspect.viewmodel.ServiceViewModel;

/**
 * Created by Joost van Stuijvenberg on 22/10/2017.
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

public class ServiceDetailsActivity extends AppCompatActivity implements TextView.OnClickListener {

    private Master mMaster;
    private ServiceViewModel mViewModel;
    private TextView mServiceName;
    private TextView mServiceProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaster = getIntent().getExtras().getParcelable(Constants.PARCEL_KEY_MASTER);
        String serviceName = getIntent().getExtras().getString(Constants.PARCEL_KEY_SERVICE);
        mViewModel = ViewModelProviders.of(this, new ServiceViewModel.Factory(mMaster, serviceName)).get(ServiceViewModel.class);

        // Inflate the user interface.
        setContentView(R.layout.activity_service_details);
        mServiceName = (TextView)findViewById(R.id.serviceName);
        //mServiceRequestType = (TextView)findViewById(R.id.serviceRequestType);
        //mServiceResponseType = (TextView)findViewById(R.id.serviceResponseType);
        mServiceProvider = (TextView)findViewById(R.id.serviceProvider);
        mServiceProvider.setOnClickListener(this);

        // Observe the ViewModel's service details and make sure changes are reflected in the user interface.
        mViewModel.serviceDetails.observe(this, new Observer<ServiceViewModel.ServiceDetailsViewModel>() {
            @Override
            public void onChanged(@Nullable ServiceViewModel.ServiceDetailsViewModel serviceDetailsViewData) {
                if (serviceDetailsViewData != null) {
                    if (serviceDetailsViewData.service != null) {
                        mServiceName.setText(serviceDetailsViewData.service.name);
                        mServiceProvider.setText(serviceDetailsViewData.provider.name);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NodeDetailsActivity.class);
        intent.putExtra(Constants.PARCEL_KEY_MASTER, mMaster);
        intent.putExtra(Constants.PARCEL_KEY_NODE, mServiceProvider.getText());
        startActivity(intent);
    }

}
