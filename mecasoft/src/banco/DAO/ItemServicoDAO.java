package banco.DAO;

import java.util.List;

import banco.connection.HibernateConnection;
import banco.modelo.ItemServico;
import banco.utils.ItemServicoUtils;

public class ItemServicoDAO extends HibernateConnection implements ItemServicoUtils{

	@Override
	public void saveOrUpdate(ItemServico modelo) {}

	@Override
	public void delete(ItemServico modelo) {
		getSession().delete(modelo);
	}

	@Override
	public ItemServico find(Long id) {
		return null;
	}

	@Override
	public List<ItemServico> findAll() {
		return null;
	}

}
