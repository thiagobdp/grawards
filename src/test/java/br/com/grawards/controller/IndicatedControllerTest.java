/**
 * 
 */
package br.com.grawards.controller;

import java.net.URI;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class IndicatedControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void listProducers() throws Exception {
		URI uri = new URI("/indicated");		

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders//
				.get(uri)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk()).andReturn();
		
		JSONArray jsonReturned = new JSONArray(mvcResult.getResponse().getContentAsString());
		JSONArray jsonExpected= new JSONArray("[{\"winner\":\"yes\",\"studios\":[{\"name\":\"Studio teste1\",\"id\":3},{\"name\":\"Studio teste2\",\"id\":4}],\"year\":1980,\"title\":\"Movie test 1\",\"producers\":[{\"name\":\"Producer1\",\"id\":3},{\"name\":\"Producer2\",\"id\":1},{\"name\":\"Producer3\",\"id\":2}]},{\"winner\":\"yes\",\"studios\":[{\"name\":\"Studio teste3\",\"id\":1}],\"year\":1981,\"title\":\"Movie test 2\",\"producers\":[{\"name\":\"Producer1\",\"id\":3}]},{\"winner\":\"\",\"studios\":[{\"name\":\"Studio teste3\",\"id\":1}],\"year\":1990,\"title\":\"Movie test 3\",\"producers\":[{\"name\":\"Producer3\",\"id\":2}]},{\"winner\":\"yes\",\"studios\":[{\"name\":\"Studio teste4\",\"id\":2}],\"year\":1991,\"title\":\"Movie test 4\",\"producers\":[{\"name\":\"Producer3\",\"id\":2}]},{\"winner\":\"\",\"studios\":[{\"name\":\"Studio teste4\",\"id\":2}],\"year\":1991,\"title\":\"Movie test 5\",\"producers\":[{\"name\":\"Producer3\",\"id\":2}]}]");
		
		JSONAssert.assertEquals(jsonExpected, jsonReturned, JSONCompareMode.LENIENT);
	}

}
