package com.adevinta.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.adevinta.data.source.converter.CsvToHtmlConverter;

public class CsvParserSimpleTest {

    private CsvToHtmlConverter parser = new CsvToHtmlConverter();

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"aa\",\"\",\"\",\"\",\"\"",
            "aa,,,,",
            "aa,,\"\",,"
    }
    )
    void test_csv_line_empty(String line) throws Exception {
        String[] result = parser.parseLine(line);
        assertEquals(5, result.length);
        assertEquals("aa", result[0]);
        assertEquals("", result[1]);
        assertEquals("", result[2]);
        assertEquals("", result[3]);
        assertEquals("", result[4]);
    }

    // RFC 4180 require comma as separator, but in real life , it can be semi colon,
    // not everyone follow RFC.
    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"aa\";\"bb\";\"cc\";\"dd\";\"ee\"",
            "aa;bb;cc;dd;ee",
            "aa;bb;\"cc\";dd;ee"
    }
    )
    void test_csv_line_custom_separator(String line) throws Exception {
        String[] result = parser.parseLine(line, ';');
        assertEquals(5, result.length);
        assertEquals("aa", result[0]);
        assertEquals("bb", result[1]);
        assertEquals("cc", result[2]);
        assertEquals("dd", result[3]);
        assertEquals("ee", result[4]);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"Australia\",\"11 Lakkari Cl, Taree, NSW 2430\"",
            "Australia,\"11 Lakkari Cl, Taree, NSW 2430\""
    }
    )
    void test_csv_line_contain_comma_in_field(String line) throws Exception {

        String[] result = parser.parseLine(line);

        assertEquals(2, result.length);
        assertEquals("Australia", result[0]);
        assertEquals("11 Lakkari Cl, Taree, NSW 2430", result[1]);

    }

    @Test
    void test_csv_line_contain_double_quotes_in_field() throws Exception {

        String line = "\"Australia\",\"51 Maritime Avenue, \"\"Western Australia\"\", 6286\"";
        String[] result = parser.parseLine(line);

        assertEquals(2, result.length);
        assertEquals("Australia", result[0]);
        assertEquals("51 Maritime Avenue, \"Western Australia\", 6286", result[1]);

    }

    @Test
    void test_csv_line_contain_double_quotes_in_field_2() throws Exception {

        String line = "Australia,Welcome to \"\"Western Australia\"\"";
        String[] result = parser.parseLine(line);

        assertEquals(2, result.length);
        assertEquals("Australia", result[0]);
        assertEquals("Welcome to \"Western Australia\"", result[1]);

    }
}
