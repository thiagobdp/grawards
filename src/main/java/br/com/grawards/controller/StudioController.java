package br.com.grawards.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grawards.controller.dto.StudioDto;
import br.com.grawards.model.Studio;
import br.com.grawards.repository.StudioRepository;

@RestController
@RequestMapping("/studio")
public class StudioController {

	@Autowired
	private StudioRepository studioRepository;

	/**
	 * List all studios
	 * 
	 * @return
	 */
	@GetMapping
	public List<StudioDto> producer() {
		List<Studio> studios = studioRepository.findAll();
		return StudioDto.converter(studios);
	}
}
