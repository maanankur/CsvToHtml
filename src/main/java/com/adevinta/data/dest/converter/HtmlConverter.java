package com.adevinta.data.dest.converter;

import java.io.PrintStream;
import java.util.List;

public class HtmlConverter {
	public static String escapeChars(String lineIn) {
		StringBuilder sb = new StringBuilder();
		int lineLength = lineIn.length();
		for (int i = 0; i < lineLength; i++) {
			char c = lineIn.charAt(i);
			switch (c) {
			case '"': 
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			default: sb.append(c);
			}
		}
		return sb.toString();
	}

	public static void tableHeader(StringBuilder sb, String[] columns) {
		sb.append("<tr>");
		for (int i = 0; i < columns.length; i++) {
			sb.append("<th>");
			sb.append(columns[i]);
			sb.append("</th>");
		}
		sb.append("</tr>");
	}

	public static void tableRow(StringBuilder sb, String[] columns) {
		sb.append("<tr>");
		for (int i = 0; i < columns.length; i++) {
			sb.append("<td>");
			sb.append(columns[i]);
			sb.append("</td>");
		}
		sb.append("</tr>");
	}

	public static StringBuilder convertToHtml(List<String[]> list, int withTableHeader, String source) {
		PrintStream stdout = System.out;
		StringBuilder sb = new StringBuilder();

		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		sb.append("<head><meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\"/>");
		sb.append("<title>" + source + "2Html</title>");
		sb.append("<style type=\"text/css\">");
		sb.append("body{background-color:#FFF;color:#000;font-family:OpenSans,sans-serif;font-size:10px;}");
		sb.append("table{border:0.2em solid #2F6FAB;border-collapse:collapse;}");
		sb.append("th{border:0.15em solid #2F6FAB;padding:0.5em;background-color:#E9E9E9;}");
		sb.append("td{border:0.1em solid #2F6FAB;padding:0.5em;background-color:#F9F9F9;}</style>");
		sb.append("</head><body><h1>" + source + "2Html</h1>");

		sb.append("<table>");
		String stdinLine;
		boolean firstLine = true;
		for (String[] columns : list) {
			if (withTableHeader == 0 && firstLine == true) {
				HtmlConverter.tableHeader(sb, columns);
				firstLine = false;
			} else {
				HtmlConverter.tableRow(sb, columns);
			}
		}
		sb.append("</table></body></html>");
		return sb;
	}
}
