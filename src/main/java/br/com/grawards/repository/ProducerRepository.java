package br.com.grawards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grawards.model.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

	public List<Producer> findByindicateds_winner(String winner);
	
}
