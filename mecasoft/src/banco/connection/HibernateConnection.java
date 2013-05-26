package banco.connection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;

public class HibernateConnection {

	private EntityManager entityManager;
	private UnitOfWorkImpl uowi;
	
	public boolean isDirty(){
		EntityManagerImpl manager = (EntityManagerImpl) getEntityManager();
		return manager.getUnitOfWork().hasChanges();
	}
	
	public void commit(){
		getEntityManager().getTransaction().commit();
	}
	
	public void rollBack() {
		getUowi().revertAndResume();
	}
	
	private UnitOfWorkImpl getUowi(){
		if(uowi == null){
			EntityManager em = getEntityManager();
			EntityManagerImpl impl = (EntityManagerImpl) em;
	
			uowi = (UnitOfWorkImpl) impl.getUnitOfWork();
		}
		
		return uowi;
	}
	
	public EntityManager getEntityManager() {
		if(entityManager == null)
			entityManager = MecasoftEntityManager.getEntityManager();

		if(!entityManager.getTransaction().isActive())
			entityManager.getTransaction().begin();
		return entityManager;
	}
	
	public Query createQueryNoCache(String query){
		Query q = getEntityManager().createQuery(query);
		q.setHint(QueryHints.CACHE_STORE_MODE, "REFRESH");
		return q;
	}
	
//	private static SessionFactory sf;
//	private static Session session;
//	private static Transaction tx;
//
//	 public void initSystem() {  
//		 // Cria uma configuração para a classe 
//		AnnotationConfiguration cfg = new AnnotationConfiguration();
//		sf = cfg.configure().buildSessionFactory();
//		create(cfg);
//	 }
//	
//	public static Session getSession() {
//		if (session == null)
//			openConnection();
//
//		return session;
//	}
//
//	public static void create(Configuration cfg) {
//		SchemaExport esquema = new SchemaExport(cfg);
//		esquema.execute(true, true, false, true);
//	}
//
//	public static void openConnection() {
//
//		session = sf.openSession();
//
//		tx = session.beginTransaction();
//
//	}
//
//	public static void rollBack(Serializable modelo) {
//		tx.rollback();
//		tx = session.beginTransaction();
//	}
//
//	public static void commit(Serializable modelo) {
//		tx.commit();
//		session.flush();
//		tx = session.beginTransaction();
//	}
//	
//	public static void revertChanges(Object entidade) {
//		session.evict(entidade);
//	}
//	
//	public Query createQuery(String query) {
//		Query q = getSession().createQuery(query);
//		return q;
//	}
//
//	public boolean isDirty() {
//		return getSession().isDirty();
//	}
}
