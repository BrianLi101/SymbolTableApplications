
/**
 * Write a description of class PointST here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PointST<Value> {
    private Node root;
    private int size;

    private class Node {
        private Point2D p;      // the point
        private Value value;    // the symbol table maps the point to this value
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D p, Value value, RectHV rect) {
            this.p = p;
            this.value = value;
            this.rect = rect;
        }
    }
    
    public PointST()                                // construct an empty symbol table of points 
    {
        size = 0;
    }

    public boolean isEmpty()                      // is the symbol table empty? 
    {
        return size == 0;
    }

    public int size()                         // number of points 
    {
        return size;
    }

    public void put(Point2D p, Value val)      // associate the value val with point p
    

    public Value get(Point2D p)                 // value associated with point p 



    public boolean contains(Point2D p)            // does the symbol table contain point p? 
    {
        return
    }


    public Iterable<Point2D> points()                       // all points in the symbol table 

    

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
    

    public Point2D nearest(Point2D p)             // a nearest neighbor to point p; null if the symbol table is empty 



    public static void main(String[] args)                  // unit testing of the methods (not graded) 
}