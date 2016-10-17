package net.macdidi.flipicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by jiahunghsu on 2016/9/28.
 */
public class PicInfoActivity extends Activity {
    ImageButton imgbtn1;
    TextView textView ;//text_content
    String path = "";
    String time = "";
    String width = "";
    String length = "";
    MyDBHelper myDb = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_info);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        path = bundle.getString("path");
        time = bundle.getString("time");
        width = bundle.getString("width");
        length = bundle.getString("length");
        imgbtn1=(ImageButton)findViewById(R.id.imageButton);
        textView = (TextView)findViewById(R.id.text_content);

        myDb = new MyDBHelper(this);
        myDb.open();
        //getResources().getString(R.string.DateTime);
        String str = getResources().getString(R.string.DateTime) + time   + "\n" +
                     getResources().getString(R.string.Length)   + length + "\n" +
                     getResources().getString(R.string.Width)    + width  + "\n" +
                     getResources().getString(R.string.Path)     + path   + "\n" +
                     getResources().getString(R.string.Desc)     + myDb.getDescByPath(path.replace("/","")) + "\n" ;
        textView.setText(str);
        imgbtn1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myDb.close();
                finish();
            }

        });
    }
}
