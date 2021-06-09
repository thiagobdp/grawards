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
public class ProducerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void fastestSlowestWinnerProducerTest() throws Exception {
		URI uri = new URI("/producer/fastestSlowestWinner");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders//
				.get(uri)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk()).andReturn();
		
		String jsonReturned = mvcResult.getResponse().getContentAsString();
		String jsonExpected="{\"min\":[{\"producer\":\"Producer1\",\"interval\":1,\"previousWin\":1980,\"followingWin\":1981}],\"max\":[{\"producer\":\"Producer3\",\"interval\":11,\"previousWin\":1980,\"followingWin\":1991}]}";
		
		JSONAssert.assertEquals(jsonExpected, jsonReturned, JSONCompareMode.LENIENT);		
	}
	
	@Test
	public void listProducers() throws Exception {
		URI uri = new URI("/producer");		

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders//
				.get(uri)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk()).andReturn();
		
		JSONArray jsonReturned = new JSONArray(mvcResult.getResponse().getContentAsString());
		JSONArray jsonExpected= new JSONArray("[{\"name\":\"Producer2\"},{\"name\":\"Producer3\"},{\"name\":\"Producer1\"}]");
		
		JSONAssert.assertEquals(jsonExpected, jsonReturned, JSONCompareMode.LENIENT);
	}

}
