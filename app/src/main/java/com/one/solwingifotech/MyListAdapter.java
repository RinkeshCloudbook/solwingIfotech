package com.one.solwingifotech;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<DownloadModel> listdata = new ArrayList<>();
    private Context context;

    // RecyclerView recyclerView;
    public MyListAdapter(Context context) {
        this.context = context;
    }

    public void AddToDownload(DownloadModel download) {
        listdata.add(download);
        notifyItemInserted(listdata.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.e("TEST", "ID = "+listdata.get(position).downloadId);
        if (listdata.get(position).downloadId == -1)
            startDownload(holder, position);

        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PRDownloader.cancel(listdata.get(position).downloadId);
            }
        });
        holder.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.buttonStart.setText("Resume");
                if (listdata.get(position).downloadId == -1)
                    startDownload(holder, position);
                else {
                    Status status = PRDownloader.getStatus(listdata.get(position).downloadId);
                    if (status == Status.PAUSED)
                        PRDownloader.resume(listdata.get(position).downloadId);
                }
            }
        });
    }


    private void startDownload(ViewHolder holder, int position) {
        DownloadModel data = listdata.get(position);
        String path = getRootDirPath(context);
        Log.e("TEST","Path :"+path+" | "+data.downloadUrl);
        String fileName = System.currentTimeMillis()+".mp4";
        Log.e("TEST", "Path = "+path+" | "+fileName);
        int downloadId = PRDownloader.download(data.downloadUrl, path, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
Log.e("TEST", "Started");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        int prog = (int) (progress.currentBytes * 100 / progress.totalBytes);
                        holder.progressBar.setProgress(prog);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                    }

                    @Override
                    public void onError(Error error) {

    Log.e("TEST", "Error =  "+error.getResponseCode());
                    }
                });

        listdata.get(position).downloadId = downloadId;
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button buttonCancel;
        public Button buttonStart;
        public TextView textViewProgressOne;
        public ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            this.buttonStart = (Button) itemView.findViewById(R.id.buttonStart);
            this.buttonCancel = (Button) itemView.findViewById(R.id.buttonCancel);
            this.textViewProgressOne = (TextView) itemView.findViewById(R.id.textViewProgressOne);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
