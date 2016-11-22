package se.plushogskolan.database.main;

import java.util.List;

import se.plushogskolan.database.model.Issue;
import se.plushogskolan.database.model.Team;
import se.plushogskolan.database.model.User;
import se.plushogskolan.database.model.WorkItem;
import se.plushogskolan.database.model.WorkItemStatus;
import se.plushogskolan.database.repository.IssueRepository;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.TeamRepository;
import se.plushogskolan.database.repository.UserRepository;
import se.plushogskolan.database.repository.WorkItemRepository;
import se.plushogskolan.database.repository.mysql.MySQLIssueRepository;
import se.plushogskolan.database.repository.mysql.MySQLTeamRepository;
import se.plushogskolan.database.repository.mysql.MySQLUserRepository;
import se.plushogskolan.database.repository.mysql.MySQLWorkItemRepository;
import se.plushogskolan.database.services.IssueService;
import se.plushogskolan.database.services.TeamService;
import se.plushogskolan.database.services.UserService;
import se.plushogskolan.database.services.WorkItemService;

public class Mian {

	public static void main(String args[]) throws RepositoryException {


		IssueRepository issueRepository = new MySQLIssueRepository();
		TeamRepository teamRepository = new MySQLTeamRepository();
		UserRepository userRepository = new MySQLUserRepository();
		WorkItemRepository workItemRepository = new MySQLWorkItemRepository();


		UserService us = new UserService(userRepository);
		// for (int i=0;i<15;i++){
		// User user=new User("fn"+i, "ln"+i, "usernameTest"+i, null);
		// us.addUser(user);
		// }

		TeamService ts = new TeamService(teamRepository);
		// for (int i=0;i<2;i++){
		// Team team=new Team("teamnameTest"+i);
		// ts.addTeam(team);
		// }
		//

		IssueService is = new IssueService(issueRepository, workItemRepository);
		// for (int i=0;i<6;i++){
		// Issue issue=new Issue("Test issue "+i);
		// is.createIssue(issue);
		// }

		WorkItemService wis = new WorkItemService(workItemRepository, userRepository);
		// for (int i=0;i<6;i++){
		// WorkItem wi=new WorkItem("Work item "+i,null);
		// wis.addWorkItem(wi);
		// }

		// Test all User functions
		// User user= new User("user","update","usernameTest00",null);
		// us.updateUser(user, "usernameTest1");

		// us.deactivateUser("AddtoTeam1");
		// System.out.println(us.getUserById("002d74b9-5cdc-4b1f-be9b-332d5d8f38e5").getUsername());
		// System.out.println(us.getUserByUsername("usernameTest00").getId());

		// Business logic user
		// User user= new User("user","update","username",null);
		// us.addUser(user);

		// Test team functions
		// ts.updateTeam("teamnameTest0", "teamnameTest2");
		// ts.deactivateTeam("teamnameTest1");
		// System.out.println(ts.getAllTeams().size());
		// ts.addUserToTeam("002d74b9-5cdc-4b1f-be9b-332d5d8f38e5",
		// "f31fe7d8-ad7f-4aaa-884b-f6cdb515bf63");

		// Business logic Team
		/*
		 * for (int i=0;i<10;i++){ User user=new User("fn"+i, "ln"+i,
		 * "AddtoTeam"+i,"5a8a8a0e-bdab-4a33-b96f-59d057413db6" );
		 * us.addUser(user); }
		 */
		// ts.addUserToTeam("002d74b9-5cdc-4b1f-be9b-332d5d8f38e5",
		// "5a8a8a0e-bdab-4a33-b96f-59d057413db6");
		// System.out.println(us.getAllUsersInTeam("5a8a8a0e-bdab-4a33-b96f-59d057413db6").size());

		// Test work item functions
		// wis.changeStatus("40c3734e-6091-4cde-bdb1-04f121ecd427",
		// WorkItemStatus.Done);
		// wis.delete("b396056f-8e9a-4ba0-af5a-a430105e8c7e");
		// wis.assignItemToUser("40c3734e-6091-4cde-bdb1-04f121ecd427",
		// "3dbf8e32-a550-46ce-82dc-6ca117202fad");
		// System.out.println(wis.getAllByStatus(WorkItemStatus.Done).size());
		// System.out.println(wis.getAllByTeam("5a8a8a0e-bdab-4a33-b96f-59d057413db6").size());
		// System.out.println(wis.getAllByUser("3dbf8e32-a550-46ce-82dc-6ca117202fad").size());

		// Business logic work item
		// us.deactivateUser("AddtoTeam0");
		// wis.assignItemToUser("40c3734e-6091-4cde-bdb1-04f121ecd427",
		// "3dbf8e32-a550-46ce-82dc-6ca117202fad");
		/*
		 * for (int i=0;i<5;i++){ WorkItem wi=new
		 * WorkItem("Assign to user "+i,"51f148c4-411d-4999-9a30-a8af43f15fd1");
		 * wis.addWorkItem(wi); }
		 */
		// wis.assignItemToUser("40c3734e-6091-4cde-bdb1-04f121ecd427",
		// "51f148c4-411d-4999-9a30-a8af43f15fd1");

		wis.changeStatus("1375d1b0-4115-4a95-bb33-e59f8fd4667f", WorkItemStatus.Done);
		// Test issue functions
		// is.updateIssue(is.getIssueByName("Test issue 2"), "Changed issue
		// name");
		 System.out.println(is.getAllItemsWithIssue().size());

		// Test issue business logic
		// wis.changeStatus("0bf34c6e-93ba-4d8d-a98a-bbe16b958326",
		// WorkItemStatus.Done);
		// is.assignToWorkItem(is.getIssueByName("Test issue 0"),
		// "0bf34c6e-93ba-4d8d-a98a-bbe16b958326");

	}
}