package com.company;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Main {
    public static void startFile(){
        String path = "C:\\Importiant Stuff\\Misc\\code\\calc.js";
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());

            } else {
                System.out.println("File already exists.");
                file.delete();
                System.out.println("File deleted, running creation again.");
                startFile();
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void writeFirst(FileWriter fw){
        try {
        String beginning = "var prompt = require('prompt-sync')();\n" +
                "console.log(\"Welcome to my CALCULTOR!\");\n" +
                "console.log(\"It only does numbers 0-50 and the basic 4 signs for now\");\n" +
                "\n" +
                "let num1 = prompt(\"Enter a number (0-50)\" , 0);\n" +
                "let sign = prompt(\"Enter a sign (+,-,*,/)\" , 0);\n" +
                "let num2 = prompt(\"Enter a number (0-50)\" , 0);\n\n";
            fw.write(beginning);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void write(int num1 , char sign, int num2 , FileWriter fw, ScriptEngine se){
        try {
            String equation = ""+num1+sign+num2;
            String current = String.format("if(num1 == \"%s\" && sign == '%s' && num2 == \"%s\"){\n" +
                    "    console.log('%s %s %s = %s');\n" +
                    "}\n" , num1 , sign, num2 , num1 ,sign , num2 , se.eval("eval("+equation+")"));
            fw.write(current);
        } catch (IOException | ScriptException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        int min = 0;
        int max = 50;
        char[] signs = {'+','-','*','/'};
        startFile();
        String path = "C:\\Importiant Stuff\\Misc\\code\\calc.js";
        FileWriter fw = null;

        ScriptEngine se = new ScriptEngineManager().getEngineByName("js");
        try {
            fw = new FileWriter(path);
            writeFirst(fw);
            for (char sign : signs) {
                for (int a = min; a <= max; a++) {
                    for (int b = min; b <= max; b++) {
                        write(a, sign, b, fw, se);
                    }
                }
            }
            fw.close();
            System.out.println("Finished");
            long end = System.nanoTime();
            long elapsedTime = end - start;
            double seconds = (double)elapsedTime / 1_000_000_000.0;
            long lines;
            try (Stream<String> stream = Files.lines(Paths.get(path))) {
                lines = stream.count();
            }
            System.out.println("Wrote " + lines + " lines in " +  seconds + " seconds");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
