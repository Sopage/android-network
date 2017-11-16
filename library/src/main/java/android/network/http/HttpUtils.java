package android.network.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
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

public class HttpUtils {

    private static Map<String, String> cookieCache = new HashMap<String, String>();
    private static SSLContext sslcontext;
    private static HostnameVerifier hostnameVerifier;

    public static String httpGet(String url, Map<String, String> params) throws IOException {
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(urlEncode(entry.getValue())).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
            if (url.indexOf("?") > 0) {
                url += sb.toString();
            } else {
                url = url + "?" + sb.toString();
            }
        }
        return httpGet(url);
    }

    public static String httpGet(String url) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        int responseCode = conn.getResponseCode();
        StringBuffer sb = null;
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return sb != null ? sb.toString() : null;
    }

    public static String httpPost(String url, Map<String, String> params) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(urlEncode(entry.getValue())).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        OutputStream out = conn.getOutputStream();
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
        sb.delete(0, sb.length());
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } else {
            sb = null;
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return sb != null ? sb.toString() : null;
    }

    public static String httpUpload(String url, Map<String, String> params, Map<String, File[]> fileMap) throws IOException {
        String boundary = "---------------------------";
        String endLine = "--" + boundary + "--";
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
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        /***************************************参数body***************************************/
        StringBuilder paramsBody = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                paramsBody.append("--").append(boundary).append("\r\n");
                paramsBody.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n\r\n");
                paramsBody.append(entry.getValue());
                paramsBody.append("\r\n");
            }
        }


        /***************************************文件body***************************************/
        StringBuilder fileBody = new StringBuilder();
        long allFileLength = 0;
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, File[]> entry : fileMap.entrySet()) {
                String fieldName = entry.getKey();
                File[] files = entry.getValue();
                if (files == null || files.length == 0) {
                    continue;
                }
                for (File file : files) {
                    fileBody.append("--").append(boundary).append("\r\n");
                    fileBody.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getName()).append("\"\r\n");
                    fileBody.append("Content-Type: application/octet-stream\r\n\r\n");
                    allFileLength += file.length();
                    fileBody.append("\r\n");
                }
            }
        }

        /***************************************计算body大小***************************************/
        int paramsBodyLength = paramsBody.length();//参数body大小
        int fileBodyLength = fileBody.length();//文件body大小
        int endLineLength = endLine.length();//结束符行大小
        long length = paramsBodyLength + fileBodyLength + allFileLength + endLineLength;
        if (length > Integer.MAX_VALUE) {
            throw new RuntimeException("body length long error");
        }
        conn.setFixedLengthStreamingMode((int) length);//设置body大小

        OutputStream out = conn.getOutputStream();
        out.write(paramsBody.toString().getBytes());//发送参数body
        out.flush();

        long progress = paramsBodyLength;
        Log.e("ESA", String.valueOf(progress * 1.0f / length));

        byte[] buffer = new byte[10240];
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, File[]> entry : fileMap.entrySet()) {
                String fieldName = entry.getKey();
                File[] files = entry.getValue();
                if (files == null || files.length == 0) {
                    continue;
                }
                for (File file : files) {
                    fileBody.delete(0, fileBody.length());
                    fileBody.append("--").append(boundary).append("\r\n");
                    fileBody.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getName()).append("\"\r\n");
                    fileBody.append("Content-Type: application/octet-stream\r\n\r\n");
                    out.write(fileBody.toString().getBytes());

                    progress += fileBody.length();
                    Log.e("ESA", String.valueOf(progress * 1.0f / length));

                    FileInputStream fis = new FileInputStream(file);
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);

                        progress += len;
                        Log.e("ESA", String.valueOf(progress * 1.0f / length));

                    }
                    out.write("\r\n".getBytes());
                    out.flush();
                    fis.close();

                    progress += 2;
                    Log.e("ESA", String.valueOf(progress * 1.0f / length));
                }
            }
        }
        out.write(endLine.getBytes());//发型结束符行
        out.flush();

        progress += endLine.length();
        Log.e("ESA", String.valueOf(progress/length * 1.0f));

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            fileBody.delete(0, fileBody.length());
            String line;
            while ((line = reader.readLine()) != null) {
                fileBody.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        out.close();
        conn.disconnect();
        return fileBody.length() > 0 ? fileBody.toString() : null;
    }

    public static boolean download(String url, File saveFile) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        int responseCode = conn.getResponseCode();
        boolean result = false;
        if (responseCode == 200) {
            InputStream stream = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(saveFile);
            byte[] buffer = new byte[1024 * 5];
            int len;
            while ((len = stream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.close();
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
