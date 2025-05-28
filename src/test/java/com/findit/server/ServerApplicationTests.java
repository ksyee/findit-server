package com.findit.server;

import com.findit.server.service.external.PoliceApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ServerApplicationTests {

	@MockBean
	private PoliceApiClient policeApiClient; // Mock the client to prevent real API calls

	@Test
	void contextLoads() {
	}

}
