/**
 * This script is an task in an exam. I needed 5 minutes for it.
 */
package recursiverangesum;

/**
 * Calculate the sum of a given range
 * 
 * @author senycorp
 */
public class RecursiveRangeSum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(sum(1,5));
    }
    
    /**
     * Calculate sum of all digits defined as range of start and end
     * 
     * @param start
     * @param end
     * @return 
     */
    public static int sum(int start, int end) {
        System.out.println("Called: " + start + " - " + end);
        int sum = start;
        System.out.println("Sum: " + sum);
        
        if (start < end) {
            System.out.println("Entering Recursion");
            start++;
            sum += sum(start, end);
        }
        
        System.out.println("Return: " + sum);
        return sum;
    }
}
