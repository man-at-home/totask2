package org.manathome.totask2.util;

import spock.lang.*

/** 
* spock unit test. 
*
* alternative to @see DurationConverterTest
*
* @see    https://totask2.wordpress.com/2015/03/07/adding-spock-to-junit-mix/
* @author man-at-home
* @since  2015-03-07
*/
class DurationConverterSpec extends spock.lang.Specification {
    def "parsing whole hour durations from string"() {
        expect:
        DurationConverter.parse(inputString) == parsedValue

        where:
        inputString | parsedValue
        "1"         | 1.0
        "2"         | 2.0
        "12"        | 12.0
        "0"         | 0.0
    }
    
    def "parsing fractions of hours durations from string (without rounding)"() {
        expect:
        DurationConverter.parse(inputString) == parsedValue

        where:
        inputString | parsedValue
        "12.5"      | 12.5
        "0.4"       |  0.4
        "0.9"       |  0.9
        "3.0"       |  3.0
        "11.9"      | 11.9
        "0.000"     |  0.0
    }
    
    def "parsing fractions of hour durations from string (with rounding)"() {
        expect:
        DurationConverter.parse(inputString) == parsedValue

        where:
        inputString | parsedValue
        "2.99"      |  3.0
        "1.11"      |  1.1
        "2.28"      |  2.3
        "1.01"      |  1.0
        "2.59"      |  2.6
        "2.55"      |  2.6
        "2.54"      |  2.5
        "2.51"      |  2.5
        "2.50"      |  2.5
        "2.49"      |  2.5
        "2.450001"  |  2.5
        "2.45"      |  2.5
        "2.450000"  |  2.5
        "2.449999"  |  2.4
    }


    def "parsing to large duration results in invalid duration exception"() {
        when: DurationConverter.parse("25")
        then: thrown(InvalidDurationException)
    }
    

    def "parsing negative duration results in invalid duration exception"() {
        when: DurationConverter.parse("-1");
        then: thrown(InvalidDurationException)
    }           
    
    def "parsing invalid text as duration results in number format exception"() {
        when: DurationConverter.parse("bubidu 33");
        then: thrown(java.lang.NumberFormatException)
    } 
}

