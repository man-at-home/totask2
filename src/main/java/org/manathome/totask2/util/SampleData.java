package org.manathome.totask2.util;

/** demo data testing report, not used in production. */
public class SampleData {
    
    private double random = Math.random();
    
    /** sample. */
    public String getName() {
        return "my name is sample-data";
    }

    /** sample. */
    public String getAge() {
        return "math.random." + random + ".X";
    }
}
