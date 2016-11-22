package se.plushogskolan.database.repository;

import java.util.List;

import se.plushogskolan.database.model.User;

public interface UserRepository {

	void addUser(User user) throws RepositoryException;

	void deactivateUser(String username) throws RepositoryException;

	void updateUser(User user, String oldUsername) throws RepositoryException;

	User getUserById(String id) throws RepositoryException;

	User getUserByUsername(String username) throws RepositoryException;

	List<User> getAllUsersInTeam(String teamid) throws RepositoryException;
}
