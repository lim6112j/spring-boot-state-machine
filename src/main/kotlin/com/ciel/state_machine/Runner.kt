package com.ciel.state_machine

import org.springframework.stereotype.Component
import org.springframework.boot.CommandLineRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.statemachine.StateMachine
import org.springframework.context.annotation.Bean
import org.springframework.messaging.support.MessageBuilder
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux
import kotlin.with

@Component
class Runner(@Autowired val statemachine : StateMachine<States, Events>) : CommandLineRunner {
		private val log = LoggerFactory.getLogger(this.javaClass)
    override fun run(vararg args: String?) {
				statemachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E1).build())).subscribe()
				statemachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E2).build())).subscribe()
				statemachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E1).build())).subscribe()
				statemachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E3).build())).subscribe()
				statemachine.sendEvents(Flux.just(
						MessageBuilder.withPayload(Events.E1).build(),
						MessageBuilder.withPayload(Events.E2).build(),
						MessageBuilder.withPayload(Events.E1).build(),
						MessageBuilder.withPayload(Events.E3).build()
				)).subscribe()
				log.info("called")
		}
}
