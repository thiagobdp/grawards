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
import br.com.grawards.controller.dto.IndicadoDto;
import br.com.grawards.modelo.Indicado;
import br.com.grawards.repository.IndicadoRepository;

@RestController
@RequestMapping("/indicados")
public class IndicadosController {

	@Autowired
	private IndicadoRepository indicadoRepository;

	@GetMapping
	public List<IndicadoDto> lista(Integer year){
		if (year == null) {
			List<Indicado> indicados = indicadoRepository.findAll();		
			return IndicadoDto.converter(indicados);	
		} else {
			List<Indicado> indicados = indicadoRepository.findByYear(year);
			return IndicadoDto.converter(indicados);
		}		
	}

	@PostConstruct
	public void carregaArquivo() throws IOException {
		File file;
		try {
			file = ResourceUtils.getFile("classpath:movielist.csv");						
			InputStream fileStream = new FileInputStream(file);
			List<Indicado> listaIndicados = CsvUtil.read(Indicado.class, fileStream);
			indicadoRepository.saveAll(listaIndicados);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

}
