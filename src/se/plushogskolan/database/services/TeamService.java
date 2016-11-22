package se.plushogskolan.database.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import se.plushogskolan.database.model.Team;
import se.plushogskolan.database.model.User;
import se.plushogskolan.database.repository.RepositoryException;
import se.plushogskolan.database.repository.TeamRepository;
import se.plushogskolan.database.repository.UserRepository;
import se.plushogskolan.database.repository.mysql.MySQLUserRepository;

public final class TeamService {

	private final TeamRepository teamRepository;
	private final UserRepository userRepository;

	public TeamService(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
		userRepository = new MySQLUserRepository();
	}

	public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
	}

	public void addTeam(Team team) {
		try {

			if (!teamRepository.exists(team.getName())) {
				teamRepository.addTeam(team);

			} else {
				throw new ServiceException("Team with this teamname already exists");
			}

		} catch (RepositoryException e) {
			throw new ServiceException("Could not add team: " + team.getName(), e);
		}

	}

	public void deactivateTeam(String name) {
		try {

			if (teamRepository.exists(name)) {
				teamRepository.deactivateTeam(name);

			} else {
				throw new ServiceException("Team with this teamname NOT exists");
			}

		} catch (RepositoryException e) {
			throw new ServiceException("Could not deactivate team: " + name, e);
		}

	}

	public List<Team> getAllTeams() {
		try {
			List<Team> teamList = teamRepository.getAllTeams();

			return teamList;

		} catch (RepositoryException e) {
			throw new ServiceException("Could not get all teams ", e);
		}
	}

	public void updateTeam(String oldName, String newName) {
		try {

			if (teamRepository.exists(oldName)) {

				if (!teamRepository.exists(newName)) {
					teamRepository.updateTeam(oldName, newName);
				} else {
					throw new ServiceException("Team with this teamname " + newName + " exists");

				}
			} else {
				throw new ServiceException("Team with this teamname NOT exists");
			}

		} catch (RepositoryException e) {
			throw new ServiceException("Could not update team", e);
		}

	}

	public void addUserToTeam(String userid, String teamid) {
		try {

			String name = teamRepository.getTeamById(teamid).getName();
			if (teamRepository.exists(name)) {

				List<User> users = userRepository.getAllUsersInTeam(teamid);
				if (users.size() < 10) {
					this.teamRepository.addUserToTeam(userid, teamid);

				} else {
					throw new ServiceException(
							"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
				}

			} else {
				throw new ServiceException("Team with this teamId NOT exists");
			}

		} catch (RepositoryException e) {
			throw new ServiceException("Could not update team", e);
		}

	}

	public Team getTeamById(String teamId) throws RepositoryException {
		try {
			Team team = teamRepository.getTeamById(teamId);
			return team;

		} catch (RepositoryException e) {
			throw new RepositoryException("Can not get  " + teamId, e);
		}

	}

}
