package se.plushogskolan.database.repository;

import java.util.List;

import se.plushogskolan.database.model.Issue;
import se.plushogskolan.database.model.WorkItem;

public interface IssueRepository {
	void createIssue(Issue issue) throws RepositoryException;

	void assignToWorkItem(Issue issue, String itemId) throws RepositoryException;

	void createIssueAndAssignToWorkItem(Issue issue, String itemId) throws RepositoryException;

	void updateIssue(Issue issue, String description) throws RepositoryException;

	List<WorkItem> getAllItemsWithIssue() throws RepositoryException;

	boolean exists(Issue issue) throws RepositoryException;

	Issue getIssueByName(String name) throws RepositoryException;
}
