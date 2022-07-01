package idv.blake.cathy.model.webservice;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import idv.blake.cathy.lib.restful_engine.Restful.WebServiceListener;
import idv.blake.cathy.lib.restful_engine.RestfulEngine;
import idv.blake.cathy.lib.restful_engine.RestfulEngine.RunnerType;
import idv.blake.cathy.lib.restful_engine.ServiceException;
import idv.blake.cathy.model.entity.coindesk.CoindeskCurrentPriceResponse;

@RunWith(SpringJUnit4ClassRunner.class)
class CoinDeskCurrentPriceWebServiceTest {

	@Test
	void test() throws Exception {
		RestfulEngine.init(RunnerType.JavaSync, null);

		RestfulEngine
				.requestSync(new CoinDeskCurrentPriceWebService(new WebServiceListener<CoindeskCurrentPriceResponse>() {

					@Override
					public void onRequestSuccess(CoindeskCurrentPriceResponse entity) {
						// TODO Auto-generated method stub
//						System.out.println(new Gson().toJson(entity.getBpi()));
						Assert.assertEquals(true, true);
					}

					@Override
					public void onRequestFail(ServiceException error) {
//						error.printStackTrace();
//						System.out.println(error);
						fail();

					}
				}));

	}

}
