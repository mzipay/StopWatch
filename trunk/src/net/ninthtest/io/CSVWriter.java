/*
 * $Id: CSVWriter.java 80 2008-04-05 05:07:24Z matt.zipay $
 */

package net.ninthtest.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class defines a writer for CSV flat files.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 80 $
 */
public class CSVWriter extends BufferedWriter {
    private static final String CLASSNAME = CSVWriter.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(CSVWriter.class.getPackage().getName());
    
    private Map formats;

    /**
     * Creates a new CSV writer that wraps another writer.
     *
     * @param out the underlying writer to which CSV rows will be written
     */
    public CSVWriter(Writer out) {
        super(out);
        
        formats = new HashMap();
    }

    /**
     * Specifies a text formatter for column values of a particular type.
     *
     * @param klass the type of an expected column value
     * @param format a text formatter
     */
    public void registerFormat(Class klass, Format format) {
        formats.put(klass, format);
    }
    
    /**
     * Writes an array of values as a comma-separated row.
     *
     * @param row an array of column values
     * @throws java.io.IOException if the row cannot be written
     */
    public void write(Object[] row) throws IOException {
        for (int i = 0; i < row.length; ++i) {
            if (i > 0) {
                write(",");
            }
            
            String value = "";
            
            Object obj = row[i];
            if (obj != null) {
                Format format = (Format) formats.get(obj.getClass());

                // apply any special formatting to the column value
                value = (format == null) ? obj.toString() : format.format(obj);
            }
            
            if (value.indexOf("\"") != -1) {
                // double quotes are "escaped" by doubling them
                value = new StringBuffer("\"").append(value.replaceAll("\"", "\"\"")).append("\"").toString();
            } else if (value.indexOf(",") != -1) {
                // the column must be enclosed in double quotes if it contains a comma
                value = new StringBuffer("\"").append(value).append("\"").toString();
            }
            
            write(value);
        }
        
        newLine();
    }
}
