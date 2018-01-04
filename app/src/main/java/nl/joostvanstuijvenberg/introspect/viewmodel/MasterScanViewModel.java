package nl.joostvanstuijvenberg.introspect.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.webkit.URLUtil;

import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.repository.ScanMastersRepository;

/**
 * Created by Joost van Stuijvenberg on 23/10/2017.
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

public class MasterScanViewModel extends AndroidViewModel {

    public final MutableLiveData<String> startIP = new MutableLiveData<>();
    public final MutableLiveData<String> startIPError = new MutableLiveData<>();
    public final MutableLiveData<String> endIP = new MutableLiveData<>();
    public final MutableLiveData<String> endIPError = new MutableLiveData<>();
    private ScanMastersRepository mScanMastersRepository;

    public MasterScanViewModel(Application application) {
        super(application);
        mScanMastersRepository = new ScanMastersRepository();
        startIP.setValue("192.168.9.1");
        startIPError.setValue("");
        endIP.setValue("192.168.9.254");
        endIPError.setValue("");
    }

    public boolean validate() {
        boolean errors = false;
        String startIPError = "";
        String endIPError = "";

        if (startIP.getValue().isEmpty()) {
            errors = true;
            startIPError = "Specify an IP-address.";
        }

        if (endIP.getValue().isEmpty()) {
            errors = true;
            startIPError = "Specify an IP-address.";
        }

        if (!errors) {
            if (!URLUtil.isValidUrl("http://" + startIP.getValue())) {
                errors = true;
                startIPError = "This is not a valid IP-address.";
            }
            if (!URLUtil.isValidUrl("http://" + endIP.getValue())) {
                errors = true;
                startIPError = "This is not a valid IP-address.";
            }
        }

        this.startIPError.setValue(startIPError);
        this.endIPError.setValue(endIPError);
        return !errors;
    }

    public LiveData<List<Master>> getFoundMasters() {
        return mScanMastersRepository.getFoundMasters();
    }

    public void startScanning() {
        mScanMastersRepository.scan(startIP.getValue(), endIP.getValue());
    }

}
