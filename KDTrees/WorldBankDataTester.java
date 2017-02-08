/**
 * Brian Li
 * 02/07/17
 * WorldBankDataTester.java
 * 
 * This program is designed to pull data from a .txt file to compare two variables.
 * Currently, it is set to graph the "Per Capita GDP" and "Number of Internet Users Per 100"
 * for various countries across the world. It operates with two data storage structures,
 * including KdTrees and standard arrays. After graphing the points of data, the user is 
 * permitted to click somewhere on the graph. The program then uses two distinct methods to
 * find the nearest point to the location of the click through the KdTree interface as well
 * as a linear search interface. The results for time efficiency are printed in the Terminal
 * Window. Repeated tests would require the user to re-run the program. The KDTrees are 
 * inefficient for small sets of data but would be significantly more effective with millions
 * or billions of points of data.
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

public class WorldBankDataTester
{
    private static KdTreeST<String> kdTree;
    private static Point2D[] allPoints;
    private static String[] allCountries;
    
    private static String fileName = "WorldBankData.txt";
    private static int N;
    private static String xAxis;
    private static String yAxis;
    private static double xMax;
    private static double xMin;
    private static double yMax;
    private static double yMin;
    
    private static double xScale;
    private static double yScale;
    
    public static void extractData()
    {
        // pre-establish scanner for default if file doesn't exist
        Scanner sc = new Scanner(System.in);
        
        // attempt to have scanner pull from a .txt file and catch if no file exists
        try
        {
            File fileLesson = new File(fileName);        
            sc = new Scanner(fileLesson);
        }
        catch(FileNotFoundException a)
        {
            System.out.print("");
        }
        
        String[] holder = sc.nextLine().split("\t");
        
        // extract number of countries
        N = Integer.parseInt(holder[0]);
        
        // extract the axis and max values
        xAxis = holder[1];
        xMax = Double.parseDouble(holder[2]);
        xMin = Double.parseDouble(holder[3]);
        yAxis = holder[4];
        yMax = Double.parseDouble(holder[5]);
        yMin = Double.parseDouble(holder[6]);
        
        xScale = 0.9/xMax;
        yScale = 0.9/yMax;
        
        // instantiate the instance variables
        kdTree = new KdTreeST<String>(xMin, xMax, yMin, yMax);;
        allPoints = new Point2D[N];
        allCountries = new String[N];
        
        // loop through all additional lines to pull country data
        for(int i = 0; i < N; i++)
        {
            // pull individual country data
            String[] countryData = sc.nextLine().split("\t");
            String countryName = countryData[0];
            double x = Double.parseDouble(countryData[1]);
            double y = Double.parseDouble(countryData[2]);
            Point2D countryPoint = new Point2D(x, y);
            
            // store country data in KdTree
            kdTree.put(countryPoint, countryName);
            
            // store country data into arrays
            allCountries[i] = countryName;
            allPoints[i] = countryPoint;
        }
    }
    
    /**
     * method to draw all of the axes and data points
     */
    public static void drawGraph()
    {
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);
        
        Font f = new Font("ITALIC", Font.BOLD, 16);
        StdDraw.setFont(f);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.5, 0.02, xAxis);
        StdDraw.text(0.02, 0.5, yAxis, 90);

        for(Point2D aPoint: allPoints)
        {          
            StdDraw.circle(0.1 + aPoint.x() * xScale, 0.1 + aPoint.y() * yScale, 0.001);
        }
        
        StdDraw.show();
    }
    
    /**
     * method that searches for the nearest data point with linear search
     */
    public static String linearSearch(Point2D target)
    {
        double startTime = System.nanoTime(); //casted to double

        int closestIndex = 0;
        double minDistance = Double.MAX_VALUE;
        
        for(int i = 0; i < N; i++)
        {
            double distanceSquared = Math.pow(target.x() - allPoints[i].x(), 2) + Math.pow(target.y() - allPoints[i].y(), 2);
            
            if(i == 0)
            {
                minDistance = distanceSquared;
            }
            
            if(distanceSquared < minDistance)
            {
               closestIndex = i; 
               minDistance = distanceSquared;
            }
        }
        
        String country = allCountries[closestIndex];
        
        double endTime = System.nanoTime(); //casted to double
        double totalTime = endTime - startTime;
        
        System.out.println("(" + allPoints[closestIndex].x() + ", " + allPoints[closestIndex].y() + ") " + country);
        System.out.println("The linear arrays completed the search in " + totalTime + " nanoseconds.");
        
        return country;
    }
    
    /**
     * method that searches for the nearest data point with the kdTree
     */
    public static Point2D kdTreeSearch(Point2D target)
    {
        double startTime = System.nanoTime(); //casted to double
        
        Point2D found = kdTree.nearest(target);
        String country = kdTree.get(found);
        
        double endTime = System.nanoTime(); //casted to double
        double totalTime = endTime - startTime;
        
        System.out.println("(" + found.x() + ", " + found.y() + ") " + country);
        System.out.println("The KdTree completed the search in " + totalTime + " nanoseconds.");
        
        return found;
    }
    
    /**
     * main method for the class
     */
    public static void main(String[] args)
    {
        // extract all of the preliminary data from the document
        extractData();
        
        // graph the data for interaction
        drawGraph();
        
        // run loop to check for mouse press
        while(true)
        {
            // check if the mouse is pressed
            if(StdDraw.mousePressed())
            {
                // store the mouse location
                double xMouse = StdDraw.mouseX();
                double yMouse = StdDraw.mouseY();
                
                // convert the mouse click data into point data
                xMouse = (xMouse - 0.1) / xScale;
                yMouse = (yMouse - 0.1) / yScale;
                Point2D queryPoint = new Point2D(xMouse, yMouse);
                
                // run both linear and KdTree search to compare times
                String country = linearSearch(queryPoint);
                Point2D data = kdTreeSearch(queryPoint);
                
                // print the data on the graph
                Font f = new Font("ITALIC", Font.BOLD, 10);
                StdDraw.setFont(f);
                StdDraw.text(0.8, 0.3, country);
                StdDraw.text(0.8, 0.25, data.toString());
                
                // exit the loop so it doesn't run continuously
                break;
            }
        }
    }
}
