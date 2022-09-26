package edu.jsu.mcis.cs310;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
        
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and JSON.simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            // Initialize CSV Reader and Iterator
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            /* INSERT YOUR CODE HERE */
            JSONObject jsonObject = new JSONObject();
            JSONArray rHeader = new JSONArray();
            JSONArray cHeader = new JSONArray();
            JSONArray data = new JSONArray();
            String[] rows = iterator.next();
            String[] headerField = iterator.next();
            
            for(String headerFields : headerField) {
                cHeader.add(headerFields);
            }
            while(iterator.hasNext()) {
                rHeader.add(rows[0]);
                for (int i = 1; i < rows.length; i++) {
                    data.add(Integer.parseInt(rows[i]));
                }
                data.add(data);
            }
            jsonObject.put("colheaders", cHeader);
            jsonObject.put("rowHeaders", rHeader);
            jsonObject.put("data", data);
            
            results = JSONValue.toJSONString(jsonObject);
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // Return JSON String
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            // Initialize JSON Parser and CSV Writer
            
            JSONParser parser = new JSONParser();
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
            /* INSERT YOUR CODE HERE */
            JSONObject jobject = (JSONObject) parser.parse(jsonString);
            JSONArray col = (JSONArray) jobject.get("colHeaders");
            JSONArray row = (JSONArray) jobject.get("rowHeaders");
            JSONArray data = (JSONArray) jobject.get("data");
            
            //String Arrays for OpenCSV
            String[] csvcol = new String[col.size()];
            String[] csvrow = new String[row.size()];
            String[] csvdata = new String[data.size()];
            String[] rowdata;
            
            //Copying column headers
            for (int i = 0; i < col.size(); i++) {
                csvcol[i] = col.get(i) + "";
            }
            
            for (int i = 0; i < row.size(); i++) {
                
                csvrow[i] = row.get(i) + "";
                csvdata[i] = data.get(i) + "";

            }
            
            csvWriter.writeNext(csvcol);
            
            for (int i = 0; i < csvdata.length; i++) {
                
                /* Strip square brackets from next row */
                
                csvdata[i] = csvdata[i].replace("[","");
                csvdata[i] = csvdata[i].replace("]","");
                
                /* Split csvdata[i] into row elements (using comma as delimiter) */

                String[] elements = csvdata[i].split(",");
                
                /* Create String[] container for row data (sized at the number of row elements, plus one for row header) */
                
                rowdata = new String[elements.length + 1];
                
                /* Copy row header into first element of "rowdata" */

                
				rowdata[0] = csvrow[i];
                
                /* Copy row elements into remaining elements of "rowdata" */
                
                
		for(int j = 1; j < csvrow.length; j++){
                    rowdata[j] = csvrow[j];
		}
                
                csvWriter.writeNext(rowdata);
        }
            results = writer.toString();
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // Return CSV String
        
        return results.trim();
        
        }
	
    }