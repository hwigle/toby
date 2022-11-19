package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

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
	
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;
             
        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        } finally {
            if(ps!=null) {try {ps.close();} catch(SQLException e) {}}
            if(c!=null) {try {ps.close();} catch(SQLException e) {}}
        }
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
	// 데이터를 찾지 못하면 예외를 발생시키도록 수정한 get() 메소드	
	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();

		// SQL을 담을 PreparedStatement 만들기
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		// 실행결과를 ResultSet에 담기
		ResultSet rs = ps.executeQuery();
		
		User user = null; // User는 null 상태로 초기화
	    if(rs.next()){ // id를 조건으로 한 쿼리의 결과가 있으면 User 오브젝트를 만들고 값을 넣는다.
	        user = new User();
	        user.setId(rs.getString("id"));
	        user.setName(rs.getString("name"));
	        user.setPassword(rs.getString("password"));
	    }

		// 리소스 닫아주기
		rs.close();
		ps.close();
		c.close();
		
		if(user == null) throw new EmptyResultDataAccessException(1);
		
		return user;
	}
	
	public void deleteAll() throws SQLException {
	    StatementStrategy st = new DeleteAllStatement();    //전략 인스턴스 생성
	    jdbcContextWithStatementStrategy(st);    //컨텍스트에 전략 인스턴스 인자로 호출
	}
	    
	public int getCount() throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try { // 예외 발생 가능성이 있는 코드를 try 블록으로 묶는다.

			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			
			rs = ps.executeQuery(); // ResultSet도 다양한 SQL Exception이 
			rs.next();				// 발생할 수 있는 코드이므 try 블록 안에 둬야 한다.
			return rs.getInt(1);
			
		} catch (SQLException e) { 
			throw e; 
		} finally { 
			if (rs != null) { //만들어진 ResultSet을 닫아주는 기능
				try {
					rs.close(); // close()는 만들어진 순서의 반대로 하는 것이 원칙
				} catch (SQLException e) { 
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) { 
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	//DB 연결과 관련된 관심사 코드 분리
	//public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
