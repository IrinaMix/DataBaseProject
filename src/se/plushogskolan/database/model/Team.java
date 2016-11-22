package se.plushogskolan.database.model;

import java.util.UUID;

public final class Team {
	private final String id;
	private final String name;
	private final String status;

	public Team(String name) {

		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.status = "Active";
	}

	public Team(String id, String name, String status) {

		this.id = id;
		this.name = name;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

}
