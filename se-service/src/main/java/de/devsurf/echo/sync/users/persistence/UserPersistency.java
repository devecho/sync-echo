package de.devsurf.echo.sync.users.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class UserPersistency {
	@Inject
	private EntityManager manager;
	
	public UserEntity get(long id) {
		return manager.find(UserEntity.class, id);
	}
	
	public UserEntity find(String email) {
		TypedQuery<UserEntity> query = manager.createQuery("select u from UserEntity u where u.email = :email", UserEntity.class);
		query.setParameter("email", email);
		return query.getSingleResult();
	}
}
