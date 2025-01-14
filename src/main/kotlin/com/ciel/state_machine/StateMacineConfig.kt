package com.ciel.state_machine

import java.util.EnumSet
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.listener.StateMachineListener
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State

enum class States {
  S1,
  S2,
  S3
}

enum class Events {
  E1,
  E2,
  E3
}

@Configuration
@EnableStateMachine
class StaticMachineConfig : EnumStateMachineConfigurerAdapter<States, Events>() {
  private val log = LoggerFactory.getLogger(StaticMachineConfig::class.java)
  @Throws(Exception::class)
  override fun configure(config: StateMachineConfigurationConfigurer<States, Events>) {
    config.withConfiguration().autoStartup(true).listener(listener())
  }
  @Throws(Exception::class)
  override fun configure(states: StateMachineStateConfigurer<States, Events>) {
    states.withStates().initial(States.S1).states(EnumSet.allOf(States::class.java))
  }
  @Throws(Exception::class)
  override fun configure(transitions: StateMachineTransitionConfigurer<States, Events>) {
    transitions
            .withExternal()
            .source(States.S1)
            .target(States.S1)
            .event(Events.E1)
            .action(executeAction())
            .and()
            .withExternal()
            .source(States.S1)
            .target(States.S2)
            .event(Events.E2)
            .action(executeAction())
            .and()
            .withExternal()
            .source(States.S2)
            .target(States.S3)
            .event(Events.E3)
            .action(executeAction())
            .and()
            .withExternal()
            .source(States.S2)
            .target(States.S1)
            .event(Events.E1)
            .action(executeAction())
  }
  @Bean
  fun listener(): StateMachineListener<States, Events> {
    return object : StateMachineListenerAdapter<States, Events>() {
      override fun stateChanged(from: State<States, Events>?, to: State<States, Events>?) {
        log.info("State changed from ${from?.id} to ${to?.id}")
      }
    }
  }
  @Bean
  fun initAction(): Action<States, Events> {
    return Action { context ->
      log.info("Action initialized ${context.target.id}")
      context
    }
  }
  @Bean
  fun executeAction(): Action<States, Events> {
    return Action { context ->
      log.info("Action executed ${context.target.id}")
	    var state = context.stateMachine.state
			if (state != null) {
				log.info("Current state ${state.id}")
			} else {
				log.info("Current state is null")
			}
			var approvals: Int = context.extendedState.variables["approvals"] as Int? ?:  0
			approvals++
			context.extendedState.variables["approvals"] = approvals
			log.info("Approvals: $approvals")
			context
    }
  }
}
