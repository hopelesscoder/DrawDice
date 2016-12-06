package com.example.daniele.drawdice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 100;

    Button buttonErase;
    Button buttonWrite;
    Button buttonD4;
    Button buttonSave;
    Button buttonNew;
    Button buttonSetBackground;
    Button buttonUndo;
    Button buttonText;
    Button buttonOnPath;


    TextView textView1;

    Button buttonBlue;
    Button buttonRed;
    Button buttonYellow;
    Button buttonBlack;
    Button buttonGreen;

    Dialog writeDialog;






    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(new SingleTouchEventView(this, null));
            setContentView(R.layout.main);

            buttonErase = (Button) findViewById(R.id.buttonErase);
            buttonErase.setOnClickListener(this);
            buttonWrite = (Button) findViewById(R.id.buttonWrite);
            buttonWrite.setOnClickListener(this);
            buttonD4 = (Button) findViewById(R.id.buttonD4);
            buttonD4.setOnClickListener(this);
            buttonSave = (Button) findViewById(R.id.buttonSave);
            buttonSave.setOnClickListener(this);
            textView1 = (TextView) findViewById(R.id.textView1);
            buttonNew = (Button) findViewById(R.id.buttonNew);
            buttonNew.setOnClickListener(this);
            buttonSetBackground = (Button) findViewById(R.id.buttonSetBackground);
            buttonSetBackground.setOnClickListener(this);
            buttonUndo = (Button) findViewById(R.id.buttonUndo);
            buttonText = (Button) findViewById(R.id.buttonText);
            buttonText.setOnClickListener(this);
            buttonOnPath = (Button) findViewById(R.id.buttonOnPath);
            buttonOnPath.setOnClickListener(this);



            if(savedInstanceState == null){
                SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
                drawFrag.setBackgroundColor(Color.WHITE);
            }else{
                //SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
                //drawFrag.write(savedInstanceState.getInt("color"));
                //drawFrag.getPaint().setColor(savedInstanceState.getInt("color"));
            }

        }





    @Override
    public void onClick(View v) {
        if((Button) v == buttonErase){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.erase();
        }else if ((Button) v == buttonWrite){
            writeDialog = new Dialog(this);
            writeDialog.setTitle("Write color:");
            writeDialog.setContentView(R.layout.color_chooser);

            buttonBlue = (Button) writeDialog.findViewById(R.id.buttonBlue);
            buttonBlue.setOnClickListener(this);
            buttonRed = (Button) writeDialog.findViewById(R.id.buttonRed);
            buttonRed.setOnClickListener(this);
            buttonYellow = (Button) writeDialog.findViewById(R.id.buttonYellow);
            buttonYellow.setOnClickListener(this);
            buttonBlack = (Button) writeDialog.findViewById(R.id.buttonBlack);
            buttonBlack.setOnClickListener(this);
            buttonGreen = (Button) writeDialog.findViewById(R.id.buttonGreen);
            buttonGreen.setOnClickListener(this);

            writeDialog.show();



        }else if ((Button) v == buttonD4){
            int res = new Random().nextInt(4-1+1) +1;
            textView1.setText(""+res);
        }else if ((Button) v == buttonSave){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            //saveDialog.setView(R.layout.image_name);
            final EditText textImage = new EditText(this);
            textImage.setHint("Name of the drawing");
            saveDialog.setView(textImage);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");

            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
                    drawFrag.setDrawingCacheEnabled(true);
                    String name;
                    if(textImage.getText().toString().length()>0){
                        name = textImage.getText().toString().concat(".jpg");
                    }else{
                        name = UUID.randomUUID().toString()+".jpg";
                    }
                    createDirectoryAndSaveFile(drawFrag.getDrawingCache(),name);



                    /*String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawFrag.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    */
                    drawFrag.destroyDrawingCache();
                }
            });

            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }else if ((Button) v == buttonNew){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.setBackgroundColor(Color.WHITE);
            drawFrag.newDraw();
        }else if ((Button) v == buttonSetBackground){
            pickImage();
        }else if ((Button) v == buttonUndo){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.undo();
        }else if ((Button) v == buttonText){


            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            final EditText text = new EditText(this);
            text.setHint("Text");
            saveDialog.setView(text);
            saveDialog.setTitle("Write");
            saveDialog.setMessage("What do you want to write?");

            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    String name;
                    if(text.getText().toString().length()>0){
                        name = text.getText().toString();
                        SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
                        drawFrag.setText(name);
                    }else{
                        Toast.makeText(getApplicationContext(),"Wrong text",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();




        }else if ((Button) v == buttonOnPath){

            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            final EditText text = new EditText(this);
            text.setHint("Text");
            saveDialog.setView(text);
            saveDialog.setTitle("Write");
            saveDialog.setMessage("What do you want to write?");

            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    String name;
                    if(text.getText().toString().length()>0){
                        name = text.getText().toString();
                        SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
                        drawFrag.setTextOnPath(name);
                    }else{
                        Toast.makeText(getApplicationContext(),"Wrong text",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();


        }else if ((Button) v == buttonBlue){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.write(Color.BLUE);
            writeDialog.dismiss();
        }else if ((Button) v == buttonRed){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.write(Color.RED);
            writeDialog.dismiss();
        }else if ((Button) v == buttonYellow){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.write(Color.YELLOW);
            writeDialog.dismiss();
        }else if ((Button) v == buttonBlack){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.write(Color.BLACK);
            writeDialog.dismiss();
        }else if ((Button) v == buttonGreen){
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            drawFrag.write(Color.GREEN);
            writeDialog.dismiss();
        }


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //try {//alternative mode
            //Bitmap imgSaved = MediaStore.Images.Media.getBitmap(getContentResolver(),
              //      Uri.parse((savedInstanceState.getString("url"))));
            //Drawable drawable = new BitmapDrawable(getResources(), imgSaved);
            SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
            //drawFrag.setBackground(drawable);

            byte[] b = savedInstanceState.getByteArray("arr");
            Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
            Drawable draw = new BitmapDrawable(getResources(), bmp);
            drawFrag.setBackground(draw);


        //} catch (IOException e) {
          //  e.printStackTrace();
        //}


            drawFrag.write(savedInstanceState.getInt("color"));
            //Toast.makeText(getApplicationContext(),""+savedInstanceState.getInt("color"),Toast.LENGTH_SHORT);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
        //drawFrag.write(Color.BLUE);
        drawFrag.setDrawingCacheEnabled(true);
        //String imgSaved = MediaStore.Images.Media.insertImage(//alternative mode
          //      getContentResolver(), drawFrag.getDrawingCache(),
            //    "background.png", "drawing");
        Bitmap draw = drawFrag.getDrawingCache(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        draw.compress(Bitmap.CompressFormat.PNG,1,baos);
        byte[] b = baos.toByteArray();
        outState.putByteArray("arr",b);

        //outState.putString("url", imgSaved);

        outState.putInt("color",drawFrag.getPaint().getColor());


        drawFrag.destroyDrawingCache();
    }




    public void pickImage() {
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent =          new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    //Get image
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    Drawable background = new BitmapDrawable(getResources(), image);
                    SingleTouchEventView drawFrag = (SingleTouchEventView) findViewById(R.id.drawing);
                    drawFrag.newDraw();
                    drawFrag.setBackground(background);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }



    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        //String folderPath = Environment.getExternalStorageDirectory() + "/Pictures/drawdice";
        String folderPath = Environment.getExternalStorageDirectory() + "/DCIM/Drawdice";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File Directory = new File(folderPath);
            Directory.mkdirs();
        }
        File newFile = new File(folderPath, fileName);
        try {
            FileOutputStream out = new FileOutputStream(newFile);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(newFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
