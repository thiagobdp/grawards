package br.com.grawards.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

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
import br.com.grawards.repository.IndicatedRepository;

@RestController
@RequestMapping("/indicateds")
public class IndicatedController {

	@Autowired
	private IndicatedRepository indicatedRepository;

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
			List<IndicatedCsv> listaIndicados = CsvUtil.read(IndicatedCsv.class, fileStream);
			listaIndicados.forEach(i -> System.out.println(i.toString()));
			System.out.println("END");
//			List<Indicado> listaIndicados = CsvUtil.read(Indicado.class, fileStream);
//			indicadoRepository.saveAll(listaIndicados);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

}
