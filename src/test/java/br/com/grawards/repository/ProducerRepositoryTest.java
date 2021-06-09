package br.com.grawards.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.grawards.model.Producer;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles ("test")
public class ProducerRepositoryTest {

	@Autowired
	private ProducerRepository repository;
	
	@Autowired
	private TestEntityManager em;
	
	@Test
	public void findByNameExistingName() {
		String producerName = "Bo Derek";
		
		Producer p = new Producer("Bo Derek");
		em.persist(p);
		
		List<Producer> prodLst=this.repository.findByName(producerName);
		Assert.assertNotNull(prodLst);
		Assert.assertTrue(prodLst.size() == 1);
		Assert.assertEquals(prodLst.get(0).getName(), producerName);
	}
	
	@Test
	public void findByNameNotExistingName() {
		String producerName = "Testing name do not exists";
		List<Producer> prodLst=this.repository.findByName(producerName);
		Assert.assertNotNull(prodLst);
		Assert.assertTrue(prodLst.size()==0);
	}

}
