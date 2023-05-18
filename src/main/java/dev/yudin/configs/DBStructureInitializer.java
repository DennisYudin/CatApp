package dev.yudin.configs;

import dev.yudin.script_runner.ScriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class DBStructureInitializer {
	private ScriptExecutor scriptExecutor;

	@Autowired
	public DBStructureInitializer(ScriptExecutor scriptExecutor) {
		this.scriptExecutor = scriptExecutor;
	}

	@PostConstruct
	public void initDatabaseStructure() {
		scriptExecutor.run("scripts/createTables.sql");
		scriptExecutor.run("scripts/populateTables.sql");
	}

	@PreDestroy
	public void cleanUp() {
		scriptExecutor.run("scripts/cleanUpTables.sql");
	}
}
