package idv.blake.cathy.controller.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import idv.blake.cathy.CathyApplication;
import idv.blake.cathy.model.currency.CurrencyServiceV1;
import idv.blake.cathy.model.entity.coindesk.CoindeskCurrentPriceResponse;
import idv.blake.cathy.model.entity.coindesk.convert.CoindeskCurrrentPriceConvertResponse;
import idv.blake.cathy.model.entity.currency.CurrencyLangRequest;
import idv.blake.cathy.model.entity.currency.CurrencyLangResponse;
import idv.blake.cathy.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CathyApplication.class)
@WebAppConfiguration
class CurrencyControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private String createCurrencyNameUrl = "/v1/currency/lang";
	private String getCurrencyNameUrl = "/v1/currency/lang/%s";
	private String updateCurrencyNameUrl = "/v1/currency/lang/%s";
	private String deleteCurrencyNameUrl = "/v1/currency/lang/%s";
	private String coindeskCurrentPrice = "/v1/currency/coindesk/currentprice";
	private String coindeskCurrentPriceConvert = "/v1/currency/coindesk/currentprice/convert";

	@Autowired
	@Resource(name = "CurrencyServiceV1")
	private CurrencyServiceV1 currencyServiceV1;

	/**
	 * ????????????coindesk API
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetCoindeskCurrentPrice() throws Exception {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// ???????????????????????????coindesk??????
		System.out.println("================coindesk API ??????===============");
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get(coindeskCurrentPrice).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
		Assert.assertEquals(false, StringUtil.isEmpty(result.getResponse().getContentAsString()));

		CoindeskCurrentPriceResponse sourceResponse = fromJson(
				result.getResponse().getContentAsString(StandardCharsets.UTF_8), CoindeskCurrentPriceResponse.class);

		// ????????????????????????

		// ??????????????????
		result = mvc.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
				.header("Content-TYPE", MediaType.APPLICATION_JSON)
				.content(toJson(new CurrencyLangRequest("EUR", "??????")))).andExpect(status().is(200)).andReturn();

		System.out.println("================coindesk API ????????????================");
		result = mvc.perform(MockMvcRequestBuilders.get(coindeskCurrentPriceConvert).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		CoindeskCurrrentPriceConvertResponse response = fromJson(
				result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CoindeskCurrrentPriceConvertResponse.class);

		assertEquals(response.getCurrency().size(), sourceResponse.getBpi().size());
		for (String code : sourceResponse.getBpi().keySet()) {
			assertNotNull(response.getCurrency().get(code));
		}
		assertNotNull(response.getUpdateTime());
		assertNotNull(response.getCurrency().get("EUR"));
		assertEquals("??????", response.getCurrency().get("EUR").getName());
		assertNotNull(response.getCurrency().get("USD"));
		assertEquals("USD", response.getCurrency().get("USD").getName());

		// ????????????
		result = mvc
				.perform(MockMvcRequestBuilders.put(String.format(updateCurrencyNameUrl, "EUR"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("EUR", "??????2"))))
				.andExpect(status().is(200)).andReturn();

		result = mvc.perform(MockMvcRequestBuilders.get(coindeskCurrentPriceConvert).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CoindeskCurrrentPriceConvertResponse.class);

		assertEquals("??????2", response.getCurrency().get("EUR").getName());

		// ??????????????????
		result = mvc
				.perform(MockMvcRequestBuilders.delete(String.format(deleteCurrencyNameUrl, "EUR"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		result = mvc.perform(MockMvcRequestBuilders.get(coindeskCurrentPriceConvert).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CoindeskCurrrentPriceConvertResponse.class);

		assertEquals("EUR", response.getCurrency().get("EUR").getName());

	}

	/**
	 * 
	 * ??????Currency Lang CURD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCurrencyLang() throws Exception {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// create currency lang1

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl)
				.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
				.content(toJson(new CurrencyLangRequest(null, null)))).andExpect(status().is(419)).andReturn();

		result = mvc.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
				.header("Content-TYPE", MediaType.APPLICATION_JSON)
				.content(toJson(new CurrencyLangRequest("AED", null)))).andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest(null, "????????????????????????????????????"))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AEDA", "????????????????????????????????????"))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "????????????????????????????????????????????????"))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "?????????????????????????????????????????????"))))
				.andExpect(status().is(200)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "????????????????????????????????????"))))
				.andExpect(status().is(409)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("TWD", "?????????"))))
				.andExpect(status().is(200)).andReturn();

		// get currency lang

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "USD"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(204)).andReturn();

		System.out.println("=============????????????????????????=============");
		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		CurrencyLangResponse response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("AED", response.getCode());
		assertEquals("?????????????????????????????????????????????", response.getName());

		// update currency lang
		result = mvc.perform(MockMvcRequestBuilders.put(String.format(updateCurrencyNameUrl, "USD"))
				.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
				.content(toJson(new CurrencyLangRequest("USD", "??????")))).andExpect(status().is(204)).andReturn();

		// ????????????????????????

		System.out.println("================?????????????????????================");
		result = mvc
				.perform(MockMvcRequestBuilders.put(String.format(updateCurrencyNameUrl, "AED"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "????????????????????????????????????"))))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("AED", response.getCode());
		assertEquals("????????????????????????????????????", response.getName());

		// ?????????????????????????????????
		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);
		assertEquals("AED", response.getCode());
		assertEquals("????????????????????????????????????", response.getName());

		// test delete currency lang
		result = mvc
				.perform(MockMvcRequestBuilders.delete(String.format(deleteCurrencyNameUrl, "AED"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(204)).andReturn();

		// ?????????????????????
		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "TWD"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);
		assertEquals("TWD", response.getCode());
		assertEquals("?????????", response.getName());

	}

	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}

	public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
		return new Gson().fromJson(json, classOfT);
	}

}
