package br.com.grawards.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import br.com.grawards.model.WinnerEnum;
import br.com.grawards.repository.IndicatedRepository;
import br.com.grawards.repository.ProducerRepository;
import br.com.grawards.repository.StudioRepository;

@RestController
@RequestMapping("/indicated")
public class IndicatedController {

	@Autowired
	private IndicatedRepository indicatedRepository;

	@Autowired
	private StudioRepository studioRepository;

	@Autowired
	private ProducerRepository producerRepository;

	@Autowired
	private Environment env;
	
	/**
	 * Loads the CSV file, read data, splits in Entities and stores in the Data Base
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void loadFile() throws IOException {		
		try {			
			File file = ResourceUtils.getFile(env.getProperty("grawards.csv.filename"));
			InputStream fileStream = new FileInputStream(file);
			List<IndicatedCsv> indicatedsCsvList = CsvUtil.read(IndicatedCsv.class, fileStream);

			HashMap<String, Studio> studioHmp = this.saveUniqueStudios(indicatedsCsvList);
			HashMap<String, Producer> producerHmp = this.saveUniqueProducers(indicatedsCsvList);

			this.indicatedRepository.saveAll(indicatedsCsvList.stream()
					.map(i -> new Indicated(i, studioHmp, producerHmp)).collect(Collectors.toList()));

			System.out.println("END");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}
	

	/**
	 * Get the producer with the longest interval between two consecutive awards,
	 * and the ones who got two awards faster
	 * 
	 * @return
	 */
	@GetMapping
	@RequestMapping("/fastestSlowestWinnerProducer")
	public FastestSlowestWinnerProducerDto fastestSlowestWinnerProducer() {

		// find producers who won at least once
//		List<Producer> prod = producerRepository.findByindicateds_winner("yes"); //TODO: assim traz duplicados, verificar qual o problema
		List<Producer> prod = producerRepository.findAll();

		// remove non-winning Indicateds
		prod.stream().forEach(p -> p.setIndicateds(p.getIndicateds().stream()
				.filter(i -> i.getWinner().equalsIgnoreCase(WinnerEnum.YES.getWinner())).collect(Collectors.toList())));

		List<Producer> prodWinners = new ArrayList<Producer>();

		// keep only the winners who have won at least twice
		prod.stream().filter(p -> p.getIndicateds().size() > 1).forEach(p -> prodWinners.add(p));

		prodWinners.stream().forEach(p -> p.getIndicateds().sort(Comparator.comparing(Indicated::getYear)));

		HashMap<Integer, List<ProducerIntervalDto>> intervalMap = new HashMap<Integer, List<ProducerIntervalDto>>();

		prodWinners.forEach(p -> p.getIndicateds().stream().iterator()
				.forEachRemaining(i1 -> this.calculateInterval(p, i1, intervalMap)));

		return new FastestSlowestWinnerProducerDto(
				intervalMap.get(intervalMap.keySet().stream().mapToInt(t -> t).min().getAsInt()),
				intervalMap.get(intervalMap.keySet().stream().mapToInt(t -> t).max().getAsInt()));
	}

	/**
	 * Calculates the interval of years between indicated1 and the next indicated in
	 * the list of the Producer if there is another one
	 * 
	 * @param p           Producer The List of Indicateds must be already ordered by
	 *                    year
	 * @param i1          Indicated
	 * @param intervalMap HashMap of "Interval between winnings" and
	 *                    "ProducerIntervalDto" that fits this interval
	 */
	public void calculateInterval(Producer p, Indicated i1, HashMap<Integer, List<ProducerIntervalDto>> intervalMap) {

		if (p.getIndicateds().indexOf(i1) < p.getIndicateds().size() - 1) {
			Indicated i2 = p.getIndicateds().get(p.getIndicateds().indexOf(i1) + 1);

			Integer diference = i2.getYear() - i1.getYear();

			if (intervalMap.containsKey(diference)) {
				intervalMap.get(diference)
						.add(new ProducerIntervalDto(p.getName(), diference, i1.getYear(), i2.getYear()));
			} else {
				intervalMap.put(diference, new LinkedList<ProducerIntervalDto>(
						Arrays.asList(new ProducerIntervalDto(p.getName(), diference, i1.getYear(), i2.getYear()))));
			}
		}
	}

	/**
	 * Create a set of non-duplicate Studio's names and save to database
	 * 
	 * @param indicatedsCsvList
	 * @return HashMap of Studios having the name of the Studio as Key
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

	/**
	 * Create a set of non-duplicate Producer's names and save to database
	 * 
	 * @param indicatedsCsvList
	 * @return HashMap of Producers having the name of the Producer as Key
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
