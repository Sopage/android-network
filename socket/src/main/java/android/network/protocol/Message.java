package android.network.protocol;

import android.network.sdk.body.BodyType;
import android.network.sdk.body.StringBody;
import android.os.Parcel;

import java.util.UUID;

/**
 * @author Mr.Huang
 * @date 2017/11/29
 */
public class Message extends com.dream.socket.codec.Message implements android.os.Parcelable, Protocol {

    private String id;
    private int type;
    private int sender;
    private int recipient;
    private byte[] body;

    public Message(int sender, Body body) {
        this(id(), sender, -1, body);
    }

    public Message(int sender, int recipient, Body body) {
        this(id(), sender, recipient, body);
    }

    public Message(String id, int sender, int recipient, Body body) {
        this.id = id;
        this.type = body.getType();
        this.sender = sender;
        this.recipient = recipient;
        this.body = body.toArray();
    }

    public Message(int type, int sender, int recipient, byte[] body) {
        this(id(), type, sender, recipient, body);
    }

    public Message(String id, int type, int sender, int recipient, byte[] body) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
    }

    public final String getId() {
        return id;
    }

    public final int getType() {
        return type;
    }

    public final int getSender() {
        return sender;
    }

    public final int getRecipient() {
        return recipient;
    }

    public final byte[] getBodyArray() {
        if (this.body == null) {
            return new byte[0];
        }
        return body;
    }

    private Message(Parcel in) {
        id = in.readString();
        type = in.readInt();
        sender = in.readInt();
        recipient = in.readInt();
        int length = in.readInt();
        if (length > 0) {
            byte[] body = new byte[length];
            in.readByteArray(body);
            this.body = body;
        }
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
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
        if (body != null && body.length > 0) {
            parcel.writeInt(body.length);
            parcel.writeByteArray(body);
        }
    }

    private static String id() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Body getBody() {
        Body body = null;
        if (type == 0 || type == BodyType.STRING) {
            body = new StringBody();
        }
        if (body != null) {
            body.source(this.body);
        }
        return body;
    }
}
