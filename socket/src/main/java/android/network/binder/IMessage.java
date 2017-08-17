package android.network.binder;

import android.os.Parcel;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */

public class IMessage implements android.os.Parcelable {

    public IMessage(){

    }

    protected IMessage(Parcel in) {

    }

    public static final Creator<IMessage> CREATOR = new Creator<IMessage>() {
        @Override
        public IMessage createFromParcel(Parcel in) {
            return new IMessage(in);
        }

        @Override
        public IMessage[] newArray(int size) {
            return new IMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
