/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.youtubetv.showcase.samples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fr.bmartel.youtubetv.YoutubeTvView;
import fr.bmartel.youtubetv.listener.IPlayerListener;
import fr.bmartel.youtubetv.model.VideoInfo;
import fr.bmartel.youtubetv.model.VideoState;
import fr.bmartel.youtubetv.showcase.R;

/**
 * YoutubeActivityApiShowcase
 *
 * @author Bertrand Martel
 */
public class YoutubeActivityApiShowcase extends Activity {

    private final static String TAG = YoutubeActivityApiShowcase.class.getSimpleName();

    private YoutubeTvView mYoutubeView1;

    private final static int POSITION_OFFSET = 5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_showcase);

        mYoutubeView1 = (YoutubeTvView) findViewById(R.id.video1);

        Button playBtn = (Button) findViewById(R.id.play_button);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYoutubeView1.start();
            }
        });
        Button pauseBtn = (Button) findViewById(R.id.pause_button);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYoutubeView1.pause();
            }
        });
        Button nextBtn = (Button) findViewById(R.id.next_button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYoutubeView1.nextVideo();
            }
        });
        Button previousBtn = (Button) findViewById(R.id.previous_button);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYoutubeView1.previousVideo();
            }
        });

        Button backwardBtn = (Button) findViewById(R.id.backward_button);
        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYoutubeView1.moveBackward(POSITION_OFFSET);
            }
        });
        Button forwardBtn = (Button) findViewById(R.id.forward_button);
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYoutubeView1.moveForward(POSITION_OFFSET);
            }
        });

        mYoutubeView1.addPlayerListener(new IPlayerListener() {
            @Override
            public void onPlayerReady(final VideoInfo videoInfo) {
                Log.i(TAG, "onPlayerReady");
            }

            @Override
            public void onPlayerStateChange(final VideoState state,
                                            final long position,
                                            final float speed,
                                            final float duration,
                                            final VideoInfo videoInfo) {
                Log.i(TAG, "onPlayerStateChange : " + state.toString() + " | position : " + position + " | speed : " + speed);
            }
        });
        requestVisibleBehind(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestVisibleBehind(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYoutubeView1.closePlayer();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}