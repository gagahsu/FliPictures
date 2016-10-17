package net.macdidi.flipicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by jiahunghsu on 2016/10/16.
 */

public class RemarkActivity  extends Activity {
    private Context context;
    MyDBHelper myDb = null;
    String path;
    EditText remark;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_rmk);
        context = this;
        remark = (EditText)findViewById(R.id.rmkText);
        ImageButton save = (ImageButton)findViewById(R.id.rmkSave);
        ImageButton back = (ImageButton)findViewById(R.id.rmkBack);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        path = bundle.getString("path");
        myDb = new MyDBHelper(context);
        myDb.open();

        remark.setText(myDb.getDescByPath(path.replace("/","")));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updDescByPath(path.replace("/", ""), remark.getText().toString());
                Toast.makeText(v.getContext(), v.getResources().getString(R.string.SaveMsg), Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.close();
                finish();
            }
        });
    }
}
