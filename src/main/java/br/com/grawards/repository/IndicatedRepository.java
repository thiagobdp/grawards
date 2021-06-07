package br.com.grawards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grawards.model.Indicated;

public interface IndicatedRepository extends JpaRepository<Indicated, Long> {

	List<Indicated> findByYear(Integer year);

}
