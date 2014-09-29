package org.regele.totask2.util;

public class SampleData {
    
    private double random = Math.random();
    
    public String getName() {
        return "my name is sample-data";
    }

    public String getAge() {
        return "math.random." + random + ".X";
    }
}