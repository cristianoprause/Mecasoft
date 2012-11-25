package banco.connection;

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
	private static Session sessionAutomatico;
	private static Transaction txAutomatico;

	 public void initSystem() {  
		 // Cria uma configura��o para a classe 
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

	public static void opneConnectionAutomatico() {
		sessionAutomatico = sf.openSession();
		txAutomatico = sessionAutomatico.beginTransaction();
	}

	public static Session getSessionAutomatico() {

		// caso n�o tenha aberto a conec��o
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
		session.clear();
		tx = session.beginTransaction();
	}
	
	public static void commitSemClear(){
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
	
	public static boolean isSessionRefresh(Object modelo){
		return !tx.isActive() || session.contains(modelo);
	}

	// para as consultas que sao influenciadas pela 2� session
	public Query createQuery(String query) {
		getSession().clear();
		return getSession().createQuery(query);
	}

	public boolean isDirty() {
		return getSession().isDirty();
	}
}
