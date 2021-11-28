package brickBreaker;

import javax.swing.*;

public class Main {

   static ImageIcon logo = new ImageIcon(".//img//brickbreakericon.jpg");

    public static void main(String[] args) {
        // To create an empty frame:
        JFrame obj = new JFrame();
        Gameplay gameplay = new Gameplay(); // because i need the game to be shown in this frame
        obj.setBounds(10, 10, 708, 600); //set the size of the frame
        obj.setTitle("Brick Breaker Game"); //set the title to the frame
        obj.setResizable(false); //the button next to exit button
        obj.setVisible(true); //the frame visibility
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //the way of exiting the game the close button
        obj.add(gameplay); //add the object 'gameplay' to the frame
        obj.setIconImage(logo.getImage()); //icon of the game
        //End of creating empty frame
    }
}
