package se.plushogskolan.database.services;

import java.util.List;

import se.plushogskolan.database.model.User;
import se.plushogskolan.database.model.WorkItem;
import se.plushogskolan.database.model.WorkItemStatus;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.UserRepository;
import se.plushogskolan.database.repository.WorkItemRepository;

public class WorkItemService {

	private final WorkItemRepository workItemRepository;
	private final UserRepository userRepository;

	public WorkItemService(WorkItemRepository workItemRepository,UserRepository userRepository) {
		this.workItemRepository = workItemRepository;
		this.userRepository = userRepository;
	}


	public void addWorkItem(WorkItem workItem) {
		workItemRepository.addWorkItem(workItem);
	}

	public void changeStatus(String id, WorkItemStatus status) {

		workItemRepository.changeStatus(id, status.toString());
	}

	//better to return boolean with delete methods
	public void delete(String id) {
		workItemRepository.delete(id);
	}

	public void assignItemToUser(String itemId, String userId) {
		User user;
		try {
			user = userRepository.getUserById(userId);
		} catch (RepositoryException e) {
			throw new ServiceException("Get user by id:"+userId+" failed.",e);
		}
		
		if ((user != null) && (user.getStatus().equals("Inactive"))) {
			throw new ServiceException("Assign workitem to user failed, user is inactive");
		}else if (user==null){
			return;
		}
		List<WorkItem> items = this.workItemRepository.getAllByUser(userId);
		if (items.size() < 5) {
			this.workItemRepository.assignItemToUser(itemId, userId);
		} else {
			throw new ServiceException("Assign work item to user failed, user has already 5 workitems.");
		}
	}

	public void removeUserId(String itemId){
		workItemRepository.removeUserId(itemId);
	}
	
	public List<WorkItem> getAllByStatus(WorkItemStatus status) {
		return this.workItemRepository.getAllByStatus(status.toString());
	}

	public List<WorkItem> getAllByTeam(String teamId) {
		return this.workItemRepository.getAllByTeam(teamId);
	}

	public List<WorkItem> getAllByUser(String id) {
		return this.workItemRepository.getAllByUser(id);
	}

	public WorkItem getById(String id) {
		return this.workItemRepository.getById(id);
	}
}
