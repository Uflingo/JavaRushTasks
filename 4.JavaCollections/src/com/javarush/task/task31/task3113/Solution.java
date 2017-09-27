package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/*
Что внутри папки?
*/
public class Solution {
    static int numOfFiles = 0, numOfDirs = -1,size = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String checkedPath = br.readLine();
        br.close();
        Path path = Paths.get(checkedPath);

        if (!Files.isDirectory(path)) {
            System.out.println(checkedPath + " - не папка");
            return;
        }

        Files.walkFileTree(path,new MyFileVisitor());

        System.out.println("Всего папок - " + numOfDirs);
        System.out.println("Всего файлов - " + numOfFiles);
        System.out.println("Общий размер - " + size);
    }

    static class MyFileVisitor extends SimpleFileVisitor<Path>{
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (Files.isDirectory(dir))
                numOfDirs++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!Files.isDirectory(file)){
                numOfFiles++;
                size+=Files.size(file);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
