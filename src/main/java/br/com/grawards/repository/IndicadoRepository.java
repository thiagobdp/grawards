package br.com.grawards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grawards.modelo.Indicado;

public interface IndicadoRepository extends JpaRepository<Indicado, Long> {

	List<Indicado> findByYear(Integer year);

}
