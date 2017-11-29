package android.network.protocol;

import android.os.Parcel;

import com.dream.socket.codec.Message;

/**
 * @author Mr.Huang
 * @date 2017/11/29
 */
public class Body extends Message implements android.os.Parcelable, Protocol {

    public Body() {

    }

    protected Body(Parcel in) {
    }

    public static final Creator<Body> CREATOR = new Creator<Body>() {
        @Override
        public Body createFromParcel(Parcel in) {
            return new Body(in);
        }

        @Override
        public Body[] newArray(int size) {
            return new Body[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
