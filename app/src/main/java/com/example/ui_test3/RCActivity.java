package com.example.ui_test3;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rc.balamp.R;

//import android.app.Application;
//import android.bluetooth.BluetoothDevice;
//import android.app.Dialog;
//import android.widget.ListView;


public class RCActivity extends Activity {

    private MyApplication value; // доступ к содержимому MyApplication

    private BluetoothSocket bts;

    static int VolumeUPCounter = 0;
    static int VolumeDOWNCounter = 0;

    static int PowerUPCounter = 0;
    static int PowerDOWNCounter = 0;

    public TextView VolumeValueRef;

    public ParamsKeeper pk = new ParamsKeeper(); // интерфейс для доступа и управления полученными по BT данными

    public Toast mytoast;// ссылка на toast для управления

    public Context locCont = this;

    // Работа со списком найденных устройств
    ListView BTList;
    ArrayList<String> itemArrey;    //= new ArrayList<String>();
    ArrayAdapter<String> itemAdapter;
    Dialog SearchDialog;
    Button dialogButton2;            // Ссылки на кнопки в диалоговом окне
    Button dialogButton3;

    private Handler mHandler = new Handler();
/*
    // CD
    private Runnable CDTask = new Runnable()
    {
        public void run()
        {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);
            //String loc_Command;

            if(value != null)
            {
            	value.WriteToSocket(new byte[] { 'C', 'D', ' ','\r','\n' });
            }
            //else Log.i("DEBUG MSG", "value is null");

        	Log.i("DEBUG MSG", "CD button REPEAT");
        }
    };


    // VINIL
    private Runnable VinilTask = new Runnable()
    {
        public void run()
        {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);
            //String loc_Command;

            if(value != null)
            {
            	value.WriteToSocket(new byte[] { 'V','I','N','I','L',' ','\r','\n' });
            }

        	Log.i("DEBUG MSG", "VINIL button REPEAT");
        }
    };

    // MEDIA
    private Runnable MediaTask = new Runnable()
    {
        public void run()
        {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);
            //String loc_Command;

            if(value != null)
            {
            	value.WriteToSocket(new byte[] { 'M','E','D','I','A',' ','\r','\n' });
            }

        	Log.i("DEBUG MSG", "MEDIA button REPEAT");
        }
    };

    // SETTINGS
    private Runnable SetTask = new Runnable()
    {
        public void run()
        {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);

            if(value != null)
            {
            	value.WriteToSocket(new byte[] {'S','E','T', ' ','\r','\n' });
            }

        	Log.i("DEBUG MSG", "SET button REPEAT");
        }
    };
 */

    // Power UP
    private Runnable PowerUPTask = new Runnable() {
        public void run() {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);

            if (value != null) {

                if (PowerUPCounter < 50) PowerUPCounter++;

                if (PowerUPCounter == 25) // 4 sec
                {
                    value.WriteToSocket(new byte[]{'P', 'O', 'W', 'E', 'R', ' ', 'U', 'P', ' ', '\r', '\n'});
                }
            }

            Log.i("DEBUG MSG", "POWER UP button REPEAT");
        }
    };

    // Power DOWN
    private Runnable PowerDOWNTask = new Runnable() {
        public void run() {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);

            if (value != null) {

                if (PowerDOWNCounter < 50) PowerDOWNCounter++;

                if (PowerDOWNCounter == 25)  // 4 sec
                {
                    value.WriteToSocket(new byte[]{'P', 'O', 'W', 'E', 'R', ' ', 'D', 'O', 'W', 'N', ' ', '\r', '\n'});
                }
            }

            Log.i("DEBUG MSG", "POWER DOWN button REPEAT");
        }
    };

    // MUTE
 /*
    private Runnable MuteTask = new Runnable()
    {
        public void run()
        {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);

            if(value != null)
            {
            	value.WriteToSocket(new byte[] {'M','U','T','E',' ','\r','\n'  });
            }

    	 	Log.i("DEBUG MSG", "POWER UP button REPEAT");
        }
    };
*/

    // UP
    private Runnable VlumeUPTask = new Runnable() {
        public void run() {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);
            //String loc_Command;

            if (VolumeUPCounter < 6) {
                VolumeUPCounter++;
            }

            if (value != null) {
                if (VolumeUPCounter <= 2) {
                    value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'U', 'P', ' ', '1', '\r', '\n'});
                    Log.i("DEBUG MSG", "UP button REPEAT - STEP = 1");
                } else if (VolumeUPCounter <= 4) {
                    value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'U', 'P', ' ', '6', '\r', '\n'});
                    Log.i("DEBUG MSG", "UP button REPEAT - STEP = 6");
                } else if (VolumeUPCounter <= 6) {
                    value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'U', 'P', ' ', '1', '2', '\r', '\n'});
                    Log.i("DEBUG MSG", "UP button REPEAT - STEP = 12");
                }

                //loc_Command=pk.GetCommand();
                //Log.i("DEBUG MSG", "GET NEXT COMMAND -> |"+ loc_Command +"|");
                //Log.i("DEBUG MSG", "GET GetResWordNum -> |"+ pk.GetCommandNum(loc_Command)+"|");
            } else Log.i("DEBUG MSG", "hercules_L_ConnectedThread NOT EXIST");


        }
    };

    // DOWN
    private Runnable VlumeDWNTask = new Runnable() {
        public void run() {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + 130);


            if (VolumeDOWNCounter < 6) {
                VolumeDOWNCounter++;
            }

            if (value != null) {
                if (VolumeDOWNCounter <= 2) {
                    value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'D', 'O', 'W', 'N', ' ', '1', '\r', '\n'});
                    Log.i("DEBUG MSG", "DOWN button REPEAT - STEP = 1");
                } else if (VolumeDOWNCounter <= 4) {
                    value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'D', 'O', 'W', 'N', ' ', '6', '\r', '\n'});
                    Log.i("DEBUG MSG", "DOWN button REPEAT - STEP = 6");
                } else if (VolumeDOWNCounter <= 6) {
                    value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'D', 'O', 'W', 'N', ' ', '1', '2', '\r', '\n'});
                    Log.i("DEBUG MSG", "DOWN button REPEAT - STEP = 12");
                }
            } else Log.i("DEBUG MSG", "hercules_L_ConnectedThread NOT EXIST");

        }
    };


    //


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // анализируем полученное
            String loc_Command, txt;
            long loc_CommandNum = 0;

            txt = intent.getStringExtra("message");
            Log.d("debug", "------ НАЧАЛО ------ RCActivity: BroadcastReceiver.onReceive() -----------");
            Log.i("debug", " Получили очередную часть команды или целую: " + txt);
            //final String txt = new String(buffer, 0, bytes);


            pk.ReadData(txt.getBytes(), txt.length());
            Log.i("debug", "Вызываем с параметром pk.ReadData(" + txt + ")");

            // Селать здесь цикл разбора команд
            //

            // Получаем очередную команду
            loc_Command = pk.GetCommand();
            loc_CommandNum = pk.GetCommandNum(loc_Command);

            Log.i("debug", "GetCommand: Очередная команда в текстовом виде -> |" + loc_Command + "|");
            Log.i("debug", "GetCommandNum: Числовой номер команды -> |" + loc_CommandNum + "|");

            //if(loc_Command != "")	showToast("Received:<"+ loc_Command+">");


            if (loc_Command != "") {
                // Получена команда
                if ((pk.GetCommandNum(loc_Command) == 1701) && (pk.IsNumericParam == 1))   // WRITE VOLUME
                //if((pk.GetCommandNum(loc_Command) == 1801) && (pk.IsNumericParam == 1))     // R: Volume XXX ( отображение уровня громкости в 1807)
                {
                    // Convert VolumeValue to dB //res = (Volume)*-0.5;
                    // сохраняем впеременную в Application
                    value.VolumeValue = pk.NumericParam;

                    // перерисовываем
                    RedrawVolumeValue();
                    Log.i("debug", " Обновление уровня звука");
                }
//                else //Write Mute
//                    if((pk.GetCommandNum(loc_Command) == 1715) && (pk.IsNumericParam == 0)) {
//                       value.VolumeValue = 0;
                //                       // перерисовываем   `
                //                       RedrawVolumeValue();
//                        Log.i("debug", " Звук отключен, MUTE");
//                    }
                else
                    //if ((pk.GetCommandNum(loc_Command) == 1819))   //R: Warning (всплывающее окно с кнопкой ОК и переходом в сервисное подменю)
                    if ((pk.GetCommandNum(loc_Command) == 19))   // WARNING (всплывающее окно с кнопкой ОК и переходом в сервисное подменю)
                    {
                        AlertDialog.Builder warning_builder = new AlertDialog.Builder(RCActivity.this);//стереть старое!!!! public String StrAccomulator;	// хранит все переданное и полученное по BT
                        warning_builder.setCancelable(false);
                        warning_builder.setTitle("Warning! Value is out of range. Please open Service Menu.");

                        warning_builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Вызов device map
                                Intent intent = new Intent(RCActivity.this, CustomActivity.class);
                                startActivity(intent);

                                //dialog.cancel();
                                dialog.dismiss(); // Do nothing but close the dialog
                            }
                        });

                        warning_builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert1 = warning_builder.create();
                        alert1.show();


                    }


            } // if
            Log.d("debug", "------ КОНЕЦ ------ RCActivity: BroadcastReceiver.onReceive() -----------");

        }
    };

    // Функция перерисовывает значение VV
    protected void RedrawVolumeValue() {

        if (value != null) {
            //value.VolumeValue = pk.NumericParam;

            // Выводим на экран
            if ((127 - value.VolumeValue) * -0.5 < 0) {
                VolumeValueRef.setText(String.valueOf((127 - value.VolumeValue) * -0.5) + " dB");
            } else {
                VolumeValueRef.setText("0.0 dB");
            }


            //VolumeValueRef.setText( String.valueOf(value.VolumeValue*-0.5) );
        } else
            Log.i("ERROR", "ERROR: Class 'Application', variable 'Value' inaccessible ");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("BalAmp Remote");

        // Получаем размеры дисплея
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //int width = size.x;
        //int height = size.y;

        // Вычисляемые параметры
        int padd_top = 0;
        int padd_bottom = 0;
        int padd_left = 0;
        int padd_right = 0;
        double iPhone_kXY = 0; // Коэфиициент оличия основного разрешение от разрешения айфона по Y и X


        //setContentView(R.layout.activity_rc);

        //setContentView(R.layout.tst);


        // Работает на таблетке Toshiba
        if (size.y > size.x) // Portrait
        {
            //setContentView(R.layout.activity_rc);
            //setContentView(R.layout.activity_rc_porter_new);
            //setContentView(R.layout.activity_rc_land);


            setContentView(R.layout.activity_rc_porter);
            //setContentView(R.layout.main_port);


            // Определяем как отличается наше разрешение от эталона
            if (((double) size.y / (double) size.x) >= 1.775) // обрезаем верх и низ
            {
                padd_top = (int) ((size.y - (size.x * 1.775)) / 2);
                padd_bottom = (int) ((size.y - (size.x * 1.775)) / 2);
                iPhone_kXY = ((double) size.x / (double) 640);

                //padd_top 	= (int)(padd_top 	* iPhone_kXY);
                //padd_bottom = (int)(padd_bottom * iPhone_kXY);

            } else // обрезаем слева и справа
            {
                padd_left = (int) ((size.x - (size.y / 1.775)) /*/2*/);
                padd_right = (int) ((size.x - (size.y / 1.775)) /*/2*/);
                iPhone_kXY = ((double) size.y / (double) 1136);

                //padd_left 	= (int)(padd_left 	* iPhone_kXY);
                //padd_right = (int)(padd_right * iPhone_kXY);
            }

            // Подрезаем экран под пропорции iPhone 5
            View view1 = findViewById(R.id.relativelayout1);
            view1.setPadding(padd_left, padd_top, padd_right, padd_bottom); //  (int left, int top, int right, int bottom)
            //view1.setBackgroundColor(0xFFEE3333);


            // Устанавливаем цвет фона
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.BLACK);

            // Устанавливаем цвет текста и фона в textView (УРОВЕНЬ ЗВУКА)
            TextView dbtext = (TextView) findViewById(R.id.textView1);
            //dbtext.setTextColor(Color.rgb(243,243,244));
            dbtext.setTextColor(Color.rgb(113, 211, 242));
            dbtext.setBackgroundColor(Color.BLACK);

            VolumeValueRef = (TextView) findViewById(R.id.textView1);
            // Устанавливаем шрифт Robot
            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto/Roboto-Thin.ttf");
            dbtext.setTypeface(typeface);


            // Устанавливаем смещения для текста (уровень звука)
            view1 = findViewById(R.id.textView1);
            //view1.setPadding(0, ((int)(125 * iPhone_kXY)), 0, ((int)(99 * iPhone_kXY))); //  (int left, int top, int right, int bottom)

            // Это твариант нормльно смотрится на Toshiba
            //view1.setPadding(0, ((int)(90 * iPhone_kXY)), 0, ((int)(75 * iPhone_kXY))); //  (int left, int top, int right, int bottom)

            // Это твариант тестируем для LG
            view1.setPadding(0, ((int) (50 * iPhone_kXY)), 0, ((int) (55 * iPhone_kXY))); //  (int left, int top, int right, int bottom)


            // Устанавливаем размер текста (уровень звука)
            //dbtext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (size.x/78)*12); //  COMPLEX_UNIT_PX     COMPLEX_UNIT_PT
            dbtext.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((int) (83 * iPhone_kXY))); //  COMPLEX_UNIT_PX     COMPLEX_UNIT_PT


            // Устанавливаем смещения для кнопок
            view1 = findViewById(R.id.imageButton1);
            view1.setPadding(((int) (35 * iPhone_kXY)), ((int) (35 * iPhone_kXY)), ((int) (35 * iPhone_kXY)), ((int) (35 * iPhone_kXY))); //  (int left, int top, int right, int bottom)


            ImageButton S1Button_tmp = (ImageButton) findViewById(R.id.imageButton1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) S1Button_tmp.getLayoutParams();
            //params.setMargins(((int)(35 * iPhone_kXY)), ((int)(35 * iPhone_kXY)), ((int)(35 * iPhone_kXY)), ((int)(35 * iPhone_kXY))); //substitute parameters for left, top, right, bottom
            params.setMargins(((int) ((35 * iPhone_kXY) / 2)), ((int) ((35 * iPhone_kXY) / 2)), ((int) ((35 * iPhone_kXY) / 2)), ((int) ((35 * iPhone_kXY) / 2))); //substitute parameters for left, top, right, bottom
            // Закомменитирвоал т.к. нарушается центровка иконки на кнопке
            params.height = ((int) (152 * iPhone_kXY));
            params.width = ((int) (152 * iPhone_kXY));
            S1Button_tmp.setLayoutParams(params);

            ImageButton S2Button_tmp = (ImageButton) findViewById(R.id.imageButton2);
            S2Button_tmp.setLayoutParams(params);

            ImageButton S3Button_tmp = (ImageButton) findViewById(R.id.imageButton3);
            S3Button_tmp.setLayoutParams(params);

            ImageButton S4Button_tmp = (ImageButton) findViewById(R.id.imageButton4);
            S4Button_tmp.setLayoutParams(params);

            ImageButton S5Button_tmp = (ImageButton) findViewById(R.id.imageButton5);
            S5Button_tmp.setLayoutParams(params);

            ImageButton S6Button_tmp = (ImageButton) findViewById(R.id.imageButton6);
            S6Button_tmp.setLayoutParams(params);

            ImageButton S7Button_tmp = (ImageButton) findViewById(R.id.imageButton7);
            S7Button_tmp.setLayoutParams(params);

            ImageButton S8Button_tmp = (ImageButton) findViewById(R.id.imageButton8);
            S8Button_tmp.setLayoutParams(params);

            ImageButton S9Button_tmp = (ImageButton) findViewById(R.id.imageButton9);
            S9Button_tmp.setLayoutParams(params);


            // Работаем с иконкой BALAMP
            View viewIG = findViewById(R.id.imageView1);

            // смотрится на Toshiba
            //viewIG.setPadding(0, ((int)(80 * iPhone_kXY)), 0, ((int)(105 * iPhone_kXY))); //  (int left, int top, int right, int bottom)

            // пробуем чтобы смотрелось на LG
            //viewIG.setPadding(0, ((int)(20 * iPhone_kXY)), 0, ((int)(30 * iPhone_kXY))); //  (int left, int top, int right, int bottom)


            // Универсальный вариант
            //viewIG.setPadding(0, ((int)(80  * iPhone_kXY)), 0, ((int)(105  * iPhone_kXY))); //  (int left, int top, int right, int bottom)


            ImageView img = (ImageView) findViewById(R.id.imageView1);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.balamp);
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            double k_xy = width / height;

            //Смотрится на Toshiba
            //height=((int)(90 * iPhone_kXY));
            //width= (int)((height * k_xy) + iPhone_kXY);  //width= (int)(height * k_xy);

            //Подбираем для LG
            //height=((int)(300 * iPhone_kXY));

            //Подбираем для всех
            //height=250;
            height = (int) (size.y / 12.6);
            width = (int) (height * k_xy);


            // Универсальный вариант
            viewIG.setPadding(0, ((int) (height * iPhone_kXY)), 0, ((int) (height * iPhone_kXY))); //  (int left, int top, int right, int bottom)

            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, (int) (width * iPhone_kXY), (int) (height * iPhone_kXY), true);
            img.setImageBitmap(resizedbitmap);


            //value.StrAccomulator+="\r\n width";
            //value.StrAccomulator+= String.valueOf(width);// Аккумулируем в переменной все полученное
            //value.StrAccomulator+="\r\n height";
            //value.StrAccomulator+= String.valueOf(height);// Аккумулируем в переменной все полученное
        } else // Landscape
        {

            //setContentView(R.layout.activity_rc);
            setContentView(R.layout.activity_rc_land);


            // Определяем как отличается наше разрешение от эталона
            if (((double) size.x / (double) size.y) >= 1.775) // обрезаем слева и справа
            {
                padd_left = (int) ((size.x - (size.y * 1.775)) / 2);
                padd_right = (int) ((size.x - (size.y * 1.775)) / 2);
                iPhone_kXY = ((double) size.y / (double) 640);

                padd_left = (int) (padd_left * iPhone_kXY);
                padd_right = (int) (padd_right * iPhone_kXY);
            } else // обрезаем верх и низ
            {
                padd_top = (int) ((size.y - (size.x / 1.775)) / 2);
                padd_bottom = (int) ((size.y - (size.x / 1.775)) / 2);
                iPhone_kXY = ((double) size.x / (double) 1136);

                padd_top = (int) (padd_top * iPhone_kXY);
                padd_bottom = (int) (padd_bottom * iPhone_kXY);
            }

            // Подрезаем экран под пропорции iPhone 5
            View view1 = findViewById(R.id.relativelayout1);
            view1.setPadding(padd_left, padd_top, padd_right, padd_bottom); //  (int left, int top, int right, int bottom)
            //view1.setBackgroundColor(0xFFEE3333);


            // Устанавливаем цвет фона
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.BLACK);
            // Устанавливаем цвет текста и фона в textView (УРОВЕНЬ ЗВУКА)
            TextView dbtext = (TextView) findViewById(R.id.textView1);
            dbtext.setTextColor(Color.rgb(113, 211, 242));
            dbtext.setBackgroundColor(Color.BLACK);

            VolumeValueRef = (TextView) findViewById(R.id.textView1);
            // Устанавливаем шрифт Robot
            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto/Roboto-Thin.ttf");
            dbtext.setTypeface(typeface);


            // Устанавливаем смещения для текста (уровень звука)
            view1 = findViewById(R.id.textView1);
            view1.setPadding(0, ((int) ((81) * iPhone_kXY)), 0, ((int) (75 * iPhone_kXY))); //  (int left, int top, int right, int bottom)

            // Устанавливаем размер текста (уровень звука)
            dbtext.setTextSize(TypedValue.COMPLEX_UNIT_PX, ((int) (91 * iPhone_kXY))); //  COMPLEX_UNIT_PX     COMPLEX_UNIT_PT


            ImageButton S1Button_tmp = (ImageButton) findViewById(R.id.imageButton1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) S1Button_tmp.getLayoutParams();
            params.setMargins(((int) ((35 * iPhone_kXY) / 2)), ((int) ((35 * iPhone_kXY) / 2)), ((int) ((35 * iPhone_kXY) / 2)), ((int) ((35 * iPhone_kXY) / 2))); //substitute parameters for left, top, right, bottom
            //params.height = ((int)(152 * iPhone_kXY));
            //params.width = ((int)(152 * iPhone_kXY));
            S1Button_tmp.setLayoutParams(params);

            ImageButton S2Button_tmp = (ImageButton) findViewById(R.id.imageButton2);
            S2Button_tmp.setLayoutParams(params);

            ImageButton S3Button_tmp = (ImageButton) findViewById(R.id.imageButton3);
            S3Button_tmp.setLayoutParams(params);

            ImageButton S4Button_tmp = (ImageButton) findViewById(R.id.imageButton4);
            S4Button_tmp.setLayoutParams(params);

            ImageButton S5Button_tmp = (ImageButton) findViewById(R.id.imageButton5);
            S5Button_tmp.setLayoutParams(params);

            ImageButton S6Button_tmp = (ImageButton) findViewById(R.id.imageButton6);
            S6Button_tmp.setLayoutParams(params);

            ImageButton S7Button_tmp = (ImageButton) findViewById(R.id.imageButton7);
            S7Button_tmp.setLayoutParams(params);

            ImageButton S8Button_tmp = (ImageButton) findViewById(R.id.imageButton8);
            S8Button_tmp.setLayoutParams(params);

            ImageButton S9Button_tmp = (ImageButton) findViewById(R.id.imageButton9);
            S9Button_tmp.setLayoutParams(params);


            // Работаем с иконкой BALAMP
            View viewIG = findViewById(R.id.imageView1);
            //viewIG.setPadding(0, ((int)(80 * iPhone_kXY)), 0, ((int)(105 * iPhone_kXY))); //  (int left, int top, int right, int bottom)
            viewIG.setPadding(0, ((int) (50 * iPhone_kXY)), 0, ((int) (80 * iPhone_kXY))); //  (int left, int top, int right, int bottom)

            ImageView img = (ImageView) findViewById(R.id.imageView1);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.balamp);
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            double k_xy = width / height;

            // Toshiba
            //height=((int)(90 * iPhone_kXY));
            //width= (int)((height * k_xy) + iPhone_kXY);

            //Bitmap resizedbitmap=Bitmap.createScaledBitmap(bmp, width, height, true);
            //img.setImageBitmap(resizedbitmap);

            // Пробуем для LG
            height = ((int) (150 * iPhone_kXY));
            width = (int) ((height * k_xy) + iPhone_kXY);

            //Bitmap resizedbitmap=Bitmap.createScaledBitmap(bmp, (int)(width * iPhone_kXY), (int)(height * iPhone_kXY), true);
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
            img.setImageBitmap(resizedbitmap);


        }


        // Получам доступ к содержимому MyApplication
        value = MyApplication.getInstance();

        // Регистрируем ресивер
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("btinput"));

        RedrawVolumeValue();

        // Подготовка (отображение списка найденных устройств)
        itemArrey = new ArrayList<String>();
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemArrey);

        // Перехватчики
        // Поиск БТ устройств
        registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        // Register for broadcasts on BluetoothAdapter state
        registerReceiver(DFmReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(DSmReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));

        registerReceiver(mTestBlueToothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));


        // CD button
        ImageButton S1Button = (ImageButton) findViewById(R.id.imageButton1);
        S1Button.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("CD Button", "MotionEvent.ACTION_DOWN");
                    ImageButton Buttontmp1 = (ImageButton) findViewById(R.id.imageButton1);
                    Buttontmp1.setBackgroundResource(R.drawable.bgblue);
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("CD Button", "MotionEvent.ACTION_UP");
                    ImageButton Buttontmp1 = (ImageButton) findViewById(R.id.imageButton1);
                    Buttontmp1.setBackgroundResource(R.drawable.bgoran);
                    //mHandler.removeCallbacks(CDTask);

                    if (value != null) {
                        value.WriteToSocket(new byte[]{'I', 'N', 'P', 'U', 'T', ' ', '1', '\r', '\n'});
                    }
                }
                return false;
            }
        });

        // VINIL button
        ImageButton S2Button = (ImageButton) findViewById(R.id.imageButton2);
        S2Button.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("Vinil Button", "MotionEvent.ACTION_DOWN");

                    ImageButton Buttontmp2 = (ImageButton) findViewById(R.id.imageButton2);
                    Buttontmp2.setBackgroundResource(R.drawable.bgblue);
                    //mHandler.removeCallbacks(VinilTask);
                    //mHandler.postAtTime(VinilTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("Vinil Button", "MotionEvent.ACTION_UP");

                    ImageButton Buttontmp2 = (ImageButton) findViewById(R.id.imageButton2);
                    Buttontmp2.setBackgroundResource(R.drawable.bgoran);

                    //mHandler.removeCallbacks(VinilTask);
                    if (value != null) {
                        value.WriteToSocket(new byte[]{'I', 'N', 'P', 'U', 'T', ' ', '2', '\r', '\n'});
                    }
                }
                return false;
            }
        });

        // MEDIA button
        ImageButton S3Button = (ImageButton) findViewById(R.id.imageButton3);
        S3Button.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("Media Button", "MotionEvent.ACTION_DOWN");
                    ImageButton Buttontmp3 = (ImageButton) findViewById(R.id.imageButton3);
                    Buttontmp3.setBackgroundResource(R.drawable.bgblue);
                    //mHandler.removeCallbacks(MediaTask);
                    //mHandler.postAtTime(MediaTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("Media Button", "MotionEvent.ACTION_UP");
                    ImageButton Buttontmp3 = (ImageButton) findViewById(R.id.imageButton3);
                    Buttontmp3.setBackgroundResource(R.drawable.bgoran);
                    //mHandler.removeCallbacks(MediaTask);
                    if (value != null) {
                        value.WriteToSocket(new byte[]{'I', 'N', 'P', 'U', 'T', ' ', '3', '\r', '\n'});
                    }
                }
                return false;
            }
        });

        // Settings button
        ImageButton S4Button = (ImageButton) findViewById(R.id.imageButton4);
        S4Button.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("SETUP Button", "MotionEvent.ACTION_DOWN");
                    ImageButton Buttontmp4 = (ImageButton) findViewById(R.id.imageButton4);
                    Buttontmp4.setBackgroundResource(R.drawable.bgblue);
                    //mHandler.removeCallbacks(SetTask);
                    //mHandler.postAtTime(SetTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("SETUP Button", "MotionEvent.ACTION_UP");
                    //if (hercules_l_ConnectedThread != null)
                    //{
                    //	hercules_l_ConnectedThread.write(new byte[] {'S','E','T','T','I','N', 'G', 'S',' ','\r','\n' });
                    //}
                    //else Log.i("DEBUG MSG", "hercules_L_ConnectedThread NOT EXIST");

                    ImageButton Buttontmp4 = (ImageButton) findViewById(R.id.imageButton4);
                    Buttontmp4.setBackgroundResource(R.drawable.bgoran);

                    if (value != null) {
                        //value.WriteToSocket(new byte[] {'S','E','T','U','P', ' ','\r','\n' });
                        value.WriteToSocket(new byte[]{'S', 'E', 'T', ' ', '\r', '\n'});
                    }

                    // Вызов device map
                    Intent intent = new Intent(RCActivity.this, CustomActivity.class);
                    startActivity(intent);

                    //mHandler.removeCallbacks(SetTask);
                }
                return false;
            }
        });


//    	static int PowerUPCounter = 0;
//    	static int PowerDOWNCounter = 0;


        //----------------------------------------------------------------------------------
        ImageButton PowerUPButton = (ImageButton) findViewById(R.id.imageButton5);
        PowerUPButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("PowerUP Button", "MotionEvent.ACTION_DOWN");

                    ImageButton Buttontmp5 = (ImageButton) findViewById(R.id.imageButton5);
                    Buttontmp5.setBackgroundResource(R.drawable.bgblue);

                    mHandler.removeCallbacks(PowerUPTask);
                    mHandler.postAtTime(PowerUPTask, SystemClock.uptimeMillis() + 70);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("PowerUP Button", "MotionEvent.ACTION_UP");

                    ImageButton Buttontmp5 = (ImageButton) findViewById(R.id.imageButton5);
                    Buttontmp5.setBackgroundResource(R.drawable.bgoran);

                    mHandler.removeCallbacks(PowerUPTask);
                    if (PowerUPCounter > 10) {
                        if (value != null) {
                            //value.WriteToSocket(new byte[] {'P', 'O', 'W', 'E', 'R',' ', 'U', 'P', ' ','\r','\n' });
                        }
                    }
                    PowerUPCounter = 0; // Обнуляем счетчик если кнопка отпущена
                }
                return false;
            }
        });


        //----------------------------------------------------------------------------------
        ImageButton PowerDOWNButton = (ImageButton) findViewById(R.id.imageButton6);
        PowerDOWNButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("PowerDOWN Button", "MotionEvent.ACTION_DOWN");

                    ImageButton Buttontmp6 = (ImageButton) findViewById(R.id.imageButton6);
                    Buttontmp6.setBackgroundResource(R.drawable.bgblue);

                    mHandler.removeCallbacks(PowerDOWNTask);
                    mHandler.postAtTime(PowerDOWNTask, SystemClock.uptimeMillis() + 70);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("PowerDOWN Button", "MotionEvent.ACTION_UP");

                    ImageButton Buttontmp6 = (ImageButton) findViewById(R.id.imageButton6);
                    Buttontmp6.setBackgroundResource(R.drawable.bgoran);

                    mHandler.removeCallbacks(PowerDOWNTask);
                    if (PowerDOWNCounter > 10) {
                        if (value != null) {
                            //value.WriteToSocket(new byte[] {'P', 'O', 'W', 'E', 'R',' ','D', 'O','W', 'N', ' ','\r','\n' });
                        }
                    }
                    PowerDOWNCounter = 0; // Обнуляем счетчик если кнопка отпущена
                }
                return false;
            }
        });


        //----------------------------------------------------------------------------------
        ImageButton VolUPButton = (ImageButton) findViewById(R.id.imageButton7);
        VolUPButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("VolUPButton", "MotionEvent.ACTION_DOWN");

                    ImageButton Buttontmp7 = (ImageButton) findViewById(R.id.imageButton7);
                    Buttontmp7.setBackgroundResource(R.drawable.bgblue);

                    mHandler.removeCallbacks(VlumeUPTask);
                    mHandler.postAtTime(VlumeUPTask, SystemClock.uptimeMillis() + 70);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("VolUPButton", "MotionEvent.ACTION_UP");

                    ImageButton Buttontmp7 = (ImageButton) findViewById(R.id.imageButton7);
                    Buttontmp7.setBackgroundResource(R.drawable.bgoran);

                    //если небыло не одного повторения, функция  private Runnable VlumeUPTask = new Runnable()  ни разу неотработала, то отправляем команду с шагом один
                    if (VolumeUPCounter == 0) {
                        if (value != null) {
                            value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'U', 'P', ' ', '1', '\r', '\n'});
                        }
                    }
                    VolumeUPCounter = 0; // Обнуляем счетчик если кнопка отпущена
                    mHandler.removeCallbacks(VlumeUPTask);
                }
                return false;
            }
        });


        //----------------------------------------------------------------------------------
        ImageButton VolDWNButton = (ImageButton) findViewById(R.id.imageButton9);
        VolDWNButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("VolDWNButton", "MotionEvent.ACTION_DOWN");

                    ImageButton Buttontmp9 = (ImageButton) findViewById(R.id.imageButton9);
                    Buttontmp9.setBackgroundResource(R.drawable.bgblue);

                    mHandler.removeCallbacks(VlumeDWNTask);
                    mHandler.postAtTime(VlumeDWNTask, SystemClock.uptimeMillis() + 70);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("VolDWNButton", "MotionEvent.ACTION_UP");

                    ImageButton Buttontmp9 = (ImageButton) findViewById(R.id.imageButton9);
                    Buttontmp9.setBackgroundResource(R.drawable.bgoran);

                    //если небыло не одного повторения, функция  private Runnable VlumeDWNTask = new Runnable()  ни разу не отработала, то отправляем команду с шагом один
                    if (VolumeDOWNCounter == 0) {
                        if (value != null) {
                            value.WriteToSocket(new byte[]{'V', 'O', 'L', 'U', 'M', 'E', ' ', 'D', 'O', 'W', 'N', ' ', '1', '\r', '\n'});
                        }
                    }
                    VolumeDOWNCounter = 0; // Обнуляем счетчик если кнопка отпущена
                    mHandler.removeCallbacks(VlumeDWNTask);
                }
                return false;
            }
        });


        // MUTE button
        ImageButton MuteButton = (ImageButton) findViewById(R.id.imageButton8);
        MuteButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i("Mute Button", "MotionEvent.ACTION_DOWN");

                    ImageButton Buttontmp8 = (ImageButton) findViewById(R.id.imageButton8);
                    Buttontmp8.setBackgroundResource(R.drawable.bgblue);

                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                    if (value != null) {
                        value.WriteToSocket(new byte[]{'M', 'U', 'T', 'E', ' ', '\r', '\n'});
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i("Mute Button", "MotionEvent.ACTION_UP");

                    ImageButton Buttontmp8 = (ImageButton) findViewById(R.id.imageButton8);
                    Buttontmp8.setBackgroundResource(R.drawable.bgoran);

                    //mHandler.removeCallbacks(CDTask);


                }
                return false;
            }
        });




      	/*
        // Вариант коннекта когда устройство выбирается из списка "спариных" устройств
		if (value.IsConnect == 0) // Проверяем есть коннект или нету
		// if( (!bts.isConnected())) // Проверяем есть коннект или нету
		{
			// Отображаем список и создаем обработчик нажатия на элемент списка
			// if (pairedDevices.size() > 0)
			if (value.listItems.size() > 0) {
				final CharSequence[] items = value.listItems.toArray(new CharSequence[value.listItems.size()]);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Select device");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// Do something with the selection
						// SetIndex(item); // Получаем позицию устройства в
						// списке и сохраняем
						Log.i("debug"," SELECT DEVICE is:" + String.valueOf(item));

						// Коннектимся к выбранному устройству
						value.ConnectToBTdevice(item); // результат коннекта число которое возвращает функция, 0 - нет коннекта, 1 - есть
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			} // if (value.listItems.size() > 0)

		}
		*/


        // Если Bluetooth выключен то предлагаем его включить
        if (!value.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            //showToast("Включите BlueTooth!");
        }


        // Вариант коннекта когда устройство выбирается из списка найденых (Сканируем пространство)
        // устройств
        if (value.IsConnect == 0) // Проверяем есть коннект или нету
        // if( (!bts.isConnected())) // Проверяем есть коннект или нету
        {
            // Отображаем список ищем и создаем обработчики
            ShowBTDevList();

        }


    }

    // Завершаем приложение интерфейс в класс Application
    public void Exit() {
        if (value != null) {
            value.Disconnect();

            // Сохраняем StrAccomulator в файл
            //Log.d("DEBUG", "Сохраняем StrAccomulator в файл/r/n" + value.StrAccomulator );
            //appendLog(value.StrAccomulator);
        }

        super.onBackPressed();

    }

    @Override
    public void onDestroy() {
        // Удаляем ресивер вместе с удалением активити
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        // Разрываем соединение
        //if(value != null)   // не использовать в onDestroy() т.к. при перевороте экрана происходит разрыв связи
        //{
        //	value.Disconnect();
        //}


        Log.i("debug", "RCActivity - onDestroy ");

        // Unregister broadcast listeners
        unregisterReceiver(ActionFoundReceiver);
        unregisterReceiver(DSmReceiver);
        unregisterReceiver(DFmReceiver);

        unregisterReceiver(mTestBlueToothReceiver);// test


        super.onDestroy();
    }


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.rc, menu);
//		return true;
//	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "About");
        return result;
    }


    // Обработка нажатия пунктов системного меню
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {

                AlertDialog.Builder aboutbuilder = new AlertDialog.Builder(this);
                aboutbuilder.setTitle("About");
                aboutbuilder.setMessage("BalAmp Bluetooth Control V1.0\r\ninfo@balamp.com\r\nbalamp.com@gmail.com\r\nBalAmp ©2013\r\n");

                aboutbuilder.setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Do nothing but close the dialog
                            }
                        });

                AlertDialog alert1 = aboutbuilder.create();
                alert1.show();

                return true;
            }

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        AlertDialog.Builder closebuilder = new AlertDialog.Builder(this);
        closebuilder.setCancelable(false);

        closebuilder.setTitle("Do you want disconnect device?");
        // builder.setMessage("Are you sure?");

        closebuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Exit();
                dialog.dismiss(); // Do nothing but close the dialog
            }
        });

        closebuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert1 = closebuilder.create();
        alert1.show();

        // super.onBackPressed();
    }

    private void showToast(String text) {
        // Первый варинат
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        //Log.i("debug","Toast.makeText " +text);

    }


    /// Раздел. Тут находятся функции для отображения диалогового окна "Поиск БТ устройсв" плюс нарзные обработчики перехватчики

    public void ShowBTDevList() {
        // custom dialog
        SearchDialog = new Dialog(locCont);
        SearchDialog.setContentView(R.layout.selectbt);
        SearchDialog.setTitle("Select BT device");
        SearchDialog.setCancelable(false);
        SearchDialog.setCanceledOnTouchOutside(false);

        BTList = (ListView) SearchDialog.findViewById(R.id.listView1);
        BTList.setAdapter(itemAdapter);

        //Select Item
        BTList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                CancelDiscovery();

                Object o = BTList.getItemAtPosition(position);
                String str = (String) o;// As you are using Default String Adapter

                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                String macaddress = str.substring(str.length() - 17);
                //Toast.makeText(getApplicationContext(), macaddress, Toast.LENGTH_SHORT).show();
                value.ConnectToBTdeviceByMAC(macaddress);

                SearchDialog.dismiss();
            }
        });

        //Cancel
        dialogButton2 = (Button) SearchDialog.findViewById(R.id.buttonCancel);        // if button is clicked, close the custom dialog
        dialogButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelDiscovery();
                SearchDialog.dismiss();
            }
        });

        //Scan
        dialogButton3 = (Button) SearchDialog.findViewById(R.id.buttonScan);        // if button is clicked, close the custom dialog
        dialogButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDiscovery();
                //SearchDialog.dismiss();
            }
        });


        //CheckBlueToothState();

        SearchDialog.show();

        //showProgressDialog();

        //itemArrey.add(0,"qqqq 1");
        //itemArrey.add(0,"qqqq 2");
        //itemArrey.add(0,"qqqq 3");
        //itemArrey.add(0,"qqqq 4");
        //itemAdapter.notifyDataSetChanged();

        StartDiscovery();
    }

    // Перехват события - "Найдено новое устройство"
    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                itemAdapter.add(device.getName() + "\n" + device.getAddress());
                itemAdapter.notifyDataSetChanged();
            }
        }
    };


    // Перехват события ACTION_DISCOVERY_FINISHED
    private final BroadcastReceiver DFmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //final String action = intent.getAction();
            //showToast(action);
            //tv.append(action+"\r\n");
            //hideProgressDialog();
            SearchDialog.setTitle("Select BT device");
            dialogButton3.setEnabled(true);
        }
    };

    // Перехват события ACTION_DISCOVERY_STARTED
    private final BroadcastReceiver DSmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //final String action = intent.getAction();
            //showToast(action);
            //tv.append(action+"\r\n");
            //showProgressDialog();
            SearchDialog.setTitle("Select BT device (Scanning...)");
            dialogButton3.setEnabled(false);
        }
    };

    // Запуск сканирования БТ устройств
    public void StartDiscovery() {
        itemAdapter.clear();
        CancelDiscovery();

        BluetoothAdapter BTAs = BluetoothAdapter.getDefaultAdapter();
        BTAs.startDiscovery();
    }

    ;

    // Отмена сканирования БТ устройств
    public void CancelDiscovery() {
        BluetoothAdapter BTAc = BluetoothAdapter.getDefaultAdapter();

        if (BTAc.isDiscovering()) {
            BTAc.cancelDiscovery();
        }
    }

    ;


    /// ------------------------------------------------------------------------------------------------------------------------

    private BroadcastReceiver mTestBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();

                showToast("TEST " + action);
                Log.d("debug", "Toast.BroadcastReceiver " + action);

/*
				if (action == null)
					return;

				if (action.equals(ACTION_BT_HEADSET_STATE_CHANGED)) {
					int extraData = intent.getIntExtra(EXTRA_STATE,STATE_DISCONNECTED);
					if (extraData == STATE_CONNECTED) {

						// TODO :

					} else if (extraData == STATE_DISCONNECTED) {

						// TODO:
					}
				}
				*/
            } catch (Exception e) {

                // TODO:

            }
        }
    };

}





