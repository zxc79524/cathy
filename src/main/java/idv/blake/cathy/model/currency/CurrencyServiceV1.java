package idv.blake.cathy.model.currency;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import idv.blake.cathy.lib.restful_engine.Restful.WebServiceListener;
import idv.blake.cathy.lib.restful_engine.RestfulEngine;
import idv.blake.cathy.lib.restful_engine.ServiceException;
import idv.blake.cathy.model.currency.dao.ICurrencyLangDao;
import idv.blake.cathy.model.entity.coindesk.CoindeskBpiResponse;
import idv.blake.cathy.model.entity.coindesk.CoindeskCurrentPriceResponse;
import idv.blake.cathy.model.entity.coindesk.convert.CoindeskBpiConvetrtResponse;
import idv.blake.cathy.model.entity.coindesk.convert.CoindeskCurrrentPriceConvertResponse;
import idv.blake.cathy.model.entity.currency.CurrencyLangDbEntity;
import idv.blake.cathy.model.entity.currency.CurrencyLangRequest;
import idv.blake.cathy.model.entity.currency.CurrencyLangResponse;
import idv.blake.cathy.model.exception.DuplicateDataException;
import idv.blake.cathy.model.exception.InvalidArgumentException;
import idv.blake.cathy.model.exception.NotFoundException;
import idv.blake.cathy.model.webservice.CoinDeskCurrentPriceWebService;
import idv.blake.cathy.util.StringUtil;

@Service("CurrencyServiceV1")
@Scope("prototype")
public class CurrencyServiceV1 {

	@Autowired
	ICurrencyLangDao dao;

	/**
	 * 建立更新資料
	 * 
	 * @param request
	 * @return
	 * @throws InvalidArgumentException
	 * @throws DuplicateDataException
	 */
	public boolean createCurrencyName(CurrencyLangRequest request)
			throws InvalidArgumentException, DuplicateDataException {
		if (request == null || StringUtil.isEmpty(request.getCode(), request.getName(), request.getLang())) {
			throw new InvalidArgumentException("Pls input valid argument!");
		}

		if (request.getCode().length() != 3) {
			throw new InvalidArgumentException(String.format("code [%s] format fail", request.getCode()));
		}

		if (request.getName().length() > 15) {
			throw new InvalidArgumentException(String.format("name [%s] length > 15", request.getName()));
		}

		CurrencyLangDbEntity result = dao.findByCodeAndLang(request.getCode(), request.getLang());

		if (result != null) {
			throw new DuplicateDataException(
					String.format("code [%s] lang [%s] data already exists", request.getCode(), request.getLang()));
		}

		dao.save(new CurrencyLangDbEntity(request.getCode(), request.getLang(), request.getName()));

		return true;

	}

	/**
	 * 取得幣別資料
	 * 
	 * @param code
	 * @return
	 * @throws InvalidArgumentException
	 * @throws NotFoundException
	 */
	public CurrencyLangResponse getCurrencyLang(String code, String lang)
			throws InvalidArgumentException, NotFoundException {

		if (StringUtil.isEmpty(code, lang)) {
			throw new InvalidArgumentException("Pls input valid argument!");
		}

		if (code.length() != 3) {
			throw new InvalidArgumentException(String.format("code [%s] fformat fail", code));
		}

		CurrencyLangDbEntity result = dao.findByCodeAndLang(code, lang);

		if (result == null) {
			throw new NotFoundException(String.format("code [%s] not found data", code));
		}

		return new CurrencyLangResponse(result.getCode(), result.getLang(), result.getName());

	}

	/**
	 * 更新幣別資料
	 * 
	 * @param code
	 * @return
	 * @throws InvalidArgumentException
	 * @throws NotFoundException
	 */
	public CurrencyLangResponse updateCurrencyLang(String code, String lang, CurrencyLangRequest request)
			throws InvalidArgumentException, NotFoundException {

		if (StringUtil.isEmpty(code, request.getName(), request.getLang()) || request == null) {
			throw new InvalidArgumentException("Pls input valid argument!");
		}

		if (code.length() != 3) {
			throw new InvalidArgumentException(String.format("code [%s] format fail", code));
		}

		CurrencyLangDbEntity result = dao.findByCodeAndLang(code, lang);

		if (result == null) {
			throw new NotFoundException(String.format("code [%s] not found data", code));
		}

		result = new CurrencyLangDbEntity(code, request.getLang(), request.getName());
		dao.save(result);

		return getCurrencyLang(code, lang);

	}

	/**
	 * 刪除幣別資料
	 * 
	 * @param code
	 * @return
	 * @throws InvalidArgumentException
	 * @throws NotFoundException
	 */
	public boolean deleteCurrencyLang(String code, String lang)
			throws InvalidArgumentException, EmptyResultDataAccessException {
		if (StringUtil.isEmpty(code)) {
			throw new InvalidArgumentException("Pls input valid argument!");
		}

		if (code.length() != 3) {
			throw new InvalidArgumentException(String.format("code [%s] format fail", code));
		}

		dao.deleteByCodeAndLang(code, lang);

		return true;

	}

	/**
	 * 取得coindesk 資料
	 * 
	 * @return
	 * @throws Exception
	 */
	public CoindeskCurrentPriceResponse getCoindeskCurrentPrice() throws Exception {

		WebserviceHelper helper = new WebserviceHelper();

		RestfulEngine.requestSync(new CoinDeskCurrentPriceWebService(helper));

		if (!helper.isSuccess()) {
			throw helper.getErrorException();
		}

		return helper.getResponse();

	}

	/**
	 * 取得coindesk 資料
	 * 
	 * @return
	 * @throws Exception
	 */
	public CoindeskCurrrentPriceConvertResponse getCoindeskCurrentPriceConvet() throws Exception {

		CoindeskCurrentPriceResponse sourceResponse = getCoindeskCurrentPrice();
		if (sourceResponse == null) {
			throw new NotFoundException("Soure data is empty");
		}

		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

		OffsetDateTime offsetDateTime = OffsetDateTime.parse(sourceResponse.getTime().getUpdatedISO(), timeFormatter);

		// 轉換時間格式
		Date updateTimeDate = Date.from(Instant.from(offsetDateTime));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.TAIWAN);
		String updateTimeString = simpleDateFormat.format(updateTimeDate);

		CoindeskCurrrentPriceConvertResponse response = new CoindeskCurrrentPriceConvertResponse();
		response.setUpdateTime(updateTimeString);

		Iterable<CurrencyLangDbEntity> dbEntities = dao.findAllById(sourceResponse.getBpi().keySet());

		// 先合併DB 有幣別資訊的
		for (CurrencyLangDbEntity dbEntity : dbEntities) {
			CoindeskBpiConvetrtResponse result = new CoindeskBpiConvetrtResponse();
			result.setCode(dbEntity.getCode());
			result.setName(dbEntity.getName());
			result.setRate(sourceResponse.getBpi().get(dbEntity.getCode()).getRate());
			result.setRateFloat(sourceResponse.getBpi().get(dbEntity.getCode()).getRate_float());
			response.getCurrency().put(dbEntity.getCode(), result);
			sourceResponse.getBpi().remove(dbEntity.getCode());
		}

		// 加上沒有幣別資訊的
		for (CoindeskBpiResponse sourceEntity : sourceResponse.getBpi().values()) {

			CoindeskBpiConvetrtResponse result = new CoindeskBpiConvetrtResponse();
			result.setCode(sourceEntity.getCode());
			result.setName(sourceEntity.getCode());
			result.setRate(sourceEntity.getRate());
			result.setRateFloat(sourceEntity.getRate_float());
			response.getCurrency().put(sourceEntity.getCode(), result);
		}

		return response;

	}

	private class WebserviceHelper implements WebServiceListener<CoindeskCurrentPriceResponse> {

		private boolean isSuccess;
		private ServiceException errorException;
		private CoindeskCurrentPriceResponse response;

		@Override
		public void onRequestFail(ServiceException arg0) {
			arg0.printStackTrace();
			isSuccess = false;
			errorException = arg0;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

		@Override
		public void onRequestSuccess(CoindeskCurrentPriceResponse entity) {
			isSuccess = true;
			response = entity;

		}

		public CoindeskCurrentPriceResponse getResponse() {
			return response;
		}

		public ServiceException getErrorException() {
			return errorException;
		}

	}
}
