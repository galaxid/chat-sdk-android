package co.chatsdk.ui.rooms;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;
import co.chatsdk.ui.R;


public class TwoLineButton extends Button {

        String name;
        String description;

        public TwoLineButton(Context context) {
            super(context);
        }

        public TwoLineButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.test);
            name = ta.getString(R.styleable.test_name);
            description = ta.getString(R.styleable.test_description);
            ta.recycle();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setTextSize(160);
            paint.setColor(Color.rgb(0xff, 0xff, 0xff));
            float tagWidth = paint.measureText(name);
            float x = (this.getWidth() - tagWidth) / 2;
            float y = this.getHeight() / 2;
            canvas.drawText(name, x, y, paint);

            Paint paint1 = new Paint();
            paint1.setTextSize(64);
            paint1.setColor(Color.rgb(0xff, 0xff, 0xff));
            float numWidth = paint1.measureText(description);
            float x1 = (this.getWidth() - numWidth) / 2;
            float y1 = this.getHeight() / 2 + 150;
            canvas.drawText(description, x1, y1, paint1);
//        canvas.translate(0,(this.getMeasuredHeight()/2) - (int) this.getTextSize());
        }

}
