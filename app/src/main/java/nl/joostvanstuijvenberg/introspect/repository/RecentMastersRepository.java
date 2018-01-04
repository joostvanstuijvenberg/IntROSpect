package nl.joostvanstuijvenberg.introspect.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.joostvanstuijvenberg.introspect.data.Master;

/**
 * Created by Joost van Stuijvenberg on 09/10/2017.
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

public class RecentMastersRepository {

    private static final String TAG = "RecentMastersRepository";
    private static final String RECENT_MASTERS_FILE = "recent_masters";
    private static final String RECENT_MASTERS_KEY = "masters";
    private Application mApplication;
    private SharedPreferences mSharedPreferences;
    private ArrayList<Master> mMasters;
    private MutableLiveData<List<Master>> mRecentMasters;

    public RecentMastersRepository(Application application) {
        Log.d(TAG, "onCreate()");
        mApplication = application;
        mSharedPreferences = mApplication.getSharedPreferences(RECENT_MASTERS_FILE, Context.MODE_PRIVATE);
        mMasters = new ArrayList<>();
        mRecentMasters = new MutableLiveData<>();
        loadRecentMasters();
    }

    public LiveData<List<Master>> getRecentMasters() {
        return mRecentMasters;
    }

    public void addRecentMaster(Master master) {
        Log.d(TAG, "addRecentMaster(), master=" + master);
        if (!mMasters.contains(master)) {
            mMasters.add(master);
            saveRecentMasters();
            mRecentMasters.setValue(mMasters);
        }
    }

    public void removeRecentMaster(Master master) {
        Log.d(TAG, "removeRecentMaster(), master=" + master);
        if (mMasters.contains(master)) {
            mMasters.remove(master);
            saveRecentMasters();
            mRecentMasters.setValue(mMasters);
        }
    }

    public void clearRecentMasters() {
        Log.d(TAG, "clearRecentMasters()");
        if (!mMasters.isEmpty()) {
            mMasters.clear();
            saveRecentMasters();
            mRecentMasters.setValue(mMasters);
        }
    }

    public void loadRecentMasters() {
        Log.d(TAG, "loadRecentMasters()");
        Set<String> set = mSharedPreferences.getStringSet(RECENT_MASTERS_KEY, null);
        if (set != null) {
            for (String s : set) {
                Master m = new Master(s);
                mMasters.add(m);
            }
            Log.i(TAG, "Loaded " + set.size() + " recent masters from shared preferences.");
        }
        mRecentMasters.setValue(mMasters);
    }

    public void saveRecentMasters() {
        Log.d(TAG, "saveRecentMasters()");
        Set<String> set = new HashSet<>();
        for (Master m : mMasters) {
            set.add(m.toString());
        }
        SharedPreferences.Editor mSharedPreferencesEditor = mSharedPreferences.edit();
        mSharedPreferencesEditor.putStringSet(RECENT_MASTERS_KEY, set);
        mSharedPreferencesEditor.commit();
        Log.d(TAG, "Saved " + set.size() + " recent masters to shared preferences.");

    }

}
