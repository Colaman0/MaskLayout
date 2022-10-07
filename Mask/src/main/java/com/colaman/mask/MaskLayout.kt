package com.colaman.mask

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import android.widget.FrameLayout


/**
 * 展示遮罩区域/遮罩区域外的内容
 */
enum class MaskMode {
    Inside, Outside
}

open class MaskLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var porterDuffMode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private var maskMode = MaskMode.Inside
    private var maskBitmap: Bitmap? = null
    private var maskSize: Size? = null
    private var maskOriginalPoint: Point? = null

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        if (maskBitmap != null) {
            val saved = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint)
            val bitmapRect = if (maskSize != null && maskOriginalPoint != null) {
                Rect(
                    maskOriginalPoint!!.x,
                    maskOriginalPoint!!.y,
                    maskOriginalPoint!!.x + maskSize!!.width,
                    maskOriginalPoint!!.y + maskSize!!.height
                )
            } else {
                Rect(0, 0, measuredWidth, measuredHeight)
            }
            if (maskMode == MaskMode.Inside) {
                canvas.clipRect(bitmapRect)
            }
            super.dispatchDraw(canvas)
            paint.xfermode = porterDuffMode
            canvas.drawBitmap(maskBitmap!!, null, bitmapRect, paint)
            paint.xfermode = null
            canvas.restoreToCount(saved)
        } else {
            super.dispatchDraw(canvas)
        }
    }

    /**
     * 更新遮罩
     * @param maskBitmap Bitmap
     * @param maskSize Size
     * @param maskOriginalPoint Point
     */
    fun mask(
        maskBitmap: Bitmap,
        maskSize: Size? = Size(maskBitmap.width, maskBitmap.height),
        maskOriginalPoint: Point? = Point(0, 0)
    ) {
        this.maskBitmap = maskBitmap
        this.maskSize = maskSize
        this.maskOriginalPoint = maskOriginalPoint
    }

    fun update(block: MaskLayout.() -> Unit) {
        block()
        postInvalidate()
    }

    fun maskMode(mode: MaskMode) {
        maskMode = mode
        porterDuffMode = when (mode) {
            MaskMode.Inside -> PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            MaskMode.Outside -> PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }
    }

    fun maskMode(mode: PorterDuffXfermode) {
        porterDuffMode = mode
    }

    fun clear() {
        maskBitmap = null
        maskSize = null
        maskMode = MaskMode.Inside
        maskOriginalPoint = null
        postInvalidate()
    }
}