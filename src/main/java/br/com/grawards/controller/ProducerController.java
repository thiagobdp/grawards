package br.com.grawards.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grawards.controller.dto.ProducerDto;
import br.com.grawards.model.Producer;
import br.com.grawards.repository.ProducerRepository;

@RestController
@RequestMapping("/producer")
public class ProducerController {

	@Autowired
	private ProducerRepository producerRepository;

	/**
	 * List all producers
	 * 
	 * @return
	 */
	@GetMapping
	public List<ProducerDto> producer() {
		List<Producer> producers = producerRepository.findAll();
		return ProducerDto.converter(producers);
	}
}
