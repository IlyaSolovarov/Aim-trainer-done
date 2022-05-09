import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DrawingComponent extends JPanel {
    static int x = 200;
    static int cur_circles = 0;
    static int max_circles = 1;
    static int total_max_circles = 5;
    static int beg_radius = 1;
    static int end_radius = 100;
    static int step = 1;
    static double sin45 = 0.5 * Math.sqrt(2);
    static int xsize = 1000;
    static int ysize = 800;
    static int score = 0;
    static List<Circle> circles = new ArrayList<Circle>();
    static volatile List<Circle> toRemove = new ArrayList<Circle>();
    static int total_circ_kol = 0;
    static int total_clicks = 0;

    public DrawingComponent() {
        super();

        Thread counter = new Thread(() -> {
            while (max_circles < total_max_circles) {
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                max_circles++;
            }
        });

        Thread game = new Thread(() -> {
            while (true) {
                spawnCircles();
                circlesMove();
                DrawingComponent.this.repaint();

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        game.start();
        counter.start();

    }

    void spawnCircles() {
        if (cur_circles < max_circles) {
            Circle circle = new Circle(getRandom(150, xsize - 150), getRandom(150, ysize - 150), beg_radius);
            circles.add(circle);
            circle.index = ++total_circ_kol;
            cur_circles++;
        }
    }

    void circlesMove() {

        for (Circle circle : circles) {

            circle.r += step;

            if (circle.r >= end_radius) {
                toRemove.add(circle);
            }
        }


        for (Circle circleToRemove : toRemove) {
            circles.remove(circleToRemove);
            cur_circles--;
        }

        toRemove.clear();
    }

    private int getRandom(int x1, int x2) {
        double f = Math.random()/Math.nextDown(1.0);
        int x = (int) Math.round(x1*(1.0 - f) + x2*f);
        return x;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("score = " + score + "   total clicks = " + total_clicks, 50,50);
        for (Circle circle : circles) {
                g.fillRoundRect((int)Math.round(circle.x - circle.r * sin45), (int)Math.round(circle.y - circle.r * sin45), (int)Math.round(circle.r / sin45), (int)Math.round(circle.r / sin45), 200, 200);
        }
    }

    static void mouseClicked(MouseEvent e) {
        total_clicks++;

        for (Circle circle : circles)
            if (circle.checkShot(e.getX(), e.getY())) {
                score++;
                toRemove.add(circle);
                break;
            }
    }
}

public class MainForm extends JFrame {

    public MainForm () {
        super("AimTrainer");
        JPanel jcp = new JPanel(new BorderLayout());
        setContentPane(jcp);
        JPanel drawer = new DrawingComponent();
        drawer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                DrawingComponent.mouseClicked(e);
            }
        });
        jcp.add(drawer, BorderLayout.CENTER);
        jcp.setBackground(Color.gray);
        setSize(DrawingComponent.xsize, DrawingComponent.ysize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void main(String[] args)   {
        MainForm f = new MainForm();
        f.setVisible(true);
    }

}



