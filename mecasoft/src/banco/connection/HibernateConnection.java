package banco.connection;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

@SuppressWarnings("deprecation")
public class HibernateConnection {

	private static SessionFactory sf;
	private static Session session;
	private static Transaction tx;

	 public void initSystem() {  
		 // Cria uma configuração para a classe 
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		sf = cfg.configure().buildSessionFactory();
		create(cfg);
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

	public static void rollBack(Serializable modelo) {
		tx.rollback();
		tx = session.beginTransaction();
	}

	public static void commit(Serializable modelo) {
		tx.commit();
		tx = session.beginTransaction();
	}
	
	public static void revertChanges(Object entidade) {
		session.evict(entidade);
	}
	
	public Query createQuery(String query) {
		getSession().clear();
		Query q = getSession().createQuery(query);
		return q;
	}

	public boolean isDirty() {
		return getSession().isDirty();
	}
}
