package com.javarush.task.task31.task3111;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SearchFileVisitor extends SimpleFileVisitor<Path> {

    private String partOfName, partOfContent;
    private int minSize, maxSize;
    private List<Path> foundFiles = new ArrayList<>();

    public List<Path> getFoundFiles() {
        return foundFiles;
    }

    public void setPartOfName(String partOfName) {

        this.partOfName = partOfName;
    }

    public void setPartOfContent(String partOfContent) {
        this.partOfContent = partOfContent;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        boolean toAdd = true;
        if ((partOfName != null) && (toAdd)){
            if (!file.getFileName().toString().contains(partOfName))
                toAdd = false;
        }

        if ((partOfContent != null) && (toAdd)){
            String content = new String(Files.readAllBytes(file));
            if (!content.contains(partOfContent))
                toAdd = false;
        }

        if ((minSize != 0) && (toAdd)){
            if (attrs.size() < minSize)
                toAdd = false;
        }

        if ((maxSize != 0) && (toAdd)){
            if (attrs.size() > maxSize)
                toAdd = false;
        }

        if (toAdd)
            foundFiles.add(file);

        return super.visitFile(file, attrs);
    }
}
