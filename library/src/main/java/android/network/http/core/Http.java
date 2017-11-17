package android.network.http.core;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Http {

    private static Map<String, String> cookieCache = new HashMap<>();
    private static SSLContext sslcontext;
    private static HostnameVerifier hostnameVerifier;

    public static String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        if (params != null && params.size() > 0) {
            url = URLFormat.getFormatUrl(url, params);
        }
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        addHeaders(conn, headers);
        int responseCode = conn.getResponseCode();
        StringBuffer body = null;
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            body = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return body != null ? body.toString() : null;
    }

    public static String post(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        URL _url = new URL(URLFormat.getFormatUrl(url, null));
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        addHeaders(conn, headers);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        StringBuilder body = new StringBuilder();
        if (params != null && params.size() > 0) {
            boolean isAppendAt = false;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (isAppendAt) {
                    body.append("&");
                }
                body.append(entry.getKey()).append("=").append(urlEncode(entry.getValue()));
                isAppendAt = true;
            }
        }
        OutputStream out = conn.getOutputStream();
        out.write(body.toString().getBytes("UTF-8"));
        out.flush();
        out.close();
        body.delete(0, body.length());
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        } else {
            body = null;
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return body != null ? body.toString() : null;
    }

    public static String post(String url, Map<String, String> headers, String json) throws IOException {
        URL _url = new URL(URLFormat.getFormatUrl(url, null));
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        addHeaders(conn, headers);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        out.write(json.getBytes("UTF-8"));
        out.flush();
        out.close();
        StringBuilder body = new StringBuilder();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        } else {
            body = null;
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return body != null ? body.toString() : null;
    }

    /**
     * https://developer.android.com/reference/java/net/HttpURLConnection.html
     * To upload data to a web server, configure the connection for output using setDoOutput(true).
     * For best performance, you should call either setFixedLengthStreamingMode(int) when the body length is known in advance,
     * or setChunkedStreamingMode(int) when it is not.
     * Otherwise HttpURLConnection will be forced to buffer the complete request body in memory before it is transmitted,
     * wasting (and possibly exhausting) heap and increasing latency.
     */
    public static String upload(String url, Map<String, String> headers, Map<String, String> params, Map<String, File[]> fileMap, Progress progress) throws IOException {
        String boundary = "---------------------------";
        byte[] boundarys = boundary.getBytes("UTF-8");
        byte[] nextLine = "\r\n".getBytes("UTF-8");
        byte[] block = "--".getBytes("UTF-8");
        byte[] colon = "\"".getBytes("UTF-8");
        byte[] name = "Content-Disposition: form-data; name=".getBytes("UTF-8");
        byte[] fileName = "; filename=".getBytes("UTF-8");
        byte[] type = "Content-Type: application/octet-stream".getBytes("UTF-8");

        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        addHeaders(conn, headers);

        int contentLength = 0;
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value == null || value.trim().length() == 0) {
                    continue;
                }
                byte[] keyBytes = entry.getKey().getBytes("UTF-8");
                byte[] valueBytes = value.getBytes("UTF-8");
                contentLength += (block.length + boundarys.length + nextLine.length);
                contentLength += (name.length + colon.length + keyBytes.length + colon.length + nextLine.length);
                contentLength += nextLine.length;
                contentLength += valueBytes.length;
                contentLength += nextLine.length;
            }
        }
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, File[]> entry : fileMap.entrySet()) {
                File[] files = entry.getValue();
                if (files == null || files.length == 0) {
                    continue;
                }
                byte[] fieldNameBytes = entry.getKey().getBytes("UTF-8");
                for (File file : files) {
                    byte[] fileNameBytes = file.getName().getBytes("UTF-8");
                    contentLength += (block.length + boundarys.length + nextLine.length);
                    contentLength += (name.length + colon.length + fieldNameBytes.length + colon.length + fileName.length + +colon.length + fileNameBytes.length + colon.length + nextLine.length);
                    contentLength += type.length + nextLine.length;
                    contentLength += nextLine.length;
                    contentLength += file.length();
                    contentLength += nextLine.length;
                }
            }
        }
        contentLength += block.length + boundarys.length + block.length;
        conn.setFixedLengthStreamingMode(contentLength);

        int writeLength = 0;

        OutputStream out = conn.getOutputStream();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value == null || value.trim().length() == 0) {
                    continue;
                }
                byte[] keyBytes = entry.getKey().getBytes("UTF-8");
                byte[] valueBytes = value.getBytes("UTF-8");
                out.write(block);
                out.write(boundarys);
                out.write(nextLine);
                writeLength += (block.length + boundarys.length + nextLine.length);
                out.write(name);
                out.write(colon);
                out.write(keyBytes);
                out.write(colon);
                out.write(nextLine);
                writeLength += (name.length + colon.length + keyBytes.length + colon.length + nextLine.length);
                out.write(nextLine);
                writeLength += nextLine.length;
                out.write(valueBytes);
                writeLength += valueBytes.length;
                out.write(nextLine);
                writeLength += nextLine.length;
                out.flush();
                progress(contentLength, writeLength, progress);
            }
        }
        byte[] buffer = new byte[10240];
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, File[]> entry : fileMap.entrySet()) {
                File[] files = entry.getValue();
                if (files == null || files.length == 0) {
                    continue;
                }
                byte[] fieldNameBytes = entry.getKey().getBytes("UTF-8");
                for (File file : files) {
                    byte[] fileNameBytes = file.getName().getBytes("UTF-8");
                    out.write(block);
                    out.write(boundarys);
                    out.write(nextLine);
                    writeLength += (block.length + boundarys.length + nextLine.length);
                    out.write(name);
                    out.write(colon);
                    out.write(fieldNameBytes);
                    out.write(colon);
                    out.write(fileName);
                    out.write(colon);
                    out.write(fileNameBytes);
                    out.write(colon);
                    out.write(nextLine);
                    writeLength += (name.length + colon.length + fieldNameBytes.length + colon.length + fileName.length + +colon.length + fileNameBytes.length + colon.length + nextLine.length);
                    out.write(type);
                    out.write(nextLine);
                    writeLength += type.length + nextLine.length;
                    out.write(nextLine);
                    writeLength += nextLine.length;
                    progress(contentLength, writeLength, progress);
                    FileInputStream fis = new FileInputStream(file);
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        writeLength += len;
                        progress(contentLength, writeLength, progress);
                    }
                    out.write(nextLine);
                    writeLength += nextLine.length;
                    out.flush();
                    fis.close();
                    progress(contentLength, writeLength, progress);
                }
            }
        }
        out.write(block);
        out.write(boundarys);
        out.write(block);
        out.flush();
        writeLength += block.length + boundarys.length + block.length;
        progress(contentLength, writeLength, progress);

        StringBuilder body = new StringBuilder();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        out.close();
        conn.disconnect();
        return body.length() > 0 ? body.toString() : null;
    }

    public static boolean download(String url, Map<String, String> headers, int range, InputStreamCallback callback) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        if (range > 0) {
            conn.addRequestProperty("Range", "bytes=" + range + "-");
        }
        addHeaders(conn, headers);
        int responseCode = conn.getResponseCode();
        boolean result = false;
        if (responseCode == 200 || responseCode == 206) {
            int contentLength = conn.getContentLength();
            InputStream stream = conn.getInputStream();
            callback.stream(stream, contentLength);
            stream.close();
            result = true;
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return result;
    }

    private static void addHeaders(HttpURLConnection conn, Map<String, String> headers) {
        if (headers == null || headers.size() == 0) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void progress(int contentLength, int writeLength, Progress progress) {
        Log.e("ESA", String.valueOf(((float) writeLength) / ((float) contentLength)));
        if (progress != null) {
            progress.progress(contentLength, writeLength);
        }
    }

    private static String urlEncode(String params) {
        try {
            if (params == null) {
                return "";
            }
            return URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return params;
        }
    }

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection conn;
        if (url.toString().startsWith("https")) {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(getSSLContext().getSocketFactory());
            connection.setHostnameVerifier(getHostnameVerifier());
            conn = connection;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        return conn;
    }

    private static SSLContext getSSLContext() {
        if (sslcontext == null) {
            TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            try {
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return sslcontext;
    }

    private static HostnameVerifier getHostnameVerifier() {
        if (hostnameVerifier == null) {
            hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
        }
        return hostnameVerifier;
    }

}
