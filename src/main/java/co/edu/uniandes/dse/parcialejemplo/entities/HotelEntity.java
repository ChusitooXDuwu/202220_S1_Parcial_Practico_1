

package co.edu.uniandes.dse.parcialejemplo.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un hotel en la persistencia
 *
 * @author Wyo Chu
 */

@Getter
@Setter
@Entity
public class HotelEntity extends BaseEntity {

	private String nombre;
	private String direccion;
	private Float estrellas;
	private Long id;

	@PodamExclude
	@OneToMany(mappedBy = "hotel")
	private List<HabitacionEntity> habitaciones = new ArrayList<>();
}
