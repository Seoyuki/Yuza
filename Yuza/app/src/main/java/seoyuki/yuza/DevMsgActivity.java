package seoyuki.yuza;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DevMsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devmessage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("개발자 한마디");

        TextView helloTxt = (TextView) findViewById(R.id.message1);
        helloTxt.setText(readTxt());

    }

    private String readTxt() {
        String data = null;
        InputStream inputStream = getResources().openRawResource(R.raw.message1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }

            data = new String(byteArrayOutputStream.toByteArray(),"UTF-8");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}