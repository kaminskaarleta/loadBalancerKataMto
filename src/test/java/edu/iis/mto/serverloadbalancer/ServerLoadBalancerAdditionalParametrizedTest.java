package edu.iis.mto.serverloadbalancer;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasLoadPercentageOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
import static edu.iis.mto.serverloadbalancer.VmBuilder.vm;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class ServerLoadBalancerAdditionalParametrizedTest extends ServerLoadBalancerBaseTest {

	private Integer[] serverCapacities;
	private Integer[] vmSizes;
	private Double[] expectedLoads;
	private Integer[][] expectedVmsOnServer;

	@Parameters
	public static Collection<Object[]> numbers() {
		return Arrays.asList(new Object[][] {
				{ 
					new Integer[] { 2, 5, 6 }, 
					new Integer[] { 4, 1, 1, 3 },
					new Double[] { 50.0, 80.0, 66.66 },
					new Integer[][] { { 1 }, { 0 }, { 2, 3 } }, 
				},
				{ 
					new Integer[] { 6, 5, 3 }, 
					new Integer[] { 1, 2, 2, 1 },					
					new Double[] { 33.33, 40.0, 66.66 },
					new Integer[][] { { 0, 3 }, { 1 }, { 2 } }, 
				},
				{ 
					new Integer[] { 3, 4, 5 }, 
					new Integer[] { 1, 2, 3, 1, 1 },					
					new Double[] { 66.66, 75.0, 60.0 },					
					new Integer[][] { { 0, 3 }, { 1, 4 }, { 2 } }, } 
				});
	}

	public ServerLoadBalancerAdditionalParametrizedTest(
			Integer[] serverCapacities, Integer[] vmSizes, Double[] expected,
			Integer[][] expectedVmsOnServer) {
		this.serverCapacities = serverCapacities;
		this.vmSizes = vmSizes;
		this.expectedLoads = expected;
		this.expectedVmsOnServer = expectedVmsOnServer;
	}

	@Test
	public void balance_serversAndVms() {
		Server[] servers = getServers();
		Vm[] vms = getVms();

		balance(servers, vms);

		for (int i=0; i < expectedLoads.length; i++) {
			assertThat(servers[i], hasLoadPercentageOf(expectedLoads[i]));
		}
		
		for (int i=0; i < expectedVmsOnServer.length; i++) {
			for (int j=0; j < expectedVmsOnServer[i].length; j++) {
				assertThat("Server " + i + " contain the vm " + expectedVmsOnServer[i][j],
						servers[i].contains(vms[expectedVmsOnServer[i][j]]));
			}
		}
	}

	private Server[] getServers() {
		Server[] servers = new Server[serverCapacities.length];

		for (int i = 0; i < serverCapacities.length; i++) {
			servers[i] = server().withCapacity(serverCapacities[i]).build();
		}

		return servers;
	}

	private Vm[] getVms() {
		Vm[] vms = new Vm[vmSizes.length];
		
		for (int i = 0; i < vmSizes.length; i++) {
			vms[i] = vm().ofSize(vmSizes[i]).build();
		}

		return vms;
	}
}