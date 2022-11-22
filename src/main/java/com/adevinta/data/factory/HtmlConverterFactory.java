package com.adevinta.data.factory;

import com.adevinta.data.enums.FileType;
import com.adevinta.data.source.converter.CsvToHtmlConverter;
import com.adevinta.data.source.converter.HtmlConverterInterface;
import com.adevinta.data.source.converter.PrnToHtmlConverter;

public class HtmlConverterFactory {
	public HtmlConverterInterface findConverter(FileType type) {
		HtmlConverterInterface converter ;
		switch(type) {
			case CSV:
				converter = new CsvToHtmlConverter();
				break;
			case PRN:
				converter = new PrnToHtmlConverter();
				break;
			default:
				converter = new CsvToHtmlConverter();
		}
		return converter;
	}
}
