package com.ciel.state_machine

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.messaging.support.MessageBuilder
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@RestController
class StateMachineController(
		@Autowired val stateMachineFactory: StateMachineFactory<States,Events>,
    val machines: MutableMap<String,StateMachine<States,Events>> = mutableMapOf()
) {
		private val log = LoggerFactory.getLogger(this.javaClass)
		@RequestMapping("/sendEvent")
		fun sendEvent(@RequestParam(value = "id") id: String, @RequestParam(value = "event") event: String): String {
			val stateMachine = getStateMachine(id)
			log.info("sendEvent: $event")
			when (event) {
				"e1" -> stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E1).build())).subscribe()
				"e2" -> stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E2).build())).subscribe()
				"e3" -> stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.E3).build())).subscribe()
			}
			return "state-machine"
		}
		@RequestMapping("/")
		fun home(): String {
			log.info("all states: $machines")
			return "redirect:/state-machine"
		}
		@RequestMapping("/state")
		fun stateMachine(@RequestParam(value = "id") id: String): String {
			val stateMachine = getStateMachine(id)
			log.info("stateMachine: $stateMachine")
			return "state-machine"
		}
		private fun getStateMachine(id: String): StateMachine<States,Events> {
			return machines[id] ?: {
				val stateMachine = stateMachineFactory.getStateMachine(id)
				machines["statemachine"] = stateMachine
				stateMachine
			}()
		}
}
