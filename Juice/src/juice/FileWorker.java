package juice;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class FileWorker {

    public static ArrayList readFromFile(String fileName, ArrayList<String> list) throws IOException {
        FileInputStream inFile = new FileInputStream(fileName); //редактирование входных данных
        byte[] str = new byte[inFile.available()];
        inFile.read(str);
        String text = new String(str);
        FileWorker.write(fileName, text.replaceAll("[\\s&&[^\r?\n]]{2,}", " ") //запись в файл отредактированного текста
                .replaceAll("( ?(\r\n)){2,}", "\r\n").replaceAll("( ?\n){2,}", "\n")
                .replaceAll("\n ", "\n").trim());
        Scanner sc = new Scanner(new FileReader(fileName));
        while (sc.hasNextLine())
        {
            list.add(sc.nextLine());
        }
        return list;
    }

    public static LinkedHashSet writeInFile(String fileName, LinkedHashSet<String> set) throws FileNotFoundException {
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.print(set.toString());
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return set;
    }

    public static void write(String fileName, String text) {
        //Определяем файл
        File file = new File(fileName);

        try {
            //проверяем, что если файл не существует то создаем его
            if(!file.exists()){
                file.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст у файл
                out.print(text);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}