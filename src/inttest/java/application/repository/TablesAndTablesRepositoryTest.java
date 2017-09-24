package application.repository;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import application.entity.TablesAndTables;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
@ActiveProfiles("test")
@Transactional
public class TablesAndTablesRepositoryTest {

	private static final String CHANGE_LOG = "./src/main/db/changelog/db.changelog-master.xml";

	Connection connection;
	Liquibase liquibase;

	@Autowired
	protected EntityManager em;

	@Before
	public void setup() throws ClassNotFoundException, SQLException, LiquibaseException {
		ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();

		Session session = em.unwrap(Session.class);
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					Database database = DatabaseFactory.getInstance()
							.findCorrectDatabaseImplementation(new JdbcConnection(connection));

					liquibase = new Liquibase(CHANGE_LOG, resourceAccessor, database);
					liquibase.dropAll();

					liquibase.update("test");
				} catch (LiquibaseException e) {
					e.printStackTrace();
					throw new SQLException(e);
				}
			}
		});

	}

	@After
	public void teardown() throws DatabaseException, LockException, SQLException {
		Session session = em.unwrap(Session.class);
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					liquibase.dropAll();
				} catch (LiquibaseException e) {
					e.printStackTrace();
					throw new SQLException(e);
				}
			}
		});
	}

	@Autowired
	TablesAndTablesRepository repo;

	@Test
	public void whenNothingThenGetAllRecords() {
		checkForNumberOfRecords(6);
	}

	private void checkForNumberOfRecords(long expected) {
		List<?> records = repo.findAll();
		assertEquals("There should be correct number of records read from the table", expected, records.size());
	}

	@Test
	public void whenNothingThenAlsoCorrectRecords() {
		List<TablesAndTables> records = repo.findAll();
		assertEquals(new Long(1L), records.get(0).getId());
		// And again, only 6 records should be there
		checkForNumberOfRecords(6);
		repo.deleteAll();
		checkForNumberOfRecords(0);
	}

}
