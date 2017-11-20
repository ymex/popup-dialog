package cn.ymex.popup.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import cn.ymex.popup.R;


public class SpotsProgressView extends FrameLayout {

    private static final int DEFAULT_COUNT = 5;
    private int spotsCount;


    private static final int DELAY = 165;
    private static final int DURATION = 1400;


    private AnimatedView[] spots;
    private AnimatorPlayer animator;

    public SpotsProgressView(Context context) {
        this(context, null);
    }

    public SpotsProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpotsProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpotsProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        dealAttr(attrs, defStyleAttr, defStyleRes);
        initProgress();
    }


    private void dealAttr(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SpotsProgressView,
                defStyleAttr, defStyleRes);

        spotsCount = a.getInt(R.styleable.SpotsProgressView_spot_count, DEFAULT_COUNT);
        a.recycle();
    }

    public int getSpotsCount() {

        return spotsCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }


    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = getContext().getResources().getDimensionPixelSize(R.dimen.spot_size);
        if (specMode == MeasureSpec.AT_MOST) {
            specSize = result;
        }
        return specSize;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = getContext().getResources().getDimensionPixelSize(R.dimen.spot_progress_width);
        if (specMode == MeasureSpec.AT_MOST) {
            specSize = result;
        }
        return specSize;
    }


    private void initProgress() {

        spots = new AnimatedView[getSpotsCount()];
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.spot_size);
        int progressWidth = getContext().getResources().getDimensionPixelSize(R.dimen.spot_progress_width);
        for (int i = 0; i < spots.length; i++) {
            AnimatedView v = new AnimatedView(getContext());
            v.setBackgroundResource(R.drawable.bg_spots_spot);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            addView(v, size, size);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[getSpotsCount()];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    private void stopAnimation() {
        if (isInEditMode()) {
            return;
        }
        if (animator == null) {
            return;
        }
        animator.stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    private void startAnimation() {
        if (isInEditMode()) {
            return;
        }

        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }


    class HesitateInterpolator implements Interpolator {

        private double POW = 1.0 / 2.0;

        @Override
        public float getInterpolation(float input) {
            return input < 0.5
                    ? (float) Math.pow(input * 2, POW) * 0.5f
                    : (float) Math.pow((1 - input) * 2, POW) * -0.5f + 1;
        }
    }

    class AnimatorPlayer extends AnimatorListenerAdapter {

        private boolean interrupted = false;
        private Animator[] animators;

        public AnimatorPlayer(Animator[] animators) {
            this.animators = animators;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!interrupted) animate();
        }

        public void play() {
            animate();
        }

        public void stop() {
            interrupted = true;
        }

        private void animate() {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animators);
            set.addListener(this);
            set.start();
        }
    }

    class AnimatedView extends View {

        private int target;

        public AnimatedView(Context context) {
            super(context);
        }

        public float getXFactor() {
            return getX() / target;
        }

        public void setXFactor(float xFactor) {
            setX(target * xFactor);
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public int getTarget() {
            return target;
        }
    }
}
