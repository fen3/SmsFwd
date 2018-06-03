package net.fenv.smsfwd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText edittxt;
    private boolean flag;
    private int state;
    private int MY_SMS_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btSave);
        edittxt = (EditText) findViewById(R.id.editText2);
        String confstring = getSettingNote(MainActivity.this, "configstring");
        edittxt.setText(confstring);
        getPermission();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberStr = edittxt.getText().toString();
                saveSettingNote(MainActivity.this,"configstring",numberStr);
                //state = CHANGE;
                //buttonState(state);
                Toast.makeText(MainActivity.this,"保存号码成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getPermission(){
        for (String permission:new String[] {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {permission}, MY_SMS_PERMISSION);
            }
        }
    }

    public static void saveSettingNote(Context context, String key, String saveData){//保存设置
        SharedPreferences.Editor settingsave = context.getSharedPreferences("configstring", Activity.MODE_PRIVATE).edit();
        settingsave.putString(key, saveData);
        settingsave.commit();
    }
    public static String getSettingNote(Context context,String key){//获取保存设置
        SharedPreferences read = context.getSharedPreferences("configstring", Activity.MODE_PRIVATE);
        return read.getString(key, "");
    }
}
