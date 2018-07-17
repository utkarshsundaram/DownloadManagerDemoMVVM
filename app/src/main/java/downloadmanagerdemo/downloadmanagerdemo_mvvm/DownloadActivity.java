package downloadmanagerdemo.downloadmanagerdemo_mvvm;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import downloadmanagerdemo.downloadmanagerdemo_mvvm.databinding.ActivityDownloadBinding;

public class DownloadActivity extends AppCompatActivity {
    private ActivityDownloadBinding binding;
    private DownloadViewModel mDownloadViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(Util.checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && Util.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))) { // if true request permission else not
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, Constants.PERMISSION_CALLBACK_CONSTANT);
            } else {
                afterPermissionGranted();
            }
        } else {

            afterPermissionGranted();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }

    /**
     * Bind the view after  we download the file
     */
    private void afterPermissionGranted()
    {
        binding = DataBindingUtil.setContentView(DownloadActivity.this, R.layout.activity_download);
        mDownloadViewModel = new DownloadViewModel(this);
        binding.setDownloadmanager(mDownloadViewModel);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CALLBACK_CONSTANT: {
                int permissionGrantCount = 0;
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        permissionGrantCount++;
                    }
                    if ((permissionGrantCount == 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) || permissionGrantCount == 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        afterPermissionGranted();
                    } else {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                Constants.PERMISSION_CALLBACK_CONSTANT);
                    }

                    break;
                }
            }
        }
    }

    /**
     *
     * A broadcast receiver to set the tone after the file has been downloaded
     *
     */
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                Cursor cursor=null;
                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                try {
                    cursor = manager.query(query);
                }catch (Exception e){
                    e.printStackTrace();
                }

                assert cursor != null;
                if ((cursor.moveToFirst())&&(cursor!=null)) {
                    if (cursor.getCount() > 0) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            String file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            Toast.makeText(context,getString(R.string.download_successful)+file,Toast.LENGTH_LONG).show();
                            // So something here on success
                        } else {
                            int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                            // So something here on failed.
                            try {
                                int bytes_downloaded = cursor.getInt(cursor
                                        .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }
                }
            }
        }


    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }
}

