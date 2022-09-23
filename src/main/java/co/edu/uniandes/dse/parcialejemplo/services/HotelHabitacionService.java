

package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialejemplo.entities.HabitacionEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.ErrorMessage;

import co.edu.uniandes.dse.parcialejemplo.repositories.HabitacionRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.HotelRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class HotelHabitacionService {

	@Autowired
	private HabitacionRepository habitacionRepository;

	@Autowired
	private HotelRepository hotelRepository;
	
	
	
	@Transactional
	public HabitacionEntity addHabitacion(Long habitacionId, Long hotelId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle un libro a la hotel con id = {0}", hotelId);
		
		Optional<HabitacionEntity> habitacionEntity = habitacionRepository.findById(habitacionId);
		if(habitacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.HABITACION_NOT_FOUND);
		
		Optional<HotelEntity> hotelEntity = hotelRepository.findById(hotelId);
		if(hotelEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.HOTEL_NOT_FOUND);
		
		habitacionEntity.get().setHotel(hotelEntity.get());
		log.info("Termina proceso de agregarle un libro a la hotel con id = {0}", hotelId);
		return habitacionEntity.get();
	}

	
}