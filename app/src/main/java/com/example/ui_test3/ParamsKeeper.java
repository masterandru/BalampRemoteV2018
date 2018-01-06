package com.example.ui_test3;

import android.util.Log;

public class ParamsKeeper {

    // класс который производит управление параметрами полученными от устройств


    // Для хранения сформированных строк с параметрами.
    private String CmdBuffer;        // Командный буффер
    public float NumericParam;    // Переменная хранит полученное числовое знаечение (параметр в команде)
    public byte IsNumericParam;
    public int cmdCounter;        // Счетчик команд, команда считается по полученной тогда, когда пришел символ окончания строки
    // public String HL_params_proxy;

    // public String HR_params; //hercules_r
    // public String HR_params_proxy;

    private final int WORDS = 30; // Количество зарезервированных слов
//    private final int CMD_SIZE = 8; // Количество букв в ASCII команде
//    String CommandsTBL[/*WORDS*/]/*[CMD_SIZE]*/= new [30];

    private String CommandsTBL[] = new String[30];


    ParamsKeeper() {
//        CmdBuffer = new String();
        CmdBuffer = "";
        IsNumericParam = 0;
        cmdCounter = 0; // После создания экземпляра объекта команд нет :-)

        // Числовой эквивалент команды (в реальности используем индекс элемента в массиве)
        CommandsTBL[0] = "EMPTY";                // 00  - пустое значение - не удалять - нужная для того чтобы было адекватное числовое представление
        CommandsTBL[1] = "VOLUME";                // 01  WRITE VOLUME XXX ( отображение уровня громкости в dB)
        CommandsTBL[2] = "FLASH";                // 02  VOLUME WRITE FLASH  - сохранить в памяти FLASH текущее значение; VOLUME READ FLASH - прочитать, установить и отослать;
        CommandsTBL[3] = "UP";                    // 03
        CommandsTBL[4] = "DOWN";                // 04
        CommandsTBL[5] = "POWER";                // 05
        CommandsTBL[6] = "CLOSE";                // 06
        CommandsTBL[7] = "RING";                    // 07
        CommandsTBL[8] = "CARRIER";                // 08
        CommandsTBL[9] = "MASTER";                // 09
        CommandsTBL[10] = "CONNECT";                // 10  0x000A
        CommandsTBL[11] = "ERROR";                // 11  0x000B
        CommandsTBL[12] = "INPUT";                // 12  INPUT READ FLASH  - прочитать из FLASH, установить и отослать; INPUT WRITE FLASH - сохранить в памяти текущее значение;
        CommandsTBL[13] = "ROLLCALL";            // 13
        CommandsTBL[14] = "Welcome";                // 14  0x000E
        CommandsTBL[15] = "MUTE";                // 15
        CommandsTBL[16] = "READ";                // 16
        CommandsTBL[17] = "WRITE";                // 17   WRITE VOLUME XXX ( отображение уровня громкости в dB)
        CommandsTBL[18] = "R:";                    // 18
        CommandsTBL[19] = "WARNING";                // 19  R: Warning (всплывающее окно с кнопкой ОК и переходом в сервисное подменю)
        CommandsTBL[20] = "cccccc";                // 20  0x0014	Данные "на или от" cccccc device
        CommandsTBL[21] = "dddddc";                // 21  0x0015 	Данные "на или от" dddddc device
        CommandsTBL[22] = "eeeeec";                // 22  0x0016 	Данные "на или от" eeeeec device
        CommandsTBL[23] = "fffffc";                // 23  0x0017	Данные "на или от" fffffc device
        CommandsTBL[24] = "aabbcc";                // 24  0x0018	Данные "на или от" aabbcc device
        CommandsTBL[25] = "ddeecc";                // 25  0x0019	Данные "на или от" ddeecc device
        CommandsTBL[26] = "SETUP";                // 26
        CommandsTBL[27] = "REPORT";                // 27	Отчет с устройств на Андроид
        CommandsTBL[28] = "MESSAGE";                // 28	Отправка с Андроида на устройства
        CommandsTBL[29] = "abcdef";                // 29	Запрос данных со всех устройств, присутствующих сейчас в сети
    }

    void ReadData(byte[] l_Buffer, int l_Size) {
        StringBuilder s = new StringBuilder();
        s.append(CmdBuffer);

        //if(l_Size > 2)
        {
            for (int i = 0; i < /*l_Buffer.length*/ l_Size/*-1*/; i++) {
                s.append((char) l_Buffer[i]);

                // Сделать нормальный контроль индексов !!!!!

                //if( (i < l_Size) && (((char)l_Buffer[i]) =='\r') && (((char)l_Buffer[i+1]) =='\n'))
                //{
                //Log.i("DEBUG MSG", "Получен символ окончания команды");
                // Если получен символ окончания команды \r\n
                // то увеличиваем счетчик принятых команд на единицу
                // данный механизм вводится для избежания ошибок т.к. данные читаются асинхронно и покускам
                //	  cmdCounter++;
                //	  Log.i("DEBUG MSG", "Получено команд ="+  String.valueOf(cmdCounter));
                //  }


            }
        }
        CmdBuffer = s.toString();
    }


    // Функция возвращает самую первую строку из CmdBuffer (с начала строки и до перевода коретки \r\n или вместе или один из)
    // после возвращения подстроки мы ее удаляем
    public String GetCommand() {
        String loc_str = new String();

        while ((CmdBuffer.indexOf("\r") == 0) || (CmdBuffer.indexOf("\n") == 0)) {
            CmdBuffer = CmdBuffer.substring(1); // отсекаем первый символ если он \n или \r
        }

        //Log.d("debug", "Функция GetCommand() - Получаем очередную строку ->"+ CmdBuffer);

        if (!CmdBuffer.isEmpty() /*&& (cmdCounter > 0)*/) // проверяем и пустоту строки и счетчик команд т.к. строка может содержать часть команды
        {
            int loc_index = (CmdBuffer.indexOf("\r") < CmdBuffer.indexOf("\n") ? CmdBuffer.indexOf("\r") : CmdBuffer.indexOf("\n"));
            if (loc_index > 0) {
                loc_str = CmdBuffer.substring(0, loc_index);
                CmdBuffer = CmdBuffer.substring(loc_index, CmdBuffer.length());
                cmdCounter--; // Уменьшаем счетчик команд
            }
              /*  // пока временно закомментирую
               else //=-1 это означает что не найдено но строка не пустя, обрабатывается самоя последняя часть строки.
			  {
				  loc_str = CmdBuffer;
				  CmdBuffer="";
			  }
			  */
            else loc_str = "";
        } else
            loc_str = "";

        Log.d("debug", "Функция GetCommand() - Получаем очередную строку ->(" + loc_str + ")");

        return loc_str;
    }


    //  Функция которая возвращает числовой код token(а), (одного слова, в команде их может быть несколько)
    public int GetResWordNum(String locTokenName) {
        int li = 0;
        int result = 0x00;
        String locCmdStr;

        for (li = 0; li < WORDS; li++) {
            locCmdStr = CommandsTBL[li];

            if (locCmdStr.equalsIgnoreCase(locTokenName)) {
                result = li;
                break;
            }
        }

        return result;
    }


    // Функция возвращает числовой эквивалент команды, плюс записывает число если оно имеется.
    // находится в процессе разработки
    public long GetCommandNum(String inCmdStr) {
        long result = 0, locCmdNum;
        String locCmdToken;
        byte locFirstCicleFlag = 1;    // Флаг вводится для того чтобы впервой итерации не умножать число на 100, тоесть не сдвигать вправо номер команды
        float locNumericParam = 0;    // Переменная хранит числовое значение полученное в команде, конечно если оно есть
        byte locIsNumericParam = 0;    // Флаг в котором отмечаем присутствует ли число в команде

        // ??? проверить нужно ли именно в этом месте сбрасывать флаг получения числового параметра и самого параметра ???
        NumericParam = 0;
        IsNumericParam = 0;

        inCmdStr = inCmdStr.trim(); //на всякий случай удаляем пробелы в начале и в конце


        while (!inCmdStr.isEmpty()) // если строка не пуста обрабатываем
        {
            Log.i("DEBUG MSG", "Обработка строки -> |" + inCmdStr + "|");

            inCmdStr = inCmdStr.trim(); //удаляем пробелы в начале и в конце

            int loc_index = inCmdStr.indexOf(" ");

            if (loc_index > 0) {
                locCmdToken = inCmdStr.substring(0, loc_index);
                inCmdStr = inCmdStr.substring(loc_index, inCmdStr.length()); // отсекаем перый токен из команды
            } else // команда состоит из одного слова или в обрабатываемой строке осталось одно слово (loc_index == -1)
            {
                // Удаляем спец символы \n или \r
                inCmdStr = inCmdStr.replace("\r", "");
                inCmdStr = inCmdStr.replace("\n", "");

                //loc_index =  0; //inCmdStr.length(); 				// устанавливаем индекс на первый символ в строке для корректного отсеченя
                locCmdToken = inCmdStr.substring(0, inCmdStr.length());
                inCmdStr = "";
            }


            // получаем слово
            locCmdNum = GetResWordNum(locCmdToken);            // Определяем числовой эквивалент токена по таблице

            if (locCmdNum != 0) {
                if (locFirstCicleFlag == 1) {
                    result = locCmdNum;
                    locFirstCicleFlag = 0;
                } else {
                    result *= 100;
                    result += locCmdNum;
                }

            } else // если слово неопознано проверяем не число ли это
            {
                try {
                    locIsNumericParam = 1; // Считаем что если мы попали в блок try то токен в таблице отсутствует получается это число или незнакомое слово
                    locNumericParam = Float.parseFloat(locCmdToken);
                    NumericParam = locNumericParam;
                } catch (NumberFormatException e) {
                    //Will Throw exception!
                    //do something! anything to handle the exception.
                    locIsNumericParam = 0; // Если преобразование в число не удалось сбрасываем флаг
                }

                IsNumericParam = locIsNumericParam; // Говорим что число присутствует в команде
            }
        }
        return result;
    }

}