package nl.joostvanstuijvenberg.introspect.data;

import java.util.ArrayList;

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

public class Service {

    public String name = "";
    public String requestType = "";
    public String responseType = "";
    public Node provider;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Service))
            return false;
        return this.name.equals(((Service) obj).name) && this.requestType.equals(((Service) obj).requestType);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
