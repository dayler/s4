/**
 * 
 */
package com.truextend.s4.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Responsible to read the CSV files.
 * 
 * @author arielsalazar
 */
public final class Csv {
    
    public static final String SEPARATOR = ",";

    private Csv() {
        // No op.
    }
    
    /**
     * Read CSV input.
     * @param in input
     * @param ignoreLines
     * @param ignoreIfStartWith If line is <code>ignoreIfStartWith<code/> ignore line.
     */
    public static List<String[]>read(InputStream in, int ignoreLines, String ignoreIfStartWith) throws IOException {
        Objects.requireNonNull(in, "null in");
        Reader reader = null;
        try {
            reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            int count = 0;
            List<String[]> data = new ArrayList<String[]>();
            while ((line = br.readLine()) != null) {
                count++;
                if (ignoreIfStartWith != null &&
                    !ignoreIfStartWith.isEmpty() &&
                    line.startsWith(ignoreIfStartWith)) {
                    // Indicates the line is ignored. Comment line.
                    continue;
                }

                if (count > ignoreLines) {
                    String[] metadata = line.split(SEPARATOR);
                    data.add(metadata);
                }
            }

            return data;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
