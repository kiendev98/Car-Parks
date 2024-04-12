package com.wego.interview.carpark.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.wego.interview.carpark.domain.CarPark;
import com.wego.interview.carpark.domain.Coordinate;
import com.wego.interview.carpark.migration.svy21_copied.LatLonCoordinate;
import com.wego.interview.carpark.migration.svy21_copied.SVY21;

import liquibase.change.custom.CustomTaskChange;
import liquibase.change.custom.CustomTaskRollback;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Liquibase custom change set to import data from csv file to database.
 */
@Slf4j
public class LiquibaseCarParkMigrator implements CustomTaskChange, CustomTaskRollback {

	private ResourceAccessor resourceAccessor;

	@Getter
	@Setter
	private String csvFilePath;

	@Override public void execute(Database database) throws CustomChangeException {
		try {
			StopWatch stopWatch = StopWatch.createStarted();
			List<CarParkCsvRecord> records = readRecords();
			List<CarPark> carParks = records.stream()
				.map(this::toCarParkDomain)
				.toList();

			JdbcConnection con = (JdbcConnection) database.getConnection();
			PreparedStatement prepareStatement = con.prepareStatement(
				"insert into car_park (id, address, latitude, longitude) values (?, ?, ?, ?)"
			);

			for (CarPark carPark : carParks) {
				prepareStatement.setString(1, carPark.getId());
				prepareStatement.setString(2, carPark.getAddress());
				prepareStatement.setDouble(3, carPark.getCoordinate().getLatitude());
				prepareStatement.setDouble(4, carPark.getCoordinate().getLongitude());

				prepareStatement.addBatch();
			}

			prepareStatement.executeBatch();
			con.commit();

			log.info("Importing car park information from csv file took [{}ms]", stopWatch.getTime(TimeUnit.MILLISECONDS));
		} catch (Exception ex) {
			throw new CustomChangeException(ex);
		}
	}

	private CarPark toCarParkDomain(CarParkCsvRecord record) {
		LatLonCoordinate latLonCoordinate = SVY21.computeLatLon(record.getYCoord(), record.getXCoord());
		return CarPark.builder()
			.id(record.getCarParkNo())
			.address(record.getAddress())
			.coordinate(
				Coordinate.builder()
					.latitude(latLonCoordinate.getLatitude())
					.longitude(latLonCoordinate.getLongitude())
					.build()
			)
			.build();
	}

	private List<CarParkCsvRecord> readRecords() throws IOException {
		Character columnSeparator = ',';
		CarParkCsvReader csvReader = new CarParkCsvReader();
		Reader fileReader = new BufferedReader(
			new InputStreamReader(
				resourceAccessor.get(csvFilePath).openInputStream()
			)
		);

		return csvReader.read(fileReader, columnSeparator);
	}

	@Override public void rollback(Database database) throws CustomChangeException, RollbackImpossibleException {
		// Not yet implemented
	}

	@Override public String getConfirmationMessage() {
		// Not yet implemented
		return null;
	}

	@Override public void setUp() throws SetupException {
		// Not yet implemented
	}

	@Override public void setFileOpener(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}

	@Override public ValidationErrors validate(Database database) {
		// Not yet implemented
		return null;
	}
}
