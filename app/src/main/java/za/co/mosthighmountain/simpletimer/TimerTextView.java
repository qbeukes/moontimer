package za.co.mosthighmountain.simpletimer;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Quintin on 9/6/2015.
 */
public class TimerTextView extends TextView {
    private static final String text10 = "0";

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

//        Paint paint = adjustTextSize(getPaint(), this.getText().length(), parentWidth, parentHeight);
//        setTextSize(TypedValue.COMPLEX_UNIT_PX, paint.getTextSize());
    }

    public Paint adjustTextSize(Paint paint, int numCharacters, int widthPixels, int heightPixels) {
        heightPixels -= 100;

        float width = paint.measureText(text10) * numCharacters / text10.length();
        float f = paint.getTextSize();
        float newSize = (int)((widthPixels/width)*paint.getTextSize());
        paint.setTextSize(newSize);

        // remeasure with font size near our desired result
        width = paint.measureText(text10)*numCharacters/text10.length();
        newSize = (int)((widthPixels/width)*paint.getTextSize());
        paint.setTextSize(newSize);

        // Check height constraints
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        float textHeight = metrics.descent-metrics.ascent;
        if (textHeight > heightPixels) {
            newSize = (int)(newSize * (heightPixels/textHeight));
            paint.setTextSize(newSize);
        }

        return paint;
    }
}
