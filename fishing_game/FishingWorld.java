//Authors: Annie Xie and Justin Zhang
//CS406 Advanced Programming Final Project

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.net.URL;
import javax.swing.JTable;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Random;

public class FishingWorld extends JFrame implements Runnable {
  FishingPanel fp;
  
  public FishingWorld() {
    fp = new FishingPanel();
  }
  public void run() {
    setExtendedState(JFrame.MAXIMIZED_BOTH); 
    setVisible(true);
    setTitle("Fishing Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container c = getContentPane();
    c.add(fp);
  }
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new FishingWorld());
  }
}

class FishingPanel extends JPanel implements KeyListener, MouseListener {
  private FishingPole pole;
  private ArrayList<Fish> fish, caught;
  private BufferedImage fishImgBlue, fishImgOrange, fishImgGreen, backgroundImg;
  private int time = 30;
  private int score = 0;
  private Timer timer, timer2;
  private Random rand;
  private boolean started = false;
  private int width;
  private int height;
  private Dimension screenSize;
  
  public FishingPanel() {
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    width = (int) (screenSize.getWidth());
    height = (int) (screenSize.getHeight());
    addKeyListener(this);
    addMouseListener(this);
    setFocusable(true);
    pole = new FishingPole(width*2/3, height*2/3, 15);
    LoadContent();
    init();
    rand = new Random();
    timer = new Timer(10, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < fish.size(); i++) {
          Fish f = fish.get(i);
          f.Update();
          if(f.getX() > (width - f.getWidth()))
            fish.remove(f);
          if(pole.collide(f)) {
            fish.remove(f);
            caught.add(f);
            score += f.getPoints();
          }
        }
        repaint();
      }
    });
    timer2 = new Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e){
        int randomY = rand.nextInt((height - 71) / 70) * 70 + 70;
        int randomWeight = rand.nextInt(20) + 1;
        int randomSpeed = randomWeight / 4 + 2;
        int randomColor = rand.nextInt(3) + 1;
        if(randomColor == 1)
          fish.add(new Fish(0, randomY, randomSpeed, "blue", randomWeight, fishImgBlue, fishImgBlue.getWidth()));
        else if(randomColor == 2)
          fish.add(new Fish(0, randomY, randomSpeed, "orange", randomWeight, fishImgOrange, fishImgOrange.getWidth()));
        else if(randomColor == 3)
          fish.add(new Fish(0, randomY, randomSpeed, "green", randomWeight, fishImgGreen, fishImgGreen.getWidth()));
        time--;
        if (time == 0) {
          timer.stop();
          timer2.stop();
          Thread t1 = new Thread(new Leaderboard(width, height, score));
          t1.start();
          Thread t2 = new Thread(new EndGame(getFish(), width, height));
          t2.start();
        }
      }
    });
  }
  public ArrayList<Fish> getFish() {
    return caught;
  }
  private void LoadContent() {
    try {
      URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
      backgroundImg = ImageIO.read(backgroundImgUrl);
      URL fishImgBlueUrl = this.getClass().getResource("/images/fish_blue.png");
      fishImgBlue = ImageIO.read(fishImgBlueUrl);
      URL fishImgOrangeUrl = this.getClass().getResource("/images/fish_orange.png");
      fishImgOrange = ImageIO.read(fishImgOrangeUrl);
      URL fishImgGreenUrl = this.getClass().getResource("/images/fish_green.png");
      fishImgGreen = ImageIO.read(fishImgGreenUrl);
    } catch (IOException ex) {
      System.out.println("Error");
    }
  }
  private void init() {
    fish = new ArrayList<Fish>();
    caught = new ArrayList<Fish>();
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(backgroundImg, 0, 0, width, height, null);
    if (started) {
      g2d.setColor(Color.BLACK);
      g2d.drawString("SCORE: " + score, 10, 21);
      g2d.drawString("TIME: " + time, 100, 21);
    } else {
      g2d.setColor(Color.WHITE);
      g2d.drawString("Use up and down arrows to control hook.", (width - 250) / 2, height / 2 - 100);
      g2d.drawString("Hook must cover at least half of fish to catch it.", (width - 280) / 2, height / 2 - 70);
      g2d.drawString("Click anywhere to begin.", (width - 160) / 2, height / 2 - 40);
    }
    for(Fish f : fish)
      f.draw(g2d);
    pole.draw(g2d);
  }
  public void mouseClicked(MouseEvent e) {
    if (!started) {
      if(e.getButton() == MouseEvent.BUTTON1) {
        started = true;
        timer.start();
        timer2.start();
        repaint();
      }
    }
  }
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void keyPressed(KeyEvent e) {
    if (started) {
      if(e.getKeyCode() == KeyEvent.VK_UP)
        pole.goUp();
      if(e.getKeyCode() == KeyEvent.VK_DOWN)
        pole.goDown();
      repaint();
    }
  }
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
}

class EndGame extends JFrame implements Runnable {
  private boolean asc;
  private String type = "points";
  private ArrayList<Fish> al = new ArrayList<Fish>();
  private DefaultTableModel model = new DefaultTableModel();
  private int width;
  private int height;
  
  public EndGame(ArrayList<Fish> _al, int _width, int _height) {
    al = _al;
    Collections.sort(al);
    width = _width;
    height = _height;
  }
  public void run() {
    setTitle("Fish Caught");
    model.addColumn("Points");
    model.addColumn("Weight");
    model.addColumn("Color");
    makeTable();
    JTable table = new JTable(model);
    JMenu sort = new JMenu("Sort By");
    JMenuItem points = new JMenuItem("Points");
    JMenuItem weight = new JMenuItem("Weight");
    JMenuItem color = new JMenuItem("Color");
    
    points.addActionListener(e -> {
      type = "points";
      makeComparator();
    });
    weight.addActionListener(e -> {
      type = "weight";
      makeComparator();
    });
    color.addActionListener(e -> {
      type = "color";
      makeComparator();
    });
    sort.add(points);
    sort.add(weight);
    sort.add(color);
    
    JMenuItem order = new JMenuItem("Ascending");
    order.addActionListener(e -> {
      asc = !asc;
      String s = asc? "Ascending": "Descending";
      order.setText(s);
      makeComparator();
    });
    
    JMenuBar menu = new JMenuBar();
    menu.add(sort);
    menu.add(order);
    
    Container container = getContentPane();
    container.add(new JScrollPane(table), BorderLayout.CENTER);
    container.add(menu, BorderLayout.NORTH);
    
    setLocation((width - 900) / 2, (height - 400) / 2);
    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 400);
    setVisible(true);
  }
  private void makeComparator() {
    if (type.equals("color")){
      Comparator<Fish> compare = (n0, n1) -> {
        String c0 = n0.getColor();
        String c1 = n1.getColor();
        if (asc)
          return c0.compareToIgnoreCase(c1);
        else
          return c1.compareToIgnoreCase(c0);
      };
      Collections.sort(al, compare);
      makeTable();
    } else if (type.equals("weight")) {
      Comparator<Fish> compare = (n0, n1) -> {
        int w0 = n0.getWeight();
        int w1 = n1.getWeight();
        if (asc)
          return (w0 - w1);
        else
          return (w1 - w0);
      };
      Collections.sort(al, compare);
      makeTable();
    } else {
      Comparator<Fish> compare = (n0, n1) -> {
        int p0 = n0.getPoints();
        int p1 = n1.getPoints();
        if (asc)
          return (p0 - p1);
        else
          return (p1 - p0);
      };
      Collections.sort(al, compare);
      makeTable();
    }
  }
  private void makeTable() {
    model.setNumRows(0);
    for (Fish f: al) {
      String[] row = {"" + f.getPoints(), "" + f.getWeight(), f.getColor()};
      model.addRow(row);
    }
  }
}

class Leaderboard extends JFrame implements Runnable {
  private DefaultTableModel model = new DefaultTableModel();
  private int width;
  private int height;
  private int score;
  private ArrayList<User> al = new ArrayList<User>();
  private JTable table;
  
  public Leaderboard(int _width, int _height, int _score) {
    width = _width;
    height = _height;
    score = _score;
  }
  public void run() {
    String name = (String) JOptionPane.showInputDialog(this, "Score: " + score + "\n" + "Enter name:", 
                                                    "Save Score?", JOptionPane.PLAIN_MESSAGE, null, null, null);
    if (name != null)
      addToLeaderboard(score, name);
    getLeaderboard();
    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    model.addColumn("Score");
    model.addColumn("Name");
    for (User u: al) {
      String[] row = {"" + u.getScore(), u.getName()};
      model.addRow(row);
    }
    table = new JTable(model);
    Container container = getContentPane();
    container.add(new JScrollPane(table));
    setTitle("Leaderboard");
    setSize(400, 400);
    setLocation((width - 900) / 2 + 500, (height - 400) / 2);
    setVisible(true);
  }
  private void addToLeaderboard(int score, String name) {
    try {
      File file = new File("leaderboard.txt");
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
      bufferedWriter.write(score + "," + name);
      bufferedWriter.newLine();
      bufferedWriter.close();
    } catch(IOException ex) {
      System.out.println("Error writing to file.");
    }
  }
  private void getLeaderboard() {
    try {
      File file = new File("leaderboard.txt");
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      String line = "";
      while((line = bufferedReader.readLine()) != null) {
        String[] data = line.split(",");
        al.add(new User(Integer.parseInt(data[0]), data[1]));
      }
      Collections.sort(al);
      bufferedReader.close();            
    } catch(FileNotFoundException ex) {
      System.out.println("Unable to open file.");                
    } catch(IOException ex) {
      System.out.println("Error reading file.");                   
    }
  }
}