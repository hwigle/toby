package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import springbook.user.domain.User;

public class UserDao {
	//주입될 의존 오브젝트 타입
	private DataSource dataSource;
	//생성자 방식 DI
    //public UserDao(ConnectionMaker connectionMaker){
    //	this.connectionMaker = connectionMaker;
    //}
    
    //수정자 메소드 방식 DI
    public void setDataSource(DataSource dataSource){
    	this.dataSource = dataSource;
    }
	
	public void add(User user) throws SQLException {
		// DB 커넥션을 가져오는 코드
		Connection c = dataSource.getConnection();
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

	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();

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
