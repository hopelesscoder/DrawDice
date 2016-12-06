package com.example.daniele.drawdice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

public class SingleTouchEventView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();
    private LinkedList<Path> pathList = new LinkedList<Path>();


    private Canvas drawCanvas;
    private Bitmap canvasBitmap;


    private boolean erase = false;
    private boolean isTextOnPath = false;
    //private boolean isText = false;
    private String text;
    private Path onPath= new Path();

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
        /*if(isText){
            Toast.makeText(this.getContext(),"enter",Toast.LENGTH_SHORT).show();
            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            textPaint.setColor(Color.RED);
            textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
            textPaint.setTextAlign(Paint.Align.LEFT);

            Paint.FontMetrics metric = textPaint.getFontMetrics();
            int textHeight = (int) Math.ceil(metric.descent - metric.ascent);
            int y = (int)(textHeight - metric.descent);
            canvas.drawText("primo testo fdsfbsòOFABOosfdOÒGDSGDSGDSSAGAAAFSDSDGFSDADG", 0,
                    y,textPaint);
        }*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {




        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                //path.reset();


                //pathList.addFirst(path);
                if(isTextOnPath){
                    onPath.moveTo(eventX,eventY);
                }else {
                    path.moveTo(eventX, eventY);
                }
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
                }else if (isTextOnPath){
                    onPath.lineTo(eventX, eventY);


                }else {
                    path.lineTo(eventX, eventY);

                    }


                break;
            case MotionEvent.ACTION_UP:
                //drawCanvas.drawPath(path, paint);
                if(erase) {
                    //path.reset();
                }else if(isTextOnPath){
                    onPath.close();
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.RED);
                    //textPaint.setARGB(200,220,0,0);
                    //textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                    textPaint.setTextSize(50);
                    textPaint.setAntiAlias(true);
                    textPaint.setDither(true);
                    drawCanvas.drawTextOnPath(text,onPath,0,0,textPaint);
                    isTextOnPath=false;
                    Toast.makeText(this.getContext(),text,Toast.LENGTH_SHORT).show();
                    onPath.reset();


                }else {
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
        paint.setAntiAlias(true);
        paint.setDither(true);
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
        this.setPath(pathList.removeFirst());
    }

    public void setPath(Path oldPath){
        path = oldPath;
        invalidate();
    }


    public void setText(String in){
        //isText=true;
        //text = in;

        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        //textPaint.setARGB(200,220,0,0);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        drawCanvas.drawText(in, drawCanvas.getWidth()/2,
                drawCanvas.getHeight()/2 - ((textPaint.descent() + textPaint.ascent()) / 2),textPaint);

        invalidate();
    }

    public void setTextOnPath(String in){
        text = in;
        isTextOnPath=true;
    }



}
