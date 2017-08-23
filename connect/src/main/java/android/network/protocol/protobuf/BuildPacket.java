package android.network.protocol.protobuf;

import android.network.protocol.Packet;

import com.google.protobuf.ByteString;

import java.nio.charset.Charset;

public class BuildPacket {

    public static Packet buildBody(String id, BodyType type, int sender, ByteString content) {
        Protobuf.Body.Builder builder = Protobuf.Body.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        builder.setType(type.getTypeCode());
        builder.setSender(sender);
        if (content != null) {
            builder.setContent(content);
        }
        return new Packet(builder.build().toByteArray());
    }

    public static ByteString buildResponse(int code, ByteString data) {
        Protobuf.Response.Builder builder = Protobuf.Response.newBuilder();
        builder.setCode(code);
        if (data != null) {
            builder.setData(data);
        }
        return builder.build().toByteString();
    }

    public static ByteString buildMessage(int receiver, MessageType type, ByteString content) {
        Protobuf.Message.Builder builder = Protobuf.Message.newBuilder();
        builder.setReceiver(receiver);
        builder.setType(type.getTypeCode());
        if (content != null) {
            builder.setContent(content);
        }
        return builder.build().toByteString();
    }

    public static Packet buildTextMessage(int sender, int receiver, int type, String text) {
        ByteString message = buildMessage(receiver, MessageType.getType(type), ByteString.copyFrom(text, Charset.forName("UTF-8")));
        return buildBody(String.valueOf(System.currentTimeMillis()), BodyType.MESSAGE, sender, message);
    }

    public static Packet buildLogin(int sender, String token) {
        Protobuf.Login login = Protobuf.Login.newBuilder().setToke("token: " + String.valueOf(System.currentTimeMillis())).build();
        return buildBody(String.valueOf(System.currentTimeMillis()), BodyType.LOGIN, sender, login.toByteString());
    }

    public static Packet buildLogout(int sender) {
        return buildBody(String.valueOf(System.currentTimeMillis()), BodyType.LOGOUT, sender, null);
    }

}
