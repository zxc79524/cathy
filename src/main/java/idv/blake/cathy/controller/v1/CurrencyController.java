package idv.blake.cathy.controller.v1;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import idv.blake.cathy.model.currency.CurrencyServiceV1;
import idv.blake.cathy.model.entity.currency.CurrencyLangRequest;
import idv.blake.cathy.model.entity.currency.CurrencyLangResponse;
import idv.blake.cathy.model.exception.DuplicateDataException;
import idv.blake.cathy.model.exception.InvalidArgumentException;
import idv.blake.cathy.model.exception.NotFoundException;

@RestController
@Controller("CurrencyControllerV1")
@Scope("prototype")
@RequestMapping("/v1/currency")
public class CurrencyController {

	@Autowired
	@Resource(name = "CurrencyServiceV1")
	private CurrencyServiceV1 currencyServiceV1;

//	/**
//	 * 
//	 * 呼叫coindesk API
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/coindesk/currentprice", method = { RequestMethod.GET })
//	@ResponseBody
//	public CoindeskCurrentPriceResponse getCoindeskCurrentPrice(HttpServletRequest request,
//			HttpServletResponse response) {
//
//		try {
//			return currencyServiceV1.getCoindeskCurrentPrice();
//		} catch (Exception e) {
//			response.setStatus(500);
//			return null;
//		}
//	}
//
//	/**
//	 * 
//	 * 呼叫coindesk 轉換 API
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/coindesk/currentprice/convert", method = { RequestMethod.GET })
//	@ResponseBody
//	public CoindeskCurrrentPriceConvertResponse getCoindeskCurrentPriceConvert(HttpServletRequest request,
//			HttpServletResponse response) {
//
//		try {
//			return currencyServiceV1.getCoindeskCurrentPriceConvet();
//		} catch (NotFoundException e) {
//			response.setStatus(204);
//			return null;
//
//		} catch (Exception e) {
//			response.setStatus(500);
//			return null;
//		}
//	}

	/**
	 * 
	 * 建立幣別資料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lang", method = { RequestMethod.POST })
	@ResponseBody
	public void createCurrencyName(@RequestBody CurrencyLangRequest requestBody, HttpServletRequest request,
			HttpServletResponse response) {

		try {

			currencyServiceV1.createCurrencyName(requestBody);

		} catch (InvalidArgumentException e) {
			response.setStatus(419);
			e.printStackTrace();

		} catch (DuplicateDataException e) {
			response.setStatus(409);
			e.printStackTrace();

		} catch (Exception e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 取得幣別資料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lang/{code}/{lang}", method = { RequestMethod.GET })
	@ResponseBody
	public CurrencyLangResponse getCurrencyName(@PathVariable("code") String code, @PathVariable("lang") String lang,
			HttpServletRequest request, HttpServletResponse response) {

		try {

			return currencyServiceV1.getCurrencyLang(code, lang);

		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			response.setStatus(419);

		} catch (NotFoundException e) {
			e.printStackTrace();
			response.setStatus(204);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
		}

		return null;
	}

	/**
	 * 
	 * 更新幣別資料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lang/{code}/{lang}", method = { RequestMethod.PUT })
	@ResponseBody
	public CurrencyLangResponse updateCurrencyName(@PathVariable("code") String code, @PathVariable("lang") String lang,
			@RequestBody CurrencyLangRequest requestBody, HttpServletRequest request, HttpServletResponse response) {

		try {
			return currencyServiceV1.updateCurrencyLang(code, lang, requestBody);

		} catch (InvalidArgumentException e) {
			response.setStatus(419);
			e.printStackTrace();

		} catch (NotFoundException e) {
			response.setStatus(204);
			e.printStackTrace();

		} catch (Exception e) {
			response.setStatus(500);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 刪除幣別資料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lang/{code}/{lang}", method = { RequestMethod.DELETE })
	@ResponseBody
	public void deleteCurrencyName(@PathVariable("code") String code, @PathVariable("lang") String lang,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			currencyServiceV1.deleteCurrencyLang(code, lang);

		} catch (InvalidArgumentException e) {
			response.setStatus(419);

		} catch (EmptyResultDataAccessException e) {
			response.setStatus(204);

		} catch (Exception e) {
			response.setStatus(500);
		}

	}

}