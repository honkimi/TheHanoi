package com.honkimi.hanoi;

import java.util.HashMap;

import org.taptwo.android.widget.TitleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.ui.widget.UIButton;
import br.com.dina.ui.widget.UIButton.ClickListener;
import br.com.dina.ui.widget.UITableView;

import com.honkimi.hanoi.AnimateStack.AnimCallback;
import com.honkimi.hanoi.AnimateStack.AnimComplete;

public class MainActivity extends Activity {
    private ViewFlow viewFlow;

    private int circleNum;

    private int currentAnimNo;
    private Hanoi hanoi;

    private Button startBtn;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.diff_title);
        setContentView(R.layout.title_layout);
        setViewPager();

        onHanoiCreate();
        onSourceCreate();
        onInfoCreate();

    }

    private void setViewPager() {
        viewFlow = (ViewFlow) findViewById(R.id.viewflow);
        PagerAdapter adapter = new PagerAdapter(this);
        viewFlow.setAdapter(adapter);
        TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
        indicator.setTitleProvider(adapter);
        viewFlow.setFlowIndicator(indicator);
    }

    private HanoiAnim hanoiAnim;

    private void onHanoiCreate() {
        startBtn = (Button) findViewById(R.id.starthanoi);
        hanoiAnim = new HanoiAnim();
        hanoi = new Hanoi(getNumber());
        hanoi.doHanoi(hanoi.num, hanoi.polls.get("S"), hanoi.polls.get("G"),
                hanoi.polls.get("B"));
        setNumClickListener();
        setSeekbarListener();

    }

    class HanoiAnim {
        public HashMap<String, AnimateStack> polls;
        private LinearLayout p1;

        public HanoiAnim() {
            p1 = (LinearLayout) findViewById(R.id.poll1);
            LinearLayout p2 = (LinearLayout) findViewById(R.id.poll2);
            LinearLayout p3 = (LinearLayout) findViewById(R.id.poll3);
            p1.removeAllViews();
            p2.removeAllViews();
            p3.removeAllViews();
            polls = new HashMap<String, AnimateStack>();
            circleNum = getNumber();
            polls.put("S", new AnimateStack(p1, "S", circleNum, handler));
            polls.put("B", new AnimateStack(p2, "B", 0, handler));
            polls.put("G", new AnimateStack(p3, "G", 0, handler));
            addInitialPolls();
        }

        private void addInitialPolls() {
            for (int i = circleNum; i != 0; i--) {
                ImageView target = new ImageView(MainActivity.this);
                target.setBackgroundResource(getResources().getIdentifier(
                        "circle" + (circleNum - i + 1), "drawable",
                        MainActivity.this.getPackageName()));
                target.setTag(circleNum - i + 1);
                p1.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                polls.get("S").asyncPush(target, null, getSpeed());
            }
        }

        public void move(String from, String to) {
            Log.v("LOG", "moving from :" + from + ", to: " + to);
            polls.get(from).asyncPop(polls.get(to), new AnimCallback() {
                @Override
                public void finished(AnimateStack to, ImageView v) {
                    to.asyncPush(v, new AnimComplete() {
                        @Override
                        public void finished() {
                            currentAnimNo++;
                            if (currentAnimNo < hanoi.moveList.size()) {
                                move(hanoi.moveList.get(currentAnimNo)[0],
                                        hanoi.moveList.get(currentAnimNo)[1]);
                            } else {
                                Toast.makeText(MainActivity.this, "Done!",
                                        Toast.LENGTH_SHORT).show();
                                startBtn.setClickable(true);
                                startBtn.setText("Restart Hanoi");
                                startBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        onHanoiCreate();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                doHanoi(v);
                                            }
                                        }, 2000);
                                    }
                                });
                            }
                        }
                    }, getSpeed());
                }
            }, getSpeed());
        }
    }

    public void doHanoi(View v) {
        currentAnimNo = 0;
        hanoiAnim.move(hanoi.moveList.get(currentAnimNo)[0],
                hanoi.moveList.get(currentAnimNo)[1]);
        startBtn.setText("Now Hanoing..");
    }

    private void onSourceCreate() {
        WebView wv = (WebView) findViewById(R.id.source);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/html/source.html");
    }

    private void onInfoCreate() {
        ((UIButton) findViewById(R.id.thanks))
                .addClickListener(new ClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("http://fireball.jpn.com/");
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                });
        ((UIButton) findViewById(R.id.author))
                .addClickListener(new ClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("https://twitter.com/kimihom");
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                });
    }

    private int getNumber() {
        return getPreferences(MODE_PRIVATE).getInt("num", 3);
    }

    private void setNumClickListener() {
        for (int i = 1; i <= 5; i++) {
            final int n = i;
            TextView tv = (TextView) findViewById(getResources().getIdentifier(
                    "num" + i, "id", getPackageName()));
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPreferences(MODE_PRIVATE).edit().putInt("num", n)
                            .commit();
                    Toast.makeText(MainActivity.this,
                            "Set Circle Num to " + n + "!", Toast.LENGTH_SHORT)
                            .show();
                    onHanoiCreate();
                }
            });
        }
    }

    private void setSeekbarListener() {
        SeekBar sb = (SeekBar) findViewById(R.id.seekbar);
        sb.setProgress(getSpeed());
        sb.setMax(5000);
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v("onStartTrackingTouch()",
                        String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromTouch) {
                Log.v("onProgressChanged()", String.valueOf(progress) + ", "
                        + String.valueOf(fromTouch));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getPreferences(MODE_PRIVATE).edit()
                        .putInt("speed", seekBar.getProgress()).commit();
                Toast.makeText(MainActivity.this, "speed changed!",
                        Toast.LENGTH_SHORT).show();
                onHanoiCreate();
            }
        });
    }

    private int getSpeed() {
        return getPreferences(MODE_PRIVATE).getInt("speed", 1000);
    }
}
