package com.example.daniele.drawdice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SingleTouchEventView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();


    private Canvas drawCanvas;
    private Bitmap canvasBitmap;


    private boolean erase = false;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }



    public SingleTouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);

        //setLayerType(View.LAYER_TYPE_SOFTWARE, paint);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //canvas.drawPath(path, paint);

        canvas.drawBitmap(canvasBitmap, 0, 0, paint);
        drawCanvas.drawPath(path, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {




        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                //path.reset();

                path.moveTo(eventX, eventY);
                //invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if(erase) {
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


                    path.lineTo(eventX, eventY);

                    drawCanvas.drawPath(path, paint);
                    paint.setXfermode(null);
                    path.reset();
                    path.moveTo(eventX, eventY);
                }else{
                    path.lineTo(eventX, eventY);

                }


                break;
            case MotionEvent.ACTION_UP:
                //drawCanvas.drawPath(path, paint);
                if(erase) {
                    //path.reset();
                }else{
                    drawCanvas.drawPath(path, paint);
                    path.reset();
                }
                //paint.setXfermode(null);
                // nothing to do
                break;
            default:
                return false;
        }
        invalidate();//calls onDraw
        return true;
    }


    public void erase(){
        //paint.setStrokeWidth(20);
        //paint.setColor(Color.WHITE);
        //paint.setStyle(Paint.Style.STROKE);
        erase = true;
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStrokeWidth(20);
    }


    public void write(int color){
        erase = false;
        //paint.setXfermode(null);
        paint.setStrokeWidth(10);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        //invalidate();
    }

    public Paint getPaint(){
        return this.paint;
    }

    public Path getPath(){
        return this.path;
    }

    public void newDraw(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void undo(){

    }



}
