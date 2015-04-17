package edu.iis.mto.serverloadbalancer;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ServerLoadBalancerTest {
	@Test
	public void itCompiles() {
		assertThat(true, equalTo(true));
	}

	@Test
	public void balancingServer_noVm_ServerStaysEmpty(){
		Server theServer = a(server().withCapacity(1));
		balance(aListOfServerWith(theServer), aEmptyListOfVms());
		assertThat(theServer, hasLoadPercentageOf(0.0d));	
	}

	private void balance(Server[] servers, Vm[] vms) {
		new ServerLoadBancer().balance(servers, vms);
	}

	private Matcher<? super Server> hasLoadPercentageOf(double expectedLoadPercentage) {
		return new CurrentLoadPercentageMatcher(expectedLoadPercentage);
	}

	private Vm[] aEmptyListOfVms() {
		return new Vm[0];
	}

	private Server[] aListOfServerWith(Server theServer) {
		return new Server[]{theServer};
	}

	private Server a(ServerBuilder builder) {
		return builder.build();
	}

	private ServerBuilder server() {
		return new ServerBuilder();
	}
}
