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
import br.com.grawards.controller.dto.FastestSlowestWinnerDto;
import br.com.grawards.controller.dto.ProducerDto;
import br.com.grawards.controller.dto.WinnerIntervalDto;
import br.com.grawards.model.Indicated;
import br.com.grawards.model.IndicatedCsv;
import br.com.grawards.model.Producer;
import br.com.grawards.model.Studio;
import br.com.grawards.model.WinnerEnum;
import br.com.grawards.repository.IndicatedRepository;
import br.com.grawards.repository.ProducerRepository;
import br.com.grawards.repository.StudioRepository;

@RestController
@RequestMapping("/producer")
public class ProducerController {

	@Autowired
	private ProducerRepository producerRepository;

	@Autowired
	private IndicatedRepository indicatedRepository;

	@Autowired
	private StudioRepository studioRepository;

	@Autowired
	private Environment env;

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

	/**
	 * Loads the CSV file, read data, splits in Entities (Producer, Studio and
	 * Indicated) and stores in the Data Base
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

			// saves all indicateds with its lists of Studios and Producers
			this.indicatedRepository.saveAll(indicatedsCsvList.stream()
					.map(i -> new Indicated(i, studioHmp, producerHmp)).collect(Collectors.toList()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the producers with the longest interval between two consecutive awards,
	 * and the ones who got two awards faster
	 * 
	 * @return
	 */
	@GetMapping
	@RequestMapping("/fastestSlowestWinner")
	public FastestSlowestWinnerDto fastestSlowestWinner() {

		List<Producer> prod = producerRepository.findAll();

		// remove non-winning Indicateds by resetting de producer list with only winning
		// indicateds
		prod.stream().forEach(p -> p.setIndicateds(p.getIndicateds().stream()
				.filter(i -> i.getWinner().equalsIgnoreCase(WinnerEnum.YES.getWinner())).collect(Collectors.toList())));

		// keep only the winners who have won at least twice or more
		List<Producer> pWinners2orMore = new ArrayList<Producer>();
		prod.stream().filter(p -> p.getIndicateds().size() > 1).forEach(p -> pWinners2orMore.add(p));

		pWinners2orMore.stream().forEach(p -> p.getIndicateds().sort(Comparator.comparing(Indicated::getYear)));

		HashMap<Integer, List<WinnerIntervalDto>> intervalMap = this.createMapOfWinnersInterval(pWinners2orMore);

		return new FastestSlowestWinnerDto(
				intervalMap.get(intervalMap.keySet().stream().mapToInt(t -> t).min().getAsInt()),
				intervalMap.get(intervalMap.keySet().stream().mapToInt(t -> t).max().getAsInt()));
	}

	/**
	 * Creates a HashMap of Interval between Winnings and its producers. Key - is
	 * the interval of winnings in years. List - All producers that have winnings in
	 * the interval key.
	 * 
	 * @param producers
	 * @return HashMap ("Interval", "All producers that have winnings in the
	 *         interval")
	 */
	private HashMap<Integer, List<WinnerIntervalDto>> createMapOfWinnersInterval(List<Producer> producers) {
		HashMap<Integer, List<WinnerIntervalDto>> winnersIntervalMap = new HashMap<Integer, List<WinnerIntervalDto>>();

		producers.forEach(p -> p.getIndicateds().stream().iterator()
				.forEachRemaining(i1 -> this.calculateInterval(p, i1, winnersIntervalMap)));

		return winnersIntervalMap;
	}

	/**
	 * Calculates the interval of years between indicated1 and the next indicated in
	 * the list of the Producer if there is another one
	 * 
	 * @param producer       The Producer's List of Indicateds must be already
	 *                       ordered by year
	 * @param indicatedFirst Indicated to be used to compare to the next
	 * @param intervalMap    HashMap of "Interval between winnings" and
	 *                       "ProducerIntervalDto" that fits this interval
	 */
	private void calculateInterval(Producer producer, Indicated indicatedFirst,
			HashMap<Integer, List<WinnerIntervalDto>> intervalMap) {

		// proceeds only if there is one more indicated in the producer's indicateds
		// list
		if (producer.getIndicateds().indexOf(indicatedFirst) < producer.getIndicateds().size() - 1) {
			// gets the next sequencial indicated
			Indicated indicatedNext = producer.getIndicateds()
					.get(producer.getIndicateds().indexOf(indicatedFirst) + 1);

			Integer interval = indicatedNext.getYear() - indicatedFirst.getYear();

			if (intervalMap.containsKey(interval)) {
				intervalMap.get(interval).add(new WinnerIntervalDto(producer.getName(), interval,
						indicatedFirst.getYear(), indicatedNext.getYear()));
			} else {
				intervalMap.put(interval,
						new LinkedList<WinnerIntervalDto>(Arrays.asList(new WinnerIntervalDto(producer.getName(),
								interval, indicatedFirst.getYear(), indicatedNext.getYear()))));
			}
		}
	}

	/**
	 * Creates a set of non-duplicate Studio's names and saves to database
	 * 
	 * @param indicatedsCsvList
	 * @return HashMap of Studios having the name of the Studio as Key
	 */
	private HashMap<String, Studio> saveUniqueStudios(List<IndicatedCsv> indicatedsCsvList) {
		// save unique studios
		Set<String> studiosUniqueNameSet = new HashSet<String>();
		indicatedsCsvList.forEach(i -> studiosUniqueNameSet.addAll(i.getListStudios()));
		List<Studio> studioList = this.studioRepository
				.saveAll(studiosUniqueNameSet.stream().map(Studio::new).collect(Collectors.toList()));

		// creates hashmap of saved studios
		HashMap<String, Studio> studioHmp = new HashMap<String, Studio>();
		studioList.forEach(s -> studioHmp.put(s.getName(), s));
		return studioHmp;
	}

	/**
	 * Creates a set of non-duplicate Producer's names and saves to database
	 * 
	 * @param indicatedsCsvList
	 * @return HashMap of Producers having the name of the Producer as Key
	 */
	private HashMap<String, Producer> saveUniqueProducers(List<IndicatedCsv> indicatedsCsvList) {
		Set<String> producersUniqueNameSet = new HashSet<String>();
		indicatedsCsvList.forEach(i -> producersUniqueNameSet.addAll(i.getListProducers()));

		List<Producer> producerList = this.producerRepository
				.saveAll(producersUniqueNameSet.stream().map(Producer::new).collect(Collectors.toList()));

		// creates hashmap of saved Producers
		HashMap<String, Producer> producerHmp = new HashMap<String, Producer>();
		producerList.forEach(p -> producerHmp.put(p.getName(), p));
		return producerHmp;
	}
}
