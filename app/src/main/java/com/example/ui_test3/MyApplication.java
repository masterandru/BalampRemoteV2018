package com.example.ui_test3;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;


public class MyApplication extends Application {

    private static MyApplication singleton;

    //
    public BluetoothAdapter mBluetoothAdapter;
    public Set<BluetoothDevice> pairedDevices;
    //    public List<String> listItems;
    public String strAccumulator;    // хранит все переданное и полученное по BT
    public float VolumeValue;        // Переменная хранит значение звука

    public ParamsKeeper pk = new ParamsKeeper(); // интерфейс для доступа и управления полученными по BT данными

    /*private*/ public ConnectedThread hercules_l_ConnectedThread; // amp left   //HERCULES_12
    static final int HL_ID = 1;

//    static int VolumeUPCounter = 0;
//    static int VolumeDOWNCounter = 0;

    public int IsConnect = 0;


    // Возвращает экземпляр данного класса
    public static MyApplication getInstance() {
        //Log.i("DEBUG", "MyApplication getInstance()");
        return singleton;
    }


    // Интерфейс для доступа к сокету из разных активити
    public void WriteToSocket(byte[] loc_input) {
        if (hercules_l_ConnectedThread != null) {
            hercules_l_ConnectedThread.write(loc_input);
            //Log.d("DEBUG MSG", "RUN - WriteToSocket" + loc_input );

            // Добавляем в переменную strAccumulator все что Андроид-устройство отправляет по БТ
            String s = new String(loc_input);
            strAccumulator += s;
        } else Log.d("DEBUG MSG", "hercules_L_ConnectedThread NOT EXIST");
    }

    // Интерфейс для закрытия коннекта
    public void Disconnect() {
        if (hercules_l_ConnectedThread != null) {
            hercules_l_ConnectedThread.cancel();
            //IsConnect = 0;
        }
        IsConnect = 0;

        // Сохраняем strAccumulator в Log
        Log.d("DEBUG", "Сохраняем strAccumulator в Log-файл");
        appendLog(strAccumulator);

        // Очищаем переменную strAccumulator
        strAccumulator = "";


    }

    @Override
    public final void onCreate() {
        super.onCreate();
        //Log.i("DEBUG", "MyApplication onCreate() ");
        singleton = this;

        VolumeValue = 20;

        //strAccumulator = new String();
        strAccumulator = "";

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


		/*
        if (mBluetoothAdapter != null)
    	{
    		// Device support Bluetooth
    		if (!mBluetoothAdapter.isEnabled())
    		{
    			//Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        	    //startActivityForResult(enableBtIntent, 1);
    			showToast("Включите BlueTooth!");
        	}// if (!mBluetoothAdapter.isEnabled())

    		listItems = new ArrayList<String>();

    		pairedDevices = mBluetoothAdapter.getBondedDevices();
    	    // If there are paired devices
    	    if (pairedDevices.size() > 0)
    	    {
    	    	// Loop through paired devices
    	    	for (BluetoothDevice device : pairedDevices)
    	    	{
    	    		// Add the name and address to an array adapter to show in a ListView
    	    		//mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
    	    		Log.d("debug", "Ustroistvo "+" N:"+ device.getName()+" A:"+device.getAddress().toString() );

    	    		listItems.add(device.getName());

    	    	}// for
    	    }// if
    	    else
    	    {
    	    	Log.i("debug", " DDDDDDDDDDDD  pairedDevices = 0" );
    	    	showToast("BT-устройства отсутствуют. Соединение невозможно!");
    	    }

    	}// if (mBluetoothAdapter != null)
		 */

    }


//	@Override
//	public final void onDestroy() {
//		super.onDestroy();
//	}

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public int ConnectToBTdevice(int loc_btdevindex) {
        int result = 0;

        if ((pairedDevices.size() > 0)) {
            int loccounter = 0;
            for (BluetoothDevice device : pairedDevices) {
                if (loccounter == loc_btdevindex) {
                    // Connecting
                    final BluetoothDevice balamp0dev = device;
                    connect(balamp0dev);
                    Log.i("debug", " IS CONNECTED to:" + device.getName());
                    result = 1;
                    IsConnect = 1; // Сохраняем состояние соединения

                }
                loccounter++;
            }
        }
        return result; // 0 - нет коннекта, 1 - есть
    }

    public int ConnectToBTdeviceByMAC(String loc_btdevmac) {
        int result = 0;

        final BluetoothDevice balamp0dev = mBluetoothAdapter.getRemoteDevice(loc_btdevmac);

        if (balamp0dev != null) {
            // Connecting
            connect(balamp0dev);
            Log.i("debug", " IS CONNECTED to:" + balamp0dev.getName());
            result = 1;
            IsConnect = 1; // Сохраняем состояние соединения
        }
        return result; // 0 - нет коннекта, 1 - есть
    }


    public synchronized void connect(BluetoothDevice device) {
        // Start the thread to connect with the given device
        new ConnectThread(device).start();
    }


    public void connected(BluetoothSocket socket, BluetoothDevice device) {
        //if(device.getName().equalsIgnoreCase("DAC"))
        {
            hercules_l_ConnectedThread = new ConnectedThread(socket, HL_ID);
            hercules_l_ConnectedThread.start();
            Log.i("debug", "FUNCTION connected RUN");

            /*
            // Отправляем устройству команду MASTER
            Log.i("debug", " Отправляем устройству команду MASTER" );
            if (hercules_l_ConnectedThread != null)
            {
                hercules_l_ConnectedThread.write(new byte[] {'M','A','S','T','E','R','\r','\n' });
            }
            else Log.d("debug", "Connected Thread NOT EXIST");
            */
        }

        //if(device.getName().equalsIgnoreCase("BALAMP 0") )
        //{
        //hercules_r_ConnectedThread = new ConnectedThread(socket,HR_ID);
        //hercules_r_ConnectedThread.start();
        //}


    }

    // / Функция сохраняет переменную String в файл
    public void appendLog(String text) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDT = sdf.format(new Date(System.currentTimeMillis()));

        // Поверяем есть ли папка для лог-файлов, если нет создаем
        File rootPath = new File("sdcard/BalAmpRemote");
        if (!rootPath.exists()) {
            Toast.makeText(this,
                    "FOLDER NOT EXIST - Try to create " + rootPath,
                    Toast.LENGTH_LONG).show();
            rootPath.mkdirs();
        }

        if (rootPath.exists() && rootPath.isDirectory()) {

            File logFile = new File("sdcard/BalAmpRemote/" + currentDT + ".txt");

            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                // BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
                        true));
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "FILE - Not Exist", Toast.LENGTH_LONG).show();
        }
    }



		    /* ------------------------------------------------------------------
             * ------------------------------------------------------------------
		     * ------------------------------------------------------------------
		     *  ------------------------------------------------------------------
		     */


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;


        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;

            BluetoothSocket tmp = null;
            try {
                // Get a BluetoothSocket to connect with the given
                // BluetoothDevice
                final Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                tmp = (BluetoothSocket) m.invoke(mmDevice, Integer.valueOf(1));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }


        public void run() {
            // Cancel discovery because it will slow down the connection
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            connected(mmSocket, mmDevice);
        }

        // Will cancel an in-progress connection, and close the socket
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    ////*********************************************

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private int DeviceID; // Механизм вводится для распознования отправителя


        public ConnectedThread(BluetoothSocket socket, int l_DeviceID) {
            mmSocket = socket;
            DeviceID = l_DeviceID;

            // Get the input and output streams, using temp objects because
            // member streams are final
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            final byte[] buffer = new byte[1024]; // buffer store for the stream
            int bytes; // bytes returned from read()
            final String Command = new String(buffer, 0, 1024);
            //final ParamsKeeper pk= new ParamsKeeper();


            //final byte[] Command = new byte[100]; // store for the 1 message
            //int CommandPos = 0; // bytes returned from read()
            //final int i = 0;

            Log.i("debug", "- run -");

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);


                    final String txt = new String(buffer, 0, bytes);


                    if (bytes > 0) {
                        Log.d("debug", " Получена строка или часть строки ->" + txt);

                        //Context.sendBroadcast();


                        // Отправляем полученное в BroadcastReceiver
                        Intent serviceStartedIntent = new Intent("btinput");
                        serviceStartedIntent.putExtra("message", txt);
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(serviceStartedIntent);

                        strAccumulator += txt; // Аккумулируем в переменной все полученное


							/*
                             Intent intent = new Intent();
							 intent.setAction("com.exmaple.AudioPlay");
							 intent.putExtra("serverStatus","Connected");
							 Context serv = getApplicationContext();
							 serv.sendBroadcast(intent);
							*/

							/*
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									//byte[] Command = new byte[100]; 	// store for the 1 message
									//int CommandPos = 0; 				// bytes returned from read()
									String loc_Command;


									Log.d("debug", " Получена строка или часть строки ->"+ txt);
									//showToast(txt);

									pk.ReadData(buffer, txt.length());
									//pk.Concat(txt, bytes);
									Log.d("debug", " Содержимое переменной CmdBuffer ->"+ pk.CmdBuffer);
									Log.d("debug", "---------------------------------------------------");
									//showToast(pk.HL_params);


									// Получаем очередную команду
					    	 		loc_Command=pk.GetCommand();
					    	 		if(loc_Command != "")
					    	 		{
					    	 			Log.i("DEBUG MSG", "GET NEXT COMMAND -> |"+ loc_Command +"|");
					    	 			Log.i("DEBUG MSG", "GET GetResWordNum -> |"+ pk.GetCommandNum(loc_Command)+"|");

					    	 			// Получена команда READ VOLUME
					    	 			if((pk.GetCommandNum(loc_Command) == 1601) && (pk.IsNumericParam == 1))
					    	 			{
					    	 				// Convert VolumeValue to dB //res = (Volume)*-0.5;
					    	 				VolumeValueRef.setText(String.valueOf(pk.NumericParam*-0.5));
					    	 			}
					    	 		}



									//
									// Обработка полученных по BT данных
									//Params.Add(txt, DeviceID);

									//if (Params.HL_params.length()>0)
									//{
									//	showToast(Params.HL_params);
									//	Params.HL_params="";
									//}

									//if (Params.HR_params.length()>0)
									//{
									//	showToast(Params.HR_params);
									//	Params.HR_params="";
									//}

								}
							});
						*/
                    } // if (bytes > 0)
//		                // Send the obtained bytes to the UI Activity
//		                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//		                        .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main Activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main Activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


}







