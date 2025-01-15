package com.ciel.state_machine

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.statemachine.service.StateMachineService
import org.springframework.statemachine.StateMachine
import org.springframework.messaging.support.MessageBuilder
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono


@SpringBootTest
class StateMachineApplicationTests(@Autowired val stateMachine: StateMachine<States,Events>) {
		val log = LoggerFactory.getLogger(this.javaClass)
		// test sendEvent
		@Test
		@Throws(Exception::class)
		fun testInitial() {
			log.info("testInitial")
			assert(stateMachine.state.id == States.S1)
		}
		@Test
		@Throws(Exception::class)
		fun testSendEvent() {
			log.info("testSendEvent")
			stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E1).build()))
			stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E2).build()))
		}
}
