package net.macdidi.flipicture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by jiahunghsu on 2016/9/30.
 */
public class GetPicPath {
    Context context;

    public GetPicPath(Context context){
        this.context = context;
    }

    protected ArrayList<String> getPathList(){
        ContentResolver cr = context.getContentResolver();
        ArrayList<String> imagePaths;  //存放圖片的路徑
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

        //查詢SD卡的圖片
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        imagePaths = new ArrayList<String>();

        for (int i = 0; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑

            imagePaths.add(filepath);
        }
        cursor.close();

        return imagePaths;
    }

}
