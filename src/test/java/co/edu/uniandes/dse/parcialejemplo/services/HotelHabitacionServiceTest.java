/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
import co.edu.uniandes.dse.parcialejemplo.services.HotelHabitacionService;
import co.edu.uniandes.dse.parcialejemplo.services.HotelService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion hotel - habitacions
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ HotelService.class, HotelHabitacionService.class })
class HotelHabitacionServiceTest {

	@Autowired
	private HotelHabitacionService hotelhabitacionService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<HotelEntity> hotelsList = new ArrayList<>();
	private List<HabitacionEntity> habitacionsList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from habitacionEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from hotelEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			HabitacionEntity habitacion = factory.manufacturePojo(HabitacionEntity.class);
			entityManager.persist(habitacion);
			habitacionsList.add(habitacion);
		}

		for (int i = 0; i < 3; i++) {
			HotelEntity entity = factory.manufacturePojo(HotelEntity.class);
			entityManager.persist(entity);
			hotelsList.add(entity);
			if (i == 0) {
				habitacionsList.get(i).setHotel(entity);
				entity.getHabitaciones().add(habitacionsList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un habitacion existente a un hotel.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddhabitacion() throws EntityNotFoundException {
		HotelEntity entity = hotelsList.get(0);
		HabitacionEntity habitacionEntity = habitacionsList.get(1);
		HabitacionEntity response = hotelhabitacionService.addHabitacion(habitacionEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(habitacionEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un habitacion que no existe a un hotel.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidhabitacion() {
		assertThrows(EntityNotFoundException.class, ()->{
			HotelEntity entity = hotelsList.get(0);
			hotelhabitacionService.addHabitacion(0L, entity.getId());
		});
	}
	
	
}
