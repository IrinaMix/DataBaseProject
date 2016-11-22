package se.plushogskolan.database.repository.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se.plushogskolan.database.model.User;
import se.plushogskolan.database.repository.UserRepository;
import se.plushogskolan.database.repository.RepositoryException;;

public final class MySQLUserRepository implements UserRepository {

	private final String URL = "jdbc:mysql://localhost:3306/DatabaseProject?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	@Override
	public void addUser(User user) throws RepositoryException {
		String insert = "INSERT INTO User (id, firstname, lastname, username, userstatus, teamid) "
				+ "VALUES (?,?,?,?,?,?)";
		// ResultSet generatedKeys;

		try (Connection connection = DriverManager.getConnection(URL, "awesome","database")) {

			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
				// Set parameters
				statement.setString(1, user.getId());
				statement.setString(2, user.getFirstname());
				statement.setString(3, user.getLastname());
				statement.setString(4, user.getUsername());
				statement.setString(5, user.getStatus());
				statement.setString(6, user.getTeamid());
				statement.executeUpdate();
				connection.commit();
				// generatedKeys = statement.getGeneratedKeys();
				// notifyMovieListeners(new MyEvent(movie, EVENT_TYPE.ADD));
			} catch (SQLException ex) {
				connection.rollback();
				throw new RepositoryException(
						"Couldn't prepare the statement for adding user \"" + user.getUsername() + "\" to the database",
						ex);
			}
		} catch (SQLException e) {
			throw new RepositoryException("Couldn't add user \"" + user.getUsername() + "\" to the database", e);
		}
		// return generatedKeys;
	}

	@Override
	public User getUserById(String id) throws RepositoryException {
		String query = "select * from User where id = ?";
		User user = null;
		try (Connection connection = DriverManager.getConnection(URL, "awesome", "database")) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				// Set parameters
				statement.setString(1, id);
				ResultSet resultSet = statement.executeQuery();
				connection.commit();
				if (resultSet.next()) {
					String id2 = resultSet.getString("id");
					String firstname = resultSet.getString("firstname");
					String lastname = resultSet.getString("lastname");
					String username = resultSet.getString("username");
					String teamid = resultSet.getString("teamid");
					String status = resultSet.getString("userstatus");
					user = new User(id2, firstname, lastname, username, teamid, status);
				}
			} catch (SQLException ex) {
				connection.rollback();
				throw new RepositoryException(
						"Couldn't prepare the statement for getting user with id: " + id + " from the database", ex);
			}
		} catch (SQLException e) {
			throw new RepositoryException("Couldn't get user with id: " + id + " from the database", e);
		}
		return user;
	}

	@Override
	public User getUserByUsername(String username) throws RepositoryException {
		String query = "select * from User where username = ?";
		User user = null;
		try (Connection connection = DriverManager.getConnection(URL, "awesome", "database")) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, username);
				ResultSet resultSet = statement.executeQuery();
				connection.commit();
				// generatedKeys = statement.getGeneratedKeys();
				// notifyMovieListeners(new MyEvent(movie, EVENT_TYPE.ADD));
				if (resultSet.next()) {
					String id = resultSet.getString("id");
					String firstname = resultSet.getString("firstname");
					String lastname = resultSet.getString("lastname");
					String uname = resultSet.getString("username");
					String teamid = resultSet.getString("teamid");
					String status = resultSet.getString("userstatus");
					user = new User(id, firstname, lastname, uname, teamid, status);
				}
			} catch (SQLException ex) {
				connection.rollback();
				throw new RepositoryException("Couldn't prepare the statement for getting user with username: "
						+ username + " from the database", ex);
			}
		} catch (SQLException e) {
			throw new RepositoryException("Couldn't get user with username: " + username + " from the database", e);
		}
		return user;
	}

	@Override
	public List<User> getAllUsersInTeam(String teamid) throws RepositoryException {
		List<User> allUsersInTeam = new ArrayList<>();
		String query = "select * from User where teamid = ?";
		try (Connection connection = DriverManager.getConnection(URL, "awesome", "database")) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, teamid);
				ResultSet resultSet = statement.executeQuery();
				connection.commit();
				// generatedKeys = statement.getGeneratedKeys();
				// notifyMovieListeners(new MyEvent(movie, EVENT_TYPE.ADD));
				while (resultSet.next()) {
					String id = resultSet.getString("id");
					String firstname = resultSet.getString("firstname");
					String lastname = resultSet.getString("lastname");
					String username = resultSet.getString("username");
					String tid = resultSet.getString("teamid");
					String status = resultSet.getString("userstatus");
					allUsersInTeam.add(new User(id, firstname, lastname, username, tid, status));
				}
			} catch (SQLException ex) {
				connection.rollback();
				throw new RepositoryException(
						"Couldn't prepare the statement for getting user with teamid: " + teamid + " from the database",
						ex);
			}
		} catch (SQLException e) {
			throw new RepositoryException("Couldn't get user with teamid: " + teamid + " from the database", e);
		}
		return allUsersInTeam;
	}

	@Override
	public void deactivateUser(String username) throws RepositoryException {

		String update = "UPDATE User SET userstatus ='Inactive' WHERE username = ?";
		try (Connection connection = DriverManager.getConnection(URL, "awesome", "database")) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
				// Set parameters
				statement.setString(1, username);
				statement.executeUpdate();
				connection.commit();
			} catch (SQLException ex) {
				connection.rollback();
				throw new RepositoryException(
						"Couldn't prepare the statement for deactivating user \"" + username + "\" to the database",
						ex);
			}
		} catch (SQLException e) {
			throw new RepositoryException("Couldn't deactivate user \"" + username + "\" to the database", e);
		}
	}

	@Override
	public void updateUser(User user, String oldUsername) throws RepositoryException {
		// we do not update id here, id must not change for the same user!
		String update = "UPDATE User SET firstname = ?, "
				+ "lastname = ?, username = ?, userstatus = ? , teamid = ? WHERE username = ?";
		try (Connection connection = DriverManager.getConnection(URL, "awesome", "database")) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
				// Set parameters
				statement.setString(1, user.getFirstname());
				statement.setString(2, user.getLastname());
				statement.setString(3, user.getUsername());
				statement.setString(4, user.getStatus());
				statement.setString(5, user.getTeamid());
				statement.setString(6, oldUsername);
				statement.executeUpdate();
				connection.commit();
			} catch (SQLException ex) {
				connection.rollback();
				throw new RepositoryException("Couldn't prepare the statement for updating user \"" + user.getUsername()
						+ "\" to the database", ex);
			}
		} catch (SQLException e) {
			throw new RepositoryException("Couldn't update user \"" + user.getUsername() + "\" to the database", e);
		}
	}
}
