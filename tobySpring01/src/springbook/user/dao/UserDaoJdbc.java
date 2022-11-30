package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao{
	// 주입될 의존 오브젝트 타입
	private DataSource dataSource;
	// 생성자 방식 DI
	// public UserDao(ConnectionMaker connectionMaker){
	// this.connectionMaker = connectionMaker;
	// }
	
	private JdbcTemplate jdbcTemplate;
	
	// 수정자 메소드 방식 DI
	public void setDataSource(DataSource dataSource) { // 수정자 메소드이면서 
													   // JdbcContext에 대한 생성 DI 작업을 동시에 수행
		this.jdbcTemplate = new JdbcTemplate(dataSource); // JdbcContext 생성(IoC)
		
		this.dataSource = dataSource; // 아직 JdbcContext를 적용하지 않은 메소드를 위해 저장
		
	}
	
	
	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getInt("level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommend"));
				return user;
			}
		};
	// jdbc 작업 흐름 분리해서 만든 클래스
	//private JdbcContext jdbcContext;

	
	public void add(User user) {
		this.jdbcTemplate.update(
				"insert into users(id, name, password, level, login, recommend) " + "values(?,?,?,?,?,?)", user.getId(),
				user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?",
				new Object[] {id}, this.userMapper);
	} 

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",this.userMapper);
	}
}
