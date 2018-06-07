# route listener test
Test listening to RabbitMQ using Apache Camel

- A camel route reads from rabbit (see config info in application.properties)
- The message type is extracted from the JSON message
- Depending on the message type, the specific message processor is called.
- If the message contained a REPLY_TO header, then camel should send the reply automatically.

