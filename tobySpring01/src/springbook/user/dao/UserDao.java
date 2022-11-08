package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbook.user.domain.User;

public class UserDao {
	//DB 커넥션 관심을 인터페이스로 추상화하여 사용
	private ConnectionMaker connectionMaker;
	
	public UserDao(ConnectionMaker connectionMaker) {
		//UserDao 생성시 파라미터로 넘어온 ConnectionMaker 타입의 클래스를 넣어줌
		this.connectionMaker = connectionMaker;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = connectionMaker.makeConnection();
		// SQL을 담을 PreparedStatement 만들기
		PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) value(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		// Statement 실행
		ps.executeUpdate();

		// 리소스 닫아주기
		ps.close();
		c.close();
	}

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = connectionMaker.makeConnection();

		// SQL을 담을 PreparedStatement 만들기
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		// 실행결과를 ResultSet에 담기
		ResultSet rs = ps.executeQuery();
		rs.next();

		// 정보를 User객체에 담기
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));

		// 리소스 닫아주기
		rs.close();
		ps.close();
		c.close();

		return user;
	}
	
	//DB 연결과 관련된 관심사 코드 분리
	//public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
