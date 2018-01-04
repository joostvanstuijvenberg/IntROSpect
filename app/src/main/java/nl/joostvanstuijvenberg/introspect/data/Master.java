package nl.joostvanstuijvenberg.introspect.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.net.InetAddress;

import nl.joostvanstuijvenberg.introspect.Constants;

/**
 * Created by Joost van Stuijvenberg on 28/10/2017.
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

public class Master implements Parcelable {

    public static final Parcelable.Creator<Master> CREATOR = new Parcelable.Creator<Master>() {
        @Override
        public Master createFromParcel(Parcel parcel) {
            return new Master(parcel);
        }

        @Override
        public Master[] newArray(int size) {
            return new Master[size];
        }
    };

    public class IllegalMasterNameException extends IllegalArgumentException {
        public IllegalMasterNameException(String message) {
            super(message);
        }
    }

    public class IllegalMasterPortException extends IllegalArgumentException {
        public IllegalMasterPortException(String message) {
            super(message);
        }
    }

    private String mHost;
    private int mPort;

    /**
     * This constructor is needed for permanent storage of masters in SharedPreferences.
     *
     * @param uri host name and port number, separated by a semicolon
     */
    public Master(String uri) throws IllegalArgumentException {
        setHost(uri.substring(0, uri.indexOf(':')));
        setPort(Integer.valueOf(uri.substring(uri.indexOf(':') + 1, uri.length())));
    }

    public Master(String host, int port) throws IllegalArgumentException {
        setHost(host);
        setPort(port);
    }

    public Master(Parcel parcel) {
        mHost = parcel.readString();
        mPort = parcel.readInt();
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) throws IllegalMasterNameException {
        if (host == null || host.isEmpty())
            throw new IllegalMasterNameException("Invalid value for a host name or IP address.");
        mHost = host;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) throws IllegalMasterPortException {
        if (port <= 0 || port > 65535)
            throw new IllegalMasterPortException(("Invalid value for a TCP port."));
        mPort = port;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mHost);
        parcel.writeInt(mPort);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Master))
            return false;
        if (this == obj)
            return true;
        return this.mHost.equals(((Master) obj).mHost) && this.mPort == (((Master) obj).mPort);
    }

    @Override
    public int hashCode() {
        return mHost.hashCode() * mPort;
    }

    @Override
    public String toString() {
        return mHost + ":" + mPort;
    }

}
