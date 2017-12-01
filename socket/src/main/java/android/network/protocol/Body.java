package android.network.protocol;

import android.network.sdk.body.StringBody;
import android.os.Parcel;

import com.dream.socket.codec.Message;

import java.util.UUID;

/**
 * @author Mr.Huang
 * @date 2017/11/29
 */
public class Body extends Message implements android.os.Parcelable, Protocol {

    public static final int TYPE_STRING = 1000;

    private String id;
    private int type;
    private int sender;
    private int recipient;
    private byte[] body;

    public Body(Body src){
        copy(src, this);
    }

    public Body(int type) {
        this(type, -1, -1);
    }

    public Body(int type, int recipient) {
        this(type, -1, recipient);
    }

    public Body(int type, int sender, int recipient) {
        this.id = id();
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Body(byte[] id, int type, int sender, int recipient, byte[] body) {
        this.id = new String(id);
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final byte[] getIdBytes() {
        return getIdBytes(id);
    }

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final int getSender() {
        return sender;
    }

    public final void setSender(int sender) {
        this.sender = sender;
    }

    public final int getRecipient() {
        return recipient;
    }

    public final void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public byte[] getBody() {
        return body;
    }

    public final void setBody(byte[] body) {
        this.body = body;
    }

    private Body(Parcel in) {
        id = in.readString();
        type = in.readInt();
        sender = in.readInt();
        recipient = in.readInt();
        int length = in.readInt();
        if (length > 0) {
            body = new byte[length];
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
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(type);
        parcel.writeInt(sender);
        parcel.writeInt(recipient);
        byte[] body = getBody();
        if (body != null && body.length > 0) {
            parcel.writeInt(body.length);
            parcel.writeByteArray(body);
        }
    }

    private static byte[] getIdBytes(String id) {
        if (id != null) {
            byte[] bytes = id.getBytes();
            if (bytes.length == Protocol.ID_LENGTH) {
                return bytes;
            }
            byte[] idBytes = new byte[Protocol.ID_LENGTH];
            if (bytes.length > Protocol.ID_LENGTH) {
                System.arraycopy(bytes, 0, idBytes, 0, idBytes.length);
            } else {
                System.arraycopy(bytes, 0, idBytes, 0, bytes.length);
            }
            return idBytes;
        }
        return new byte[Protocol.ID_LENGTH];
    }

    private static String id() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static void copy(Body src, Body dest){
        dest.setId(src.getId());
        dest.setSender(src.getSender());
        dest.setRecipient(src.getRecipient());
        dest.setRemoteAddress(src.getRemoteAddress());
        dest.setType(src.getType());
    }

    public Body getBodyType(){
        switch (type){
            case TYPE_STRING:
                return new StringBody(this);
        }
        return this;
    }
}
