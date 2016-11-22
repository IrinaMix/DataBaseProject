package se.plushogskolan.database.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.plushogskolan.database.model.Issue;
import se.plushogskolan.database.model.WorkItem;
import se.plushogskolan.database.repository.IssueRepository;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.WorkItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class IssueServiceTest {

	@Mock
	private IssueRepository issueRepository;
	@Mock
	private WorkItemRepository workItemRepository;

	@InjectMocks
	private IssueService issueService;

	private static Issue issue;
	private static WorkItem workItem;

	@BeforeClass
	public static void beforeClassCreatIssueAndWorkItem() throws RepositoryException {
		issue = new Issue("01", "Issue");
		workItem = new WorkItem("01", "title", "Done", null, null);
	}

	@Test
	public void createIssueTest() throws RepositoryException {
		when(issueRepository.exists(issue)).thenReturn(false);
		issueService.createIssue(issue);
		verify(issueRepository).createIssue(issue);
	}

	@Test
	public void assignToWorkItemTest() throws RepositoryException {
		when(workItemRepository.getById("01")).thenReturn(workItem);
		issueService.assignToWorkItem(issue, workItem.getId());
		assertEquals("01", issue.getId());
		verify(workItemRepository).changeStatus(workItem.getId(), "Unstarted");
		verify(issueRepository).assignToWorkItem(issue, workItem.getId());
	}

	@Test(expected = ServiceException.class)
	public void canNotAssignToWorkItemWithStatusOtherThanDoneTest() throws RepositoryException {
		WorkItem workItem2 = new WorkItem("02", "TestWorkItem2", "Started", null, null);
		when(workItemRepository.getById("02")).thenReturn(workItem2);
		issueService.assignToWorkItem(issue, "02");
		verify(issueRepository).assignToWorkItem(issue, "02");
		
	}

	@Test
	public void updateIssueTest() throws RepositoryException {
		when(issueRepository.exists(issue)).thenReturn(true);
		when(issueRepository.getIssueByName("Issue")).thenReturn(null);
		issueService.updateIssue(issue, "UpdateIssue");
		verify(issueRepository).updateIssue(issue, "UpdateIssue");
	}

	@Test
	public void getAllItemsWithIssueTest() throws RepositoryException {
		List<WorkItem> workItemList = new ArrayList<>();
		workItemList.add(new WorkItem("03", "WorkItemTest3", "Done", null, "1"));
		workItemList.add(new WorkItem("04", "WorkItemTest4", "Done", null, "2"));
		when(issueRepository.getAllItemsWithIssue()).thenReturn(workItemList);
		List<WorkItem> allWorkItems = issueService.getAllItemsWithIssue();
		assertEquals(workItemList.size(), allWorkItems.size());
		verify(issueRepository).getAllItemsWithIssue();
	}

}
