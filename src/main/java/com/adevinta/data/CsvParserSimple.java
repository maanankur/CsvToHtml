package com.adevinta.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.adevinta.data.dest.converter.HtmlConverter;
import com.adevinta.data.enums.FileType;
import com.adevinta.data.factory.HtmlConverterFactory;
import com.adevinta.data.source.converter.HtmlConverterInterface;

public class CsvParserSimple {
	private static HtmlConverterFactory factory = new HtmlConverterFactory();

	public static void main(String[] args) throws Exception {
		int withTableHeader = args.length != 0 ? 0 : 1;
		StringBuilder sb = new StringBuilder();
		HtmlConverterInterface htmlConverter = factory.findConverter(FileType.CSV);
		sb = convertToHtml(htmlConverter, withTableHeader, "Csv");
		createHtmlFile("html/CsvType.html", sb);

		htmlConverter = factory.findConverter(FileType.PRN);
		sb = convertToHtml(htmlConverter, withTableHeader, "Prn");
		createHtmlFile("html/PrnType.html", sb);
		System.out.println("Both the files converted to HTML.");
	}

	private static void createHtmlFile(String name, StringBuilder sb) {
		File f = new File(name);
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		try {
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//dir will change directory and specifies file name for writer
			File dir = new File(f.getParentFile(), f.getName());
			PrintWriter writer = new PrintWriter(dir);
			writer.print(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	private static StringBuilder convertToHtml(HtmlConverterInterface csvConverter, int withTableHeader, String source) {
		List<String[]> result = csvConverter.convertToHtml(withTableHeader);
		return HtmlConverter.convertToHtml(result, withTableHeader, source);
	}
}
