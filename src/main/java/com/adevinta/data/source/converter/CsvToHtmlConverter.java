package com.adevinta.data.source.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.adevinta.data.CsvParserSimple;

public class CsvToHtmlConverter implements HtmlConverterInterface {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DOUBLE_QUOTES = '"';
    private static final char DEFAULT_QUOTE_CHAR = DOUBLE_QUOTES;
    private static final String NEW_LINE = "\n";

    private boolean isMultiLine = false;
    private String pendingField = "";
    private String[] pendingFieldLine = new String[]{};
    
	@Override
	public List<String[]> convertToHtml(int withTableHeader) {
		// Loads file from resources folder
		InputStream stream = CsvParserSimple.class.getClassLoader().getResourceAsStream("csv/Workbook2.csv");
		return readFile(stream, withTableHeader);
	}
	
	public List<String[]> readFile(InputStream stream) throws Exception {
        return readFile(stream, 0);
    }

    public List<String[]> readFile(InputStream stream, int skipLine) {
        List<String[]> result = new ArrayList<String[]>();
        int indexLine = 1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (indexLine++ <= skipLine) {
                    continue;
                }
                String[] csvLineInArray = parseLine(line);
                if (isMultiLine) {
                    pendingFieldLine = joinArrays(pendingFieldLine, csvLineInArray);
                } else {
                    if (pendingFieldLine != null && pendingFieldLine.length > 0) {
                        // joins all fields and add to list
                        result.add(joinArrays(pendingFieldLine, csvLineInArray));
                        pendingFieldLine = new String[]{};
                    } else {
                        // if dun want to support multiline, only this line is required.
                        result.add(csvLineInArray);
                    }
                }
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

    public String[] parseLine(String line) throws Exception {
        return parseLine(line, DEFAULT_SEPARATOR);
    }

    public String[] parseLine(String line, char separator) throws Exception {
        return parse(line, separator, DEFAULT_QUOTE_CHAR).toArray(String[]::new);
    }

    private List<String> parse(String line, char separator, char quoteChar) throws Exception {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        boolean isFieldWithEmbeddedDoubleQuotes = false;
        StringBuilder field = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == DOUBLE_QUOTES) {               // handle embedded double quotes ""
                if (isFieldWithEmbeddedDoubleQuotes) {
                    if (field.length() > 0) {       // handle for empty field like "",""
                        field.append(DOUBLE_QUOTES);
                        isFieldWithEmbeddedDoubleQuotes = false;
                    }
                } else {
                    isFieldWithEmbeddedDoubleQuotes = true;
                }
            } else {
                isFieldWithEmbeddedDoubleQuotes = false;
            }
            if (isMultiLine) {                      // multiline, add pending from the previous field
                field.append(pendingField).append(NEW_LINE);
                pendingField = "";
                inQuotes = true;
                isMultiLine = false;
            }
            if (c == quoteChar) {
                inQuotes = !inQuotes;
            } else {
                if (c == separator && !inQuotes) {  // if find separator and not in quotes, add field to the list
                    result.add(field.toString());
                    field.setLength(0);             // empty the field and ready for the next
                } else {
                    field.append(c);                // else append the char into a field
                }
            }
        }

        //line done, what to do next?
        if (inQuotes) {
            pendingField = field.toString();        // multiline
            isMultiLine = true;
        } else {
            result.add(field.toString());           // this is the last field
        }
        return result;

    }

    private String[] joinArrays(String[] array1, String[] array2) {
        return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
                .toArray(String[]::new);
    }
}