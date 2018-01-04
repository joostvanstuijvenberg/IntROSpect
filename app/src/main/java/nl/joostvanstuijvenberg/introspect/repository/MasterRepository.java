package nl.joostvanstuijvenberg.introspect.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import nl.joostvanstuijvenberg.introspect.data.Graph;
import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.data.MasterProxy;
import nl.joostvanstuijvenberg.introspect.data.Node;
import nl.joostvanstuijvenberg.introspect.data.Service;
import nl.joostvanstuijvenberg.introspect.data.Topic;

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
public class MasterRepository implements Runnable {

    private final Master mMaster;
    private MutableLiveData<NetworkResource<Graph>> mGraph = new MutableLiveData<>();
    private Graph previousGraph;
    private int refreshInterval = 5000;
    private Thread mThread;

    public MasterRepository(Master master) {
        mMaster = master;
        setRefreshInterval(refreshInterval);
        mThread = new Thread(this);
        mThread.start();
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void run() {
        while (true) {

            mGraph.postValue(NetworkResource.loading(previousGraph));
            try {
                MasterProxy masterProxy = new MasterProxy(mMaster);
                if (masterProxy.isReachable()) {
                    Graph g = masterProxy.getGraph();
                    mGraph.postValue(NetworkResource.available(g));
                    previousGraph = g;
                } else
                    mGraph.postValue((NetworkResource.timeout(null)));
            } catch (Exception e) {
                mGraph.postValue(NetworkResource.error(null));
            }

            try {
                Thread.sleep(refreshInterval);
            } catch (InterruptedException ie) {}
        }
    }

    public void stop() {
        mThread.interrupt();
    }

    public LiveData<NetworkResource<Graph>> getGraph() {
        return mGraph;
    }

    public LiveData<NetworkResource<Node>> getNode(final String nodeName) {
        final MutableLiveData<NetworkResource<Node>> result = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Node node = null;
                result.postValue(NetworkResource.loading(node));
                try {
                    MasterProxy masterProxy = new MasterProxy(mMaster);
                    if (masterProxy.isReachable()) {
                        node = masterProxy.getNode(nodeName);
                        result.postValue(NetworkResource.available(node));
                    } else
                        result.postValue((NetworkResource.timeout(null)));
                } catch (Exception e) {
                    result.postValue(NetworkResource.error(node));
                }
            }
        }).start();
        return result;
    }

    public LiveData<NetworkResource<Topic>> getTopic(final String topicName) {
        final MutableLiveData<NetworkResource<Topic>> result = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Topic topic = null;
                result.postValue(NetworkResource.loading(topic));
                try {
                    MasterProxy masterProxy = new MasterProxy(mMaster);
                    if (masterProxy.isReachable()) {
                        topic = masterProxy.getTopic(topicName);
                        result.postValue(NetworkResource.available(topic));
                    } else
                        result.postValue((NetworkResource.timeout(null)));
                } catch (Exception e) {
                    result.postValue(NetworkResource.error(topic));
                }
            }
        }).start();
        return result;
    }

    public LiveData<NetworkResource<Service>> getService(final String serviceName) {
        final MutableLiveData<NetworkResource<Service>> result = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Service service = null;
                result.postValue(NetworkResource.loading(service));
                try {
                    MasterProxy masterProxy = new MasterProxy(mMaster);
                    if (masterProxy.isReachable()) {
                        service = masterProxy.getService(serviceName);
                        result.postValue(NetworkResource.available(service));
                    } else
                        result.postValue((NetworkResource.timeout(null)));
                } catch (Exception e) {
                    result.postValue(NetworkResource.error(service));
                }
            }
        }).start();
        return result;
    }

}
