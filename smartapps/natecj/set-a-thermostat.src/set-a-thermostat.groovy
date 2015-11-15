/**
 *  Copyright 2015 Nathan Jacobson <natecj@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

definition(
  name: "Set a Thermostat",
  namespace: "natecj",
  author: "Nathan Jacobson",
  description: "Change a Thermostat's heating and cooling settings between active and inactive depending on what mode is set.",
  category: "Convenience",
  iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
  iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

preferences {
  section("Global Settings") {
    input "activeThermostat", "capability.thermostat", title: "Thermostat"
  }
  section("Active Settings") {
    input "activeHeatingSetpoint", "number", title: "Heat Setting"
    input "activeCoolingSetpoint", "number", title: "Cool Setting"
    input "activeModes", "mode", title: "Mode(s)", multiple: true
  }
  section("Inactive Settings") {
    input "inactiveHeatingSetpoint", "number", title: "Heat Setting"
    input "inactiveCoolingSetpoint", "number", title: "Cool Setting"
    input "inactiveModes", "mode", title: "Mode(s)", multiple: true
  }
}

def installed() {
  initialize()
}

def updated() {
  unsubscribe()
  initialize()
}

def initialize() {
  subscribe(location, changedLocationMode)
}

def changedLocationMode(evt) {
  if (settings.activeModes?.contains(evt.value)) {
    setThermostatActive()
  } else if (settings.inactiveModes?.contains(evt.value)) {
    setThermostatInactive()
  }
}

def setThermostatActive() {
  thermostat.setHeatingSetpoint(activeHeatingSetpoint)
  thermostat.setCoolingSetpoint(activeCoolingSetpoint)
  thermostat.poll()
}

def setThermostatInactive() {
  thermostat.setHeatingSetpoint(inactiveHeatingSetpoint)
  thermostat.setCoolingSetpoint(inactiveCoolingSetpoint)
  thermostat.poll()
}
