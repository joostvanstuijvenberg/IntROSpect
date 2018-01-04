package nl.joostvanstuijvenberg.introspect.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import nl.joostvanstuijvenberg.introspect.Constants;
import nl.joostvanstuijvenberg.introspect.data.Master;

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

public class MasterNewViewModel extends AndroidViewModel {

    public enum State {EMPTY, CHECKING, ERROR, CHECKED}

    private MutableLiveData<State> mState;
    public String host;
    public String hostError;
    public int port = Constants.DEFAULT_PORT_NUMBER;
    public String portError;

    public MasterNewViewModel(Application application) {
        super(application);
    }

    public LiveData<State> getState() {
        if (mState == null) {
            mState = new MutableLiveData<>();
            mState.setValue(State.EMPTY);
        }
        return mState;
    }

    public void connect() {
        mState.setValue(State.CHECKING);
        hostError = "";
        portError = "";

        // Validate the given host and port information.
        try {
            Master m = new Master(host, port);
        } catch (IllegalArgumentException iae) {
            if (iae instanceof Master.IllegalMasterNameException)
                hostError = iae.getMessage();
            if (iae instanceof Master.IllegalMasterPortException)
                portError = iae.getMessage();
        }

        if (!hostError.isEmpty() || !portError.isEmpty())
            mState.setValue(State.ERROR);
        else
            mState.setValue(State.CHECKED);
    }

}
