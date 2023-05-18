package dev.yudin.script_runner;

import dev.yudin.exceptions.AppConfigurationException;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

@Log4j
@Component
public class ScriptExecutor implements Runnable {

	public static final String ERROR_MESSAGE_CONNECTION = "Could not get connection";
	public static final String FILE_NOT_FOUND_ERROR_MESSAGE = "file not found! ";
	private DataSource dataSource;

	@Autowired
	public ScriptExecutor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void run(String fileName) {
		try (Connection connection = dataSource.getConnection()) {
			ScriptRunner runner = new ScriptRunner(connection);
			InputStreamReader reader = new InputStreamReader(getFileFromResourceFolder(fileName));
			runner.runScript(reader);
		} catch (SQLException ex) {
			log.error(ERROR_MESSAGE_CONNECTION, ex);
			throw new AppConfigurationException(ERROR_MESSAGE_CONNECTION, ex);
		}
	}

	private InputStream getFileFromResourceFolder(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		if (inputStream == null) {
			log.error(FILE_NOT_FOUND_ERROR_MESSAGE + fileName);
			throw new AppConfigurationException(FILE_NOT_FOUND_ERROR_MESSAGE + fileName);
		} else {
			return inputStream;
		}
	}
}
