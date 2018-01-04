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
import nl.joostvanstuijvenberg.introspect.data.Service;
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

public class NodeViewModel extends ViewModel {

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private Master mMaster;
        private String mNodeName;

        public Factory(Master master, String nodeName) {
            mMaster = master;
            mNodeName = nodeName;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new NodeViewModel(mMaster, mNodeName);
        }

    }

    public class NodeListViewModel {
        public List<Node> nodes = new ArrayList<>();
        public boolean loading = false;
        public boolean timeout = false;
        public boolean error = false;
        public String errorText = "";
    }

    public class NodeDetailsViewModel {
        public Node node;
        public List<Topic> publishingTo = new ArrayList<>();
        public List<Topic> subscribedTo = new ArrayList<>();
        public List<Service> providing = new ArrayList<>();
    }

    //private final Master master;
    private String nodeName;
    private MasterRepository mMasterRepository;
    private LiveData<NetworkResource<Node>> mNode;
    private LiveData<NetworkResource<Graph>> mGraph;
    public LiveData<NodeListViewModel> nodeList;
    public LiveData<NodeDetailsViewModel> nodeDetails;

    public NodeViewModel(Master master) {
        //this.master = master;
        mMasterRepository = new MasterRepository(master);
        mGraph = mMasterRepository.getGraph();
        nodeList = Transformations.map(mGraph, input -> {
            NodeListViewModel result = new NodeListViewModel();
            if (input != null) {
                if (input.data != null)
                    result.nodes = input.data.getNodes();
                result.loading = (input.status == NetworkResource.Status.LOADING);
                result.timeout = (input.status == NetworkResource.Status.TIMEOUT);
                result.error = (input.status == NetworkResource.Status.ERROR);
                result.errorText = "Something went wrong.";
            }
            return result;
        });
    }

    public NodeViewModel(Master master, String nodeName) {
        this(master);
        this.nodeName = nodeName;
        mNode = mMasterRepository.getNode(nodeName);
        nodeDetails = Transformations.map(mNode, input -> {
            NodeDetailsViewModel result = new NodeDetailsViewModel();
            if (input != null) {
                if (input.data != null) {
                    result.node = input.data;
                    if (input.data.publishing != null)
                        result.publishingTo = input.data.publishing;
                    if (input.data.subscribed != null)
                        result.subscribedTo = input.data.subscribed;
                    if (input.data.providing != null)
                        result.providing = input.data.providing;
                }
            }
            return result;
        });
    }

}
