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

import se.plushogskolan.database.model.Team;
import se.plushogskolan.database.model.User;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.TeamRepository;
import se.plushogskolan.database.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

	@Mock
	private TeamRepository teamRepository;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private TeamService teamService;
	private static Team team;

	@BeforeClass
	public static void beforeClassCreatTeam() throws RepositoryException {
		team = new Team("01", "TeamTest", "Active");
	}

	@Test
	public void canAddTeamTest() throws RepositoryException {
		when(teamRepository.exists(team.getName())).thenReturn(false);
		teamService.addTeam(team);
		verify(teamRepository).addTeam(team);
	}

	@Test
	public void canDeactivateTeamTest() throws RepositoryException {
		when(teamRepository.exists(team.getName())).thenReturn(true);
		teamService.deactivateTeam(team.getName());
		verify(teamRepository).deactivateTeam(team.getName());
	}

	@Test
	public void canGetAllTeamsTest() throws RepositoryException {
		List<Team> teamList = new ArrayList<>();
		Team team2 = new Team("02", "teamnameTest2", "Active");
		Team team3 = new Team("03", "teamnameTest3", "Active");
		teamList.add(team2);
		teamList.add(team3);
		when(teamRepository.getAllTeams()).thenReturn(teamList);
		List<Team> allTeams = teamService.getAllTeams();
		assertEquals(allTeams.size(), teamList.size());
		verify(teamRepository).getAllTeams();
	}

	@Test
	public void canUpdateTeamTest() throws RepositoryException {
		String old_name = "TeamTest";
		String new_name = "TeamTestNewName";
		when(teamRepository.exists(old_name)).thenReturn(true);
		when(teamRepository.exists(new_name)).thenReturn(false);
		teamService.updateTeam(old_name, new_name);
		verify(teamRepository).updateTeam(old_name, new_name);
	}

	@Test
	public void canAddUserToTeamTest() throws RepositoryException {
		when(teamRepository.getTeamById(team.getId())).thenReturn(team);
		when(teamRepository.exists(team.getName())).thenReturn(true);
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			User user = new User(Integer.toString(i), "fn" + Integer.toString(i), "ln" + Integer.toString(i),
					"usernameTest" + Integer.toString(i), team.getId(), "Active");
			userList.add(user);
		}
		when(userRepository.getAllUsersInTeam(team.getId())).thenReturn(userList);
		User user = new User("15", "Test", "TestLastName", "TestUserName", null, "Active");
		teamService.addUserToTeam(user.getId(), team.getId());
		verify(teamRepository).addUserToTeam(user.getId(), team.getId());

	}

	@Test(expected = ServiceException.class)
	public void canNotAddUserToTeamIfMoreThen10MembersTest() throws RepositoryException {
		when(teamRepository.getTeamById(team.getId())).thenReturn(team);
		when(teamRepository.exists(team.getName())).thenReturn(true);
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			User user = new User(Integer.toString(i), "fn" + Integer.toString(i), "ln" + Integer.toString(i),
					"usernameTest" + Integer.toString(i), team.getId(), "Active");
			userList.add(user);
		}
		when(userRepository.getAllUsersInTeam(team.getId())).thenReturn(userList);
		User user = new User("15", "Test", "TestLastName", "TestUserName", null, "Active");
		teamService.addUserToTeam(user.getId(), team.getId());
		verify(teamRepository).addUserToTeam(user.getId(), team.getId());

	}

}
