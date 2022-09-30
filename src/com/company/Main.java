package com.company;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean flag = true;

        while (flag == true) {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            System.out.println("Type 0 to exit, 1 to continue.");
            int quit = myObj.nextInt();  // Read user input

            switch (quit){
                case 1:
                    System.out.println("Enter a country name");
                    String country = myObj.next();  // Read user input

                    System.out.println("Enter the count of days ");
                    int countOfDays = myObj.nextInt();  // Read user input

                    API.getConnect(country, countOfDays);
                    System.out.println("-------------------------------");
                    break;
                case 0:
                    System.out.println("Good Bye");
                    flag = false;
                    break;
                default:
                    System.out.println("Please Try Again");
            }
        }
    }
}
