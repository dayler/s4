/**
 * 
 */
package com.truextend.s4.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for the strings.
 * @author arielsalazar
 */
public final class Strings {

    private Strings() {
        // No op.
    }
    
    /**
     * @param str0
     * @param str1
     * @return <code>true</code> if both strings (str0 and str1) are equals. This method is null safe.
     */
    public static boolean equals(String str0, String str1) {
        if (str0 == null && str1 == null) {
            return true;
        }
        if (str0 == null) {
            return false;
        }
        if (str1 == null) {
            return false;
        }
        return str0.equals(str1);
    }
    
    /**
    * @param str0
    * @param str1
    * @return <code>true</code> if both strings (str0 and str1) are equals ignoring case. This method is null safe.
    */
    public static boolean equalsIgnoreCase(String str0, String str1) {
        if (str0 == null && str1 == null) {
            return true;
        }
        if (str0 == null) {
            return false;
        }
        if (str1 == null) {
            return false;
        }
        return str0.equalsIgnoreCase(str1);
    }
    
    /**
     * @param toFind
     * @param source
     * @return <code>true</code> if "toFind" is contained in "source.". This method is null safe.
     */
    public static boolean contains(String toFind, String source) {
        if (toFind == null && source == null) {
            return true;
        }
        if (toFind == null) {
            return false;
        }
        if (source == null) {
            return false;
        }
        return source.contains(toFind);
    }
    
    /**
     * @param arr
     * @return String representation for an array.
     */
    public static String join(String[]arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        return Arrays.stream(arr).collect(Collectors.joining(","));
    }
}
