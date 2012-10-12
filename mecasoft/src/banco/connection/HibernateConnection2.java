package banco.connection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateConnection2 {

	private static SessionFactory sf;
	private static Session session;
	private static Transaction tx;
	private static ServiceRegistry serviceRegistry;
	private static Session sessionAutomatico;
	private static Transaction txAutomatico;

	public void initSystem() {

		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		sf = configuration.buildSessionFactory(serviceRegistry);
	}

	public static Session getSession() {
		if (session == null)
			openConnection();

		return session;
	}

	public static void create(Configuration cfg) {
		SchemaExport esquema = new SchemaExport(cfg);
		esquema.execute(true, true, false, true);
	}

	public static void openConnection() {

		session = sf.openSession();

		tx = session.beginTransaction();

	}

	public static void opneConnectionAutomatico() {
		sessionAutomatico = sf.openSession();
		txAutomatico = sessionAutomatico.beginTransaction();
	}

	public static Session getSessionAutomatico() {

		// caso não tenha aberto a conecção
		if (session == null)
			openConnection();

		if (sessionAutomatico == null)
			opneConnectionAutomatico();

		return sessionAutomatico;
	}

	public static void rollBack() {
		tx.rollback();
		session.clear();
		tx = session.beginTransaction();
	}

	public static void commit() {
		tx.commit();
		tx = session.beginTransaction();
	}

	public static void autoCommit() {
		txAutomatico.commit();
		sessionAutomatico.clear();
		txAutomatico = sessionAutomatico.beginTransaction();
	}

	public static void revertChanges(Object entidade) {
		session.evict(entidade);
	}

	// para as consultas que sao influenciadas pela 2º session
	public Query createQuery(String query) {
		getSession().clear();
		return getSession().createQuery(query);
	}

	public boolean isDirty() {
		return getSession().isDirty();
	}
}
