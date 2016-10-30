package seoyuki.yuza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class FirstInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_info);

        final SharedPreferences checkFirstInfoEnd = getSharedPreferences("checkFirstInfoEnd", MODE_PRIVATE);
        SharedPreferences.Editor editor = checkFirstInfoEnd.edit();
        editor.putBoolean("isFirstInfoEnd", true);
        editor.commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("반가워요!");

        ImageView firstInfoStartImage = (ImageView) findViewById(R.id.firstInfoStartImage);
        firstInfoStartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("yuza", "main start: ");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
