package edu.iis.mto.serverloadbalancer;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class ServerLoadBalancerTest {
	@Test
	public void itCompiles() {
		assertThat(true, equalTo(true));
	}

	@Test
	public void balancingServer_noVm_ServerStaysEmpty(){
		Server server = a(server().withCapacity(1));
		balance(listOfServerWith(server), emptyListOfVms());
		assertThat(server, hasLoadPercentageOf(0.0d));	
	}
}
