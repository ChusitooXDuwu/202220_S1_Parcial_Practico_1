

package co.edu.uniandes.dse.parcialejemplo.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import co.edu.uniandes.dse.parcialejemplo.entities.HotelEntity;


import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.HotelRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HotelService {

	@Autowired
	HotelRepository hotelRepository;

@Transactional
	public HotelEntity createHoteles(HotelEntity hotelEntity) throws IllegalOperationException {
		log.info("Inicia proceso de creación del hotel");
		if (!hotelRepository.findByNombre(hotelEntity.getNombre()).isEmpty()) {
			throw new IllegalOperationException("hotel name already exists");
		}
		if (!validateEstrellas(hotelEntity.getEstrellas()))
			throw new IllegalOperationException("No cumple que estrellas entre 2 y 6");
		log.info("Termina proceso de creación de la hotel");
		return hotelRepository.save(hotelEntity);
	}

	private boolean validateEstrellas(Float estrellas) {
		return !(estrellas >= 2 && estrellas <= 6);
	}
}
