package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Serializable, Cloneable {
    Entry<String> root;
    int sizeOfTree = 0;

    @Override
    public String get(int index) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw  new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw  new UnsupportedOperationException();
    }


    @Override
    public int size() {
        return sizeOfTree;
    }

    private boolean add(String s, Entry<String> curEntry, int curLevel) {
        if (curEntry == null)
            return false;
        if (curEntry.lineNumber == curLevel) {
            if (curEntry.isAvailableToAddChildren()) {
                Entry<String> newEntry = new Entry<>(s);
                if (curEntry.availableToAddLeftChildren)
                    curEntry.leftChild = newEntry;
                else if (curEntry.availableToAddRightChildren)
                    curEntry.rightChild = newEntry;
                newEntry.parent = curEntry;
                newEntry.lineNumber = curEntry.lineNumber+1;
                curEntry.checkChildren();
                return true;
            }
            else {
                return false;
            }
        }
        else
            return add(s,curEntry.leftChild,curLevel) || add(s,curEntry.rightChild,curLevel);
    }

    @Override
    public boolean add(String s) {
        try {
            if (root == null) {
                root = new Entry<>(s);
                root.lineNumber = 0;
                sizeOfTree++;
                return true;
            } else {
                int curLevel = 0;
                while (!add(s, root, curLevel)) {
                    curLevel++;
                }
                sizeOfTree++;
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private Entry<String> findEntry(String s, Entry<String> entry){
        if (entry == null)
            return null;
        else if (entry.elementName.equals(s))
            return entry;
        else{
            Entry<String> leftEntry = findEntry(s,entry.leftChild);
            if (leftEntry != null)
                return leftEntry;
            Entry<String> rightEntry = findEntry(s,entry.rightChild);
            if (rightEntry!=null)
                return rightEntry;
            return null;
        }
    }

    private int countElements(Entry<String> curElement){
        if(curElement == null)
            return 0;
        else{
            return countElements(curElement.leftChild) + countElements(curElement.rightChild) + 1;
        }
    }

    private void dropTree(Entry<String> curEntry){
        if(curEntry == null)
            return;
        dropTree(curEntry.leftChild);
        dropTree(curEntry.rightChild);
        curEntry = null;
    }

    @Override
    public boolean remove(Object o){
        String s = (String) o;
        Entry<String> entryToDelete = findEntry(s, root);
        if (entryToDelete != null){
            Entry<String> parent = entryToDelete.parent;
            int k = countElements(entryToDelete);
            sizeOfTree -= k;
            if (parent != null) {
                if (parent.leftChild.elementName.equals(s))
                    parent.leftChild = null;
                else if (parent.rightChild.elementName.equals(s))
                    parent.rightChild = null;
            }
            dropTree(entryToDelete);
            return true;
        }
        else
            return false;
    }

    public String getParent(String s){
        Entry<String> neededElement = findEntry(s, root);
        if (neededElement == null)
            return null;
        return neededElement.parent.elementName;
    }


    static class Entry<T> implements Serializable{

        String elementName;
        int lineNumber;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        void checkChildren() {
            if (leftChild != null) {
                availableToAddLeftChildren = false;
            }
            if (rightChild != null) {
                availableToAddRightChildren = false;
            }
        }
        boolean isAvailableToAddChildren(){
            return availableToAddRightChildren || availableToAddLeftChildren;
        }
    }

    public static void main(String[] args) {
        List<String> list = new CustomTree();
        for (int i = 0; i < 16; i++) {
            list.add(String.valueOf(i));
        }
        System.out.println( ((CustomTree) list).size());
        System.out.println(((CustomTree) list).getParent("15"));
        list.remove("3");
        System.out.println( ((CustomTree) list).size());
        ((CustomTree) list).add("16");
        System.out.println(((CustomTree) list).getParent("16"));
        System.out.println( ((CustomTree) list).size());
//        System.out.println("Expected 3, actual is " + ((CustomTree) list).getParent("8"));
//        list.remove("5");
//        System.out.println("Expected null, actual is " + ((CustomTree) list).getParent("11"));
    }
}
