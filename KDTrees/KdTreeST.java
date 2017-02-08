/**
 * Brian Li
 * 01/14/16
 * KdTreeST.java
 *
 * KdTree assignment
 */
public class KdTreeST<Value>
{
    private Node root;
    private int size;
    
    private static double X_MIN, X_MAX, Y_MIN, Y_MAX;

    private class Node 
    {
        public Point2D p;      // the point
        public Value value;    // the symbol table maps the point to this value
        public RectHV rect;    // the axis-aligned rectangle corresponding to this node
        public Node lb;        // the left/bottom subtree
        public Node rb;        // the right/top subtree

        public Node(Point2D p, Value value, RectHV rect) 
        {
            this.p = p;
            this.value = value;
            this.rect = rect;

            lb = null;
            rb = null;
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
        
        root = put(point, value, root, 0, X_MIN, X_MAX, Y_MIN, Y_MAX);
    }

    /**
     * helper method for put that acts recursively
     */
    private Node put(Point2D point, Value value, Node r, int level, double x_min, double x_max, double y_min, double y_max)
    {
        if(r == null)
        {
            size += 1;
            return new Node(point, value, new RectHV(x_min, y_min, x_max, y_max)); 
        }
        else if(level % 2 == 0)
        {
            if(point.x() < r.p.x())
            {
                r.lb = put(point, value, r.lb, ++level, x_min, r.p.x(), y_min, y_max);
            }
            else
            {
                r.rb = put(point, value, r.rb, ++level, r.p.x(), x_max, y_min, y_max);
            }
        }
        else
        {
            if(point.y() < r.p.y())
            {
                r.lb = put(point, value, r.lb, ++level, x_min, x_max, y_min, r.p.y());
            }
            else
            {
                r.rb = put(point, value, r.rb, ++level, x_min, x_max, r.p.y(), y_max);
            }
        }
        
        return r;
    }

    /**
     * get the Value associated with a specific point
     */
    public Value get(Point2D point)
    {
        return get(point, root, 0);
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
            if(point.x() < r.p.x())
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
            if(point.y() < r.p.y())
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
            if(point.x() < r.p.x())
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
            if(point.y() < r.p.y())
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
    public void points(Node r, Queue<Point2D> queue)
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
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, queue);
        return queue;
    }
    
    /**
     * helper method for range method
     */
    public void range(Node r, RectHV rect, Queue<Point2D> queue)
    {
        if(r == null)
        {
            // do nothing but condition included for efficiency
        }
        else if(r.rect.intersects(rect))
        {
            if(rect.contains(r.p))
            {
                queue.enqueue(r.p);
            }
            
            // recusively check left and right branches
            range(r.lb, rect, queue);
            range(r.rb, rect, queue);
        }
    }
    
    /**
     * find the point nearest to the target point
     */
    public Point2D nearest(Point2D point)
    {
        if(isEmpty())
        {
            return null;
        }
        else
        {
            Point2D closestPoint = nearest(root, point, root.p);
            return closestPoint;
        }
    }
    
    /**
     * helper method for nearest
     */
    private Point2D nearest(Node r, Point2D point, Point2D closestPoint)
    {
        // if the current node is null, return the previous closestPoint
        if(r == null)
        {
            return closestPoint;
        }
        else
        {
            if(closestPoint == null)
            {
                closestPoint = r.p;
            }
            else if(r.rect.distanceSquaredTo(point) <= closestPoint.distanceSquaredTo(point))
            {
                if(r.p.distanceSquaredTo(point) < closestPoint.distanceSquaredTo(point))
                {
                    closestPoint = r.p;
                }
                
                if(r.rb != null && r.rb.rect.contains(point))
                {
                    closestPoint = nearest(r.rb, point, closestPoint);
                    closestPoint = nearest(r.lb, point, closestPoint);
                }
                else
                {
                    closestPoint = nearest(r.lb, point, closestPoint);
                    closestPoint = nearest(r.rb, point, closestPoint);
                }
            }
            
            return closestPoint;
        }
    }
}