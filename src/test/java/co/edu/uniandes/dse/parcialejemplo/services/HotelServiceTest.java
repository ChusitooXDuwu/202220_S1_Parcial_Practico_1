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
import co.edu.uniandes.dse.parcialejemplo.services.HotelService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(HotelService.class)
class HotelServiceTest {

	@Autowired
	private HotelService hotelService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<HotelEntity> hotelList = new ArrayList<>();

	private List<HabitacionEntity> habitacionList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from habitacionEntity");
		entityManager.getEntityManager().createQuery("delete from hotelEntity");
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
			entityManager.persist(habitacionEntity);
			habitacionList.add(habitacionEntity);
		}
		habitacionList.get(0).setHotel(hotelList.get(0));
		hotelList.get(0).getHabitaciones().add(habitacionList.get(0));
	}

	/**
	 * Prueba para crear un hotel.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testCreatehotel() throws EntityNotFoundException, IllegalOperationException {
		HotelEntity newEntity = factory.manufacturePojo(HotelEntity.class);
		HotelEntity result = hotelService.createHoteles(newEntity);
		assertNotNull(result);

		HotelEntity entity = entityManager.find(HotelEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
	}

	/**
	 * Prueba para crear un hotel con el mismo nombre de un hotel que ya
	 * existe.
	 *
	 * @throws IllegalOperationException
	 */
	@Test
	void testCreatehotelWithSameName() {
		assertThrows(IllegalOperationException.class, () -> {
			HotelEntity newEntity = factory.manufacturePojo(HotelEntity.class);
			newEntity.setNombre(hotelList.get(0).getNombre());
			hotelService.createHoteles(newEntity);
		});
	}

	
}
