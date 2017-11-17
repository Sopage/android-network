package android.network.http.sample;

import android.app.Activity;
import android.network.http.core.Http;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class UploadActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        findViewById(R.id.btn_upload).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new Thread(){
            @Override
            public void run() {
                try {
                    Map<String, String> params = new HashMap<>();
                    params.put("a", "a");
                    params.put("b", "b");
                    params.put("c", "c");
                    File file = new File("/mnt/sdcard/image.jpg");
                    Map<String, File[]> fileMap = new HashMap<>();
                    fileMap.put("file", new File[]{file});
                    String s = Http.upload("http://192.168.31.43:8080/upload", params, fileMap);
                    Log.e("ESA", s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
