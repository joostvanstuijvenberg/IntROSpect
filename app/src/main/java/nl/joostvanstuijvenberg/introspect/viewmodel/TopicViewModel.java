package nl.joostvanstuijvenberg.introspect.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Graph;
import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.data.Node;
import nl.joostvanstuijvenberg.introspect.data.Topic;
import nl.joostvanstuijvenberg.introspect.repository.MasterRepository;
import nl.joostvanstuijvenberg.introspect.repository.NetworkResource;

/**
 * Created by Joost van Stuijvenberg on 07/10/2017.
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

public class TopicViewModel extends ViewModel {

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private Master mMaster;
        private String mTopicName;

        public Factory(Master master, String topicName) {
            mMaster = master;
            mTopicName = topicName;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new TopicViewModel(mMaster, mTopicName);
        }

    }

    public class TopicListViewModel {
        public List<Topic> topics = new ArrayList<>();
        public boolean loading = false;
        public boolean error = false;
        public String errorText = "";
    }

    public class TopicDetailsViewModel {
        public Topic topic;
        public List<Node> publishers = new ArrayList<>();
        public List<Node> subscribers = new ArrayList<>();
    }

    private final Master master;
    private String topicName;
    private MasterRepository mMasterRepository;
    private LiveData<NetworkResource<Graph>> mGraph;
    private LiveData<NetworkResource<Topic>> mTopic;
    public LiveData<TopicListViewModel> topicList;
    public LiveData<TopicDetailsViewModel> topicDetails;

    public TopicViewModel(Master master) {
        this.master = master;
        mMasterRepository = new MasterRepository(master);
        mGraph = mMasterRepository.getGraph();
        topicList = Transformations.map(mGraph, input -> {
            TopicListViewModel result = new TopicListViewModel();
            if (input != null) {
                if (input.data != null)
                    result.topics = input.data.getTopics();
                result.loading = (input.status == NetworkResource.Status.LOADING);
                result.error = (input.status == NetworkResource.Status.ERROR);
                result.errorText = "Something went terribly wrong.";
            }
            return result;
        });
    }

    public TopicViewModel(Master master, String topicName) {
        this(master);
        this.topicName = topicName;
        mTopic = mMasterRepository.getTopic(topicName);
        topicDetails = Transformations.map(mTopic, input -> {
            TopicDetailsViewModel result = new TopicDetailsViewModel();
            if (input != null) {
                if (input.data != null) {
                    result.topic = input.data;
                    if (input.data.publishers != null)
                        result.publishers = input.data.publishers;
                    if (input.data.subscribers != null)
                        result.subscribers = input.data.subscribers;
                }
            }
            return result;
        });
    }

}
