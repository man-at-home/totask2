package org.manathome.totask2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;


/** 
 * checking arguments (convenience class). 
 * 
 * @author man-at-home
 * */
public abstract class AAssert {
    
    private static final Logger LOG = LoggerFactory.getLogger(AAssert.class);
    
    
    /** 
     * test zero == 0.
     *  
     * @throws AssertionError if zero != 0
     * */
    public static long checkZero(final long zero) {
        return checkZero(zero, "value must be 0");
    }
    
    /** 
     * test zero == 0.
     *  
     * @throws AssertionError if zero != 0
     * */
    public static long checkZero(final long zero, @NotNull String msg) {
        if (zero != 0) {
            LOG.error("checkZero(" + zero + ", " + msg + ") not satisfied");
            throw new AssertionError(msg);
        }
        return 0;   
    }
    
    /** argument checking.
     *  
     * @throws NullPointerException
     * */
    public static <T> T checkNotNull(T reference) {
        return checkNotNull(reference, "not null reference required.");
    }
    
    

    /** 
    * argument notNull checking.
    *  
    * @param  expectedNotNullReference the reference to be checked here
    * @param  msg to be thrown as NullPointerException
    * @return expectedNotNullReference, garantied to be not null
    * @throws NullPointerException
    */
    public static <T> T checkNotNull(T expectedNotNullReference, @NotNull String msg) {
        if (expectedNotNullReference == null) {
            LOG.error("checkNotNull(null) not satisfied ->" + msg);
            throw new NullPointerException(msg);
        }
        return expectedNotNullReference;
    }
    
    /**
     * string argument notNull/"" checking.
     * 
     * @throws NullPointerException 
     * @throws AssertionError
     * 
     * @param nonEmptyString (may not be null or "")
     * @param msg error message
     * @return nonEmptyString unchanged
     */
    public static String checkNotNullOrEmpty(final String nonEmptyString, @NotNull final String msg) {
        
        if (checkNotNull(nonEmptyString, msg).trim().length() == 0) {
            LOG.error("checkNotNullOrEmpty(" + nonEmptyString + ") not satisfied ->" + msg);
            throw new AssertionError(msg);
        }
        return nonEmptyString;
    }


    /** 
     * assert condition. 
     * 
     * @throws AssertionError
     * */
    public static void checkTrue(final boolean expectedTrueCondition, @NotNull final String msg) {
        if (!expectedTrueCondition){
            LOG.error("checkTrue(false) not satisfied ->" + msg);
            throw new AssertionError(msg);
        }
    }
    
    
    /** validate number to valueLargerThanZero > 0.
     * 
     * @param valueLargerThanZero
     * @return the given number
     * @throws AssertionError if <= 0
     */
    public static int checkPositive(final int valueLargerThanZero) {
        if (valueLargerThanZero <= 0){
            LOG.error("checkPositiv(" + valueLargerThanZero + ") not satisfied");
            throw new AssertionError("Value " + valueLargerThanZero + " not positive.");
        }
        return valueLargerThanZero;
    }
 
    /** validate number to valueLargerThanZero > 0.
     * 
     * @param valueLargerThanZero
     * @return the given number
     * @throws AssertionError if <= 0
     */    
    public static long checkPositive(final long valueLargerThanZero) {
        if (valueLargerThanZero <= 0){
            LOG.error("checkPositiv(" + valueLargerThanZero + ") not satisfied");
            throw new AssertionError("Value " + valueLargerThanZero + " not positive.");
        }
        return valueLargerThanZero;
    }    
    
    /** 
     * check index bound, requires index >= arraySize-1.
     * 
     * @param arraySize array.length or collection.size
     * @param index     0 <= index < arraySize
     *  
     * @throws IndexOutOfBoundsException 
     * @return index as provided as argument
     */
    public static int checkIndex(int arraySize, int index) {
        if (index < 0 || index > arraySize - 1) {
            LOG.error("checkIndex(" + arraySize + "," + index + ") not satisfied");
            throw new IndexOutOfBoundsException("Index " + index + " not in bounds [0.." + arraySize + "]");
        }
        return index;
    } 
}
