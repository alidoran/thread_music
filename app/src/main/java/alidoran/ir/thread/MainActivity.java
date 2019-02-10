package alidoran.ir.thread;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	ImageButton imgBtn;
	TextView titleTextView;
	MediaPlayer mp;
	SeekBar seekbar;
	Handler handler;
	Runnable runnable;
	ImageView songImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imgBtn = (ImageButton) findViewById(R.id.playBtn);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		songImageView = (ImageView) findViewById(R.id.songImage);
		titleTextView = (TextView) findViewById(R.id.title);
		
		titleTextView.setText("Ashoobam - Chartar");
		songImageView.setImageResource(R.drawable.song_image);
		mp = MediaPlayer.create(MainActivity.this, R.raw.music);

		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				updateSeekbar();
			}
		};

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				long now = (long) ((float) seekbar.getProgress() / 100 * mp
						.getDuration());
				mp.seekTo((int) now);
				handler.postDelayed(runnable, 1000);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				handler.removeCallbacks(runnable);
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {

			}
		});

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				pause();
			}
		});

		imgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = (String) imgBtn.getTag();
				if (s.equals("pause")) {
					play();
				} else {
					pause();
				}
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		mp.pause();
		imgBtn.setBackgroundResource(R.drawable.play_selector);
	}

	@Override
	protected void onResume() {
		super.onResume();
		String s = (String) imgBtn.getTag();
		if (!s.equals("pause")) {
			mp.start();
			imgBtn.setBackgroundResource(R.drawable.pause_selector);
		}

	}

	public void updateSeekbar() {

		float progress = ((float) mp.getCurrentPosition() / mp.getDuration());
		seekbar.setProgress((int) (progress * 100));
		handler.postDelayed(runnable, 1000);

	}

	public void pause() {
		mp.pause();
		imgBtn.setTag("pause");
		imgBtn.setBackgroundResource(R.drawable.play_selector);
		handler.removeCallbacks(runnable);
	}

	public void play() {
		mp.start();
		imgBtn.setTag("play");
		imgBtn.setBackgroundResource(R.drawable.pause_selector);
		updateSeekbar();
	}

}
