package banco.connection;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.BatchWriting;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.postgresql.Driver;

public class MecasoftEntityManager {

	private static Logger log = Logger.getLogger(MecasoftEntityManager.class);
	private static EntityManagerFactory emf = null;
	private static Map<String, Object> properties = new HashMap<String, Object>();
	private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();
	
	public synchronized static void init() {
		properties.put(PersistenceUnitProperties.TARGET_DATABASE, TargetDatabase.PostgreSQL);
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, Driver.class.getCanonicalName());
		properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://localhost:5432/mecasoft");
		properties.put(PersistenceUnitProperties.JDBC_USER, "postgres");
		properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "admin");
		properties.put(PersistenceUnitProperties.CONNECTION_POOL_MIN, "1");
		properties.put(PersistenceUnitProperties.CONNECTION_POOL_MAX, "10");
		properties.put(PersistenceUnitProperties.CACHE_STATEMENTS, "true");
		properties.put(PersistenceUnitProperties.BATCH_WRITING,BatchWriting.JDBC);
//		properties.put(PersistenceUnitProperties.CLASSLOADER, Bootstrap.class.getClassLoader());
		properties.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT,"true");

		// Cria as tabelas
		log.info("Gerando tabelas...");
		properties.put(PersistenceUnitProperties.DDL_GENERATION,PersistenceUnitProperties.CREATE_OR_EXTEND);

		properties.put("eclipselink.logging.level", "FINE");
		properties.put("eclipselink.logging.timestamp", "true");
		properties.put("eclipselink.logging.session", "true");
		properties.put("eclipselink.logging.thread", "true");
		properties.put("eclipselink.logging.exceptions", "true");

		emf = Persistence.createEntityManagerFactory("mecasoft", properties);
	}
	
	public static EntityManager getEntityManager(){
		EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
		if (entityManager == null) {
			ENTITY_MANAGER_CACHE.set(getEntityManagerFactory().createEntityManager());
			return ENTITY_MANAGER_CACHE.get();
		}
		return entityManager;
	}
	
	public static EntityManagerFactory getEntityManagerFactory(){
		if(emf == null)
			init();
		
		return emf;
	}

}
