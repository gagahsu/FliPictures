package net.macdidi.flipicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by jiahunghsu on 2016/9/27.
 */
public class DetailActivity extends Activity {
    private Context context;
    private ImageView imgView;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton fab_info, fab_rotate_left, fab_rotate_right, fab_remark;
    String path = "";
    String textView = "";
    String time = "";
    String width = "";
    String length = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_detail);
        context = this;
        imgView = (ImageView) findViewById(R.id.imageView);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fab_info = (FloatingActionButton) findViewById(R.id.fab_info);
        fab_rotate_left = (FloatingActionButton) findViewById(R.id.fab_rotate_left);
        fab_rotate_right = (FloatingActionButton) findViewById(R.id.fab_rotate_right);
        fab_remark = (FloatingActionButton) findViewById(R.id.fab_remark);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        path = bundle.getString("path");
        textView = bundle.getString("textView");
        time = bundle.getString("time");
        width = bundle.getString("width");
        length = bundle.getString("length");
        File imgFile = new File(path);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgView.setImageBitmap(myBitmap);
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ViewGroup.LayoutParams params = imgView.getLayoutParams();  //需import android.view.ViewGroup.LayoutParams;
            //params.height = ScreenHeight/2;
            imgView.setLayoutParams(params);
        }
        fab_rotate_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rotate(imgView, -90, path);
            }
        });

        fab_rotate_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rotate(imgView, 90, path);
            }
        });

        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Info(path);
            }
        });

        fab_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                HashMap<String, String> exifInfo = getImgExifInfo(path);
                extras.putString("path", path);
                extras.putString("date", exifInfo.get("date"));
                extras.putString("time", exifInfo.get("time"));
                extras.putString("width", exifInfo.get("width"));
                extras.putString("length", exifInfo.get("length"));
                Intent intent = new Intent(context, RemarkActivity.class);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent newIntent = new Intent(context,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }
        return false;
    }

    private void jump2Info(String path){
        Bundle extras = new Bundle();
        HashMap<String, String> exifInfo = getImgExifInfo(path);
        extras.putString("path", path);
        extras.putString("date", exifInfo.get("date"));
        extras.putString("time", exifInfo.get("time"));
        extras.putString("width", exifInfo.get("width"));
        extras.putString("length", exifInfo.get("length"));
        Intent intent = new Intent(context, PicInfoActivity.class);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    private HashMap<String, String> getImgExifInfo(String picUri){
        HashMap<String, String> exifInfo = new HashMap<>();
        String verHoz;
        String date  ;
        String time = "";
        try {
            ExifInterface exifInterface = new ExifInterface(picUri);
            int width = Integer.valueOf(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            int length = Integer.valueOf(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            String picDateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            if (length > width || length == width){
                verHoz = "vertical";
            }else{
                verHoz = "horizontal";
            }

            if(null == picDateTime || "".equals(picDateTime.trim())){
                date = "";
            }else{
                date = picDateTime.substring(0,10).replace(":", "/");
                time = picDateTime.substring(11, picDateTime.length());
            }
            exifInfo.put("date"  , date);
            exifInfo.put("time"  , time);
            exifInfo.put("verHoz", verHoz);
            exifInfo.put("width" , String.valueOf(width));
            exifInfo.put("length", String.valueOf(length));
        }catch(Exception e){

        }
        return exifInfo;
    }
    private void rotate(ImageView imgView, int rotateAngle, String path){
        imgView.buildDrawingCache();
        //imageView_left.setDrawingCacheEnabled(true);
        //建立圖片的緩存，圖片的緩存本身就是一個Bitmap
        imgView.buildDrawingCache();
        //取得緩存圖片的Bitmap檔
        //Bitmap bmp = imgView.getDrawingCache();
        Bitmap bmp = BitmapFactory.decodeFile(path);
        //定義一個矩陣圖
        System.out.println(path);
        Matrix m=  new Matrix();
        //取得圖片的寬度
        int width = bmp.getWidth();
        //取得圖片的長度
        int height = bmp.getHeight();
        //順時針旋轉90度
        m.setRotate(rotateAngle , (float) width, (float)height);
        //產生新的旋轉後Bitmap檔
        Bitmap b = null;

        try{
            b = Bitmap.createBitmap(bmp, 0, 0, width, height, m, true);
            String[] strarr = path.split("/");
            String fileDir = "";
            String fileName = "";
            for(int i = 0; i<strarr.length;i++){
                if(i + 1 < strarr.length){
                    fileDir = fileDir + strarr[i] + "/";
                }else{
                    fileName = strarr[i];
                }
            }
            System.out.println(fileName + "," + fileDir);
            saveBmpToSd(fileDir, b, fileName, 100, false);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file:/" + fileDir)));
        }catch(Exception e){
            System.out.println("Error : " + e.getMessage());
        }
        //顯示圖片
        imgView.setImageBitmap(b);
    }

    public static boolean saveBmpToSd(String dir, Bitmap bm, String filename, int quantity, boolean recyle) {
        boolean ret = true;
        if (bm == null) {
            return false;}
        File dirPath = new File(dir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        File file = new File(dir, filename);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, quantity, outStream);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (recyle && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
        return ret;
    }
}
