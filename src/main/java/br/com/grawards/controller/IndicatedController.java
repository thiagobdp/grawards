package br.com.grawards.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grawards.Utils.CsvUtil;
import br.com.grawards.controller.dto.IndicatedDto;
import br.com.grawards.model.Indicated;
import br.com.grawards.model.IndicatedCsv;
import br.com.grawards.model.Producer;
import br.com.grawards.model.Studio;
import br.com.grawards.repository.IndicatedRepository;
import br.com.grawards.repository.ProducerRepository;
import br.com.grawards.repository.StudioRepository;

@RestController
@RequestMapping("/indicateds")
public class IndicatedController {

	@Autowired
	private IndicatedRepository indicatedRepository;
	
	@Autowired
	private StudioRepository studioRepository;
	
	@Autowired
	private ProducerRepository producerRepository;

	@GetMapping
	public List<IndicatedDto> list(Integer year){
		if (year == null) {
			List<Indicated> indicateds = indicatedRepository.findAll();		
			return IndicatedDto.converter(indicateds);	
		} else {
			List<Indicated> indicateds = indicatedRepository.findByYear(year);
			return IndicatedDto.converter(indicateds);
		}		
	}

	@PostConstruct
	public void loadFile() throws IOException {
		File file;
		try {
			file = ResourceUtils.getFile("classpath:movielist.csv");						
			InputStream fileStream = new FileInputStream(file);			
			List<IndicatedCsv> indicatedsList = CsvUtil.read(IndicatedCsv.class, fileStream);
			indicatedsList.forEach(i -> System.out.println(i.toString()));
			
			//load and saves unique studios
			Set<String> studiosSet= new HashSet<String>();
			indicatedsList.forEach(i -> studiosSet.addAll(i.getListStudios()));
			List<Studio> studioList = this.studioRepository.saveAll(studiosSet.stream().map(Studio::new).collect(Collectors.toList()));

			//load and saves unique producers
			Set<String> producersSet= new HashSet<String>();
			indicatedsList.forEach(i -> producersSet.addAll(i.getListProducers()));
			List<Producer> producerList = this.producerRepository.saveAll(producersSet.stream().map(Producer::new).collect(Collectors.toList()));
			
			
			System.out.println("END");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

}
