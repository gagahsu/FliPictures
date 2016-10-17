package net.macdidi.flipicture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jiahunghsu on 2016/9/27.
 */
public class MyDBHelper {
    private SQLiteDatabase db = null;
    private final static String dbName = "db1.db";
    private final static String tableName = "picInfo";
    private final static String picPath = "picPath";
    private final static String picDesc = "picDesc";
    private final static String createTable = "CREATE TABLE " + tableName + "(" + picPath + " TEXT," + picDesc + " TEXT)";
    private Context context = null;

    public MyDBHelper(Context context){
        this.context = context;
    }

    protected void open(){
        db = context.openOrCreateDatabase(dbName,0,null);
        createTable();
    }

    protected void createTable(){
        try{
            db.execSQL(createTable);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    protected void close(){
        db.close();
    }

    protected String getDescByPath(String path){
        Cursor cr = db.query(tableName, new String[]{picPath, picDesc}, picPath + "='" + path + "'", null, null, null ,null, null);
        String desc = "";
        if(null != cr && cr.getCount() > 0 && cr.moveToFirst()){
            desc = cr.getString(1);
        }
        return desc;
    }

    protected void append(String path, String desc){
        String insSt = "INSERT INTO " + tableName + " ( " + picPath + "," + picDesc + " ) values( '" + path + "', '" + desc + "');";
        System.out.println("Insert : " + insSt);
        db.execSQL(insSt);
    }

    protected void updDescByPath(String path, String desc){
        ContentValues cv = new ContentValues();
        cv.put("picDesc", desc);
        //String updSt = "UPDATE " + tableName + " SET " + picDesc + " = '" +desc + "' WHERE " + picPath + " = '" + path + "';";
        try{
            int num = db.update(tableName, cv, picPath + " = ?", new String[]{path});
            if(num == 0){
                append(path,desc);
            }
            System.out.println("upd : " + num);
            //db.execSQL(updSt);
        }catch (Exception e){
            System.out.println("Update Error" + e.getMessage());
        }
    }
}
