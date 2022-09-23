
package co.edu.uniandes.dse.parcialejemplo.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialejemplo.entities.HabitacionEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.services.HabitacionService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(HabitacionService.class)
class HabitacionServiceTest {

	@Autowired
	private HabitacionService habitacionService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<HabitacionEntity> habitacionList = new ArrayList<>();  //si mimso
	private List<HotelEntity> hotelList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from HabitacionEntity");
		entityManager.getEntityManager().createQuery("delete from HotelEntity");

	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			HotelEntity hotelEntity = factory.manufacturePojo(HotelEntity.class);
			entityManager.persist(hotelEntity);
			hotelList.add(hotelEntity);
		}

		for (int i = 0; i < 3; i++) {
			HabitacionEntity habitacionEntity = factory.manufacturePojo(HabitacionEntity.class);
			habitacionEntity.setHotel(hotelList.get(0));
			entityManager.persist(habitacionEntity);
			habitacionList.add(habitacionEntity);
		}

	}

	/**
	 * Prueba para crear un habitacion
	 */
	@Test
	void testCreatehabitacion() throws EntityNotFoundException, IllegalOperationException {
		HabitacionEntity newEntity = factory.manufacturePojo(HabitacionEntity.class);
		newEntity.setHotel(hotelList.get(0));
		
		HabitacionEntity result = habitacionService.createHabitacion(newEntity);
		assertNotNull(result);
		HabitacionEntity entity = entityManager.find(HabitacionEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNumero_camas(), entity.getNumero_camas());
		assertEquals(newEntity.getNumero_identificacion(), entity.getNumero_identificacion());
		assertEquals(newEntity.getNumero_personas(), entity.getNumero_personas());
		assertEquals(newEntity.getNumero_wc(), entity.getNumero_wc());
		
	}

	/**
	 * Prueba para crear un habitacion con su restriccion
	 */
	@Test
	void testCreateHabitacionWithNoValidId() {
		assertThrows(IllegalOperationException.class, () -> {
			HabitacionEntity newEntity = factory.manufacturePojo(HabitacionEntity.class);
			newEntity.setHotel(hotelList.get(0));
			newEntity.setId(null);
			habitacionService.createHabitacion(newEntity);
		});
	}

	
}
