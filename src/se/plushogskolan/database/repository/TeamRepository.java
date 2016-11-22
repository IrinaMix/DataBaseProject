package se.plushogskolan.database.repository;

import java.util.List;

import se.plushogskolan.database.model.Team;

public interface TeamRepository {

	void addTeam(Team team) throws RepositoryException;

	void deactivateTeam(String name) throws RepositoryException;

	List<Team> getAllTeams() throws RepositoryException;

	void addUserToTeam(String userid, String teamid) throws RepositoryException;

	void updateTeam(String oldName, String newName) throws RepositoryException;

	public boolean exists(String teamname) throws RepositoryException;

	public Team getTeamById(String teamId) throws RepositoryException;

}
