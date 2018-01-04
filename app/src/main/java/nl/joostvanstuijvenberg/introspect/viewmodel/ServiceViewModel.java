package nl.joostvanstuijvenberg.introspect.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import nl.joostvanstuijvenberg.introspect.data.Graph;
import nl.joostvanstuijvenberg.introspect.data.Master;
import nl.joostvanstuijvenberg.introspect.data.Node;
import nl.joostvanstuijvenberg.introspect.data.Service;
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

public class ServiceViewModel extends ViewModel {

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private Master mMaster;
        private String mServiceName;

        public Factory(Master master, String serviceName) {
            mMaster = master;
            mServiceName = serviceName;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ServiceViewModel(mMaster, mServiceName);
        }

    }

    public class ServiceListViewModel {
        public List<Service> services = new ArrayList<>();
        public boolean loading = false;
        public boolean error = false;
        public String errorText = "";
    }

    public class ServiceDetailsViewModel {
        public Service service = null;
        public Node provider = null;
    }

    private final Master master;
    private String ServiceName;
    private MasterRepository mMasterRepository;
    private LiveData<NetworkResource<Graph>> mGraph;
    private LiveData<NetworkResource<Service>> mService;
    public LiveData<ServiceListViewModel> serviceList;
    public LiveData<ServiceDetailsViewModel> serviceDetails;


    public ServiceViewModel(Master master) {
        this.master = master;
        mMasterRepository = new MasterRepository(master);
        mGraph = mMasterRepository.getGraph();
        serviceList = Transformations.map(mGraph, input -> {
            ServiceListViewModel result = new ServiceListViewModel();
            if (input != null) {
                if (input.data != null)
                    result.services = input.data.getServices();
                result.loading = (input.status == NetworkResource.Status.LOADING);
                result.error = (input.status == NetworkResource.Status.ERROR);
                result.errorText = "Something went wrong.";
            }
            return result;
        });
    }

    public ServiceViewModel(Master master, String ServiceName) {
        this(master);
        this.ServiceName = ServiceName;
        mService = mMasterRepository.getService(ServiceName);
        serviceDetails = Transformations.map(mService, input -> {
            ServiceDetailsViewModel result = new ServiceDetailsViewModel();
            if (input != null) {
                if (input.data != null) {
                    result.service = input.data;
                    if (input.data.provider != null)
                        result.provider = input.data.provider;
                }
            }
            return result;
        });
    }

}
