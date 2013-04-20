package com.honkimi.hanoi;

import java.util.Stack;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AnimateStack {
    interface AnimCallback {
        public void finished(AnimateStack to, ImageView v);
    }

    interface AnimComplete {
        public void finished();
    }

    public Stack<ImageView> stack;
    private String name;
    private int maxY;
    private LinearLayout poll;
    private Handler handler;

    public AnimateStack(LinearLayout poll, String name, int maxY,
            Handler handler) {
        stack = new Stack<ImageView>();
        this.name = name;
        this.maxY = maxY;
        this.poll = poll;
        this.handler = handler;
    }

    public void asyncPush(ImageView v, final AnimComplete callback, int speed) {
        int tag = (Integer) v.getTag();
        ImageView target = new ImageView(v.getContext());
        target.setBackgroundResource(v
                .getContext()
                .getResources()
                .getIdentifier("circle" + tag, "drawable",
                        v.getContext().getPackageName()));
        target.setTag(tag);
        stack.push(target);
        Animation anim = AnimationUtils.loadAnimation(target.getContext(),
                R.anim.animator_down);
        anim.setDuration(speed);
        target.startAnimation(anim);
        poll.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        poll.addView(target, 0, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.finished();
                        }
                    }
                });
            }
        });
    }

    public void asyncPop(final AnimateStack to, final AnimCallback callback, int speed) {
        if (stack.isEmpty()) {
            throw new RuntimeException("stack" + name + "is empty!");
        }
        final ImageView target = stack.pop();
        Animation anim = AnimationUtils.loadAnimation(target.getContext(),
                R.anim.animator);
        anim.setDuration(speed);
        target.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.v("TEST", "started___");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.finished(to, target);
                        }
                        poll.removeView(target);
                    }
                });
            }
        });
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
