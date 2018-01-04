package nl.joostvanstuijvenberg.introspect.data;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import nl.joostvanstuijvenberg.introspect.Constants;
import nl.joostvanstuijvenberg.introspect.ros.MasterAPI;
import nl.joostvanstuijvenberg.introspect.ros.MasterAPIFactory;

/**
 * Created by Joost van Stuijvenberg on 06/10/2017.
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
public class MasterProxy {

    private Master master;
    private MasterAPI masterAPI;

    /**
     * Creates a ROS master proxy.
     * @param master wrapper that contains the network connection details.
     * @throws MalformedURLException
     */
    public MasterProxy(Master master) throws MalformedURLException {
        this.master = master;
        URL url = new URL("http://" + master.getHost() + ":" + master.getPort());
        masterAPI = MasterAPIFactory.getInstance(url);
    }

    /**
     * Checks to see if the master can be reached via IP.
     * @return whether the master is reachable or not.
     * @throws IOException
     */
    public boolean isReachable() throws IOException {
        if (InetAddress.getByName(master.getHost()).isReachable(Constants.PING_TIMEOUT))
            return true;
        return false;
    }

    /**
     * @see MasterAPI
     */
    public Graph getGraph() {
        return masterAPI.getSystemState();
    }

    /**
     * Returns one specific Node instance and its fine grained details, when available.
     * @param nodeName name of the requested node
     * @return a Node instance, or null when it was not found in the current graph.
     */
    public Node getNode(String nodeName) {
        Graph g = masterAPI.getSystemState();
        for (Node n : g.getNodes())
            if (n.name.equals(nodeName))
                return n;
        return null;
    }

    /**
     * Returns one specific Topic instance and its fine grained details, when available.
     * @param topicName name of the requested topic
     * @return a Topic instance, or null when it was not found in the current graph.
     */
    public Topic getTopic(String topicName) {
        Topic result = null;
        Graph g = masterAPI.getSystemState();
        for (Topic t : g.getTopics())
            if (t.name.equals(topicName)) {
                result = t;
                break;
            }
        Map<String, String> m = masterAPI.getTopicTypes();
        if (m.containsKey(result.name)) {
            result.type = m.get(result.name);
        }
        return result;
    }

    /**
     * Returns one specific Service instance and its fine grained details, when available.
     * @param serviceName name of the requested service
     * @return a Service instance, or null when it was not found in the current graph.
     */
    public Service getService(String serviceName) {
        Graph g = masterAPI.getSystemState();
        for (Service s : g.getServices())
            if (s.name.equals(serviceName))
                return s;
        return null;
    }

}
