package com.adevinta.data.source.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.adevinta.data.CsvParserSimple;

public class PrnToHtmlConverter implements HtmlConverterInterface {
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
		InputStream stream = CsvParserSimple.class.getClassLoader().getResourceAsStream("csv/Workbook2.prn");
		return readFile(stream, withTableHeader);	
	}

	public List<String[]> readFile(InputStream stream) throws Exception {
		return readFile(stream, 0);
	}

	public List<String[]> readFile(InputStream stream, int skipLine){
		List<String[]> result = new ArrayList<String[]>();
		int indexLine = 1;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			StringBuilder sb;
			while ((line = br.readLine()) != null) {
				if (indexLine++ <= skipLine) {
					continue;
				}
				if (line.trim().equals("")){ 
					continue; 
				}
				sb = new StringBuilder();
				// Method called with supplied file data line and the widths of
				// each column as outlined within the file.
				String[] parts = splitStringToChunks(line, 16, 22, 9, 14, 13, 8);
				result.add(parts);
			}
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}

	public static String[] splitStringToChunks(String inputString, int... chunkSizes) {
		List<String> list = new ArrayList<>();
		int chunkStart = 0, chunkEnd = 0;
		for (int length : chunkSizes) {
			chunkStart = chunkEnd;
			chunkEnd = chunkStart + length;
			String dataChunk = inputString.substring(chunkStart, chunkEnd);
			list.add(dataChunk.trim());
		}
		return list.toArray(new String[0]);
	}
}