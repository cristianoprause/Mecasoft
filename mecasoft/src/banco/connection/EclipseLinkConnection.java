package banco.connection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;

public class EclipseLinkConnection {

	private EntityManager entityManager;
	
	public boolean isDirty(){
		EntityManagerImpl manager = (EntityManagerImpl) getEntityManager();
		return manager.getUnitOfWork().hasChanges();
	}
	
	public void rollBack() {
		getEntityManager().getTransaction().rollback();
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
	
	public void merge(Object modelo){
		getEntityManager().merge(modelo);
		getEntityManager().getTransaction().commit();
	}
	
	public void persist(Object modelo){
		getEntityManager().persist(modelo);
		getEntityManager().getTransaction().commit();
	}
	
	public void remove(Object modelo){
		getEntityManager().remove(modelo);
		getEntityManager().getTransaction().commit();
	}
	
}
