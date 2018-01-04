package nl.joostvanstuijvenberg.introspect.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import nl.joostvanstuijvenberg.introspect.data.Graph;
import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.repository.MasterRepository;
import nl.joostvanstuijvenberg.introspect.repository.NetworkResource;

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

public class MasterDetailsViewModel extends ViewModel {

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private Master mMaster;

        public Factory(Master master) {
            mMaster = master;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MasterDetailsViewModel(mMaster);
        }

    }

    public class MasterViewModel {
        public int numberOfNodes;
        public int numberOfTopics;
        public int numberOfServices;
        public boolean enableButtons;
        public boolean showLoading;
        public boolean showTimeout;
        public boolean showError;
    }

    private MasterRepository mMasterRepository;
    public final LiveData<MasterViewModel> masterViewModel;

    public MasterDetailsViewModel(Master master) {
        mMasterRepository = new MasterRepository(master);
        LiveData<NetworkResource<Graph>> g = mMasterRepository.getGraph();
        masterViewModel = Transformations.map(g, networkResource -> {
            MasterViewModel result = new MasterViewModel();
            if (networkResource != null) {
                result.showLoading = networkResource.status == NetworkResource.Status.LOADING;
                result.showTimeout = networkResource.status == NetworkResource.Status.TIMEOUT;
                result.showError = networkResource.status == NetworkResource.Status.ERROR;
                if (networkResource.data != null) {
                    result.enableButtons = !result.showError && !result.showTimeout;
                    Graph graph = networkResource.data;
                    result.numberOfNodes = graph.getNodes().size();
                    result.numberOfTopics = graph.getTopics().size();
                    result.numberOfServices = graph.getServices().size();
                }
            }
            return result;
        });
    }

    public void setRefreshInterval(int refreshInterval) {
        mMasterRepository.setRefreshInterval(refreshInterval);
    }

    @Override
    protected void onCleared() {
        mMasterRepository.stop();
    }
}
