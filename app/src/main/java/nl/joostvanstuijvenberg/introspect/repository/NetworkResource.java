package nl.joostvanstuijvenberg.introspect.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Joost van Stuijvenberg on 10/11/2017.
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

public class NetworkResource<T> {

    public enum Status { LOADING, AVAILABLE, TIMEOUT, ERROR };

    @NonNull public final Status status;
    @Nullable public final T data;

    private NetworkResource(@NonNull Status status, @Nullable T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> NetworkResource<T> loading(@Nullable T data) {
        return new NetworkResource<>(Status.LOADING, data);
    }

    public static <T> NetworkResource<T> available(@NonNull T data) {
        return new NetworkResource<>(Status.AVAILABLE, data);
    }

    public static <T> NetworkResource<T> timeout(@Nullable T data) {
        return new NetworkResource<>(Status.TIMEOUT, data);
    }

    public static <T> NetworkResource<T> error(@Nullable T data) {
        return new NetworkResource<>(Status.AVAILABLE, data);
    }

}
