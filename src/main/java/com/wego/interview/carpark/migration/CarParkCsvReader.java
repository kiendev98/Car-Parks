package com.wego.interview.carpark.migration;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * Read the cvs string and convert it into list of {@link CarParkCsvRecord}
 * The cvs string must contain headers and rows follow this structure.
 *
 * <table>
 *     <tr>car_park_no,address,x_coord,y_coord</tr>
 *     <tr>A1,Test Address1,1.01,1.02</tr>
 *     <tr>A2,Test Address2,1.02,1.03</tr>
 * <table/>
 */
public class CarParkCsvReader {

	public List<CarParkCsvRecord> read(Reader reader, Character separator) throws IOException {
		List<CarParkCsvRecord> records = new LinkedList<>();
		MappingIterator<CarParkCsvRecord> rowIterator = createRowIterator(reader, separator);

		while (rowIterator.hasNext()) {
			records.add(rowIterator.next());
		}

		return records;
	}

	private MappingIterator<CarParkCsvRecord> createRowIterator(Reader reader, Character separator) throws IOException {
		CsvMapper csvMapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		schema.withColumnSeparator(separator);
		return csvMapper.readerFor(CarParkCsvRecord.class)
			.with(schema)
			.readValues(reader);
	}
}
