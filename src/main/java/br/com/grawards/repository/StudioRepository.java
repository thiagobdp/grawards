package br.com.grawards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grawards.model.Studio;

public interface StudioRepository extends JpaRepository<Studio, Long> {

}
