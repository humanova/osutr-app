package com.humanova.osutr;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper {

    public static final String MESSAGE_FILE = "messageinfo.dat";
    public static final String LINE_FILE = "line.dat";

    public static void writeData(ArrayList<String> messages, int lineCount, Context context)
    {
        try{
            FileOutputStream message_fos = context.openFileOutput(MESSAGE_FILE, Context.MODE_PRIVATE);
            FileOutputStream line_fos = context.openFileOutput(LINE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream message_oos = new ObjectOutputStream(message_fos);
            ObjectOutputStream line_oos = new ObjectOutputStream(line_fos);
            message_oos.writeObject(messages);
            line_oos.writeObject(lineCount);
            message_oos.close();
            line_oos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static int readLineData(Context context)
    {
        int lineCount = 0;

        try{
            FileInputStream fis = context.openFileInput(LINE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            lineCount = (int) ois.readObject();
        }catch(FileNotFoundException e)
        {
            lineCount = 0;
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return lineCount;
    }

    public static ArrayList<String> readData(Context context)
    {
        ArrayList<String> messageList = null;

        try{
            FileInputStream fis = context.openFileInput(MESSAGE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            messageList = (ArrayList<String>) ois.readObject();
        }catch(FileNotFoundException e)
        {
            messageList = new ArrayList<>();
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return messageList;
    }
}
