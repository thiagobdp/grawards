package br.com.grawards.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import br.com.grawards.controller.dto.FastestSlowestWinnerProducerDto;
import br.com.grawards.controller.dto.ProducerIntervalDto;
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
	public FastestSlowestWinnerProducerDto fastestSlowestWinnerProducer() {

		// find producers who won at least once
//		List<Producer> prod = producerRepository.findByindicateds_winner("yes"); //TODO: assim traz duplicados, verificar qual o problema
		List<Producer> prod = producerRepository.findAll();

		System.out.println("JOEL: "
				+ prod.stream().filter(p -> p.getName().equalsIgnoreCase("Joel Silver")).collect(Collectors.toList()));

		prod.stream().forEach(p -> p.setIndicateds(p.getIndicateds().stream()
				.filter(i -> i.getWinner().equalsIgnoreCase("yes")).collect(Collectors.toList())));// remove indicateds
																									// not winners

//		System.out.println("JOEL WINNER: "
//				+ prod.stream().filter(p -> p.getName().equalsIgnoreCase("Joel Silver")).collect(Collectors.toList()));

//		System.out.println("size prod: " + prod.size());
		List<Producer> prodWinners = new ArrayList<Producer>();

		prod.stream().filter(p -> p.getIndicateds().size() > 1).forEach(p -> prodWinners.add(p)); // keep only winners
																									// with more then 1
																									// win

//		System.out.println("JOEL WINNER+d1: " + prodWinners.stream()
//				.filter(p -> p.getName().equalsIgnoreCase("Joel Silver")).collect(Collectors.toList()));
//
//		System.out.println("size prodWinners: " + prodWinners.size());

		HashMap<Integer, List<ProducerIntervalDto>> intervalMap = new HashMap<Integer, List<ProducerIntervalDto>>();

		prodWinners.forEach(
				p -> p.getIndicateds().stream().iterator().forEachRemaining(i1 -> this.difference(p, i1, intervalMap)));

//		System.out.println("MIN: " + intervalMap.keySet().stream().mapToInt(t -> t).min());
//		System.out.println("MAX: " + intervalMap.keySet().stream().mapToInt(t -> t).max());
//		System.out.println(intervalMap);

		return new FastestSlowestWinnerProducerDto(
				intervalMap.get(intervalMap.keySet().stream().mapToInt(t -> t).min().getAsInt()),
				intervalMap.get(intervalMap.keySet().stream().mapToInt(t -> t).max().getAsInt()));

	}

	public void difference(Producer p, Indicated i1, HashMap<Integer, List<ProducerIntervalDto>> intervalMap) {

		Integer diference;

		if (p.getIndicateds().indexOf(i1) < p.getIndicateds().size() - 1) {
			Indicated i2 = p.getIndicateds().get(p.getIndicateds().indexOf(i1) + 1);

			if (i1.getYear() > i2.getYear()) {
				diference = i1.getYear() - i2.getYear();
			} else {
				diference = i2.getYear() - i1.getYear();
			}

			if (intervalMap.containsKey(diference)) {
				intervalMap.get(diference)
						.add(new ProducerIntervalDto(p.getName(), diference, i1.getYear(), i2.getYear()));
			} else {
				intervalMap.put(diference, new LinkedList<ProducerIntervalDto>(
						Arrays.asList(new ProducerIntervalDto(p.getName(), diference, i1.getYear(), i2.getYear()))));
			}
		}
	}

	@PostConstruct
	public void loadFile() throws IOException {
		File file;
		try {
			file = ResourceUtils.getFile("classpath:movielist.csv");
			InputStream fileStream = new FileInputStream(file);
			List<IndicatedCsv> indicatedsCsvList = CsvUtil.read(IndicatedCsv.class, fileStream);

			HashMap<String, Studio> studioHmp = this.saveUniqueStudios(indicatedsCsvList);
			HashMap<String, Producer> producerHmp = this.saveUniqueProducers(indicatedsCsvList);

			List<Indicated> indicatedsList = this.indicatedRepository.saveAll(indicatedsCsvList.stream()
					.map(i -> new Indicated(i, studioHmp, producerHmp)).collect(Collectors.toList()));

			System.out.println("END");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	/*
	 * Creates a Set of unduplicated Studios names and save in DB
	 */
	private HashMap<String, Studio> saveUniqueStudios(List<IndicatedCsv> indicatedsCsvList) {
		// save unique studios
		Set<String> studiosSet = new HashSet<String>();
		indicatedsCsvList.forEach(i -> studiosSet.addAll(i.getListStudios()));
		List<Studio> studioList = this.studioRepository
				.saveAll(studiosSet.stream().map(Studio::new).collect(Collectors.toList()));

		// creates hashmap of saved studios
		HashMap<String, Studio> studioHmp = new HashMap<String, Studio>();
		studioList.forEach(s -> studioHmp.put(s.getName(), s));
		return studioHmp;
	}

	/*
	 * Creates a Set of unduplicated Producers names and save in DB
	 */
	private HashMap<String, Producer> saveUniqueProducers(List<IndicatedCsv> indicatedsCsvList) {
		// load and save unique producers
		Set<String> producersSet = new HashSet<String>();
		indicatedsCsvList.forEach(i -> producersSet.addAll(i.getListProducers()));
		List<Producer> producerList = this.producerRepository
				.saveAll(producersSet.stream().map(Producer::new).collect(Collectors.toList()));

		// creates hashmap of saved Producers
		HashMap<String, Producer> producerHmp = new HashMap<String, Producer>();
		producerList.forEach(p -> producerHmp.put(p.getName(), p));
		return producerHmp;
	}

}
