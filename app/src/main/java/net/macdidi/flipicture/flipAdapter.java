package net.macdidi.flipicture;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jiahunghsu on 2016/9/30.
 */
public class flipAdapter  extends BaseAdapter {

    private ArrayList<String> pathList;
    private LayoutInflater inflater;
    private Context context;
    ViewHolder viewHolder;
    String imgPath;
    HashMap<String, String> exifInfo ;

    public flipAdapter(Context context, ArrayList<String> pathList) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pathList = pathList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public Object getItem(int position) {
        return pathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        imgPath = pathList.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_main, parent, false);
            viewHolder.mainImgView = (ImageView)convertView.findViewById(R.id.mainImgView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();

        ImageLoader.ImageListener upon_listener = ImageLoader.getImageListener(viewHolder.mainImgView,
                R.drawable.ic_loading, R.drawable.ic_error);
        imageLoader.get(imgPath, upon_listener);
        viewHolder.mainImgView.setTag(imgPath);
        exifInfo = getImgExifInfo(imgPath);

        viewHolder.mainImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("path", v.getTag().toString());
                extras.putString("time", exifInfo.get("date"));
                extras.putString("width", exifInfo.get("width"));
                extras.putString("length", exifInfo.get("length"));
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView mainImgView;
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
}
