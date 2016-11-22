package se.plushogskolan.database.services;

import java.util.List;

import se.plushogskolan.database.model.User;
import se.plushogskolan.database.model.WorkItem;
import se.plushogskolan.database.model.WorkItemStatus;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.UserRepository;
import se.plushogskolan.database.repository.WorkItemRepository;

public class UserService {

	UserRepository userRepository;
	WorkItemRepository workItemRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserService(UserRepository userRepository,WorkItemRepository workItemRepository) {
		this.userRepository = userRepository;
		this.workItemRepository = workItemRepository;
		}

	public void addUser(User user) throws ServiceException {

		try {
			List<User> users = userRepository.getAllUsersInTeam(user.getTeamid());

			if (user.getUsername().length() >= 10) {
				if (users.size() < 10) {
					userRepository.addUser(user);
				} else {
					throw new ServiceException(
							"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
				}
			} else
				throw new ServiceException("Username must be at least 10 characters long!");
		} catch (RepositoryException e) {
			throw new ServiceException("Could not add user to database", e);
		}
	} 

	public User getUserById(String id) throws ServiceException {
		try {
			return userRepository.getUserById(id);
		} catch (RepositoryException e) {
			throw new ServiceException("Could not get user by id: " + id, e);
		}
	}

	public User getUserByUsername(String username) throws ServiceException {
		try {
			return userRepository.getUserByUsername(username);
		} catch (RepositoryException e) {
			throw new ServiceException("Could not get user by id: " + username, e);
		}
	}

	public List<User> getAllUsersInTeam(String teamid) throws ServiceException {
		try {
			return userRepository.getAllUsersInTeam(teamid);
		} catch (RepositoryException e) {
			throw new ServiceException("Could not get all users in team with id: " + teamid, e);
		}
	}

	public void deactivateUser(String username) throws ServiceException {
		try {
			userRepository.deactivateUser(username);
			String userId = userRepository.getUserByUsername(username).getId();
			List<WorkItem> itemList = workItemRepository.getAllByUser(userId);
			for (WorkItem wi : itemList) {
				workItemRepository.changeStatus(wi.getId(), WorkItemStatus.Unstarted.toString());
				workItemRepository.removeUserId(wi.getId());
			}

		} catch (Exception e) {
			throw new ServiceException("Couldn't deactivate user \"" + username + "\" to the database", e);
		}
	}

	public void updateUser(User user, String oldUsername) throws ServiceException {
		try {
			userRepository.updateUser(user, oldUsername);
		} catch (Exception e) {
			throw new ServiceException("Couldn't update user \"" + user.getUsername() + "\" to the database", e);
		}
	};

}
