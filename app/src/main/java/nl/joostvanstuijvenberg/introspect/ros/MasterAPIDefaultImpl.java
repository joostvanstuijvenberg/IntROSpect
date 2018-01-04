package nl.joostvanstuijvenberg.introspect.ros;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCTimeoutException;
import nl.joostvanstuijvenberg.introspect.data.Graph;
import nl.joostvanstuijvenberg.introspect.ros.MasterAPI;

/**
 * Created by Joost van Stuijvenberg on 22/11/2017.
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

class MasterAPIDefaultImpl implements MasterAPI {

    /**
     * Our side of the connection to the ROS master node.
     */
    private XMLRPCClient client;

    public MasterAPIDefaultImpl(URL url) {
        client = new XMLRPCClient(url);
    }

    /**
     * @see MasterAPI
     */
    @Override
    public void setTimeout(int timeout) {
        client.setTimeout(timeout);
    }

    /**
     * @see MasterAPI
     */
    @Override
    public Graph getSystemState() {
        try {
            Graph graph = null;
            Object[] answer = (Object[]) client.call("getSystemState", "/");
            Object[] systemState = (Object[]) answer[2];
            graph = new Graph(systemState);
            return graph;
        } catch (XMLRPCTimeoutException xrte) {
            return null;
        } catch (XMLRPCException xre) {
            return null;
        }
    }

    /**
     * @see MasterAPI
     */
    @Override
    public Map<String, String> getTopicTypes() {
        try {
            HashMap<String, String> result = new HashMap<>();
            Object[] answer = (Object[]) client.call("getTopicTypes", "/");
            Object[] topicTypes = (Object[]) answer[2];
            for (Object topicType : topicTypes) {
                Object[] topicAndType = (Object[]) topicType;
                String topic = (String) topicAndType[0];
                String type = (String) topicAndType[1];
                result.put(topic, type);
            }
            return result;
        } catch (XMLRPCTimeoutException xrte) {
            return null;
        } catch (XMLRPCException xre) {
            return null;
        }
    }

}
