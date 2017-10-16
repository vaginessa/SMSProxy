package net.dgistudio.guillaume.webbysms;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillaume on 14/05/2016.
 */
public class ParcelableRequest extends Request implements Parcelable {
    public static final Creator<ParcelableRequest> CREATOR = new Creator<ParcelableRequest>() {
        @Override
        public ParcelableRequest createFromParcel(Parcel in) {
            return new ParcelableRequest(in);
        }

        @Override
        public ParcelableRequest[] newArray(int size) {
            return new ParcelableRequest[size];
        }
    };

    public ParcelableRequest(String url) {
        super(url);
    }


    protected ParcelableRequest(Parcel in) {
        super(in.readString());
    }

    public static ParcelableRequest createFromRequest(Request request) {
        return new ParcelableRequest(request.getUrl());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getUrl());
        dest.writeString(this.getMethod());
    }
}
