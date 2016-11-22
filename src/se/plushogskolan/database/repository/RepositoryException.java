package se.plushogskolan.database.repository;

public final class RepositoryException extends Exception {

	private static final long serialVersionUID = -4873403939274419488L;

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
