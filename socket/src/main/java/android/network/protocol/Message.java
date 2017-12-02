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
    private Body body;

    public Message(int type, int sender) {
        this(type, sender, null);
    }

    public Message(int type, int sender, Body body) {
        this(id(), type, sender, -1, body);
    }

    public Message(String id, int type, int sender, int recipient, Body body) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
    }

    public Message(String id, int type, int sender, int recipient, byte[] body) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.body = create(body);
    }

    public final String getId() {
        return id;
    }

    public final byte[] getIdBytes() {
        return getIdBytes(id);
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
        byte[] body;
        if (this.body == null || (body = this.body.toArray()) == null) {
            return new byte[0];
        }
        return body;
    }

    public final Body getBody() {
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
            this.body = create(body);
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
        byte[] array = body.toArray();
        if (array != null && array.length > 0) {
            parcel.writeInt(array.length);
            parcel.writeByteArray(array);
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

    private Body create(byte[] body) {
        if (type == 0 || type == BodyType.STRING) {
            return new StringBody(new String(body));
        }
        return null;
    }
}
