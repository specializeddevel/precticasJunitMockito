package org.rburgos;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            readFile("nonexistent.txt");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage() + " " + e.getCause());
        }
    }

    public static void readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName); // Puede lanzar IOException
        // Lógica para leer el archivo
        fileReader.close();
    }
            //Class<?> classObj = Class.forName("boolean");
            //Class<?> classObj = char.class;
            //System.out.println(classObj.isPrimitive());


}