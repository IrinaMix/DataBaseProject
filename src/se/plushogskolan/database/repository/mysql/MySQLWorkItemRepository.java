package se.plushogskolan.database.repository.mysql;

import se.plushogskolan.database.model.WorkItem;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import se.plushogskolan.database.repository.WorkItemRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//ta bort, ändra status, create
public class MySQLWorkItemRepository implements WorkItemRepository {

	final String devDb = "jdbc:mysql://localhost:3306/DatabaseProject?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	final String username = "awesome";
	final String password = "database";

	private static final String GET_WORKITEM_BY_ID = "SELECT * FROM WorkItem WHERE id=?";
	private static final String CREATE = "INSERT INTO WorkItem VALUES(?,?,?,?,?)";
	private static final String UPDATE_STATUS = "UPDATE WorkItem SET itemstatus=? WHERE id=?";
	// private static final String DELETE = "DELETE FROM WorkItem WHERE id=?";
	private static final String DELETE = "UPDATE WorkItem SET itemstatus='Deleted',userid=null WHERE id=?";
    private static final String UPDATE_USERID_TO_NULL ="UPDATE workitem Set userid = null where id=?";
	private final static String UPDATE_USER = "UPDATE WorkItem set UserId = ? WHERE id = ?";
	private final static String RETURN_ALL_BY_STATUS = "SELECT * FROM WorkItem WHERE itemstatus = ?";
	private final static String RETURN_ALL_BY_TEAM = "SELECT Team.id as TeamId, User.id as UserId, \n"
			+ "WorkItem.id as WorkItemId, WorkItem.title as title, \n" + "WorkItem.itemstatus as WorkItemStatus,\n"
			+ "WorkItem.userid as userid,\n" + "WorkItem.issueid as issueid\n" + "FROM Team \n"
			+ "RIGHT JOIN User ON Team.id = User.teamid \n"
			+ "RIGHT JOIN WorkItem ON WorkItem.userid = User.id WHERE Team.id = ?";
	private final static String RETURN_ALL_BY_USER = "SELECT User.id as UserId, \n"
			+ "WorkItem.id as WorkItemId, WorkItem.title as title, \n" + "WorkItem.itemstatus as WorkItemStatus,\n"
			+ "WorkItem.userid as userid,\n" + "WorkItem.issueid as issueid\n" + "FROM User\n"
			+ "RIGHT JOIN WorkItem ON WorkItem.userid = user.id \n" + "WHERE User.id = ?";

	public void assignItemToUser(String itemId, String userId) {
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			connection.setAutoCommit(false);
			try (PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {
				ps.setString(1, userId);
				ps.setString(2, itemId);
				ps.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				throw new RuntimeException("Error with assigning item to user:" + itemId + " " + e.toString());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to database " + e.toString());
		}
	}

	public List<WorkItem> getAllByStatus(String status) {
		List<WorkItem> li = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			PreparedStatement ps = connection.prepareStatement(RETURN_ALL_BY_STATUS);
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				WorkItem wi = new WorkItem(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				li.add(wi);
			}
			return li;
		} catch (SQLException e) {
			throw new RuntimeException("Could not retrieve work items by status" + " " + e.toString());
		}
	}

	public List<WorkItem> getAllByTeam(String teamId) {
		List<WorkItem> li = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			PreparedStatement ps = connection.prepareStatement(RETURN_ALL_BY_TEAM);
			ps.setString(1, teamId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				WorkItem wi = new WorkItem(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
						rs.getString(7));
				li.add(wi);
			}
			return li;
		} catch (SQLException e) {
			throw new RuntimeException("Could not retrieve work items by team" + " " + e.toString());
		}
	}

	public List<WorkItem> getAllByUser(String userId) {
		List<WorkItem> li = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			PreparedStatement ps = connection.prepareStatement(RETURN_ALL_BY_USER);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				WorkItem wi = new WorkItem(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6));
				li.add(wi);
			}
			return li;
		} catch (SQLException e) {
			throw new RuntimeException("Could not retrieve work items by user" + " " + e.toString());
		}
	}

	public void addWorkItem(WorkItem item) {
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			connection.setAutoCommit(false);
			try (PreparedStatement create = connection.prepareStatement(CREATE)) {
				
				create.setString(1, item.getId());
				create.setString(2, item.getTitle());
				create.setString(3, item.getStatus());
				create.setString(4, item.getUserId());
				create.setString(5, item.getIssueId());
				create.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException("Could not add work item" + " " + e.toString());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to database" + " " + e.toString());
		}
	}

	public void changeStatus(String id, String status) {
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			connection.setAutoCommit(false);
			try (PreparedStatement updateStatus = connection.prepareStatement(UPDATE_STATUS)) {
				updateStatus.setString(2, id);
				updateStatus.setString(1, status);
				updateStatus.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				throw new RuntimeException("Could not change status on work item" + " " + e.toString());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to database" + " " + e.toString());
		}
	}

	public void delete(String id) {
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			connection.setAutoCommit(false);
			try (PreparedStatement delete = connection.prepareStatement(DELETE)) {
				delete.setString(1, id);
				delete.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException("Could not delete work item" + " " + e.toString());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to database" + " " + e.toString());
		}
	}

	@Override
	public WorkItem getById(String id) {
		WorkItem result = null;
		try (Connection connection = DriverManager.getConnection(devDb, username, password)) {
			PreparedStatement getWorkItemById = connection.prepareStatement(GET_WORKITEM_BY_ID);
			getWorkItemById.setString(1, id);
			ResultSet rs = getWorkItemById.executeQuery();
			while (rs.next()) {
				result = new WorkItem(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
			}

		} catch (SQLException e) {
			throw new RuntimeException("Could not get work item" + " " + e.toString());
		}
		return result;
	}
	
	public void removeUserId(String itemId){
		try(Connection connection = DriverManager.getConnection(devDb,username,password)){		
			connection.setAutoCommit(false);
			try (PreparedStatement statement= connection.prepareStatement(UPDATE_USERID_TO_NULL)) {
				statement.setString(1, itemId);
				statement.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException("Could not set userid to null" + " " + e.toString());
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
