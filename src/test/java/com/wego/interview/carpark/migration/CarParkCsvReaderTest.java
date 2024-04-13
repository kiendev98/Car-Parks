package com.wego.interview.carpark.migration;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.groups.Tuple.tuple;

public class CarParkCsvReaderTest {

	public static final String HEADER = "car_park_no,address,x_coord,y_coord";
	public static final String NEWLINE = "\n";

	private final CarParkCsvReader carParkCsvReader = new CarParkCsvReader();

	@Test
	void readRecords() throws IOException {
		//given
		String row1 = "A1,Test Address1,1.01,1.02";
		String row2 = "A2,Test Address2,1.02,1.03";
		String row3 = "A3,Test Address3,1.03,1.04";
		String row4 = "A4,Test Address4,1.04,1.05";
		String csvString = HEADER + NEWLINE + row1 + NEWLINE + row2 + NEWLINE + row3 + NEWLINE + row4;
		Reader reader = new StringReader(csvString);

		//when
		List<CarParkCsvRecord> records =  carParkCsvReader.read(reader, ',');

		//then
		Assertions.assertThat(records)
			.hasSize(4)
			.extracting(
				CarParkCsvRecord::getCarParkNo,
				CarParkCsvRecord::getAddress,
				CarParkCsvRecord::getXCoord,
				CarParkCsvRecord::getYCoord)
			.contains(
				tuple("A1", "Test Address1", 1.01, 1.02),
				tuple("A2", "Test Address2", 1.02, 1.03),
				tuple("A3", "Test Address3", 1.03, 1.04),
				tuple("A4", "Test Address4", 1.04, 1.05)
			);
	}

	@Test
	void readRecords_shouldIgnoreUnknownProperties() throws IOException {
		//given
		String header = HEADER + ",extraRow1, extraRow2";
		String row = "A1,Test Address1,1.01,1.02,extra1,extra2";
		String csvString = header + NEWLINE + row ;
		Reader reader = new StringReader(csvString);

		//when
		List<CarParkCsvRecord> records =  carParkCsvReader.read(reader, ',');

		//then
		Assertions.assertThat(records)
			.hasSize(1)
			.extracting(
				CarParkCsvRecord::getCarParkNo,
				CarParkCsvRecord::getAddress,
				CarParkCsvRecord::getXCoord,
				CarParkCsvRecord::getYCoord)
			.contains(
				tuple("A1", "Test Address1", 1.01, 1.02)
			);
	}
}
