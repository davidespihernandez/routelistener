package com.optimile.routelistener;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class RabbitRouteBuilder extends RouteBuilder {

	private final MessageTypeProcessor messageTypeProcessor;
	private final RouteRequestProcessor routeRequestProcessor;

	@Autowired
	public RabbitRouteBuilder(MessageTypeProcessor messageTypeProcessor,
							  RouteRequestProcessor routeRequestProcessor) {
		this.messageTypeProcessor = messageTypeProcessor;
		this.routeRequestProcessor = routeRequestProcessor;
	}

	@Override
	public void configure() {
		// @formatter:off
		from("rabbitmq:{{spring.rabbitmq.exchange}}?hostname={{spring.rabbitmq.host}}" +
				"&portNumber={{spring.rabbitmq.port}}" +
				"&queue={{spring.rabbitmq.queue}}" +
				"&username={{spring.rabbitmq.username}}" +
				"&password={{spring.rabbitmq.password}}" +
				"&routingKey={{spring.rabbitmq.routingKey}}" +
				"&prefetchSize=1&autoAck=false&autoDelete=false")
			.setExchangePattern(ExchangePattern.InOut)
			.process(messageTypeProcessor)
			.log("Received: ${body}")
			.log("Message type: ${in.header.MESSAGE_TYPE}")
			.choice()
				.when(header(MessageTypeProcessor.MESSAGE_TYPE).isEqualTo(RouteRequestProcessor.MESSAGE_TYPE))
					.process(routeRequestProcessor)
				.otherwise()
					.log("Wrong message type: ${in.header.MESSAGE_TYPE}")
			.end()
			//reply the message
			.log("Response body is: ${body}")
			//rabbit consumer should send the reply message if the message contains a REPLY_TO header
			.to("mock:result");
		// @formatter:on
	}
}
