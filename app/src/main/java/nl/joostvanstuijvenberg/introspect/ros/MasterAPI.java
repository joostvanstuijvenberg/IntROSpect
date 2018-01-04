package nl.joostvanstuijvenberg.introspect.ros;

import java.util.Map;

import nl.joostvanstuijvenberg.introspect.data.Graph;

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

public interface MasterAPI {

    /**
     * Sets the timeout of the XML-RPC connection.
     * @param timeout in seconds.
     */
    void setTimeout(int timeout);

    /**
     * MasterProxy API: getSystemState
     * @return a Graph object
     */
    Graph getSystemState();

    /**
     * MasterProxy API: getTopicTypes
     * @return a Map of topics and their type.
     */
    Map<String, String> getTopicTypes();

}
