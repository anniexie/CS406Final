//Authors: Annie Xie and Justin Zhang
//CS406 Advanced Programming Final Project

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.IOException;

public class FishingPole
{
  private int x;
  private int y;
  private int speed;
  private BufferedImage hookImg;
  
  public FishingPole(int _x, int _y, int _speed) {
    x = _x;
    y = _y;
    speed = _speed;
    try {
      URL hookImgUrl = this.getClass().getResource("/images/hook.png");
      hookImg = ImageIO.read(hookImgUrl);
    } catch (IOException ex) {
      System.out.println("Error");
    }
  }
  public boolean collide(Fish f) {
    return ( (x-36)<f.getX() && (x+36)>f.getX()&&
            (y-48)<f.getY() && (y+48)>f.getY());
  }
  public void draw(Graphics2D g2d) { 
    g2d.drawImage(hookImg, x, y, null);
  }
  public void goUp() {
    y-=speed;
  }
  public void goDown() {
    y+=speed;
  }
}