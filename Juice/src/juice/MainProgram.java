package juice;
import java.io.*;
import java.util.*;

public class MainProgram {

    public static void main(String[] args) throws IOException {
      Juice juice = new Juice();
        try {
            juice = juice.juiceCreation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        juice.listMentioned();
        final Juice finalJuice = juice;
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    finalJuice.sortListOfComponents();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        myThread.start();
        juice.minimalNumberOfWashing();
    }
}
