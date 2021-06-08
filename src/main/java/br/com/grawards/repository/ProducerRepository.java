package br.com.grawards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grawards.model.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

}
