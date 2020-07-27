package ba.genijalno.android.appiumclicktest;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Random;

public class RandomDelay extends AsyncTask<Void, Integer, Void> {

  private String TAG = RandomDelay.class.getSimpleName();
  private final String sourceTag;

  private final int low;
  private final int high;
  private final Handler.Callback callback;

  public RandomDelay(int low, int high, Handler.Callback callback) {
    this.low = low;
    this.high = high;
    this.callback = callback;
    this.sourceTag = callback instanceof MainActivity
        ? MainActivity.class.getSimpleName() : SecondActivity.class.getSimpleName();
  }

  @Override
  protected Void doInBackground(Void... voids) {
    try {
      long randomMilliseconds = randomMilliseconds();
      Log.i(TAG, "(" + sourceTag + ") Delaying for " + randomMilliseconds + " ms");
      Thread.sleep(randomMilliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    Log.i(TAG, "(" + sourceTag + ") Delaying for done, invoke callback...");
    callback.handleMessage(Message.obtain());
  }

  private long randomMilliseconds() {
    Random r = new Random();
    int result = r.nextInt(high - low) + low;
    return result * 1000;
  }
}
