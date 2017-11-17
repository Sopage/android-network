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
                    params.put("参数1", "参数值1");
                    params.put("参数2", "参数值2");
                    params.put("参数3", "参数值3");
                    File file = new File("/mnt/sdcard/image.jpg");
                    Map<String, File[]> fileMap = new HashMap<>();
                    fileMap.put("file", new File[]{file});
                    String s = Http.get("http://10.0.2.2:8080/get", null, params);
                    Log.e("ESA", s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
