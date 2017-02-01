/**
 * Brian Li
 * 01/14/16
 * KdTreeST.java
 *
 * 
 */
public class KdTreeST
{
    private Node root;
    private int size;
    
    private static final double X_MIN, X_MAX, Y_MIN, Y_MAX;

    private class Node 
    {
        private Point2D p;      // the point
        private Value value;    // the symbol table maps the point to this value
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, Value value, RectHV rect) 
        {
            this.p = p;
            this.value = value;
            this.rect = rect;

            lb = null;
            rt = null;
        }
    }

    /**
     * Constructor for objects of class KdTreeST
     */
    public KdTreeST(double X_MIN, double X_MAX, double Y_MIN, double Y_MAX)
    {
        size = 0;
        this.X_MIN = X_MIN;
        this.X_MAX = X_MAX;
        this.Y_MIN = Y_MIN;
        this.Y_MAX = Y_MAX;
    }

    /**
     * return true if the KDTree contains no elements
     * else return false
     */
    public boolean isEmpty()                    
    {
        return size == 0;
    }

    /**
     * returnt the number of elements in the KDTree
     */
    public int size()                   
    {
        return size;
    }

    /**
     * publically accessible put method to place a point into the KdTree
     */
    public void put(Point2D point, Value value)
    {
        if(point == null)
            throw new IllegalArgumentException("first argument to put() is null");

        // employ a private helper method to store data points
        put(point, value, root, 0, X_MIN, X_MAX, Y_MIN, Y_MAX);
    }

    /**
     * helper method for put that acts recursively
     */
    private void put(Point2D point, Value value, Node r, int level, double x_min, double x_max, double y_min, double y_max)
    {
        if(r == null)
        {
            r = new Node(point, value, new RectHV(x_min, x_max, y_min, y_max));
            size += 1;
        }
        else if(level % 2 == 0)
        {
            if(point.x < r.p.x)
            {
                put(point, value, r.lb, ++level, x_min, r.p.x, y_min, y_max);
            }
            else
            {
                put(point, value, r.rb, ++level, r.p.x, x_max, y_min, y_max);
            }
        }
        else
        {
            if(point.y < r.p.y)
            {
                put(point, value, r.lb, ++level, x_min, x_max, y_min, r.p.y);
            }
            else
            {
                put(point, value, r.rb, ++level, x_min, x_max, r.p.y, y_max);
            }
        }
    }

    /**
     * get the Value associated with a specific point
     */
    public Value get(Point2D point)
    {
        return get(Point2D point, Node root, 0);
    }

    /**
     * helper method for get(Point2D point)
     */
    private Value get(Point2D point, Node r, int level)
    {
        if(r == null)
        {
            return null;
        }
        else if(point.equals(r.p))
        {
            return r.value;
        }
        else if(level % 2 == 0)
        {
            if(point.x < r.p.x)
            {
                return get(point, r.lb, ++level);
            }
            else
            {
                return get(point, r.rb, ++level);
            }
        }
        else
        {
            if(point.y < r.p.y)
            {
                return get(point, r.lb, ++level);
            }
            else
            {
                return get(point, r.rb, ++level);
            }
        }
    }

    /**
     * returns true if the point is contained in the KDTree
     */
    public boolean contains(Point2D point)
    {
        return contains(root, point, 0);
    }
    
    /**
     * helper method for contains
     */
    private boolean contains(Node r, Point2D point, int level)
    {
        if(r == null)
        {
            return false;
        }
        else if(point.equals(r.p))
        {
            return true;
        }
        else if(level % 2 == 0)
        {
            if(point.x < r.p.x)
            {
                return contains(r.lb, point, ++level);
            }
            else
            {
                return contains(r.rb, point, ++level);
            }
        }
        else
        {
            if(point.y < r.p.y)
            {
                return contains(r.lb, point, ++level);
            }
            else
            {
                return contains(r.rb, point, ++level);
            }
        }
    }
    
    /**
     * return all of the points in the KDTree
     */
    public Iterable<Point2D> points() 
    {
        Queue<Point2D> queue = new Queue<Point2D>();
        points(root, queue);
        return queue;   
    }
    
    /**
     * helper method to recursively add the points of nodes into the queue
     */
    public void points(Node r, Queue<Points2D> queue)
    {
        if(r != null)
        {
            // add the 'root' node point into the queue of all points
            queue.enqueue(r.p);
            
            // recursively add in all points of the left and right branches
            points(r.lb, queue);
            points(r.rb, queue);
        }
    }
    
    /**
     * return all of the points contained within a range
     */
    public Iterable<Point2D> range(RectHV rect) 
    {
        Queue<Points2D> queue = new Queue<Points2D>();
        range(root, rect, queue);
        return queue;
    }
    
    /**
     * helper method for range method
     */
    public void range(Node r, RectHV rect, Queue<Point2D> queue)
    {
        // check to see if the rectangles intersect. then check for containing. then run on both subtrees
    }
}