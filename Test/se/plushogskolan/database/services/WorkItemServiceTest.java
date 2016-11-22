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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import se.plushogskolan.database.model.User;
import se.plushogskolan.database.model.WorkItem;
import se.plushogskolan.database.model.WorkItemStatus;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.UserRepository;
import se.plushogskolan.database.repository.WorkItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class WorkItemServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private WorkItemRepository workItemRepository;
	@InjectMocks
	private WorkItemService workItemService;

	private static WorkItem workItem;

	@BeforeClass
	public static void beforeClassCreatWorkItem() {
		workItem = new WorkItem("01", "WorkItem1", "Unstarted", "01", null);

	}

	@Test
	public void canAddWorkItemTest() throws RepositoryException {

		workItemService.addWorkItem(workItem);
		verify(workItemRepository).addWorkItem(workItem);
	}

	@Test
	public void canChangeStatusTest() {
		workItemService.changeStatus(workItem.getId(), WorkItemStatus.Started);
		verify(workItemRepository).changeStatus(workItem.getId(), WorkItemStatus.Started.toString());
	}

	@Test
	public void canDeleteTest() {
		workItemService.delete(workItem.getId());
		verify(workItemRepository).delete(workItem.getId());
	}

	@Test
	public void canAssignItemToUserTest() throws RepositoryException {
		User user = new User("01", "Irina", "Fatkoulin", "irinafatkoulin", "01", "Active");
		List<WorkItem> workItemList = new ArrayList<>();
		workItemList.add(new WorkItem("02", "WorkItem2", "Unstarted", "01", null));
		when(userRepository.getUserById("01")).thenReturn(user);
		when(workItemRepository.getAllByUser(user.getId())).thenReturn(workItemList);
		workItemService.assignItemToUser(workItem.getId(), user.getId());
		verify(workItemRepository).assignItemToUser(workItem.getId(), user.getId());
	}

	@Test(expected = ServiceException.class)
	public void canNotAssignItemToInactiveUserTest() throws RepositoryException {
		User user = new User("01", "Irina", "Fatkoulin", "irinafatkoulin", "01", "Inactive");
		when(userRepository.getUserById("01")).thenReturn(user);
		workItemService.assignItemToUser(workItem.getId(), user.getId());
		verify(workItemRepository).assignItemToUser(workItem.getId(), user.getId());

	}

	@Test(expected = ServiceException.class)
	public void userCanNotHaveMoreThen5ItemTest() throws RepositoryException {
		User user = new User("01", "Irina", "Fatkoulin", "irinafatkoulin", "01", "Active");
		when(userRepository.getUserById(user.getId())).thenReturn(user);
		List<WorkItem> workItemList = new ArrayList<WorkItem>();
		for (int i = 0; i < 5; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Started", user.getId(), null));
		}
		when(workItemRepository.getAllByUser(user.getId())).thenReturn(workItemList);
		workItemService.assignItemToUser(workItem.getId(), user.getId());
		verify(workItemRepository).assignItemToUser(workItem.getId(), user.getId());

	}

	@Test
	public void canGetByStatusTest() throws RepositoryException {
		List<WorkItem> workItemList = new ArrayList<WorkItem>();
		for (int i = 0; i < 2; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Started", null, null));
		}

		for (int i = 0; i < 3; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Done", null, null));
		}
		when(workItemRepository.getAllByStatus("Done")).thenAnswer(new Answer<List<WorkItem>>() {

			@Override
			public List<WorkItem> answer(InvocationOnMock invocation) throws Throwable {
				List<WorkItem> workItemListFinal = new ArrayList<>();
				String status = (String) invocation.getArguments()[0];
				for (WorkItem workItem : workItemList) {
					if (workItem.getStatus().equals(status)) {
						workItemListFinal.add(workItem);
					}
				}
				return workItemListFinal;
			}

		});
		workItemService.getAllByStatus(WorkItemStatus.Done);
		List<WorkItem> workItem = workItemService.getAllByStatus(WorkItemStatus.Done);
		assertEquals(workItem.size(), 3);

	}

	@Test
	public void canGetAllByTeamTest() throws RepositoryException {
		User user1 = new User("01", "Irina", "Fatkoulin", "irinafatkoulin", "5", "Active");
		User user2 = new User("02", "Irina", "Fatk", "irina", "10", "Active");

		List<WorkItem> workItemList = new ArrayList<WorkItem>();
		for (int i = 0; i < 2; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Started", user1.getId(), null));
		}

		for (int i = 0; i < 3; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Done", user2.getId(), null));
		}
		when(workItemRepository.getAllByTeam("10")).thenAnswer(new Answer<List<WorkItem>>() {

			@Override
			public List<WorkItem> answer(InvocationOnMock invocation) throws Throwable {
				List<WorkItem> workItemListFinal = new ArrayList<>();
				String teamId = (String) invocation.getArguments()[0];
				List<User> userList = new ArrayList<>();
				userList.add(user1);
				userList.add(user2);

				for (WorkItem workItem : workItemList) {
					for (User user : userList) {
						if (user.getTeamid().equals(teamId)) {
							if (workItem.getUserId().equals(user.getId())) {
								workItemListFinal.add(workItem);
							}
						}
					}
				}
				return workItemListFinal;
			}

		});
		workItemService.getAllByTeam("10");
		List<WorkItem> workItem = workItemService.getAllByTeam("10");
		assertEquals(workItem.size(), 3);
	}

	@Test
	public void canGetAllByUserTest() throws RepositoryException {
		User user1 = new User("01", "Irina", "Fatkoulin", "irinafatkoulin", "01", "Active");
		User user2 = new User("02", "Irina", "Fatk", "irina", "02", "Active");

		List<WorkItem> workItemList = new ArrayList<WorkItem>();
		for (int i = 0; i < 2; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Started", user1.getId(), null));
		}

		for (int i = 0; i < 3; i++) {
			workItemList.add(new WorkItem(Integer.toString(i), "WorkItem " + i, "Done", user2.getId(), null));
		}
		when(workItemRepository.getAllByUser("02")).thenAnswer(new Answer<List<WorkItem>>() {

			@Override
			public List<WorkItem> answer(InvocationOnMock invocation) throws Throwable {
				List<WorkItem> workItemListFinal = new ArrayList<>();
				String userId = (String) invocation.getArguments()[0];
				for (WorkItem workItem : workItemList) {
					if (workItem.getUserId().equals(userId)) {
						workItemListFinal.add(workItem);
					}
				}
				return workItemListFinal;
			}

		});
		workItemService.getAllByUser("02");
		List<WorkItem> workItem = workItemService.getAllByUser("02");
		assertEquals(workItem.size(), 3);
	}

}
