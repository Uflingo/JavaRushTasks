package com.javarush.task.task35.task3513;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by ASentsov on 23.06.2017.
 */
public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450, 500);
        game.setResizable(false);

        game.add(controller.getView());


        game.setLocationRelativeTo(null);
        game.setVisible(true);

        try {
            Robot robot = new Robot();
            for (int i = 0; i < 1000; i++) {
                if (controller.getView().isGameLost)
                    robot.keyPress(KeyEvent.VK_ESCAPE);;
                robot.keyPress(KeyEvent.VK_A);
                Thread.sleep(100);
            }
        }
        catch (Exception e){}

    }
}