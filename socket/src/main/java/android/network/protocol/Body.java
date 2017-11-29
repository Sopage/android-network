package android.network.protocol;

import android.os.Parcel;

import com.dream.socket.codec.Message;

/**
 * @author Mr.Huang
 * @date 2017/11/29
 */
public class Body extends Message implements android.os.Parcelable, Protocol {

    private int type;
    private int bodyLength;
    private byte[] body;

    public Body() {

    }

    public Body(int type, byte[] body) {
        this.type = type;
        this.body = body;
        this.bodyLength = body.length;
    }

    protected Body(Parcel in) {
        type = in.readInt();
        bodyLength = in.readInt();
        if (bodyLength > 0) {
            body = new byte[bodyLength];
            in.readByteArray(body);
        }
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
        parcel.writeInt(type);
        body = getBody();
        if (body != null && body.length > 0) {
            parcel.writeInt(body.length);
            parcel.writeByteArray(body);
        }
    }

    public int getType() {
        return type;
    }

    public byte[] getBody() {
        return body;
    }

}
