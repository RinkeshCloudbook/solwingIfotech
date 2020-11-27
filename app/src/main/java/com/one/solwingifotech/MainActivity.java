package com.one.solwingifotech;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.DownloadStatusListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;

public class MainActivity extends AppCompatActivity {

    final String URL2 = "http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk";

    private RecyclerView recyclerView;
    private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PRDownloader.initialize(getApplicationContext());
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        recyclerView = findViewById(R.id.recyclerView);


        adapter = new MyListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                String url = ((EditText) findViewById(R.id.edt_url)).getText().toString();
                DownloadModel download = new DownloadModel();
                download.downloadUrl = url;
                download.downloadId = -1;
                adapter.AddToDownload(download);
            }
        });
    }
}