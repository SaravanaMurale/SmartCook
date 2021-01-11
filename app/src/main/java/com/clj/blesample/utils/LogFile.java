package com.clj.blesample.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFile {

    public static void addLogInFile(String text)
    {
        File logFile = new File("sdcard/scsklog.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            System.out.println("LOGFILEEXCEPTION"+e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
