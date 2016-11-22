package se.plushogskolan.database.model;

import java.util.UUID;

public final class Issue {
	private final String id;
	private final String description;

	public Issue(String description) {
		this.id = UUID.randomUUID().toString();
		this.description = description;
	}

	public Issue(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
