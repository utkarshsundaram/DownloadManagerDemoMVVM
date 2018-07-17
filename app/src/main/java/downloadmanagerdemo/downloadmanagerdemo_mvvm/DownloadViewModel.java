package downloadmanagerdemo.downloadmanagerdemo_mvvm;

import android.app.DownloadManager;
import android.content.Context;
import android.databinding.BaseObservable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import static downloadmanagerdemo.downloadmanagerdemo_mvvm.Constants.downloadingFile;

/**
 * Created by user on 6/7/18.
 */

public class DownloadViewModel extends BaseObservable
{
    private Context mContext;
    DownloadManager dManager;
    public DownloadViewModel(Context mContext){
        this.mContext=mContext;
        dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }
    public View.OnClickListener downloadFileClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectivityUtils.isNetworkEnabled(mContext)){
                    String fileName = downloadingFile.substring(downloadingFile.lastIndexOf("/") + 1);

                    // Do something else.
                    // Create Download Request object
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse((downloadingFile)));
                    // Display download progress and status message in notification bar
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED|DownloadManager.Request.VISIBILITY_VISIBLE);
                    // Set description to display in notification
                    request.setDescription("Download This "  + " from " + fileName);
                    // Set title
                    request.setTitle("DownloadManager");
                    // Set destination location for the downloaded file
                    request.setDestinationUri(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + fileName));
                    dManager.enqueue(request);
                }else{
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.not_downloaded),Toast.LENGTH_LONG).show();
                }

            }
        };
    }
}
