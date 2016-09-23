package swaghack;

import GeneticAlgorithm.Bound;
import NeuralNetwork.Matrix;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.Population;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class BestPredictorNA extends JPanel {

    public static final Dimension SCREEN = new Dimension(1920, 1080);
    public static final Dimension PLOT = new Dimension(1200, 800);

    private static final int xSource = (SCREEN.width - PLOT.width) / 2;
    private static final int ySource = PLOT.height;

    public static void main(String[] args) {
        demo2();
    }

    private static void demo2() {
        List<Data> data = readData("testData.csv");
        int popSize = 50;
        int totalGenerations = 5000;
        int[] L = {1, 5, 5, 5, 1};
        double xMin = 0, xMax = 530;
        double yMin = 15500, yMax = 18500;
        double xDot = 265;
        double yDot = 3000;
        double xOffset = 256;
        double yOffset = 15500;
        double b = 0;
        double c = 0.5;
        Population pop = new Population(popSize);
        pop.generate(L, new Bound(-2, 2));
        JFrame frame = new JFrame("HackTheMarket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(SCREEN);
        frame.setResizable(false);
        BestPredictorNA genius = new BestPredictorNA(xDot, yDot, xMin, xMax, yMin, yMax);
        genius.setPreferredSize(SCREEN);
        genius.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        frame.setContentPane(genius);
        frame.setLayout(null);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        genius.setData(data);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                genius.repaint();
            }
        }, 100, 100);
        for (int i = 0; i < totalGenerations; i++) {
            for (int j = 0; j < popSize; j++) {
                NeuralNetwork nn = new NeuralNetwork(L, 265, 3000, b, c);
                nn.loadIndividual(pop.get(j));
                double cost = 0;
                for (Data datum : data) {
                    double[][] arrX = {{datum.x - 265}};
                    Matrix X = new Matrix(arrX);
                    Matrix Y = nn.forward(X);
                    double[][] arrY = Y.toArray();
                    double yHat = arrY[0][0] + 15500;
                    double err = datum.y - yHat;
                    double sqErr = err * err;
                    cost += sqErr;
                }
                pop.get(j).setFitness(-cost);
            }
            pop.order();
            System.out.println(String.format("%d, %.5f", i, pop.get(0).fitness));
            NeuralNetwork draw = new NeuralNetwork(L, xDot, yDot, b, c);
            draw.loadIndividual(pop.get(0));
            genius.setNN(draw);
            if (i == totalGenerations - 2) {
                break;
            }
            Population newPop = pop.evolve(popSize / 50, popSize / 20, 1.0, 0.4, 0.2, 2);
            pop = newPop;
        }
        NeuralNetwork elite = new NeuralNetwork(L, xDot, yDot, b, c);
        elite.loadIndividual(pop.get(0));
        for (int i = 0; i <= 6000; i++) {
            System.out.println(elite.forward(new Matrix(
                    new double[][]{{i - xDot}})).toArray()[0][0] + yDot);
        }
        int NNi = 0;
        for (;;) {
            try {
                File file = new File(String.format("savedNN%d.nn", NNi));
                if (file.exists()) {
                    throw new IOException();
                }
                FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(elite);
                oos.close();
                break;
            } catch (IOException ex) {
                NNi++;
            }
        }
        /*File file = new File("savedNN7.nn");
         try {
         FileInputStream fin = new FileInputStream(file);
         ObjectInputStream ois = new ObjectInputStream(fin);
         NeuralNetwork elite = (NeuralNetwork) ois.readObject();
         for (double p = 0; p <= 530; p += 1) {
         double l = p - 530 / 2;
         System.out.println(String.format("%.2f",elite.forward(new Matrix(new double[][]{{l}})).toArray()[0][0] + 15500));
         }
         } catch (Exception ex) {
         }*/
    }

    private static void demo1() {
        List<Data> data = readData("dollarData.csv");
        int popSize = 100;
        int totalGenerations = 70;
        int[] L = {1, 6, 6, 6, 1};
        double xMin = 0, xMax = 1600;
        double yMin = 0, yMax = 2;
        double xDot = (xMax - xMin) / 2;
        double yDot = (yMax - yMin) / 2;
        double xOffset = (xMax + xMin) / 2;
        double yOffset = (yMax + yMin) / 2;
        double b = 0;
        double c = 0.5;
        Population pop = new Population(popSize);
        pop.generate(L, new Bound(-2, 2));
        JFrame frame = new JFrame("HackTheMarket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(SCREEN);
        frame.setResizable(false);
        BestPredictorNA genius = new BestPredictorNA(xDot, yDot, xMin, xMax, yMin, yMax);
        genius.setPreferredSize(SCREEN);
        genius.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        frame.setContentPane(genius);
        frame.setLayout(null);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        genius.setData(data);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                genius.repaint();
            }
        }, 50, 50);
        for (int i = 0; i < totalGenerations; i++) {
            for (int j = 0; j < popSize; j++) {
                NeuralNetwork nn = new NeuralNetwork(L, xDot, yDot, b, c);
                nn.loadIndividual(pop.get(j));
                double cost = 0;
                for (Data datum : data) {
                    double[][] arrX = {{datum.x - xOffset}};
                    Matrix X = new Matrix(arrX);
                    Matrix Y = nn.forward(X);
                    double[][] arrY = Y.toArray();
                    double yHat = arrY[0][0] + yOffset;
                    double err = datum.y - yHat;
                    double sqErr = Math.abs(err);
                    cost += sqErr;
                }
                pop.get(j).setFitness(-cost);
            }
            pop.order();
            System.out.println(String.format("%d, %.5f", i, pop.get(0).fitness));
            NeuralNetwork draw = new NeuralNetwork(L, xDot, yDot, b, c);
            draw.loadIndividual(pop.get(0));
            genius.setNN(draw);
            if (i == totalGenerations - 2) {
                break;
            }
            Population newPop = pop.evolve(popSize / 50, popSize / 20, 1.0, 0.3, 0.3, 3);
            pop = newPop;
        }
        NeuralNetwork elite = new NeuralNetwork(L, xDot, yDot, b, c);
        elite.loadIndividual(pop.get(0));
        for (int i = 1588; i >= 0; i--) {
            System.out.println(elite.forward(new Matrix(
                    new double[][]{{i - xDot}})).toArray()[0][0] + yDot);
        }
        for (int i = 1588; i <= 2000; i++) {
            System.out.println(elite.forward(new Matrix(
                    new double[][]{{i - xDot}})).toArray()[0][0] + yDot);
        }
        int NNi = 0;
        for (;;) {
            try {
                File file = new File(String.format("savedNN%d.nn", NNi));
                if (file.exists()) {
                    throw new IOException();
                }
                FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(elite);
                oos.close();
                break;
            } catch (IOException ex) {
                NNi++;
            }
        }
        /*File file = new File("savedNN7.nn");
         try {
         FileInputStream fin = new FileInputStream(file);
         ObjectInputStream ois = new ObjectInputStream(fin);
         NeuralNetwork elite = (NeuralNetwork) ois.readObject();
         for (double p = 0; p <= 530; p += 1) {
         double l = p - 530 / 2;
         System.out.println(String.format("%.2f",elite.forward(new Matrix(new double[][]{{l}})).toArray()[0][0] + 15500));
         }
         } catch (Exception ex) {
         }*/
    }

    private static List<Data> readData(String s) {
        File f = new File(s);
        try {
            BufferedReader rdr = new BufferedReader(new FileReader(f));
            Stream<String> lines = rdr.lines();
            List<Data> data = new ArrayList<>();
            lines.forEach((String line) -> {
                String[] vals = line.split(",");
                //Scanner dateScnr = new Scanner(vals[0]);
                int timeStamp = Integer.parseInt(vals[0]);
                double open = Double.parseDouble(vals[1]);
                // double high = Double.parseDouble(vals[2]);
                // double low = Double.parseDouble(vals[3]);
                // double close = Double.parseDouble(vals[4]);
                // dateScnr.useDelimiter("/");
                // int month = dateScnr.nextInt();
                // int day = dateScnr.nextInt();
                // int year = dateScnr.nextInt();
                // if (year < 50) {
                //     year += 2000;
                // } else {
                //     year += 1900;
                // }
                // dateScnr.close();
                // int timeStamp = 0;
                // boolean leapYear = isLeapYear(year);
                // timeStamp += day;
                // timeStamp += daysUpToMonth(month, leapYear);
                // while (year > 1900) {
                //     if (isLeapYear(year)) {
                //         timeStamp += 366;
                //     } else {
                //         timeStamp += 365;
                //     }
                //     year--;
                // }
                Data datum = new Data(timeStamp,
                        open, 0, 0, 0);// high,
                //  low, close);
                data.add(datum);
            });
            Collections.sort(data);
            int timeRadix = data.get(0).x;
            for (Data datum : data) {
                datum.x -= timeRadix;
            }
            return data;
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /*
     private static boolean isLeapYear(int year) {
     boolean leapYear;
     if (year % 4 != 0) {
     leapYear = false;
     } else if (year % 100 != 0) {
     leapYear = true;
     } else {
     leapYear = year % 400 == 0;
     }
     return leapYear;
     }

     private static int daysUpToMonth(int month, boolean leapYear) {
     int days = 0;
     for (int i = 0; i < month; i++) {
     days += daysInMonth(i, leapYear);
     }
     return days;
     }

     private static int daysInMonth(int month, boolean leapYear) {
     int[] monthToDayMap = {0,
     31,
     28,
     31,
     30,
     31,
     30,
     31,
     31,
     30,
     31,
     30,
     31
     };
     int days = monthToDayMap[month];
     if (leapYear && month == 2) {
     days++;
     }
     return days;
     }*/
    private NeuralNetwork nn = null;
    private List<Data> data = null;

    double xDot, yDot;
    double xMax, xMin, yMax, yMin;

    public BestPredictorNA(double xDot, double yDot,
            double xMin, double xMax,
            double yMin, double yMax) {
        this.xDot = xDot;
        this.yDot = yDot;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.yMin = yMin;
    }

    private void setNN(NeuralNetwork nn) {
        this.nn = nn;
    }

    private void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double xConvert = PLOT.width / 2 / xDot;
        double yConvert = PLOT.height / 2 / yDot;
        g.setColor(Color.BLUE);
        if (data != null) {
            for (Data datum : data) {
                int xMap = xSource + (int) Math.round(datum.x / xMax * 1200);
                int yMap = ySource - (int) Math.round((datum.y - yMin) * yConvert);
                g.fillOval(xMap, yMap, 5, 5);
            }
        }
        g.setColor(Color.BLACK);
        if (nn != null) {
            double xPrev, yPrev;
            xPrev = xMin - xDot;
            yPrev = nn.forward(new Matrix(
                    new double[][]{{xPrev}})).toArray()[0][0];
            yPrev = ySource - (yPrev + 1) * yConvert;
            xPrev = xSource;
            for (double i = xMin; i <= xMax * 1.3; i++) {
                double x = i - xMax / 2;
                double[][] xArr = {{x}};
                Matrix X = new Matrix(xArr);
                Matrix Y = nn.forward(X);
                double[][] yArr = Y.toArray();
                double yHat = yArr[0][0];
                double yMap = 800 - (yHat + 1) * yConvert;
                double xMap = 360 + i * xConvert;
                g.drawLine((int) Math.round(xPrev), (int) Math.round(yPrev),
                        (int) Math.round(xMap), (int) Math.round(yMap));
                xPrev = xMap;
                yPrev = yMap;
            }
        }
    }

}

class Data implements Comparable<Data> {

    int x;
    final double y, high, low, close;

    Data(int date,
            double open, double high,
            double low, double close) {
        this.x = date;
        this.y = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    @Override
    public int compareTo(Data t) {
        return x - t.x;
    }

    @Override
    public String toString() {
        return String.format("%d,%.2f,%.2f,%.2f,%.2f%n",
                x, y, high, low, close);
    }

}
