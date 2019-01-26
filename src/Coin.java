import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Coin {
    public int label;
    private boolean realCoin;
    private int weight;
    public boolean selected = false;
    public boolean onLeft = false;
    public boolean onRight = false;
    public boolean hMark = false;
    public boolean lMark = false;
    public boolean nMark = false;
    Random rng = new Random();
    public int xPosition;
    public int yPosition;
    final int WIDTH = 50;
    final int HEIGHT = 50;

    public Coin(int xPos, int yPos){
        this.realCoin = true;
        xPosition = xPos;
        yPosition= yPos;
    }

    public void setLabel(int givenLabel){
        this.label = givenLabel;
    }

    public void adjustWeight(){
        if (realCoin){
            weight = 2;
        }
        else if (!realCoin){
            if (rng.nextBoolean()){
                weight = 3;
            }
            else{
                weight = 1;
            }
        }
    }

    public void setFake(){
        this.realCoin = false;
    }

    public boolean getValidity(){
        return realCoin;
    }

    public int getWeight(){
        return weight;
    }

    public void select(){
        /*
        if (!selected)
            Driver.textBox.append("\nCoin " + getLabel() + " selected.");
        else
            Driver.textBox.append("\nCoin " + getLabel() + " deselected.");
            */

        selected = !selected;
    }

    public boolean isSelected(){
        return selected;
    }

    public boolean onScale(){
        if (onLeft || onRight){
            return true;
        }
        else {
            return false;
        }
    }

    public void placeLeft(){
        onLeft = true;
        selected = false;
    }

    public void placeRight(){
        onRight = true;
        selected = false;
    }

    public void takeOffScale(){
        onLeft = false;
        onRight = false;
        selected = false;
    }

    public void takeOffLeft(){
        onLeft = false;
        selected = false;
    }

    public void takeOffRight(){
        onRight = false;
        selected = false;
    }

    public void removeLeft(){
        onLeft = false;
    }

    public void removeRight(){
        onRight = false;
    }

    public int getLabel(){
        return label;
    }

    public void setXpos(int xPos){
        xPosition = xPos;
    }

    public void setYpos(int yPos){
        yPosition = yPos;
    }

    public void paint(Graphics g){
        Color coinColor = new Color(218, 165, 32);
        Color selectedCoin = new Color(56, 93, 56, 127);
        Color leftCoin = new Color(255, 0, 0, 127);
        Color rightCoin = new Color(0, 0, 255, 127);

        g.setColor(coinColor);
        g.fillOval(xPosition, yPosition, WIDTH, HEIGHT);

        g.setColor(Color.black);
        g.drawString(Integer.toString(label),xPosition,yPosition);

        if (selected){
            g.setColor(selectedCoin);
            g.fillOval(xPosition, yPosition, WIDTH, HEIGHT);
        }
        else if (onLeft){
            g.setColor(leftCoin);
            g.fillOval(xPosition, yPosition, WIDTH, HEIGHT);
        }
        else if (onRight){
            g.setColor(rightCoin);
            g.fillOval(xPosition, yPosition, WIDTH, HEIGHT);
        }

    }
}
