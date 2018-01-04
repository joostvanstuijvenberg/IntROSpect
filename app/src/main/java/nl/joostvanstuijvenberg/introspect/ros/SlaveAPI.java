package nl.joostvanstuijvenberg.introspect.ros;

import nl.joostvanstuijvenberg.introspect.data.Node;

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

public interface SlaveAPI {

    /**
     * Sets the timeout of the XML-RPC connection.
     * @param timeout in seconds.
     */
    void setTimeout(int timeout);

    void getBusStats();

    void getBusInfo();

    void getMasterUri();

    void shutdown();

    int getPid(Node node);

    void getSubscriptions(Node node);

    void getPublications(Node node);

    void paramUpdate();

    void publisherUpdate();

    void requestTopic();

}
