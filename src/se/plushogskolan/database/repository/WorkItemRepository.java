package se.plushogskolan.database.repository;

import java.util.List;

import se.plushogskolan.database.model.WorkItem;

public interface WorkItemRepository {
	void addWorkItem(WorkItem workItem);

	void changeStatus(String id, String status);

	void delete(String id);

	void assignItemToUser(String itemId, String userId);

	List<WorkItem> getAllByStatus(String status);

	List<WorkItem> getAllByTeam(String teamId);

	List<WorkItem> getAllByUser(String userId);

	WorkItem getById(String id);
	
	void removeUserId(String itemId);
}
