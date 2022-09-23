


package co.edu.uniandes.dse.parcialejemplo.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import co.edu.uniandes.dse.parcialejemplo.entities.HabitacionEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;

import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.HabitacionRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.HotelRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class HabitacionService {

	@Autowired
	HabitacionRepository habitacionRepository;

	@Autowired
	HotelRepository hotelRepository;
	
	/**
	 * Guardar un nuevo libro
	 *
	 * @param habitacionEntity La entidad de tipo libro del nuevo libro a persistir.
	 * @return La entidad luego de persistirla
	 * @throws IllegalOperationException Si el ISBN es inválido o ya existe en la
	 *                                   persistencia o si la hotel es inválida
	 */
	@Transactional
	public HabitacionEntity createHabitacion(HabitacionEntity habitacionEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación de la habitacion");
		
		if (habitacionEntity.getHotel() == null)
			throw new IllegalOperationException("hotel is not valid");
		
		Optional<HotelEntity> hotelEntity = hotelRepository.findById(habitacionEntity.getHotel().getId());
		if (hotelEntity.isEmpty())
			throw new IllegalOperationException("hotel is not valid");

		if (!validateWC(habitacionEntity.getNumero_personas(), habitacionEntity.getNumero_wc()))
			throw new IllegalOperationException("No cumple que númeroBaños <= númeroPersonas.");

		

		habitacionEntity.setHotel(hotelEntity.get());
		log.info("Termina proceso de creación del libro");
		return habitacionRepository.save(habitacionEntity);
	}

	private boolean validateWC(Float personas, Float WC) {
		return !(WC <= personas);
	}
}
