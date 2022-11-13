package springbook.user.dao;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")	//어노테이션 추가
public class UserDaoTest {
	@Autowired
	private ApplicationContext context; // 실행 시 인스턴스가 이 변수에 값 자동 주입
	private UserDao dao; // setUp() 메서드에서 만드는 오브젝트를 테스트 메소드에서
						 // 사용할 수 있도록 인스턴스 변수로 선언
	private User user1;
	private User user2;
	private User user3;
	
	@Before // JUnit이 제공하는 애노테이션, @Test 메소드가 실행되기전 먼저 실행되야 하는 메소드 정의
	public void setUp() {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	//DaoFactory 설정정보를 생성자의 인자로 줌
		this.dao = context.getBean("userDao", UserDao.class);	//getBean(메소드명, 리턴타입)
		
		this.user1 = new User("a1", "kim", "1234");
		this.user2 = new User("a2", "kim", "1234");
		this.user3 = new User("a3", "kim", "1234");
	}
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		
		dao.deleteAll(); // 최초에 데이터 초기화
		assertThat(dao.getCount(), is(0)); // 데이터 개수 0 인지 확인
		
		dao.add(user1);
		dao.add(user2);
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
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
