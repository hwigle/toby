package spring.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import spring.user.domain.User;

public class UserDaoConnectionCountingTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		//테스트용 데이터 셋팅
		User user = new User();
		user.setId("countMan4");
		user.setName("kimMinSu");
		user.setPassword("1234");
		
		//dao 함수 호출
		dao.add(user);	//카운팅 1개 추가
		dao.get("countMan2");	//카운팅 1개 추가
		
		//counting 하기
		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());	//Connection counter : 2
	}
}
