package removeBetTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import domain.Bet;
import domain.Question;
import domain.User;
import removeBetUtility.TestUtilityDataAccess;
import removeBetUtility.TestUtilityFacadeImplementation;

class RemoveBetBLMockTest {
	
	TestUtilityDataAccess MockDA = Mockito.mock(TestUtilityDataAccess.class);
	TestUtilityFacadeImplementation sut = new TestUtilityFacadeImplementation(MockDA);

	@Test
	@DisplayName("Test1: user == null")
	void test1() {
		sut.user = null;
		Bet bet = new Bet(new Question(), 100, 1.5, "");
		
		Mockito.doThrow(new NullPointerException()).when(MockDA).removeBet(null, bet);
		
		assertThrows(NullPointerException.class, () -> sut.removeBet(bet));
		
		Mockito.verify(MockDA, Mockito.times(1)).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
	}
	
	@Test
	@DisplayName("Test2: Bet == null")
	void test2() {
		sut.user = new User();
		Bet bet = null;
		
		Mockito.doThrow(new NullPointerException()).when(MockDA).removeBet(sut.user, null);
		
		assertThrows(NullPointerException.class, () -> sut.removeBet(bet));
		
		Mockito.verify(MockDA, Mockito.times(1)).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
	}
	
	
	@Test
	@DisplayName("Test3: User not in DB")
	void test3() {
		sut.user = new User();
		Bet bet = new Bet(new Question(), 100, 1.5, "");
		
		Mockito.doThrow(new NullPointerException()).when(MockDA).getUserWithUsernamePassword(Mockito.anyString(), Mockito.anyString());
		
		assertThrows(NullPointerException.class, () -> sut.removeBet(bet));
		
		Mockito.verify(MockDA, Mockito.times(1)).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
	}
	
	
	@Test
	@DisplayName("Test4: User no tiene bet realizadas")
	void test4() {
		sut.user = new User();
		Bet bet = new Bet(new Question(), 100, 1.5, "");
		
		Mockito.doReturn(false).when(MockDA).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
		
		assertFalse(sut.removeBet(bet));
		
		Mockito.verify(MockDA, Mockito.times(1)).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
	}
	
	
	@Test
	@DisplayName("Test5: User no ha realizado la bet a retirar")
	void test5() {
		sut.user = new User("", "", "");
		Question q1 = new Question();
		Question q2 = new Question();
		Question q3 = new Question();
		q1.setQuestionNumber(1);
		q2.setQuestionNumber(2);
		q3.setQuestionNumber(3);
		Bet bet = new Bet(q1, 100, 1.5, "d");
		sut.user.increaseCurrency(2000);
		sut.user.placeBet(q2, 200, "a");
		sut.user.placeBet(q3, 200, "b");
		
		Mockito.doReturn(false).when(MockDA).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
		
		assertFalse(sut.removeBet(bet));
		
		Mockito.verify(MockDA, Mockito.times(1)).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
	}
	
	
	@Test
	@DisplayName("Test6: Apuesta retirada de forma exitosa")
	void test6() {
		sut.user = new User("", "", ""); // Se crea un nuevo usuario con cartera inicializada
		Question q1 = new Question(); // Se crean y preparan las preguntas a utilizar
		Question q2 = new Question();
		q1.setQuestionNumber(1);
		q2.setQuestionNumber(2);
		Bet bet = new Bet(q1, 100, 1.5, "d"); // Se crea una apuesta de referencia
		sut.user.increaseCurrency(2000); // Se establece un dinero
		sut.user.placeBet(q2, 200, "a"); // Se crean más apuestas
		sut.user.placeBet(q1, 200, "b");
		
		Mockito.doReturn(true).when(MockDA).removeBet(Mockito.any(User.class), Mockito.any(Bet.class)); // Se programa el comportamiento de la aplicación
		
		assertTrue(sut.removeBet(bet)); // Se prueba la ejecución
		
		Mockito.verify(MockDA, Mockito.times(1)).removeBet(Mockito.any(User.class), Mockito.any(Bet.class));
	}

}
