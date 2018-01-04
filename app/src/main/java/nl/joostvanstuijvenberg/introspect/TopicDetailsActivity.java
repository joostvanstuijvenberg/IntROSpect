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
import nl.joostvanstuijvenberg.introspect.viewmodel.TopicViewModel;

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

public class TopicDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Master mMaster;
    private TopicViewModel mViewModel;
    private TextView mTopicName;
    private TextView mTopicType;
    private ListView mTopicPublishers;
    private ArrayAdapter<Node> mPublishersAdapter;
    private ListView mTopicSubscribers;
    private ArrayAdapter<Node> mSubscribersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaster = getIntent().getExtras().getParcelable(Constants.PARCEL_KEY_MASTER);
        String topicName = getIntent().getExtras().getString(Constants.PARCEL_KEY_TOPIC);
        mViewModel = ViewModelProviders.of(this, new TopicViewModel.Factory(mMaster, topicName)).get(TopicViewModel.class);

        // Inflate the user interface.
        setContentView(R.layout.activity_topic_details);
        mTopicName = (TextView)findViewById(R.id.topicName);
        mTopicType = (TextView)findViewById(R.id.topicType);
        mTopicPublishers = (ListView)findViewById(R.id.topicPublishers);
        mTopicSubscribers = (ListView)findViewById(R.id.topicSubscribers);

        final ArrayList<Node> publishers = new ArrayList<>();
        final ArrayList<Node> subscribers = new ArrayList<>();
        mPublishersAdapter = new ArrayAdapter<Node>(this, R.layout.activity_node_list_row, publishers);
        mTopicPublishers.setAdapter(mPublishersAdapter);
        mTopicPublishers.setOnItemClickListener(this);
        mSubscribersAdapter = new ArrayAdapter<Node>(this, R.layout.activity_node_list_row, subscribers);
        mTopicSubscribers.setAdapter(mSubscribersAdapter);
        mTopicSubscribers.setOnItemClickListener(this);

        // Observe the ViewModel's topic details and make sure changes are reflected in the user interface.
        mViewModel.topicDetails.observe(this, new Observer<TopicViewModel.TopicDetailsViewModel>() {
            @Override
            public void onChanged(@Nullable TopicViewModel.TopicDetailsViewModel topicDetailsViewData) {
                if (topicDetailsViewData != null) {
                    if (topicDetailsViewData.topic != null) {
                        mTopicName.setText(topicDetailsViewData.topic.name);
                        mTopicType.setText(topicDetailsViewData.topic.type);
                        publishers.clear();
                        publishers.addAll(topicDetailsViewData.publishers);
                        mPublishersAdapter.notifyDataSetChanged();
                        subscribers.clear();
                        subscribers.addAll(topicDetailsViewData.subscribers);
                        mSubscribersAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Node node = (Node)adapterView.getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), NodeDetailsActivity.class);
        intent.putExtra(Constants.PARCEL_KEY_MASTER, mMaster);
        intent.putExtra(Constants.PARCEL_KEY_NODE, node.name);
        startActivity(intent);
    }

}
