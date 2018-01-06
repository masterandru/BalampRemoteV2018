package com.example.ui_test3;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import rc.balamp.R;

public class CustomActivity extends Activity {

    private TextView tv;
    //private  ScrollView mScrollView;
    private ScrollView sv;
    private MyApplication value; // доступ к содержимому MyApplication

    final Context context = this;

    // Счетчики повторений приудержании кнопки
    private int MAXPressCounter = 50;    // максимаьное значение счетчика (вводится для того чтобы не переходить через ноль)
//    private int B1PressCounter = 0;        //
    private int B2PressCounter = 0;        //


    // Настройки самой активити
    private boolean ConfigMode = false; // Для настройки кнопок
    private boolean PutFinalizeSumbols = true; // завершать посылку симаволами /r/n или нет
    private int ButtonNumber = 0; // Номер нажатой кнопки, передаем в обработчик кнопки
    private int msec = 130;                // Задержка в милисекундах

    TextView bname; // Переменные для связи EеxtEdit в диалоге параметров
    TextView bvalue;


    // Пеерменные хранят параметры кнопок
    private String B1Name = "";
    private String B1Value = "";

    private String B2Name = "";
    private String B2Value = "";

    private String B3Name = "";
    private String B3Value = "";

    private String B4Name = "";
    private String B4Value = "";

    private String B5Name = "";
    private String B5Value = "";

    private String B6Name = "";
    private String B6Value = "";

    private String B7Name = "";
    private String B7Value = "";

    private String B8Name = "";
    private String B8Value = "";

    private String B9Name = "";
    private String B9Value = "";

    private String B10Name = "";
    private String B10Value = "";

    private String B11Name = "";
    private String B11Value = "";

    private String B12Name = "";
    private String B12Value = "";

    private String B13Name = "";
    private String B13Value = "";

    private String B14Name = "";
    private String B14Value = "";

    private String B15Name = "";
    private String B15Value = "";

    private String B16Name = "";
    private String B16Value = "";


    // -----------

    // Функция-интерфейс отправляет данные по блутус с учетом флагов (добавлять завершающие символы или нет)
    private int SendCommand(byte a[]) {
        int result = -1; // результат-ошибка (-1) (+1 - все хорошо)

        if (value != null) {

            if (PutFinalizeSumbols) // добавляем символы окончания /r/n
            {
                //byte a[] = B2Value.getBytes();
                byte b[] = new byte[]{'\r', '\n'};
                byte[] c = new byte[a.length + b.length];
                System.arraycopy(a, 0, c, 0, a.length);
                System.arraycopy(b, 0, c, a.length, b.length);
                value.WriteToSocket(c);
                result = 1;
            } else                    // без символов окончания
            {
                byte d[] = B2Value.getBytes();
                value.WriteToSocket(d);
                result = 1;
            }

            tv.setText(value.strAccumulator);    // Подгружаем текст из переменной
            sv.scrollTo(0, tv.getBottom());        // Прокручиваем текст максимально вниз

            // Проверить что из (sv.scrollTo(0, tv.getBottom());) или вариант диже работает.
            // Прокручиваем текст максимально вниз
            sv = (ScrollView) findViewById(R.id.scrollView1);
            sv.post(new Runnable() {
                public void run() {
                    sv.scrollTo(0, tv.getBottom());
                }
            });

        }
        return result;

    }

    private Handler mHandler = new Handler();
    // BTN 01
    private Runnable B01Task = new Runnable() {
        public void run() {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + msec);
            //String loc_Command;


            if (value != null) {
                value.WriteToSocket(new byte[]{'C', 'D', ' ', '\r', '\n'});

            }
            //else Log.i("DEBUG MSG", "value is null");

            Log.i("DEBUG MSG", "BTN 01 button REPEAT");
        }
    };

    // BTN 02
    private Runnable B02Task = new Runnable() {
        public void run() {
            mHandler.postAtTime(this, SystemClock.uptimeMillis() + msec);
            SendCommand(B2Value.getBytes());
            if (MAXPressCounter < 50) B2PressCounter++;
            Log.i("DEBUG MSG", "BTN 02 button REPEAT");
        }
    };


    //
    private BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("lbr", "CustomActivity: BroadcastReceiver - Message received: " + intent.getStringExtra("message"));

            // Добавляем полученное
            //tv.append(intent.getStringExtra("message"));


            // Загружаем текст из класса Application
            tv.setText(value.strAccumulator);

            // Прокручиваем текст максимально вниз
            //ScrollView sv = (ScrollView)findViewById(R.id.scrollView1);
            //sv.scrollTo(0, sv.getBottom());
            sv.scrollTo(0, tv.getBottom());


            // Проверить что из (sv.scrollTo(0, tv.getBottom());) или вариант диже работает.
            // Прокручиваем текст максимально вниз
            sv = (ScrollView) findViewById(R.id.scrollView1);
            sv.post(new Runnable() {
                public void run() {
                    sv.scrollTo(0, tv.getBottom());
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Custom control");
        setContentView(R.layout.activity_custom);


        tv = (TextView) findViewById(R.id.textView1);

        // Регистрируем ресивер
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver1, new IntentFilter("btinput"));

        // Получам доступ к содержимому MyApplication
        value = MyApplication.getInstance();


        // Загружаем текст из класса Application сразу после создания
        tv.setText(value.strAccumulator);

        // Прокручиваем текст максимально вниз
        sv = (ScrollView) findViewById(R.id.scrollView1);
        sv.post(new Runnable() {
            public void run() {
                sv.scrollTo(0, tv.getBottom());
            }
        });


        // Загрузка параметров
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        B1Name = sp.getString("Button1Name", "Button 01");
        B1Value = sp.getString("Button1Value", "empty");

        B2Name = sp.getString("Button2Name", "Button 02");
        B2Value = sp.getString("Button2Value", "empty");

        B3Name = sp.getString("Button3Name", "Button 03");
        B3Value = sp.getString("Button3Value", "empty");

        B4Name = sp.getString("Button4Name", "Button 04");
        B4Value = sp.getString("Button4Value", "empty");

        B5Name = sp.getString("Button5Name", "Button 05");
        B5Value = sp.getString("Button5Value", "empty");

        B6Name = sp.getString("Button6Name", "Button 06");
        B6Value = sp.getString("Button6Value", "empty");

        B7Name = sp.getString("Button7Name", "Button 07");
        B7Value = sp.getString("Button7Value", "empty");

        B8Name = sp.getString("Button8Name", "Button 08");
        B8Value = sp.getString("Button8Value", "empty");

        B9Name = sp.getString("Button9Name", "Button 09");
        B9Value = sp.getString("Button9Value", "empty");

        B10Name = sp.getString("Button10Name", "Button 10");
        B10Value = sp.getString("Button10Value", "empty");

        B11Name = sp.getString("Button11Name", "Button 11");
        B11Value = sp.getString("Button11Value", "empty");

        B12Name = sp.getString("Button12Name", "Button 12");
        B12Value = sp.getString("Button12Value", "empty");

        B13Name = sp.getString("Button13Name", "Button 13");
        B13Value = sp.getString("Button13Value", "empty");

        B14Name = sp.getString("Button14Name", "Button 14");
        B14Value = sp.getString("Button14Value", "empty");

        B15Name = sp.getString("Button15Name", "Button 15");
        B15Value = sp.getString("Button15Value", "empty");

        B16Name = sp.getString("Button16Name", "Button 16");
        B16Value = sp.getString("Button16Value", "empty");

        PutFinalizeSumbols = sp.getBoolean("FinalizeSumbols", true);

        //tv.append("("+B1Name+")");
        //tv.append("("+B1Value+")");


        // button 1
        Button MyButton01 = (Button) findViewById(R.id.Button01);
        MyButton01.setText(B1Name); // Одновляем имя кнопки
        MyButton01.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B1Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(B01Task);
                    //mHandler.postAtTime(B01Task, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B1Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(B02Task);
                        //if(B2PressCounter == 0)
                        //{
                        SendCommand(B1Value.getBytes());
                        //}
                    } else // настраиваем
                    {
                        ShowDialog(1);

                    }

                }
                return false;
            }
        });

        // button 2
        Button MyButton02 = (Button) findViewById(R.id.Button02);
        MyButton02.setText(B2Name); // Одновляем имя кнопки
        MyButton02.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B2Name, "MotionEvent.ACTION_DOWN");

                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(B02Task);
                        //mHandler.postAtTime(B02Task, SystemClock.uptimeMillis() + 100); // разобраться зачем два значения
                    }
                } else if (action == MotionEvent.ACTION_UP)        // при отпускании кнопки
                {
                    Log.i(B2Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(B02Task);
                        //if(B2PressCounter == 0)				// если кнопка была нажата слишком быстро, так что Runnable Task
                        //{										// не отработал, то отправляем команду один раз
                        SendCommand(B2Value.getBytes());    // отправляем комнду
                        //B2PressCounter = 0; 				// обнуляем счетчик
                        //}
                    } else // настраиваем
                    {
                        ShowDialog(2);

                    }
                }
                return false;
            }
        });


        // button 3
        Button MyButton03 = (Button) findViewById(R.id.Button03);
        MyButton03.setText(B3Name); // Одновляем имя кнопки
        MyButton03.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B3Name, "MotionEvent.ACTION_DOWN");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                    }

                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B3Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B3Value.getBytes());
                    } else {
                        ShowDialog(3);
                    }

                }
                return false;
            }
        });

        // button 4
        Button MyButton04 = (Button) findViewById(R.id.Button04);
        MyButton04.setText(B4Name); // Одновляем имя кнопки
        MyButton04.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B4Name, "MotionEvent.ACTION_DOWN");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B4Name, "MotionEvent.ACTION_UP");

                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B4Value.getBytes());
                    } else {
                        ShowDialog(4);
                    }
                }
                return false;
            }
        });

        // button 5
        Button MyButton05 = (Button) findViewById(R.id.Button05);
        MyButton05.setText(B5Name); // Одновляем имя кнопки
        MyButton05.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B5Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B5Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B5Value.getBytes());
                    } else {
                        ShowDialog(5);
                    }
                }
                return false;
            }
        });

        // button 6
        Button MyButton06 = (Button) findViewById(R.id.Button06);
        MyButton06.setText(B6Name); // Одновляем имя кнопки
        MyButton06.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B6Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B6Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B6Value.getBytes());
                    } else {
                        ShowDialog(6);
                    }
                }
                return false;
            }
        });

        // button 7
        Button MyButton07 = (Button) findViewById(R.id.Button07);
        MyButton07.setText(B7Name); // Одновляем имя кнопки
        MyButton07.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B7Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B7Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B7Value.getBytes());
                    } else {
                        ShowDialog(7);
                    }
                }
                return false;
            }
        });

        // button 8
        Button MyButton08 = (Button) findViewById(R.id.Button08);
        MyButton08.setText(B8Name); // Одновляем имя кнопки
        MyButton08.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B8Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B8Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B8Value.getBytes());
                    } else {
                        ShowDialog(8);
                    }
                }
                return false;
            }
        });

        // button 9
        Button MyButton09 = (Button) findViewById(R.id.Button09);
        MyButton09.setText(B9Name); // Одновляем имя кнопки
        MyButton09.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B9Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B9Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B9Value.getBytes());
                    } else {
                        ShowDialog(9);
                    }
                }
                return false;
            }
        });


        // button 10
        Button MyButton10 = (Button) findViewById(R.id.Button10);
        MyButton10.setText(B10Name); // Одновляем имя кнопки
        MyButton10.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B10Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B10Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B10Value.getBytes());
                    } else {
                        ShowDialog(10);
                    }
                }
                return false;
            }
        });

        // button 11
        Button MyButton11 = (Button) findViewById(R.id.Button11);
        MyButton11.setText(B11Name); // Одновляем имя кнопки
        MyButton11.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B11Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B11Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B11Value.getBytes());
                    } else {
                        ShowDialog(11);
                    }
                }
                return false;
            }
        });

        // button 12
        Button MyButton12 = (Button) findViewById(R.id.Button12);
        MyButton12.setText(B12Name); // Одновляем имя кнопки
        MyButton12.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B12Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B12Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B12Value.getBytes());
                    } else {
                        ShowDialog(12);
                    }
                }
                return false;
            }
        });

        // button 13
        Button MyButton13 = (Button) findViewById(R.id.Button13);
        MyButton13.setText(B13Name); // Одновляем имя кнопки
        MyButton13.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B13Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B13Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B13Value.getBytes());
                    } else {
                        ShowDialog(13);
                    }
                }
                return false;
            }
        });

        // button 14
        Button MyButton14 = (Button) findViewById(R.id.Button14);
        MyButton14.setText(B14Name); // Одновляем имя кнопки
        MyButton14.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B14Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B14Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B14Value.getBytes());
                    } else {
                        ShowDialog(14);
                    }
                }
                return false;
            }
        });

        // button 15
        Button MyButton15 = (Button) findViewById(R.id.Button15);
        MyButton15.setText(B15Name); // Одновляем имя кнопки
        MyButton15.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B15Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B15Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B15Value.getBytes());
                    } else {
                        ShowDialog(15);
                    }
                }
                return false;
            }
        });

        // button 16
        Button MyButton16 = (Button) findViewById(R.id.Button16);
        MyButton16.setText(B16Name); // Одновляем имя кнопки
        MyButton16.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Log.i(B16Name, "MotionEvent.ACTION_DOWN");
                    //mHandler.removeCallbacks(CDTask);
                    //mHandler.postAtTime(CDTask, SystemClock.uptimeMillis() + 100);
                } else if (action == MotionEvent.ACTION_UP) {
                    Log.i(B16Name, "MotionEvent.ACTION_UP");
                    if (!ConfigMode) {
                        //mHandler.removeCallbacks(CDTask);
                        SendCommand(B16Value.getBytes());
                    } else {
                        ShowDialog(16);
                    }
                }
                return false;
            }
        });
    }

    // Сохраняет параметры кнопки после изменения их при помощи диалога
    public void SaveButtonSettings() {
        Button locButton;

        switch (ButtonNumber) {
            case 1: // Кнопка 1
            {
                B1Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button01);
                locButton.setText(B1Name);
                B1Value = bvalue.getText().toString();
                break;
            }
            case 2: // Кнопка 2
            {
                B2Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button02);
                locButton.setText(B2Name);
                B2Value = bvalue.getText().toString();
                break;
            }

            case 3: // Кнопка 3
            {
                B3Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button03);
                locButton.setText(B3Name);
                B3Value = bvalue.getText().toString();
                break;
            }
            case 4: // Кнопка 4
            {
                B4Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button04);
                locButton.setText(B4Name);
                B4Value = bvalue.getText().toString();
                break;
            }
            case 5: // Кнопка 5
            {
                B5Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button05);
                locButton.setText(B5Name);
                B5Value = bvalue.getText().toString();
                break;
            }

            case 6: // Кнопка 6
            {
                B6Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button06);
                locButton.setText(B6Name);
                B6Value = bvalue.getText().toString();
                break;
            }
            case 7: // Кнопка 7
            {
                B7Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button07);
                locButton.setText(B7Name);
                B7Value = bvalue.getText().toString();
                break;
            }
            case 8: // Кнопка 8
            {
                B8Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button08);
                locButton.setText(B8Name);
                B8Value = bvalue.getText().toString();
                break;
            }
            case 9: // Кнопка 9
            {
                B9Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button09);
                locButton.setText(B9Name);
                B9Value = bvalue.getText().toString();
                break;
            }
            case 10: // Кнопка 10
            {
                B10Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button10);
                locButton.setText(B10Name);
                B10Value = bvalue.getText().toString();
                break;
            }
            case 11: // Кнопка 11
            {
                B11Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button11);
                locButton.setText(B11Name);
                B11Value = bvalue.getText().toString();
                break;
            }
            case 12: // Кнопка 12
            {
                B12Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button12);
                locButton.setText(B12Name);
                B12Value = bvalue.getText().toString();
                break;
            }
            case 13: // Кнопка 13
            {
                B13Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button13);
                locButton.setText(B13Name);
                B13Value = bvalue.getText().toString();
                break;
            }
            case 14: // Кнопка 14
            {
                B14Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button14);
                locButton.setText(B14Name);
                B14Value = bvalue.getText().toString();
                break;
            }
            case 15: // Кнопка 15
            {
                B15Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button15);
                locButton.setText(B15Name);
                B15Value = bvalue.getText().toString();
                break;
            }
            case 16: // Кнопка 16
            {
                B16Name = bname.getText().toString();
                locButton = (Button) findViewById(R.id.Button16);
                locButton.setText(B16Name);
                B16Value = bvalue.getText().toString();
                break;
            }


            default: {
            }
        }

    }


    public void ShowDialog(int locButtonNumber) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.buttonconfigdialog);
        dialog.setTitle("Change button settings");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ButtonNumber = locButtonNumber; // Сохраняем номер кнопки чтобы использовать его в SaveButtonSettings()

        // set the custom dialog components - text, image and button
        bname = (TextView) dialog.findViewById(R.id.btnname);
        bvalue = (TextView) dialog.findViewById(R.id.btnvalue);

        switch (locButtonNumber) {
            case 1: // Кнопка 1
            {
                bname.setText(B1Name);
                bvalue.setText(B1Value);
                break;
            }
            case 2: // Кнопка 2
            {
                bname.setText(B2Name);
                bvalue.setText(B2Value);
                break;
            }
            case 3: // Кнопка 3
            {
                bname.setText(B3Name);
                bvalue.setText(B3Value);
                break;
            }
            case 4: // Кнопка 4
            {
                bname.setText(B4Name);
                bvalue.setText(B4Value);
                break;
            }
            case 5: // Кнопка 5
            {
                bname.setText(B5Name);
                bvalue.setText(B5Value);
                break;
            }
            case 6: // Кнопка 6
            {
                bname.setText(B6Name);
                bvalue.setText(B6Value);
                break;
            }
            case 7: // Кнопка 7
            {
                bname.setText(B7Name);
                bvalue.setText(B7Value);
                break;
            }
            case 8: // Кнопка 8
            {
                bname.setText(B8Name);
                bvalue.setText(B8Value);
                break;
            }
            case 9: // Кнопка 9
            {
                bname.setText(B9Name);
                bvalue.setText(B9Value);
                break;
            }
            case 10: // Кнопка 10
            {
                bname.setText(B10Name);
                bvalue.setText(B10Value);
                break;
            }
            case 11: // Кнопка 11
            {
                bname.setText(B11Name);
                bvalue.setText(B11Value);
                break;
            }
            case 12: // Кнопка 12
            {
                bname.setText(B12Name);
                bvalue.setText(B12Value);
                break;
            }
            case 13: // Кнопка 13
            {
                bname.setText(B13Name);
                bvalue.setText(B13Value);
                break;
            }
            case 14: // Кнопка 14
            {
                bname.setText(B14Name);
                bvalue.setText(B14Value);
                break;
            }
            case 15: // Кнопка 15
            {
                bname.setText(B15Name);
                bvalue.setText(B15Value);
                break;
            }
            case 16: // Кнопка 16
            {
                bname.setText(B16Name);
                bvalue.setText(B16Value);
                break;
            }


            default: {
            }


        }


        //ImageView image = (ImageView) dialog.findViewById(R.id.image);
        //image.setImageResource(R.drawable.ic_launcher);

        Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new OnClickListener() {
            //dialogButton.setOnTouchListener( new OnTouchListener()
            @Override
            public void onClick(View v)
            //public boolean onTouch(View view, MotionEvent motionevent)
            {
                SaveButtonSettings();
                dialog.dismiss();
            }
        });


        Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonCancel);        // if button is clicked, close the custom dialog
        dialogButton2.setOnClickListener(new OnClickListener() {
            //dialogButton.setOnTouchListener( new OnTouchListener()
            @Override
            public void onClick(View v)
            //public boolean onTouch(View view, MotionEvent motionevent)
            {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    @Override
    public void onDestroy() {
        // Удаляем ресивер вместе с удалением активити
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver1);

        Log.i("debug", "CustomActivity - onDestroy ");

        // Сохраняем настройки кнопок
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Button1Name", B1Name);
        editor.putString("Button1Value", B1Value);

        editor.putString("Button2Name", B2Name);
        editor.putString("Button2Value", B2Value);

        editor.putString("Button3Name", B3Name);
        editor.putString("Button3Value", B3Value);

        editor.putString("Button4Name", B4Name);
        editor.putString("Button4Value", B4Value);

        editor.putString("Button5Name", B5Name);
        editor.putString("Button5Value", B5Value);

        editor.putString("Button6Name", B6Name);
        editor.putString("Button6Value", B6Value);

        editor.putString("Button7Name", B7Name);
        editor.putString("Button7Value", B7Value);

        editor.putString("Button8Name", B8Name);
        editor.putString("Button8Value", B8Value);

        editor.putString("Button9Name", B9Name);
        editor.putString("Button9Value", B9Value);

        editor.putString("Button10Name", B10Name);
        editor.putString("Button10Value", B10Value);

        editor.putString("Button11Name", B11Name);
        editor.putString("Button11Value", B11Value);

        editor.putString("Button12Name", B12Name);
        editor.putString("Button12Value", B12Value);

        editor.putString("Button13Name", B13Name);
        editor.putString("Button13Value", B13Value);

        editor.putString("Button14Name", B14Name);
        editor.putString("Button14Value", B14Value);

        editor.putString("Button15Name", B15Name);
        editor.putString("Button15Value", B15Value);

        editor.putString("Button16Name", B16Name);
        editor.putString("Button16Value", B16Value);

        editor.putBoolean("FinalizeSumbols", PutFinalizeSumbols);


        editor.commit();

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //i getMenuInflater().inflate(R.menu.custom, menu);

        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "Config Mode").setCheckable(true);
        //menu.add(0, 2, 0, "Use Finalize Sumbols").setCheckable(true);
        return result;
        //i return true;
    }


    // Обработка нажатия пунктов системного меню
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                if (ConfigMode == false) {
                    ConfigMode = true;
                } else {
                    ConfigMode = false;
                }
                return true;
            }
/*		case 2:
        {
			if (PutFinalizeSumbols == false)
			{
				PutFinalizeSumbols = true;
			}
			else
			{
				PutFinalizeSumbols = false;
			}
			return true;
		}
*/
        }

        return false;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);

        if (ConfigMode)
            menu.findItem(1).setChecked(true);
        else
            menu.findItem(1).setChecked(false);

        //if(PutFinalizeSumbols)
        //     menu.findItem(2).setChecked(true);
        //else
        //     menu.findItem(2).setChecked(false);


        return result;
    }


}
