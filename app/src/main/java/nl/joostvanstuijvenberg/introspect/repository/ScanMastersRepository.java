package nl.joostvanstuijvenberg.introspect.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Master;

/**
 * Created by Joost van Stuijvenberg on 27/10/2017.
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

public class ScanMastersRepository {

    public static final String TAG = "ScanMastersRepository";

    private MutableLiveData<List<Master>> foundMasters = new MutableLiveData<>();

    public LiveData<List<Master>> getFoundMasters() {
        return foundMasters;
    }

    public void scan(final String startIP, final String endIP) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress startAddress = InetAddress.getByName(startIP);
                    InetAddress endAddress = InetAddress.getByName(endIP);
                    ArrayList<Master> results = new ArrayList<>();
                    int[] begin = new int[4];
                    int[] end = new int[4];
                    for (int i = 0; i < 4; i++) {
                        begin[i] = Integer.valueOf(startAddress.getAddress()[i] & 0xFF);
                        end[i] = Integer.valueOf(endAddress.getAddress()[i] & 0xFF);
                    }

                    MasterProbe probe = new MasterProbe();
                    for (int a = begin[0]; a <= end[0]; a++) {
                        for (int b = begin[1]; b <= end[1]; b++) {
                            for (int c = begin[2]; c <= end[2]; c++) {
                                for (int d = begin[3]; d <= end[3]; d++) {
                                    String host = a + "." + b + "." + c + "." + d;
                                    Master m = new Master(host, 11311);
                                    Log.d(TAG, "Trying " + m.toString() + "...");
                                    if (probe.isMasterReachable(m)) {
                                        Log.i(TAG, "Found a ROS core at: " + host);
                                        //TODO: master POJO en master-functionaliteit scheiden
                                        results.add(m);
                                        foundMasters.postValue(results);
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ioe) {
                }
            }
        }).start();
    }

    private class MasterProbe {

        public boolean isMasterReachable(Master master) {
            boolean result = false;
            try {
                // First try a 'ping'...
                if (InetAddress.getByName(master.getHost()).isReachable(100)) {
                    // ...then try a TCP socket connection...
                    Socket s = new Socket(master.getHost(), master.getPort());
                    if (s.isConnected()) {
                        s.close();
                        result = true;
                    }
                }
            } catch (IOException ioe) {
            }
            return result;
        }

    }

}
