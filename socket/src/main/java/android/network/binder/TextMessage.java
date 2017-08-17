package android.network.binder;

import android.os.Parcel;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */

public class TextMessage extends IMessage implements android.os.Parcelable {

    private int receiver;
    public String text;

    public TextMessage(){

    }

    protected TextMessage(Parcel in) {
        super(in);
        receiver = in.readInt();
        text = in.readString();
    }

    public static final Creator<IMessage> CREATOR = new Creator<IMessage>() {
        @Override
        public TextMessage createFromParcel(Parcel in) {
            return new TextMessage(in);
        }

        @Override
        public TextMessage[] newArray(int size) {
            return new TextMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(receiver);
        dest.writeString(text);
    }
}
