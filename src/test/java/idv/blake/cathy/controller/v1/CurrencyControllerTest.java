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
	private String getCurrencyNameUrl = "/v1/currency/lang/%s/%s";
	private String updateCurrencyNameUrl = "/v1/currency/lang/%s/%s";
	private String deleteCurrencyNameUrl = "/v1/currency/lang/%s/%s";
	private String coindeskCurrentPrice = "/v1/currency/coindesk/currentprice";
	private String coindeskCurrentPriceConvert = "/v1/currency/coindesk/currentprice/convert";

	@Autowired
	@Resource(name = "CurrencyServiceV1")
	private CurrencyServiceV1 currencyServiceV1;

	/**
	 * 測試呼叫coindesk API
	 * 
	 * @throws Exception
	 */
	void testGetCoindeskCurrentPrice() throws Exception {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// 測試是否有成功抓取coindesk資料
		System.out.println("================coindesk API 內容===============");
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get(coindeskCurrentPrice).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
		Assert.assertEquals(false, StringUtil.isEmpty(result.getResponse().getContentAsString()));

		CoindeskCurrentPriceResponse sourceResponse = fromJson(
				result.getResponse().getContentAsString(StandardCharsets.UTF_8), CoindeskCurrentPriceResponse.class);

		// 測試轉換後的資料

		// 新增歐元幣別
		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("EUR", "zh_TW", "歐元"))))
				.andExpect(status().is(200)).andReturn();

		System.out.println("================coindesk API 轉換內容================");
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
		assertEquals("歐元", response.getCurrency().get("EUR").getName());
		assertNotNull(response.getCurrency().get("USD"));
		assertEquals("USD", response.getCurrency().get("USD").getName());

		// 更新歐元
		result = mvc
				.perform(MockMvcRequestBuilders.put(String.format(updateCurrencyNameUrl, "EUR"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("EUR", "zh_TW", "歐元2"))))
				.andExpect(status().is(200)).andReturn();

		result = mvc.perform(MockMvcRequestBuilders.get(coindeskCurrentPriceConvert).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CoindeskCurrrentPriceConvertResponse.class);

		assertEquals("歐元2", response.getCurrency().get("EUR").getName());

		// 移除歐元幣別
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
	 * 測試Currency Lang CURD
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCurrencyLang() throws Exception {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// create currency lang1

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest(null, null, null))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", null, null))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest(null, null, "阿拉伯聯合酋長國迪拉姆國"))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AEDA", "zh_TW", "阿拉伯聯合酋長國迪拉姆國"))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "zh_TW", "一二三四五一二三四五一二三四五一"))))
				.andExpect(status().is(419)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "zh_TW", "一二三四五一二三四五一二三四五"))))
				.andExpect(status().is(200)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "zh_CN", "阿拉伯联合酋长国迪拉姆国"))))
				.andExpect(status().is(200)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "zh_TW", "阿拉伯聯合酋長國迪拉姆國"))))
				.andExpect(status().is(409)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("TWD", "zh_TW", "新台幣"))))
				.andExpect(status().is(200)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.post(createCurrencyNameUrl).accept(MediaType.APPLICATION_JSON)
						.header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("TWD", "zh_CN", "新台币"))))
				.andExpect(status().is(200)).andReturn();

		// get currency lang

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "USD", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(204)).andReturn();

		System.out.println("=============取的幣別資料內容=============");
		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		CurrencyLangResponse response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("AED", response.getCode());
		assertEquals("zh_TW", response.getLang());
		assertEquals("一二三四五一二三四五一二三四五", response.getName());

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED", "zh_CN"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("AED", response.getCode());
		assertEquals("zh_CN", response.getLang());
		assertEquals("阿拉伯联合酋长国迪拉姆国", response.getName());

		// update currency lang
		result = mvc
				.perform(MockMvcRequestBuilders.put(String.format(updateCurrencyNameUrl, "USD", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("USD", "zh_TW", "美元"))))
				.andExpect(status().is(204)).andReturn();

		// 確認更新後的資料

		System.out.println("================更新後資料內容================");
		result = mvc
				.perform(MockMvcRequestBuilders.put(String.format(updateCurrencyNameUrl, "AED", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON)
						.content(toJson(new CurrencyLangRequest("AED", "zh_TW", "阿拉伯聯合酋長國迪拉姆國"))))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("zh_TW", response.getLang());
		assertEquals("AED", response.getCode());
		assertEquals("阿拉伯聯合酋長國迪拉姆國", response.getName());

		// 取得資料確定是否有更新
		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);
		assertEquals("AED", response.getCode());
		assertEquals("阿拉伯聯合酋長國迪拉姆國", response.getName());

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED", "zh_CN"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("AED", response.getCode());
		assertEquals("zh_CN", response.getLang());
		assertEquals("阿拉伯联合酋长国迪拉姆国", response.getName());

		// test delete currency lang
		result = mvc
				.perform(MockMvcRequestBuilders.delete(String.format(deleteCurrencyNameUrl, "AED", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(204)).andReturn();

		// 檢查是否有誤刪
		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "TWD", "zh_TW"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();
		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);
		assertEquals("TWD", response.getCode());
		assertEquals("新台幣", response.getName());

		result = mvc
				.perform(MockMvcRequestBuilders.get(String.format(getCurrencyNameUrl, "AED", "zh_CN"))
						.accept(MediaType.APPLICATION_JSON).header("Content-TYPE", MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn();

		response = fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
				CurrencyLangResponse.class);

		assertEquals("AED", response.getCode());
		assertEquals("zh_CN", response.getLang());
		assertEquals("阿拉伯联合酋长国迪拉姆国", response.getName());

	}

	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}

	public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
		return new Gson().fromJson(json, classOfT);
	}

}
