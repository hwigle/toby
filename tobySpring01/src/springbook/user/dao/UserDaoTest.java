package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")	//어노테이션 추가
public class UserDaoTest {
	@Autowired UserDao dao; 
	@Autowired DataSource dataSource;
	private User user1;
	private User user2;
	private User user3;
	@Before
	public void setUp() {
		this.user1 = new User("gyumee", "진휘용", "springno1", Level.BASIC, 1, 0);
		this.user2 = new User("leegw700", "진휘", "springno2", Level.SILVER, 55, 10);
		this.user3 = new User("bumjin", "휘용", "springno3", Level.GOLD, 100, 40);
	}

	/*
	 * @Before // JUnit이 제공하는 애노테이션, @Test 메소드가 실행되기전 먼저 실행되야 하는 메소드 정의 public void
	 * setUp() { ApplicationContext context = new
	 * GenericXmlApplicationContext("applicationContext.xml"); //DaoFactory 설정정보를
	 * 생성자의 인자로 줌 this.dao = context.getBean("userDao", UserDaoJdbc.class);
	 * //getBean(메소드명, 리턴타입)
	 * 
	 * this.user1 = new User("a1", "kim", "1234"); this.user2 = new User("a2",
	 * "kim", "1234"); this.user3 = new User("a3", "kim", "1234"); }
	 */
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}
		catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			
			DataAccessException transEx = set.translate(null, null, sqlEx);
			assertThat(transEx, is(DuplicateKeyException.class));
		}
	}
	@Test(expected=DuplicateKeyException.class)
	public void duplicateKey() {
	    dao.deleteAll();
	    dao.add(user1);
	    dao.add(user1);
	}
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		
		dao.deleteAll(); // 최초에 데이터 초기화
		assertThat(dao.getCount(), is(0)); // 데이터 개수 0 인지 확인
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
    }
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
	
	@Test(expected = EmptyResultDataAccessException.class) // 테스트 중에 발생할 것으로 기대하는 예외 클래스 지정
	public void getUserFailure() throws SQLException{

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown.id"); // 이 메소드 실행 중에 예외가 발생 -> 예외 미발생시 테스트 실패
	}
	
    //@Test어노테이션 메소드를 실행하기 위해 최초 main 작성
    public static void main(String[] args) {
        JUnitCore.main("springbook.user.dao.UserDaoTest");
    }
}
