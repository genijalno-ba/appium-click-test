package ba.genijalno.android.appiumclicktest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

  private String TAG = MainActivity.class.getSimpleName();

  private Button btnClickMe;
  private int count = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    count = 0;
    btnClickMe = findViewById(R.id.btn_click_me);
    TextView tvVersionName = findViewById(R.id.tv_version_name);
    tvVersionName.setText(BuildConfig.VERSION_NAME);
  }

  public void incrementClicks(View view) {
    ++count;
    Intent secondActivity = new Intent(this, SecondActivity.class);
    startActivityForResult(secondActivity, 5);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 5 && resultCode == RESULT_OK) {
      AsyncTask<Void, Integer, Void> delayedTask = new RandomDelay(4, 5, this);
      delayedTask.execute();
    }
  }

  @Override
  public boolean handleMessage(@NonNull Message message) {
    setContentView(R.layout.activity_main);
    Button newBtnClickMe = findViewById(R.id.btn_click_me);
    if (btnClickMe.toString().equals(newBtnClickMe.toString())) {
      throw new AssertionError("buttons are the same");
    } else {
      Log.i(TAG, "New button instance");
    }
    newBtnClickMe.setText(getString(R.string.click_me_counter, count));
    TextView tvVersionName = findViewById(R.id.tv_version_name);
    tvVersionName.setText(BuildConfig.VERSION_NAME);
    btnClickMe = newBtnClickMe;
    return true;
  }
}