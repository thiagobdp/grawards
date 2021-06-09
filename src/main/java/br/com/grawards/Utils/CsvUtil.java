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
	
	/**
	 * Maps the stream to class T using as Column Separator ';'
	 * @param <T>
	 * @param class1
	 * @param stream
	 * @return List of class T mapped from the stream
	 * @throws IOException
	 */
    public static <T> List<T> read(Class<T> class1, InputStream stream) throws IOException {
    	
    	CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
        ObjectReader reader = mapper.readerFor(class1).with(schema);
        return reader.<T>readValues(stream).readAll();        
    } 
}
