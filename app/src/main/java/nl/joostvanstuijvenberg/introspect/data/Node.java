package nl.joostvanstuijvenberg.introspect.data;

import java.util.ArrayList;
import java.util.List;

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

public class Node {

    public String name = "";
    public List<Topic> subscribed = new ArrayList<>();
    public List<Topic> publishing = new ArrayList<>();
    public List<Service> providing = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Node))
            return false;
        Node n = (Node) obj;
        return this.name.equals(n.name) &&
                this.subscribed.equals(n.subscribed) &&
                this.publishing.equals(n.publishing) &&
                this.providing.equals(n.providing);
    }

    @Override
    public int hashCode() {
        if (name == null || subscribed == null || publishing == null || providing == null)
            return 31;
        return 31 + name.hashCode() + subscribed.hashCode() + publishing.hashCode() + providing.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
