package removeBetTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import configuration.UtilDate;
import domain.Bet;
import domain.Event;
import domain.Question;
import domain.User;
import enums.QuestionTypes;
import exceptions.QuestionAlreadyExist;
import removeBetUtility.TestUtilityDataAccess;

class RemoveBetTest {
	
	TestUtilityDataAccess DA = new TestUtilityDataAccess();

	@Test
	@DisplayName("Test 1: User == null")
	void removeBetTest1() {
		DA.open(true);
		assertThrows(NullPointerException.class, () -> DA.removeBet(null, new Bet(new Question(), 200, 1.2, null)));
		DA.close();
	}
	
	@Test
	@DisplayName("Test 2: Bet == null")
	void removeBetTest2() {
		DA.open(true);
		assertThrows(NullPointerException.class, () -> DA.removeBet(new User(), null));
		DA.close();
	}
	
	@Test
	@DisplayName("Test 3: User not in DB")
	void removeBetTest3() {
		DA.open(true);
		assertThrows(NullPointerException.class, () -> DA.removeBet(new User(), new Bet(new Question(), 200, 1.2, null)));
		DA.close();
	}
	
	@Test
	@DisplayName("Test 4: User sin bets")
	void removeBetTest4() {
		DA.open(true);
		User usr = new User();
		Bet bet;
		Question q = new Question();
		Event ev;
		
		try {
			String uName = "Ben";
			String pWord = "12345678";
			String mail = "ben@mail.com";
			
			DA.createEvent(100, UtilDate.newDate(2021, 10, 25));
			ev = DA.getEventWithID(100);
		
			DA.createUser(uName, pWord, mail);
			usr = DA.getUserWithUsernamePassword(uName, pWord);
			try {
				q = DA.createQuestion(ev, "", 10, QuestionTypes.FREE);
			} catch (QuestionAlreadyExist e) {
				fail("Shouldn't reach here");
			}
		
			bet = new Bet(q, 100, 1.5, "");
			assertFalse(DA.removeBet(usr, bet));
		}
		catch(Exception e) {
			e.printStackTrace();
			fail("Algo malo pasa");
		}
		finally {
			DA.removeUser(usr);
			DA.removeEvent(100);
			DA.close();
		}
	}
	
	@Test
	@DisplayName("Test 5: User no ha realizado la bet a borrar")
	void removeBetTest5() {
		DA.open(true);
		User usr = new User();
		Bet bet;
		Event ev;
		
		Question q = new Question();
		Question q1 = new Question();
		Question q2 = new Question();
		
		try {
			String uName = "Ben";
			String pWord = "12345678";
			String mail = "ben@mail.com";
			
			DA.createEvent(100, UtilDate.newDate(2021, 10, 25));
			ev = DA.getEventWithID(100);
		
			DA.createUser(uName, pWord, mail);
			usr = DA.getUserWithUsernamePassword(uName, pWord);
			try {
				q = DA.createQuestion(ev, "a", 10, QuestionTypes.FREE);
				q1 = DA.createQuestion(ev, "b", 10, QuestionTypes.FREE);
				q2 = DA.createQuestion(ev, "c", 10, QuestionTypes.FREE);
			
			} catch (QuestionAlreadyExist e) {
				fail("Shouldn't reach here");
			}
		
			bet = new Bet(q, 100, 1.5, "");
			DA.placeBet(usr, q1, 100, "");
			DA.placeBet(usr, q2, 100, "");
			
			assertFalse(DA.removeBet(usr, bet));
		}
		catch(Exception e) {
			e.printStackTrace();
			fail("algo malo pasa");
		}
		finally {
			DA.removeUser(usr);
			DA.removeEvent(100);
			DA.close();
		}
	}
	
	@Test
	@DisplayName("Test 6: Bet is removed successfully")
	void removeBetTest6() {
		DA.open(true);
		User usr = new User();
		Bet bet;
		Question q = new Question();
		Event ev;
		
		try {
			String uName = "Ben";
			String pWord = "12345678";
			String mail = "ben@mail.com";
			
			DA.createEvent(100, UtilDate.newDate(2021, 10, 25));
			ev = DA.getEventWithID(100);
		
			DA.createUser(uName, pWord, mail);
			usr = DA.getUserWithUsernamePassword(uName, pWord);
			DA.addMoneyToUser(usr.getId(), 1000);
			
			try {
				q = DA.createQuestion(ev, "", 10, QuestionTypes.FREE);
			} catch (QuestionAlreadyExist e) {
				fail("Shouldn't reach here");
			}
		
			assertTrue(DA.placeBet(usr, q, 100, "d"));
			bet = DA.getUserWithUsernamePassword(uName, pWord).getBets().get(0);
			
			assertTrue(usr.getWallet().getCurrency() == 900);
			assertTrue(usr.getBets().size() == 1);
			assertTrue(DA.removeBet(usr, bet));
			assertTrue(usr.getBets().size() == 0);
			assertTrue(usr.getWallet().getCurrency() == 975);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail("Algo malo pasa");
		}
		finally {
			DA.removeUser(usr);
			DA.removeEvent(100);
			DA.close();
		}
	}
}
