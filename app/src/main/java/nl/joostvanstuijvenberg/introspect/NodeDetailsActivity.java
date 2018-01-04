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
import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.data.Service;
import nl.joostvanstuijvenberg.introspect.data.Topic;
import nl.joostvanstuijvenberg.introspect.viewmodel.NodeViewModel;

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

public class NodeDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Master mMaster;
    private String mNodeName;
    private NodeViewModel mViewModel;
    private TextView mTVNodeName;
    private ListView mTopicsPublishedTo;
    private ArrayAdapter<Topic> mPublishedToAdapter;
    private ListView mTopicsSubscribedTo;
    private ArrayAdapter<Topic> mSubscribedToAdapter;
    private ListView mServicesProviding;
    private ArrayAdapter<Service> mProvidingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaster = getIntent().getExtras().getParcelable(Constants.PARCEL_KEY_MASTER);
        mNodeName = getIntent().getExtras().getString(Constants.PARCEL_KEY_NODE);
        mViewModel = ViewModelProviders.of(this, new NodeViewModel.Factory(mMaster, mNodeName)).get(NodeViewModel.class);

        // Inflate the user interface.
        setContentView(R.layout.activity_node_details);
        mTVNodeName = (TextView) findViewById(R.id.nodeName);

        final List<Topic> publishingTo = new ArrayList<>();
        final List<Topic> subscribedTo = new ArrayList<>();

        mTopicsPublishedTo = (ListView) findViewById(R.id.topicsPublishedTo);
        mPublishedToAdapter = new ArrayAdapter<Topic>(this, R.layout.activity_topic_list_row, publishingTo);
        mTopicsPublishedTo.setAdapter(mPublishedToAdapter);
        mTopicsPublishedTo.setOnItemClickListener(this);

        mTopicsSubscribedTo = (ListView) findViewById(R.id.topicsSubscribedTo);
        mSubscribedToAdapter = new ArrayAdapter<Topic>(this, R.layout.activity_topic_list_row, subscribedTo);
        mTopicsSubscribedTo.setAdapter(mSubscribedToAdapter);
        mTopicsSubscribedTo.setOnItemClickListener(this);

        final List<Service> providing = new ArrayList<>();
        final List<Service> calling = new ArrayList<>();

        mServicesProviding = (ListView) findViewById(R.id.servicesProvided);
        mProvidingAdapter = new ArrayAdapter<Service>(this, R.layout.activity_service_list_row, providing);
        mServicesProviding.setAdapter(mProvidingAdapter);
        mServicesProviding.setOnItemClickListener(this);

        // Observe the ViewModel's node details and make sure changes are reflected in the user interface.
        mViewModel.nodeDetails.observe(this, new Observer<NodeViewModel.NodeDetailsViewModel>() {
            @Override
            public void onChanged(@Nullable NodeViewModel.NodeDetailsViewModel nodeDetailsViewData) {
                if (nodeDetailsViewData != null) {
                    if (nodeDetailsViewData.node != null) {
                        mTVNodeName.setText(nodeDetailsViewData.node.name);
                        publishingTo.clear();
                        publishingTo.addAll(nodeDetailsViewData.publishingTo);
                        mPublishedToAdapter.notifyDataSetChanged();
                        subscribedTo.clear();
                        subscribedTo.addAll(nodeDetailsViewData.subscribedTo);
                        mSubscribedToAdapter.notifyDataSetChanged();
                        providing.clear();
                        providing.addAll(nodeDetailsViewData.providing);
                        mProvidingAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.topicListRow:
                Topic topic = (Topic) adapterView.getItemAtPosition(position);
                intent = new Intent(getApplicationContext(), TopicDetailsActivity.class);
                intent.putExtra(Constants.PARCEL_KEY_MASTER, mMaster);
                intent.putExtra(Constants.PARCEL_KEY_TOPIC, topic.name);
                break;
            case R.id.serviceListRow:
                Service service = (Service) adapterView.getItemAtPosition(position);
                intent = new Intent(getApplicationContext(), ServiceDetailsActivity.class);
                intent.putExtra(Constants.PARCEL_KEY_MASTER, mMaster);
                intent.putExtra(Constants.PARCEL_KEY_SERVICE, service.name);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

}
