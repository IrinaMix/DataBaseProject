package se.plushogskolan.database.repository.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se.plushogskolan.database.model.Issue;
import se.plushogskolan.database.model.WorkItem;
import se.plushogskolan.database.repository.IssueRepository;
import se.plushogskolan.database.repository.RepositoryException;

public final class MySQLIssueRepository implements IssueRepository {

	private final String URL = "jdbc:mysql://localhost:3306/DatabaseProject?useSSL=false";
	private final String DB_USER = "awesome";
	private final String DB_PASSWORD = "database";

	@Override
	public void createIssueAndAssignToWorkItem(Issue issue, String itemId) throws RepositoryException {
		String create_issue_sql = "insert into issue values(?,?)";
		String assign_issue_sql = "update workitem set issueid=? where id=?";

		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
			conn.setAutoCommit(false);
			try (PreparedStatement insertStatement = conn.prepareStatement(create_issue_sql);
					PreparedStatement updateStatement = conn.prepareStatement(assign_issue_sql)) {

				insertStatement.setString(1, issue.getId());
				insertStatement.setString(2, issue.getDescription());
				updateStatement.setString(1, issue.getId());
				updateStatement.setString(2, itemId);
				insertStatement.executeUpdate();
				updateStatement.executeUpdate();
				conn.commit();

			} catch (SQLException e) {
				conn.rollback();
				throw new RepositoryException("Can not create issue and assign to work item with id:" + itemId, e);
			}
		} catch (SQLException e) {

			throw new RepositoryException("Can not connect to issue repository ", e);
		}
	}

	@Override
	public void updateIssue(Issue issue, String new_description) throws RepositoryException {
		String sql = "update issue set description = ? where description = ?";
		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
			conn.setAutoCommit(false);
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, new_description);
				statement.setString(2, issue.getDescription());
				statement.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw new RepositoryException("Can not update issue.", e);
			}

		} catch (SQLException e) {
			throw new RepositoryException(
					"Can not update description:" + issue.getDescription() + " to:" + new_description, e);
		}
	}

	@Override
	public List<WorkItem> getAllItemsWithIssue() throws RepositoryException {
		String sql = "select * from workitem where issueid is not null";
		List<WorkItem> itemList = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
				Statement statement = conn.createStatement();) {

			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String id = result.getString("id");
				String title = result.getString("title");
				String status = result.getString("itemstatus");
				String userid = result.getString("userid");
				String issueid = result.getString("issueid");
				WorkItem workitem = new WorkItem(id, title, status, userid, issueid);
				itemList.add(workitem);
			}

		} catch (SQLException e) {
			throw new RepositoryException("Can not get all items with issue.", e);
		}
		return itemList;
	}

	@Override
	public boolean exists(Issue issue) throws RepositoryException {
		String sql = "select * from issue where description = ?";
		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
				PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, issue.getDescription());
			ResultSet result = statement.executeQuery();
			return result.next();
		} catch (SQLException e) {
			throw new RepositoryException("Can not get issue with descriprtion:" + issue.getDescription(), e);
		}

	}

	@Override
	public void createIssue(Issue issue) throws RepositoryException {
		String create_issue_sql = "insert into issue values(?,?)";

		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
			conn.setAutoCommit(false);
			try (PreparedStatement insertStatement = conn.prepareStatement(create_issue_sql)) {

				insertStatement.setString(1, issue.getId());
				insertStatement.setString(2, issue.getDescription());
				insertStatement.executeUpdate();
				conn.commit();

			} catch (SQLException e) {
				conn.rollback();
				throw new RepositoryException("Can not create issue with description:" + issue.getDescription(), e);
			}
		} catch (SQLException e) {

			throw new RepositoryException("Can not connect to issue repository ", e);
		}

	}

	@Override
	public void assignToWorkItem(Issue issue, String itemid) throws RepositoryException {
		String assign_issue_sql = "update workitem set issueid=? where id=?";

		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)) {
			conn.setAutoCommit(false);
			try (PreparedStatement updateStatement = conn.prepareStatement(assign_issue_sql)) {

				updateStatement.setString(1, issue.getId());
				updateStatement.setString(2, itemid);
				updateStatement.executeUpdate();
				conn.commit();

			} catch (SQLException e) {
				conn.rollback();
				throw new RepositoryException("Can not assign to work item with id:" + itemid, e);
			}
		} catch (SQLException e) {

			throw new RepositoryException("Can not connect to issue repository ", e);
		}

	}

	@Override
	public Issue getIssueByName(String name) throws RepositoryException {
		String sql = "select * from issue where description =?";
		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
				PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, name);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return new Issue(result.getString(1), result.getString(2));
			}

		} catch (SQLException e) {

			throw new RepositoryException("Can not get issue by name:" + name, e);
		}
		return null;
	}
}
