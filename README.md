# SymbolTableApplications
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
 
 * A further explanation of the inefficiency of KdTrees in this particular implementation is
 * the fact that the linear search for a point simply traverses the array of allPoints to find
 * the closest point. Then the index of that value is stored and used to directly access the
 * country name in all countries. However, the KdTreee implementation is required to run the 
 * code for nearest, traversing the data points once to find the nearest point. Then, it needs 
 * to run the code for get so that it can actually find the value of the node at that point.
 * Effectively, it traverses the data twice to find the value. Thus, points that are close to
 * the root of the KdTree have great efficiency while farther points are less so. If the nearest
 * method were modified to return the country value directly, this problem would be mitigated.
