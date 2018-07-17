package downloadmanagerdemo.downloadmanagerdemo_mvvm;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by user on 6/7/18.
 */

public class Util
{
    public static boolean checkPermission(Context mContext, String permission) {
        if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
}
