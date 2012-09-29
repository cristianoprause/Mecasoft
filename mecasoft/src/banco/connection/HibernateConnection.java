package banco.connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

@SuppressWarnings("deprecation")
public class HibernateConnection {
	
	private static SessionFactory sf;
	private static Session session;
	private static Session sessionAutomatico;
	private static Transaction tx;
	private static Transaction txAutomatico;
  
	public void initSystem() {  
  
        // Cria uma configuração para a classe 
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		sf = cfg.configure().buildSessionFactory();
		create(cfg);
    }  
	
	public static Session getSession(){
		if(session == null)
			openConnection();
		
		return session;
	}
	
	public static Session getSessionAutomatico() {
		
		//caso não tenha aberto a conecção
		if(session == null)
			openConnection();
		
		if(sessionAutomatico == null)
			opneConnectionAutomatico();
		
		return sessionAutomatico;
	}
	
	public static void create(AnnotationConfiguration cfg) {
		SchemaExport esquema = new SchemaExport(cfg);
		esquema.execute(true, true, false, true);
    }
	
	public static void openConnection(){
		
		session = sf.openSession();

		tx = session.beginTransaction();

	}
	
	public static void opneConnectionAutomatico(){
		sessionAutomatico = sf.openSession();
		txAutomatico = sessionAutomatico.beginTransaction();
	}
	
	public static void rollBack(){
		tx.rollback();
		session.clear();
		tx = session.beginTransaction();
	}
	
	public static void commit(){
		tx.commit();
		session.clear();
		tx = session.beginTransaction();
	}
	
	public static void autoCommit(){
		txAutomatico.commit();
		sessionAutomatico.clear();
		txAutomatico = sessionAutomatico.beginTransaction();
	}
	
	public static void revertChanges(Object entidade){
		session.evict(entidade);
	}
	
	public boolean isDirty(){
		return getSession().isDirty();
	}
}  
