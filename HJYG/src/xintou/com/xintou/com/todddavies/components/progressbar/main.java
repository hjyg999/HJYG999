package xintou.com.xintou.com.todddavies.components.progressbar;

import android.app.Activity;
import android.os.Bundle;

/**
 * A sample activity showing some of the functions of the progress bar
 */
public class main extends Activity {
	boolean running;
	ProgressWheel pw_two;
	int progress = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.progress_wheel_activity);
		// pw_two = (ProgressWheel) findViewById(R.id.progressBarTwo);
		pw_two.progress = 90;

	}

}
