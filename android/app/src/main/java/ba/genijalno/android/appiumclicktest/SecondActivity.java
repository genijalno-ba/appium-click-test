package ba.genijalno.android.appiumclicktest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity implements Handler.Callback {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
    TextView tvVersionName = findViewById(R.id.tv_version_name);
    tvVersionName.setText(BuildConfig.VERSION_NAME);
  }

  public void closeSecondActivity(View view) {
    AsyncTask<Void, Integer, Void> delayedTask = new RandomDelay(1, 2, this);
    delayedTask.execute();
  }

  @Override
  public boolean handleMessage(@NonNull Message message) {
    setResult(RESULT_OK);
    finish();
    return true;
  }
}
