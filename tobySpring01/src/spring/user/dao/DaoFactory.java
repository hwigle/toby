package spring.user.dao;

public class DaoFactory {
	public UserDao userDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();

		return new UserDao(connectionMaker);
	}

	public ConnectionMaker connectionmaker() {
		return new DConnectionMaker();
	}
}
