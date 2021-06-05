package br.com.grawards.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser.Feature;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class CsvUtil {
	
	
	private static final CsvMapper mapper = new CsvMapper();
	
    public static <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
    	
    	CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        return reader.<T>readValues(stream).readAll();        
    } 
}
