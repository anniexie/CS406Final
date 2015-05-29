//Authors: Annie Xie and Justin Zhang
//CS406 Advanced Programming Final Project

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Fish implements Comparable<Fish> {
  private int x;
  private int y;
  private int width;
  private int speed;
  private BufferedImage fishImg;
  private int weight;
  private String color;
  private int points;
  
  public Fish(int _x, int _y, int _speed, String _color, int _weight, BufferedImage _fishImg, int _width) {
    x = _x;
    y = _y;
    speed = _speed;
    color = _color;
    weight = _weight;
    points = calculatePoints(_color, _weight);
    fishImg = _fishImg;
    width = _width;
  }
  @Override
  public int compareTo(Fish that) {
    if(points != (that.points))
      return that.points - points;
    else if(weight != (that.weight))
      return that.weight - weight;
    return color.compareTo(that.color);
  }
  public void Update() {
    x += speed;
  }
  private int calculatePoints(String _color, int _weight) {
    return speed;
  }
  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }
  public int getWeight() {
    return weight; 
  }
  public String getColor() {
    return color;
  }
  public int getPoints() {
    return points;
  }
  public int getWidth() {
    return width;
  }
  public void draw(Graphics2D g2d) {
    g2d.drawImage(fishImg, x, y, null);
  }
}