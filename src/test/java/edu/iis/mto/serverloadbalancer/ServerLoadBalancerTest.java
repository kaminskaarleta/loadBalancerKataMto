package edu.iis.mto.serverloadbalancer;

import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasLoadPercentageOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
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
		Server theServer = a(server().withCapacity(1));
		balance(aListOfServerWith(theServer), aEmptyListOfVms());
		assertThat(theServer, hasLoadPercentageOf(0.0d));	
	}
	
	@Test
	public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm(){
		Server theServer = a(server().withCapacity(1));
		Vm theVm = a(vm().ofSize(1));
		balance(aListOfServerWith(theServer), aListOfVmsWith(theVm));
		assertThat(theServer, hasLoadPercentageOf(100.0d));	
		assertThat("the server should contain vm", theServer.contains(theVm));
		
	}

	private void balance(Server[] servers, Vm[] vms) {
		new ServerLoadBancer().balance(servers, vms);
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

}
