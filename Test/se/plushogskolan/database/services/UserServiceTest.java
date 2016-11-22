package se.plushogskolan.database.services;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private WorkItemRepository workItemRepository;
	@InjectMocks
	private UserService userService;

	private static User user1;
	private static User user2;

	@BeforeClass
	public static void beforeClassCreatUser() {
		user1 = new User("01", "Irina", "Fatkoulin", "irinafatkoulin", "01", "Active");
		user2 = new User("02", "Irina", "Fatk", "irina", null, "Active");
	}

	@Test(expected = ServiceException.class)
	public void canNotAddUserWithNameLessThan10Test() throws RepositoryException {
		userService.addUser(user2);
		verify(userRepository).addUser(user2);
	}

	@Test(expected = ServiceException.class)
	public void canNotAddUserToTeamMoreThan10PeopleTest() throws RepositoryException {
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			userList.add(new User(Integer.toString(i), "fn", "ln", "Username_" + Integer.toString(i), "01", "Active"));
		}
		when(userRepository.getAllUsersInTeam("01")).thenReturn(userList);
		userService.addUser(user1);
		verify(userRepository).addUser(user1);
	}

	@Test
	public void canUpdateUserTest() throws RepositoryException {
		User user3 = new User("03", "Test", "Update", "testUpdateUser", "01", "Active");
		when(userRepository.getUserByUsername("irinafatkoulin")).thenReturn(user1);
		userService.updateUser(user3, "irinafatkoulin");
		verify(userRepository).updateUser(user3, "irinafatkoulin");
		assertEquals("testUpdateUser", user3.getUsername());
	}

	@Test
	public void canDeactivateUserTest() throws RepositoryException {

		when(userRepository.getUserByUsername("irinafatkoulin")).thenReturn(user1);
		List<WorkItem> result = new ArrayList<>();
		result.add(new WorkItem("01", "item 1", "Started", user1.getId(), null));
		result.add(new WorkItem("02", "item 2", "Done", user1.getId(), null));
		result.add(new WorkItem("03", "item 3", "UnStarted", user1.getId(), null));

		when(workItemRepository.getAllByUser(user1.getId())).thenReturn(result);

		List<WorkItem> result1 = new ArrayList<>();
		doAnswer(new Answer<List<WorkItem>>() {
			@Override
			public List<WorkItem> answer(InvocationOnMock invocation) throws Throwable {
				String status = (String) invocation.getArguments()[1];
				String itemId = (String) invocation.getArguments()[0];
				result1.add(new WorkItem(itemId, "item" + itemId, status, user1.getId(), null));

				return result1;
			}

		}).when(workItemRepository).changeStatus(Mockito.anyString(), Mockito.eq(WorkItemStatus.Unstarted.toString()));

		doNothing().when(workItemRepository).removeUserId(Mockito.anyString());
		userService.deactivateUser("irinafatkoulin");
		assertEquals(result1.size(), 3);
		assertEquals(result1.get(0).getStatus(), "Unstarted");
		verify(workItemRepository, times(3)).changeStatus(Mockito.anyString(),
				Mockito.eq(WorkItemStatus.Unstarted.toString()));
		verify(workItemRepository, times(3)).removeUserId(Mockito.anyString());
	}

}
