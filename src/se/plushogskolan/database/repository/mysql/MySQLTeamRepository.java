package se.plushogskolan.database.repository.mysql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import se.plushogskolan.database.model.Team;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.TeamRepository;

public final class MySQLTeamRepository implements TeamRepository {

	private final String url = "jdbc:mysql://localhost:3306/DatabaseProject?useSSL=false";
	private final String DB_USER = "awesome";
	private final String DB_PASSWORD = "database";

	@Override
	public void addTeam(Team team) throws RepositoryException {

		String insert = "INSERT INTO Team ( id, teamname, teamstatus) VALUES (?,?,?)";
		try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(insert)) {

				statement.setString(1, team.getId());
				statement.setString(2, team.getName());
				statement.setString(3, team.getStatus());
				statement.executeUpdate();
				connection.commit();

			} catch (SQLException e) {
				connection.rollback();
				throw new RepositoryException("Can not add team:" + team.getName(), e);
			}

		} catch (SQLException e) {
			throw new RepositoryException("Can not connect to team repository", e);
		}
	}

	@Override
	public void updateTeam(String oldName, String newName) throws RepositoryException {

		String update = "UPDATE Team SET teamname = ? WHERE teamname = ?";
		try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(update)) {
				statement.setString(1, newName);
				statement.setString(2, oldName);
				statement.executeUpdate();
				connection.commit();

			} catch (SQLException e) {
				connection.rollback();
				throw new RepositoryException("Can not update team " + oldName, e);

			}
		} catch (SQLException e) {
			throw new RepositoryException("Can not connect to team repository", e);
		}
	}

	@Override
	public void deactivateTeam(String name) throws RepositoryException {

		String deactivate = "UPDATE Team SET teamstatus = ? WHERE teamname = ?";
		try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(deactivate)) {

				statement.setString(1, "Inactive");
				statement.setString(2, name);
				statement.executeUpdate();
				connection.commit();

			} catch (SQLException e) {
				connection.rollback();
				throw new RepositoryException("Can not deactivate team:", e);

			}

		} catch (SQLException e) {
			throw new RepositoryException("Can not connect to team repository", e);
		}
	}

	@Override
	public List<Team> getAllTeams() throws RepositoryException {
		String sql = "select * from Team";
		List<Team> teamList = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
				Statement statement = connection.createStatement();) {

			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String id = result.getString("id");
				String teamName = result.getString("teamname");
				String status = result.getString("teamstatus");
				Team team = new Team(id, teamName, status);
				teamList.add(team);
			}

		} catch (SQLException e) {
			throw new RepositoryException("Can not get all teams", e);
		}
		return teamList;
	}

	@Override
	public void addUserToTeam(String userid, String teamid) throws RepositoryException {

		String addUserToTeam = "UPDATE User SET teamid = ? WHERE id = ?";
		try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)) {
			connection.setAutoCommit(false);
			try (PreparedStatement statement = connection.prepareStatement(addUserToTeam)) {
				statement.setString(1, teamid);
				statement.setString(2, userid);
				statement.executeUpdate();
				connection.commit();

			} catch (SQLException e) {
				connection.rollback();
				throw new RepositoryException("Can not add user " + userid + " to  team: " + teamid, e);

			}
		} catch (SQLException e) {
			throw new RepositoryException("Can not connect to team repository", e);
		}
	}

	@Override
	public boolean exists(String teamname) throws RepositoryException {
		try {
			String checkIfTeamExists = "select * from Team where teamname=?";
			try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
					PreparedStatement statement = connection.prepareStatement(checkIfTeamExists)) {
				statement.setString(1, teamname);
				ResultSet result = statement.executeQuery();
				return result.next();

			}
		} catch (SQLException e) {
			throw new RepositoryException("Can not get  " + teamname + " because it not  exists", e);
		}

	}

	@Override
	public Team getTeamById(String teamId) throws RepositoryException {
		try {
			String checkIfTeamExists = "select * from Team where id=?";
			try (Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
					PreparedStatement statement = connection.prepareStatement(checkIfTeamExists)) {
				statement.setString(1, teamId);
				ResultSet result = statement.executeQuery();
				while (result.next()) {
					String id = result.getString("id");
					String teamName = result.getString("teamname");
					String status = result.getString("teamstatus");
					Team team = new Team(id, teamName, status);
					return team;
				}

			}
		} catch (SQLException e) {
			throw new RepositoryException("Can not get  " + teamId, e);
		}
		return null;
	}

}
