package com.leo.automatically_deploy_android_applications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.leo.automatically_deploy_android_applications.Utils.ALog;
import com.leo.automatically_deploy_android_applications.Utils.TypeConvertUtils;
import com.leo.automatically_deploy_android_applications.core.decor.Generate;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String xmlString = null;
        try {
            InputStream is = this.getAssets().open("desfile");
            xmlString = TypeConvertUtils.InputStreamTOString(is, "UTF-8");
            ALog.i("xml", xmlString + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(xmlString)) {
            Generate generate = new Generate();
            generate.init(this);
            setContentView(generate.generateView(xmlString));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
