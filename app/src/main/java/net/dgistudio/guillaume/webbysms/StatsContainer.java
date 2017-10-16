package net.dgistudio.guillaume.webbysms;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Guillaume on 14/05/2016.
 */
public class StatsContainer implements Parcelable {

    public static final int MINUTES_TO_LIVE = 10;
    public static final Creator<StatsContainer> CREATOR = new Creator<StatsContainer>() {
        @Override
        public StatsContainer createFromParcel(Parcel in) {
            return new StatsContainer(in);
        }

        @Override
        public StatsContainer[] newArray(int size) {
            return new StatsContainer[size];
        }
    };
    public int smsSent;
    public int smsReceived;
    public long lastTime;
    public ArrayList<ParcelableRequest> requests;

    public StatsContainer() {
        smsSent = 0;
        smsReceived = 0;
        lastTime = 0;
        requests = new ArrayList<>();
    }

    protected StatsContainer(Parcel in) {
        smsSent = in.readInt();
        smsReceived = in.readInt();
        lastTime = in.readLong();
        requests = in.createTypedArrayList(ParcelableRequest.CREATOR);
    }

    public int getSmsReceived() {
        return smsReceived;
    }

    public int getSmsSent() {
        return smsSent;
    }

    public long getRemainingTime() {
        return (MINUTES_TO_LIVE * 60) - getElapsedTime();
    }

    public long getMaxTime() {
        return MINUTES_TO_LIVE * 60;
    }

    public long getElapsedTime() {
        return (new Date().getTime() / 1000) - getLastTime();
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public ArrayList<ParcelableRequest> getParcelableRequests() {
        return requests;
    }

    public ArrayList<Request> getRequests() {
        ArrayList<Request> requests = new ArrayList<>();

        for (ParcelableRequest parcelableRequest : this.getParcelableRequests()) {
            requests.add((Request) parcelableRequest);
        }

        return requests;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(smsSent);
        dest.writeInt(smsReceived);
        dest.writeLong(lastTime);
        dest.writeTypedList(requests);
    }
}
