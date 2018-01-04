package nl.joostvanstuijvenberg.introspect.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.repository.RecentMastersRepository;

/**
 * Created by Joost van Stuijvenberg on 10/10/2017.
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

public class MasterListViewModel extends AndroidViewModel {

    private RecentMastersRepository mRecentMastersRepository;
    private LiveData<List<Master>> mRecentMasters;

    public MasterListViewModel(Application application) {
        super(application);
        mRecentMastersRepository = new RecentMastersRepository(application);
    }

    public LiveData<List<Master>> getRecentMasters() {
        if (mRecentMasters == null)
            mRecentMasters = mRecentMastersRepository.getRecentMasters();
        return mRecentMasters;
    }

    public void addRecentMaster(Master master) {
        mRecentMastersRepository.addRecentMaster(master);
    }

    public void clearRecentMasters() {
        mRecentMastersRepository.clearRecentMasters();
    }

}
