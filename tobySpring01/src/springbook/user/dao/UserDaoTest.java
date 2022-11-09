package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");	//DaoFactory 설정정보를 생성자의 인자로 줌
		UserDao dao = context.getBean("userDao", UserDao.class);	//getBean(메소드명, 리턴타입)
		
		//테스트
		User user = new User();
		user.setId("ki123254");
		user.setName("kimminsu");
		user.setPassword("1234");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 완료");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
			
		System.out.println(user2.getId() + " 조회 완료");
	}
}
