package com.billing.mybilling.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.view.ViewCompat
import com.billing.mybilling.R

/**
 * SpinView is a port of Spin.js to a native android view
 */
class SpinView : View {
    private var lines = 0
    private var length = 0f
    private var lineWidth = 0f
    private var radius = 0f
    private var scale = 0f
    private var corners = 0f
    private var color = 0
    private var fadeColor = 0
    private var opacity = 0
    private var rotate = 0
    private var direction = 0
    private var speed = 0f
    private var trail = 0f
    private var mColorPaint: Paint? = null
    private var mFadeColorPaint: Paint? = null
    private var mFrame = 0
    private var mAnimationHandlerThread: HandlerThread? = null
    private var mAnimationHandler: Handler? = null
    private var mBounds: RectF? = null

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(attrs, 0)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val dm = resources.displayMetrics
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.SpinView, defStyle, 0)
        lines = a.getInt(R.styleable.SpinView_lines, 13)
        length = a.getDimension(
            R.styleable.SpinView_length,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38f, dm)
        )
        lineWidth = a.getDimension(
            R.styleable.SpinView_width,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17f, dm)
        )
        radius = a.getDimension(
            R.styleable.SpinView_radius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45f, dm)
        )
        scale = a.getFloat(R.styleable.SpinView_scale, 1f)
        corners = a.getFloat(R.styleable.SpinView_corners, 1f)
        color = a.getColor(R.styleable.SpinView_ccolor, Color.WHITE)
        fadeColor = a.getColor(R.styleable.SpinView_fadeColor, Color.TRANSPARENT)
        opacity = (255 * a.getFloat(R.styleable.SpinView_opacity, .25f)).toInt()
        rotate = a.getInt(R.styleable.SpinView_rotate, 0)
        direction =
            a.getInt(R.styleable.SpinView_direction, DIRECTION_CLOCKWISE)
        speed = a.getFloat(R.styleable.SpinView_speed, 1f)
        trail = a.getInt(R.styleable.SpinView_trail, 60).toFloat() / 100f
        a.recycle()

        // set up the view
        mColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mColorPaint!!.color = color
        mFadeColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFadeColorPaint!!.color = fadeColor

        // create the animation handler thread
        mAnimationHandlerThread = HandlerThread(TAG)
        mAnimationHandlerThread!!.start()
        mAnimationHandler = Handler(mAnimationHandlerThread!!.looper)
        mBounds = RectF(
            radius * scale,
            -lineWidth / 2 * scale,
            (radius + length) * scale,
            lineWidth / 2 * scale
        )
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (mAnimationHandler == null) {
            mAnimationHandler = Handler(mAnimationHandlerThread!!.looper)
        }
        // start the animation
        mAnimationHandler?.post(object : Runnable {
            override fun run() {
                // stop the animation
                if (!ViewCompat.isAttachedToWindow(this@SpinView)) {
                    try {
                        mAnimationHandlerThread!!.quit()
                    } catch (e: Exception) {
                        Log.e(TAG, e.message, e)
                    }
                    return
                }
                mFrame = (mFrame + 1) % Int.MAX_VALUE
                postInvalidate()
                mAnimationHandler?.postDelayed(this, 1000 / sFPS.toLong())
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimationHandler?.removeCallbacksAndMessages(null)
        mAnimationHandler = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(
            ((radius + length) * 2 * scale).toInt(),
            MeasureSpec.EXACTLY
        )
        val height = MeasureSpec.makeMeasureSpec(
            ((radius + length) * 2 * scale).toInt(),
            MeasureSpec.EXACTLY
        )
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until lines) {
            canvas.save()
            canvas.translate(width / 2.toFloat(), height / 2.toFloat())
            canvas.rotate(Math.floor(360 / lines * i + rotate.toDouble()).toFloat())

            // draw the fade color
            if (Color.alpha(fadeColor) > 0) {
                canvas.drawRoundRect(
                    mBounds!!,
                    corners * lineWidth * scale,
                    corners * lineWidth * scale,
                    mFadeColorPaint!!
                )
            }
            // draw the main color over top
            mColorPaint!!.alpha = getLineOpacity(
                i,
                mFrame.toFloat() / sFPS * speed % 1
            )
            canvas.drawRoundRect(
                mBounds!!,
                corners * lineWidth * scale,
                corners * lineWidth * scale,
                mColorPaint!!
            )
            canvas.restore()
        }
    }

    private fun getLineOpacity(line: Int, animationPercentCompleted: Float): Int {
        val linePercent = (line + 1).toFloat() / lines
        var diff = animationPercentCompleted - linePercent * direction
        if (diff < 0 || diff > 1) {
            diff += direction.toFloat()
        }

        // opacity should start at 1, and approach opacity option as diff reaches trail percentage
        val opacityPercent = 1 - diff / trail
        return if (opacityPercent < 0) {
            opacity
        } else ((255 - opacity) * opacityPercent).toInt() + opacity
    }

    companion object {
        val TAG = SpinView::class.java.simpleName

        /**
         * DIRECTION_CLOCKWISE causes the spinner to spin in the clockwise direction
         */
        const val DIRECTION_CLOCKWISE = 1

        /**
         * DIRECTION_COUNTERCLOCKWISE causes the spinner to spin in the counterclockwise direction
         */
        const val DIRECTION_COUNTERCLOCKWISE = -1

        /**
         * sFPS is the frames per second of the animation
         */
        private const val sFPS = 50
    }
}