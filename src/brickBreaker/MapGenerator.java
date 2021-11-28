package brickBreaker;

import java.awt.*;

public class MapGenerator {
    public int map[][]; //to create the bricks
    public int brickWidth;
    public int brickHeight;
    public MapGenerator(int row, int col){ // constructor decides how many rows and columns are needed to generate a particular number of bricks
        map = new int[row][col];
        for(int i = 0; i < map.length; i++){
            for( int j = 0; j < map[0].length; j++){
                map[i][j] = 1; //1 represent that this brick have not intersected by the ball yet
            }
        }
        brickWidth = 540 /col;
        brickHeight = 150/ row;
    }
    public void draw(Graphics2D g){ // this method is to draw the bricks
        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] > 0){
                    g.setColor(new Color(204, 204, 255));
                    g.fillRect(j*brickWidth + 80, i*brickHeight + 50, brickWidth, brickHeight);

                    //draw borders around each break
                    g.setStroke(new BasicStroke(3));
                    g.setColor(new Color(204, 102, 255));
                    g.drawRect(j*brickWidth + 80, i*brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }
    //method for the intersections between the ball and the bricks
    public void setBrickValue(int value, int row, int col){
        map[row][col] = value;
    }
}
