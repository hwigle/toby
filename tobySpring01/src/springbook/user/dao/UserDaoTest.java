package springbook.user.dao;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	//DaoFactory 설정정보를 생성자의 인자로 줌
		
		UserDao dao = context.getBean("userDao", UserDao.class);	//getBean(메소드명, 리턴타입)
		
		dao.deleteAll(); // 최초에 데이터 초기화
		assertThat(dao.getCount(), is(0)); // 데이터 개수 0 인지 확인
		//테스트
		User user = new User();
		user.setId("ka11254");
		user.setName("kimminsu");
		user.setPassword("1234");
		
		// dao 함수 호출
		dao.add(user);
		assertThat(dao.getCount(), is(1)); // add() 이후 데이터 개수 1개 인지 확인
		
		System.out.println(user.getId() + " 등록 완료");
		
		User user2 = dao.get(user.getId());
		
		 //if조건비교와 같은 기능의 assertThat
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
    }
	
    //@Test어노테이션 메소드를 실행하기 위해 최초 main 작성
    public static void main(String[] args) {
        JUnitCore.main("springbook.user.dao.UserDaoTest");
    }
}
