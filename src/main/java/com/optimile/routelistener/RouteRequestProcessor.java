package com.optimile.routelistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RouteRequestProcessor implements Processor {
	final public static String MESSAGE_TYPE = "RouteRequest";
	private ObjectMapper objectMapper;

	@Autowired
	public RouteRequestProcessor() {
		this.objectMapper = new ObjectMapper();
	}

	public void process(Exchange exchange) throws IOException {
		exchange.setOut(exchange.getIn());
		RouteRequestMessage routeRequestMessage = objectMapper.readValue(exchange.getIn().getBody(String.class), RouteRequestMessage.class);
		//calculate the path and response in a Json String
		RouteReply reply = new RouteReply("RouteReply");
		exchange.getOut().setBody(objectMapper.writeValueAsString(reply));
	}
}

class RouteReply {
	private String message;

	public RouteReply() {
	}

	public RouteReply(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

class Location {
	public Location() {
	}

	private Double lat, lon;

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}
}

class RouteRequestMessage {
	public RouteRequestMessage() {
	}

	private String message;
	private Location start, end;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}
}
