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

class RemoveBetDATest {
	
	TestUtilityDataAccess DA = new TestUtilityDataAccess();
	
	/**
	 * Este test demuestra que cuando se da un User nulo, el método lanza un NullPointerException
	 */
	@Test
	@DisplayName("Test 1: User == null")
	void removeBetTest1() {
		DA.open(true);
		assertThrows(NullPointerException.class, () -> DA.removeBet(null, new Bet(new Question(), 200, 1.2, null))); // Cuando User == null, se lanza un NullPointerException
		DA.close();
	}
	
	/**
	 * Este test demuestra que cuando se da un Bet nulo, el método lanza un NullPointerException
	 */
	@Test
	@DisplayName("Test 2: Bet == null")
	void removeBetTest2() {
		DA.open(true);
		assertThrows(NullPointerException.class, () -> DA.removeBet(new User(), null)); // Cuando Bet == null, se lanza un NullPointerException
		DA.close();
	}
	
	/**
	 * Este test demuestra que cuando un usuario no está en la base de datos, el método lanza una excepción de NullPointerException
	 */
	@Test
	@DisplayName("Test 3: User not in DB")
	void removeBetTest3() {
		DA.open(true);
		assertThrows(NullPointerException.class, () -> DA.removeBet(new User(), new Bet(new Question(), 200, 1.2, null))); // Cuando User no está en la base de datos, se lanza un NullPointerException
		DA.close();
	}
	
	/**
	 * Este test demuestra que, cuando un usuario no ha realizado apuestas, independientemente de la apuesta indicada a retirar, el método proporciona un resultado desfavorable
	 */
	@Test
	@DisplayName("Test 4: User sin bets")
	void removeBetTest4() {
		// Se establecen las variables
		DA.open(true);
		User usr = new User();
		Bet bet;
		Question q = new Question();
		Event ev;
		
		try {
			String uName = "Ben";
			String pWord = "12345678";
			String mail = "ben@mail.com";
			
			DA.createEvent(100, UtilDate.newDate(2021, 10, 25)); // Se crea un evento y se añade a la base de datos
			ev = DA.getEventWithID(100);
		
			DA.createUser(uName, pWord, mail); // Se crea un usuario y se añade a la base de datos
			usr = DA.getUserWithUsernamePassword(uName, pWord);
			try {
				q = DA.createQuestion(ev, "", 10, QuestionTypes.FREE); // Se crea la pregunta sobre la que se harán las apuestas
			} catch (QuestionAlreadyExist e) { // Se comprueba que la pregunta no exista previamente
				fail("Shouldn't reach here");
			}
		
			bet = new Bet(q, 100, 1.5, ""); // Se crea una apuesta con la que comparar
			assertFalse(DA.removeBet(usr, bet)); // Se certifica que, como el usuario no ha realizado ninguna apuesta, el método devuelve false
		}
		catch(Exception e) { // Se capta cualquier posible error que ocurra durante la ejecución.
			e.printStackTrace();
			fail("Algo malo pasa");
		}
		finally { // Se reestablece la base de datos al estado inicial
			DA.removeUser(usr);
			DA.removeEvent(100);
			DA.close();
		}
	}
	
	/**
	 * Este método demuestra una ejecución erronea en la que, pese a haber realizado varias apuestas, no ha realizado la apuesta especificada a eliminar, obteniendo un resultado no favorable
	 */
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
			
			DA.createEvent(100, UtilDate.newDate(2021, 10, 25)); // Se crea un evento y se añade a la base de datos
			ev = DA.getEventWithID(100);
		
			DA.createUser(uName, pWord, mail); // Se crea un usuario y se añade a la base de datos
			usr = DA.getUserWithUsernamePassword(uName, pWord);
			DA.addMoneyToUser(usr.getId(), 1000); // Se le añade dinero para poder realizar las apuestas correspondientes
			
			try {
				q = DA.createQuestion(ev, "a", 10, QuestionTypes.FREE); // Se crean la pregunta sobre la que se harán las apuestas
				q1 = DA.createQuestion(ev, "b", 10, QuestionTypes.FREE);
				q2 = DA.createQuestion(ev, "c", 10, QuestionTypes.FREE);
			
			} catch (QuestionAlreadyExist e) { // Se comprueba que la pregunta no exista previamente
				fail("Shouldn't reach here");
			}
		
			bet = new Bet(q, 100, 1.5, "c"); // Se crea una apuesta con la que comparar
			DA.placeBet(usr, q1, 100, "a"); // Se crean las apuestas del usuario
			DA.placeBet(usr, q2, 100, "b");
			
			assertFalse(DA.removeBet(usr, bet)); // Se comprueba que no se ha eliminado ninguna apuesta
		}
		catch(Exception e) { // Se capta cualquier posible error que ocurra durante la ejecución.
			e.printStackTrace();
			fail("algo malo pasa");
		}
		finally { // Se reestablece la base de datos al estado inicial
			DA.removeUser(usr);
			DA.removeEvent(100);
			DA.close();
		}
	}
	
	/**
	 * Este test demuestra una ejecución exitosa en la que el usuario retira una apuesta y recibe el immporte correcto.
	 */
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
			
			DA.createEvent(100, UtilDate.newDate(2021, 10, 25)); // Se crea un evento y se añade a la base de datos
			ev = DA.getEventWithID(100);
		
			DA.createUser(uName, pWord, mail); // Se crea un usuario y se añade a la base de datos
			usr = DA.getUserWithUsernamePassword(uName, pWord);
			DA.addMoneyToUser(usr.getId(), 1000); // Se le añade dinero para poder realizar las apuestas correspondientes
			
			try {
				q = DA.createQuestion(ev, "", 10, QuestionTypes.FREE); // Se crea la pregunta sobre la que se harán las apuestas
			} catch (QuestionAlreadyExist e) { // Se comprueba que la pregunta no exista previamente
				fail("Shouldn't reach here");
			}
		
			assertTrue(DA.placeBet(usr, q, 100, "d")); // Se crea la apuesta del usuario
			bet = DA.getUserWithUsernamePassword(uName, pWord).getBets().get(0); // Se guarda la apuesta realizada por el usuario en una variable para comprobar su retiración
			
			assertEquals(900, usr.getWallet().getCurrency()); // Se comprueba el estado del saldo para comparar con el futuro
			assertEquals(1, usr.getBets().size()); // Se compara la cantidad de apuestas realizadas por el usuario
			assertTrue(DA.removeBet(usr, bet)); // Se realiza la operación de forma exitosa
			assertFalse(DA.removeBet(usr, bet)); // Al no tener la apuesta, el método al ejecutarlo esta vez devuelve false
			assertEquals(0, usr.getBets().size()); // Se comprueba que el usuario no tiene más apuestas realizadas
			assertEquals(975, usr.getWallet().getCurrency()); // Se comprueba que el usuario ha recibido la cantidad de dinero adecuada tras la eliminación de la apuesta
		}
		catch(Exception e) { // Se capta cualquier posible error que ocurra durante la ejecución.
			e.printStackTrace();
			fail("Algo malo pasa");
		}
		finally { // Se reestablece la base de datos al estado inicial
			DA.removeUser(usr);
			DA.removeEvent(100);
			DA.close();
		}
	}
}
