package cn.ymex.popup.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.ymex.popup.R;

/**
 * @author Silvestr Predko.
 */
public class DotProgressView extends View {

    public static final int RIGHT_DIRECTION = 1;
    public static final int LEFT_DIRECTION = -1;

    /**
     * Dots amount
     */
    private int dotAmount;

    /**
     * Drawing tools
     */
    private Paint primaryPaint;
    private Paint startPaint;
    private Paint endPaint;

    /**
     * Animation tools
     */
    private long animationTime = 300;
    private float animatedRadius;
    private boolean isFirstLaunch = true;
    private ValueAnimator startValueAnimator;
    private ValueAnimator endValueAnimator;

    /**
     * Circle size
     */
    private float dotRadius;
    private float bounceDotRadius;
    /**
     * Circle coordinates
     */
    private float xCoordinate;
    private int dotPosition;

    /**
     * Colors
     */
    private int startColor;
    private int endColor;

    /**
     * This value detect direction of circle animation direction
     * {@link DotProgressView#RIGHT_DIRECTION} and {@link DotProgressView#LEFT_DIRECTION}
     */
    private int animationDirection;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeAttributes(attrs);
        init();
    }

    public DotProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(attrs);
        init();
    }

    public DotProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs);
        init();
    }

    public DotProgressView(Context context) {
        super(context);
        initializeAttributes(null);
        init();
    }

    public static int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension(width, height);

        if (height > width) {
            dotRadius = width / dotAmount / 4;
        } else {
            dotRadius = height / 4;
        }

        bounceDotRadius = dotRadius + (dotRadius / 3);
        float circlesWidth = (dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1);
        xCoordinate = (width - circlesWidth) / 2 + dotRadius;


    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 8 * 2;
        if (specMode == MeasureSpec.AT_MOST) {
            specSize = result;
        }
        return specSize;
    }

    private int measureWidth(int measureSpec) {
        int specSize = MeasureSpec.getSize(measureSpec);
        return specSize;
    }

    private void initializeAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DotProgressView,
                    0, 0);

            try {
                setDotAmount(a.getInteger(R.styleable.DotProgressView_amount, 5));
                setAnimationTime(animationTime = a.getInteger(
                        R.styleable.DotProgressView_duration, (int) animationTime));
                setStartColor(
                        a.getInteger(
                                R.styleable.DotProgressView_startColor,
                                ContextCompat.getColor(getContext(), R.color.blue_dark)
                        )
                );
                setEndColor(
                        a.getInteger(
                                R.styleable.DotProgressView_endColor,
                                ContextCompat.getColor(getContext(), R.color.blue_light)
                        )
                );
                setAnimationDirection(a.getInt(R.styleable.DotProgressView_animationDirection, 1));
            } finally {
                a.recycle();
            }

        } else {
            setDotAmount(5);
            setAnimationTime(animationTime);
            setStartColor(ContextCompat.getColor(getContext(), R.color.blue_dark));
            setEndColor(ContextCompat.getColor(getContext(), R.color.blue_light));
            setAnimationDirection(1);
        }
    }

    private void init() {
        primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        primaryPaint.setColor(startColor);
        primaryPaint.setStrokeJoin(Paint.Join.ROUND);
        primaryPaint.setStrokeCap(Paint.Cap.ROUND);
        primaryPaint.setStrokeWidth(20);

        startPaint = new Paint(primaryPaint);
        endPaint = new Paint(primaryPaint);

        startValueAnimator = ValueAnimator.ofInt(startColor, endColor);
        startValueAnimator.setDuration(animationTime);
        startValueAnimator.setEvaluator(new ArgbEvaluator());
        startValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startPaint.setColor(((Integer) animation.getAnimatedValue()));
            }
        });

        endValueAnimator = ValueAnimator.ofInt(endColor, startColor);
        endValueAnimator.setDuration(animationTime);
        endValueAnimator.setEvaluator(new ArgbEvaluator());
        endValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                endPaint.setColor(((Integer) animation.getAnimatedValue()));
            }
        });
    }

    /**
     * setters
     */
    void setDotAmount(int amount) {
        this.dotAmount = amount;
    }

    void setStartColor(@ColorInt int color) {
        this.startColor = color;
    }

    void setEndColor(@ColorInt int color) {
        this.endColor = color;
    }

    void setAnimationTime(long animationTime) {
        this.animationTime = animationTime;
    }

    private void setDotPosition(int dotPosition) {
        this.dotPosition = dotPosition;
    }

    /**
     * Set amount of dots, it will be restarted your view
     *
     * @param amount number of dots, dot size automatically adjust
     */
    public void changeDotAmount(int amount) {
        stopAnimation();
        setDotAmount(amount);
        setDotPosition(0);
        reinitialize();
    }

    /**
     * It will be restarted your view
     */
    public void changeStartColor(@ColorInt int color) {
        stopAnimation();
        setStartColor(color);
        reinitialize();
    }

    /**
     * It will be restarted your view
     */
    public void changeEndColor(@ColorInt int color) {
        stopAnimation();
        setEndColor(color);
        reinitialize();
    }

    /**
     * It will be restarted your view
     */
    public void changeAnimationTime(long animationTime) {
        stopAnimation();
        setAnimationTime(animationTime);
        reinitialize();
    }

    /**
     * Change animation direction, doesn't restart view.
     *
     * @param animationDirection left or right animation direction
     */
    public void changeAnimationDirection(@AnimationDirection int animationDirection) {
        setAnimationDirection(animationDirection);
    }

    public int getAnimationDirection() {
        return animationDirection;
    }

    void setAnimationDirection(int direction) {
        this.animationDirection = direction;
    }

    /**
     * Reinitialize animators instances
     */
    void reinitialize() {
        init();
        requestLayout();
        startAnimation();
    }

    private void drawCirclesLeftToRight(Canvas canvas, float radius) {
        float step = 0;
        for (int i = 0; i < dotAmount; i++) {
            drawCircles(canvas, i, step, radius);
            step += dotRadius * 3;
        }
    }

    private void drawCirclesRightToLeft(Canvas canvas, float radius) {
        float step = 0;
        for (int i = dotAmount - 1; i >= 0; i--) {
            drawCircles(canvas, i, step, radius);
            step += dotRadius * 3;
        }
    }

    private void drawCircles(@NonNull Canvas canvas, int i, float step, float radius) {
        if (dotPosition == i) {
            drawCircleUp(canvas, step, radius);
        } else {
            if ((i == (dotAmount - 1) && dotPosition == 0 && !isFirstLaunch) || ((dotPosition - 1) == i)) {
                drawCircleDown(canvas, step, radius);
            } else {
                drawCircle(canvas, step);
            }
        }
    }

    /**
     * Circle radius is grow
     */
    private void drawCircleUp(@NonNull Canvas canvas, float step, float radius) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() / 2,
                dotRadius + radius,
                startPaint
        );
    }

    private void drawCircle(@NonNull Canvas canvas, float step) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() / 2,
                dotRadius,
                primaryPaint
        );
    }

    /**
     * Circle radius is decrease
     */
    private void drawCircleDown(@NonNull Canvas canvas, float step, float radius) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() / 2,
                bounceDotRadius - radius,
                endPaint
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animationDirection < 0) {
            drawCirclesRightToLeft(canvas, animatedRadius);
        } else {
            drawCirclesLeftToRight(canvas, animatedRadius);
        }
    }

    private void stopAnimation() {
        this.clearAnimation();
        postInvalidate();
    }

    private void startAnimation() {
        final BounceAnimation bounceAnimation = new BounceAnimation();
        bounceAnimation.setDuration(animationTime);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setInterpolator(new LinearInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                dotPosition++;
                if (dotPosition == dotAmount) {
                    dotPosition = 0;
                }

                startValueAnimator.start();

                if (!isFirstLaunch) {
                    endValueAnimator.start();
                }

                isFirstLaunch = false;
            }
        });
        startAnimation(bounceAnimation);
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RIGHT_DIRECTION, LEFT_DIRECTION})
    public @interface AnimationDirection {
    }

    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            animatedRadius = (bounceDotRadius - dotRadius) * interpolatedTime;
            invalidate();
        }
    }
}