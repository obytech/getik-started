package com.tikal.gettingstarted.actuator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

import com.tikal.gettingstarted.actuator.ShowEndpoints.EndpointData;

@Component
public class ShowEndpoints implements Endpoint<List<EndpointData>> {
	
	private List<EndpointData> endpoints;

	@Autowired
	public ShowEndpoints(List<Endpoint<?>> endpoints) {
		List<EndpointData> temp = new ArrayList<>(endpoints.size());
		endpoints.forEach(anEndpoint -> temp.add(new EndpointData(anEndpoint.isEnabled(), anEndpoint.isSensitive(), anEndpoint.getId())));
		temp.add(new EndpointData(isEnabled(), isSensitive(), getId()));
		this.endpoints = temp;
	}

	@Override
	public List<EndpointData> invoke() {
		return endpoints;
	}

	@Override
	public String getId() {
		return "showEP";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isSensitive() {
		return true;
	}
	
	static class EndpointData {
		public boolean enabled;
		
		public boolean sensitive;
		
		public String id;

		public EndpointData(boolean enabled, boolean sensitive, String id) {
			super();
			this.enabled = enabled;
			this.sensitive = sensitive;
			this.id = id;
		}
	}
}
