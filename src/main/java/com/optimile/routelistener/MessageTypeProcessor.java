package com.optimile.routelistener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageTypeProcessor implements Processor {
	final public static String MESSAGE_TYPE = "MESSAGE_TYPE";
	private ObjectMapper objectMapper;

	public MessageTypeProcessor() {
		this.objectMapper = new ObjectMapper();
	}

	public void process(Exchange exchange) throws IOException {
		exchange.setOut(exchange.getIn());
		JsonNode rootNode = objectMapper.readTree(exchange.getIn().getBody(String.class));
		JsonNode messageNode = rootNode.path("message");
		exchange.getOut().setHeader(MESSAGE_TYPE, messageNode.asText());
	}
}
