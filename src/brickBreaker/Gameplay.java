package brickBreaker;

//import org.w3c.dom.css.Rect;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*; // for Graphics and Color
import java.awt.event.*;

public class Gameplay extends JPanel implements KeyListener, ActionListener { //KeyListener to move the slider //ActionListener to move the ball
    private boolean play = false; //this will prevent the game to start by itself
    private int score = 0;

    private int totalBricks = 21; // number of boxes 3 * 7

    private final Timer timer; // setting the time of the ball to establish how fast it will move
    private final int delay = 0; // the speed that will be given to the timer

    private int playerX = 310; // the starting position of our slider

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1; //direction of the ball
    private int ballYdir = -2; //direction of the ball

    private MapGenerator map; // so we can use MapGenerator class

    public Gameplay(){ //constructor
        map = new MapGenerator(3,7);
        addKeyListener(this); //in order to work with key listener
        setFocusable(true); //enables the component to gain focus once the Frame is displayed "متل لما تكوني قاسمة الشاشة بالنص الفارة بيكون تركيزها على القسم اللي شغليتيها فيه أخر مرة"
        setFocusTraversalKeysEnabled(false); //decides whether focus traversal keys (TAB key, SHIFT+TAB, etc.) are allowed to be used when the current Component has focus.
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g){ // g object will be used to draw the ball or the slider...
        //background
        g.setColor(new Color(204, 102, 255));
        g.fillRect(1,1,692, 592);// x and y for its position

        //Drawing map for bricks
        map.draw((Graphics2D) g); //pass g object after type casting to 'Graphics2D'

        //borders
        g.setColor(new Color(102, 0, 102));
        //create three rectangular for the borders
        g.fillRect(0,0, 3, 592); //Left
        g.fillRect(0,0, 692, 3);// "Top" draws a rectangle that is filled according to the current fillStyle
        g.fillRect(691,0, 3, 592);//Right
        /* we do not add a border at the bottom because we want the ball to fall down to end up the game*/

        //Scores
        g.setColor(new Color(242, 242, 242));
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);

        //The paddle
        g.setColor(new Color(102, 0, 102));
        g.fillRect(playerX, 550, 100, 8); //playerX is the starting position of our slider

        // the ball
        g.setColor(new Color(242, 242, 242));
        g.fillOval(ballposX, ballposY, 20, 20);

        //YOU WON! if the number of bricks are done
        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            //the style of the wining phrase
            g.setColor(new Color(102, 0, 102));
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WON !, SCORE:"+score, 180, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press 'Enter' to restart", 230, 350);
        }

        //GAME OVER "if the ball went down"
        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            //the style of the losing phrase
            g.setColor(new Color(102, 0, 102));
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER, SCORE:" + score, 180, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press 'Enter' to restart", 230, 350);
        }

        g.dispose(); //causes the JFrame window to be destroyed and cleaned up by the operating system.

    }

    //need these methods to make Keylistener and Actionlistener work.
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        //Method for moving the ball
        if(play){
            //for detecting the intersection between two objects "the paddle & the ball"
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100,8))){
                ballYdir = -ballYdir;
            }
            A: for(int i = 0; i < map.map.length; i++){ //first map is the object, the second map is array in the MapGenerator class
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){ //if the brick is there detect the intersection
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        //create the rectangle around the brick
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20);
                        Rectangle brickRect = rect;

                        //Changing the value of the brick from 1 to 0 if the ball intersect it
                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--; // decrease the number of bricks by 1
                            score += 5; // increase the user score by 5

                            //for the left & right intersections
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir = -ballYdir;
                            }
                            break A; //here we used break label because we need to take the compiler out of the outer loop, in case of the bricks are all end or the ball kept moving ...
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            //For the left border
            if(ballposX < 0){
                ballXdir = -ballXdir;
            }
            //For the top border
            if(ballposY < 0){
                ballYdir = -ballYdir;
            }
            //For the right border
            if(ballposX > 670){
                ballXdir = -ballXdir;
            }
        }
        repaint(); //from JComponent.java class
        //it will recall paint method and draw everything again because when we are incrementing or decrementing playerX we need all the shapes to be drawn again
    }

    @Override
    public void keyPressed(KeyEvent e) {//inside this method we can do the detecting of the arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { // detecting the right key
            if (playerX >= 600) { //we are checking if the ball getting outside the border, if it is we keep it inside the border of our panel
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){ //we are checking if the ball getting outside the border, if it is we keep it inside the border of our panel
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        //If the game end and the user pressed enter the default values of the game should be restored
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);

                repaint();
            }
        }
    }

    public void moveRight(){
        play = true;
        playerX += 20; //if the user pressed right key the ball will move 20px to the right side
    }

    public void moveLeft(){
        play = true;
        playerX -= 20; //if the user pressed right key the ball will move 20px to the left side
    }

    //unused but necessary
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
