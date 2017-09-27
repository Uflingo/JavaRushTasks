package com.javarush.task.task22.task2212;

/* 
Проверка номера телефона
*/
public class Solution {
    public static boolean checkTelNumber(String telNumber) {
        if ( telNumber == null || telNumber.length() == 0) return false;

        if (telNumber.charAt(0) == '+') { //1
            if (!telNumber.replaceAll("[^\\d]", "").matches("[\\d]{12}")){
                return false;
            }
        }
        else if (telNumber.matches("^[(|\\d].*")){ //2
            if (!telNumber.replaceAll("[^\\d]", "").matches("[\\d]{10}")){
                return false;
            }
        }
        else
            return false;

        if (telNumber.matches(".*--.*"))//3
            return false;
        else if (!telNumber.replaceAll("[^-]","").matches("[-]{0,2}"))
            return false;

        if (telNumber.contains("(") || telNumber.contains(")")){ //4,5
            if (!telNumber.matches("^[^-|)]*[(][\\d]{3}[)][^(|)]*$"))
                return false;
        }

        if (telNumber.matches(".*[a-zA-Zа-яА-Я]+.*")) //6
            return false;

        if (!telNumber.matches("^.*[\\d]$")) //7
            return false;

        return true;
    }

    public static void main(String[] args) {
//        System.out.println("+380501234567 - true. Got: "+ checkTelNumber("+380501234567"));
//        System.out.println("+38(050)1234567 - true. Got: "+checkTelNumber("+38(050)1234567"));
//        System.out.println("+38050123-45-67 - true. Got: "+checkTelNumber("+38050123-45-67"));
//        System.out.println("050123-4567 - true. Got: "+checkTelNumber("050123-4567"));
//        System.out.println("+38)050(1234567 - false. Got: "+checkTelNumber("+38)050(1234567"));
//        System.out.println("+38(050)1-23-45-6-7 - false. Got: "+checkTelNumber("+38(050)1-23-45-6-7"));
//        System.out.println("050ххх4567 - false. Got: "+checkTelNumber("050ххх4567"));
//        System.out.println("050123456 - false. Got: "+checkTelNumber("050123456"));
//        System.out.println("(0)501234567 - false. Got: "+checkTelNumber("(0)501234567"));


        System.out.println(checkTelNumber("+(38)050123-45-67"));
    }
}
