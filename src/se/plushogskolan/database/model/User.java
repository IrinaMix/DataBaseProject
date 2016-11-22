package se.plushogskolan.database.model;

import java.util.UUID;

public final class User {
	private final String id;
	private final String firstname;
	private final String lastname;
	private final String username;
	private final String teamid;
	private final String status;

	public User(String id, String firstname, String lastname, String username, String teamid, String status) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.teamid = teamid;
		this.status = status;
	}

	public User(String firstname, String lastname, String username, String teamid) {
		this.id = UUID.randomUUID().toString();
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.teamid = teamid;
		this.status = "Active";
	}

	public String getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getUsername() {
		return username;
	}

	public String getTeamid() {
		return teamid;
	}

	public String getStatus() {
		return status;
	}
}
