/**
 *	 Thermostat
 *
 *	Author: SmartThings
 *	Date: 2013-12-02
 */
metadata {
	// Automatically generated. Make future change here.
	definition (name: "Fidure Thermostat", namespace: "smartthings", author: "SmartThings") {
		capability "Actuator"
		capability "Temperature Measurement"
		capability "Thermostat"
		capability "Configuration"
		capability "Refresh"
		capability "Sensor"
        capability "Polling"

    attribute "modeStatus", "string"

	command "setThermostatTime"

	fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0201,0204,0B05", outClusters: "000A, 0019"

	}

	// simulator metadata
	simulator { }
  // pref
     preferences {
 		input "tempOffset", "number", title: "Temperature Offset", description: "Adjust temperature by this many degrees", range: "*..*", displayDuringSetup: false
        input ("sync_clock", "boolean", title: "Synchronize Thermostat Clock Automatically?", options: ["Yes","No"])

 	}

	tiles {
		valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label:'${currentValue}°', unit:"F",
				backgroundColors:[
        [value: 0, color: "#153591"],
				[value: 7, color: "#1e9cbb"],
				[value: 15, color: "#90d2a7"],
				[value: 23, color: "#44b621"],
				[value: 29, color: "#f1d801"],
				[value: 35, color: "#d04e00"],
				[value: 36, color: "#bc2323"],
									// fahrenheit range
				[value: 37, color: "#153591"],
				[value: 44, color: "#1e9cbb"],
				[value: 59, color: "#90d2a7"],
				[value: 74, color: "#44b621"],
				[value: 84, color: "#f1d801"],
				[value: 95, color: "#d04e00"],
				[value: 96, color: "#bc2323"]
        ]
			)
		}
		standardTile("mode", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
			state "off",   action:"thermostat.setThermostatMode", icon:"st.thermostat.heating-cooling-off"
			state "cool",  action:"thermostat.setThermostatMode", icon:"st.thermostat.cool"
			state "heat",  action:"thermostat.setThermostatMode", icon:"st.thermostat.heat"
			state "emergencyHeat", label:'${name}', action:"thermostat.setThermostatMode", icon:"st.thermostat.auto"
		}
		standardTile("fanMode", "device.thermostatFanMode", inactiveLabel: false, decoration: "flat") {
			state "fanAuto", label:'${name}', action:"thermostat.setThermostatFanMode"
			state "fanOn", label:'${name}', action:"thermostat.setThermostatFanMode"
		}

    standardTile("hvacStatus", "modeStatus", inactiveLabel: false, decoration: "flat") {
				state "Resting",  label: 'Resting'
				state "Heating",  icon:"st.thermostat.heating"
				state "Cooling",  icon:"st.thermostat.cooling"
		}


		controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setHeatingSetpoint", action:"thermostat.setHeatingSetpoint", backgroundColor:"#d04e00"
		}
		valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "heat", label:'${currentValue}° heat', unit:"F", backgroundColor:"#ffffff"
		}
		controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setCoolingSetpoint", action:"thermostat.setCoolingSetpoint", backgroundColor: "#1e9cbb"
		}
		valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "cool", label:'${currentValue}° cool', unit:"F", backgroundColor:"#ffffff"
		}
		standardTile("refresh", "device.temperature", inactiveLabel: false, decoration: "flat") {
			state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat") {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}
		main "temperature"
		details(["temperature", "mode", "hvacStatus", "heatSliderControl", "heatingSetpoint", "coolSliderControl", "coolingSetpoint", "refresh", "configure"])
	}
}


// parse events into attributes
def parse(String description) {
	log.debug "Parse description $description"
	def map = [:]
	if (description?.startsWith("read attr -")) {
		def descMap = parseDescriptionAsMap(description)
		log.debug "Desc Map: $descMap"
		if (descMap.cluster == "0201" && descMap.attrId == "0000") {
			log.debug "TEMP"
			map.name = "temperature"
			map.value = getTemperature(descMap.value)
		} else if (descMap.cluster == "0201" && descMap.attrId == "0011") {
			log.debug "COOLING SETPOINT"
			map.name = "coolingSetpoint"
			map.value = getTemperature(descMap.value)
		} else if (descMap.cluster == "0201" && descMap.attrId == "0012") {
			log.debug "HEATING SETPOINT"
			map.name = "heatingSetpoint"
			map.value = getTemperature(descMap.value)
		} else if (descMap.cluster == "0201" && descMap.attrId == "001c") {
			log.debug "MODE"
			map.name = "thermostatMode"
			map.value = getModeMap()[descMap.value]
		} else if (descMap.cluster == "0202" && descMap.attrId == "0000") {
			log.debug "FAN MODE"
			map.name = "thermostatFanMode"
			map.value = getFanModeMap()[descMap.value]
		} else if (descMap.cluster == "0201" && descMap.attrId == "0029") {
      log.debug "Mode Status"
      map.name = "modeStatus"
      map.value = getModeStatus(descMap.value)
    }
	}

	def result = null
	if (map) {
		result = createEvent(map)
	}
	log.debug "Parse returned $map"
	return result
}

def parseDescriptionAsMap(description) {
	(description - "read attr - ").split(",").inject([:]) { map, param ->
		def nameAndValue = param.split(":")
		map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}
}

def getModeMap() { [
	"00":"off",
	"03":"cool",
	"04":"heat"
]}

def getFanModeMap() { [
	"04":"fanOn",
	"05":"fanAuto"
]}

def getModeStatus(value)
{
	String[] m = [ "Heating", "Cooling", "Fan", "Heat2", "Cool2", "Fan2", "Fan3"]
	String desc = 'Resting'
		value = Integer.parseInt(value, 16)

		// only check for 1-stage  for A1730
	for ( i in 0..2 ) {
		if (value & 1 << i)
			desc = m[i]
	}

	desc
}

def refresh()
{
	log.debug "refresh called"
	[
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x11", "delay 200",
  		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x12", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x1C", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x29"
	]
}

def poll() {
	log.debug "Executing 'poll'"
	refresh()
}

def getTemperature(value) {
	def celsius = Integer.parseInt(value, 16) / 100
	if(getTemperatureScale() == "C"){
		return celsius as Integer
	} else {
		return celsiusToFahrenheit(celsius) as Integer
	}
}

def setHeatingSetpoint(degrees) {
	def temperatureScale = getTemperatureScale()

	def degreesInteger = degrees as Integer
	log.debug "setHeatingSetpoint({$degreesInteger} ${temperatureScale})"
	sendEvent("name":"heatingSetpoint", "value":degreesInteger)

	def celsius = (getTemperatureScale() == "C") ? degreesInteger : (fahrenheitToCelsius(degreesInteger) as Double).round(2)
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x12 0x29 {" + hex(celsius*100) + "}"
}

def setCoolingSetpoint(degrees) {
	def degreesInteger = degrees as Integer
	log.debug "setCoolingSetpoint({$degreesInteger} ${temperatureScale})"
	sendEvent("name":"coolingSetpoint", "value":degreesInteger)
	def celsius = (getTemperatureScale() == "C") ? degreesInteger : (fahrenheitToCelsius(degreesInteger) as Double).round(2)
	[
    "st wattr 0x${device.deviceNetworkId} 1 0x201 0x11 0x29 {" + hex(celsius*100) + "}",
    "info"
  ] //+ setThermostatTime()
}

def modes() {
	["off", "heat", "cool"]
}

def setThermostatMode() {
	log.debug "switching thermostatMode"
	def currentMode = device.currentState("thermostatMode")?.value
	def modeOrder = modes()
	def index = modeOrder.indexOf(currentMode)
	def next = index >= 0 && index < modeOrder.size() - 1 ? modeOrder[index + 1] : modeOrder[0]
	log.debug "switching mode from $currentMode to $next"
	"$next"()
}

def setThermostatFanMode() {
	log.debug "Switching fan mode"
	def currentFanMode = device.currentState("thermostatFanMode")?.value
	log.debug "switching fan from current mode: $currentFanMode"
	def returnCommand

	switch (currentFanMode) {
		case "fanAuto":
			returnCommand = fanOn()
			break
		case "fanOn":
			returnCommand = fanAuto()
			break
	}
	if(!currentFanMode) { returnCommand = fanAuto() }
	returnCommand
}

def setThermostatMode(String value) {
	log.debug "setThermostatMode({$value})"
	"$value"()
}

def setThermostatFanMode(String value) {
	log.debug "setThermostatFanMode({$value})"
	"$value"()
}

def off() {
	log.debug "off"
	sendEvent("name":"thermostatMode", "value":"off")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {00}"
}

def cool() {
	log.debug "cool"
	sendEvent("name":"thermostatMode", "value":"cool")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {03}"
}

def heat() {
	log.debug "heat"
	sendEvent("name":"thermostatMode", "value":"heat")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {04}"
}

def emergencyHeat() {
	log.debug "emergencyHeat"
	sendEvent("name":"thermostatMode", "value":"emergency heat")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {05}"
}

def on() {
	fanOn()
}

def fanOn() {
	log.debug "fanOn"
	sendEvent("name":"thermostatFanMode", "value":"fanOn")
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {04}"
}

def auto() {
	fanAuto()
}

def fanAuto() {
	log.debug "fanAuto"
	sendEvent("name":"thermostatFanMode", "value":"fanAuto")
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {05}"
}

def updated()
{
    log.debug "updated, scheduling set time"
    // run every two minutes
    //schedule("0 0/2 * * * ?", setThermostatTime)
    //unschedule()
    //log.trace "commands: "  + device
}

def setThermostatTime()
{

  log.debug "setting time called.  Sending to: ${device.deviceNetworkId}"

  if ((settings.sync_clock ?: "No") == "No")
    {
      log.debug "sync time is disabled, leaving"
      return []
    }

  //Calendar calendar = Calendar.getInstance();
  //calendar.set(Calendar.MILLISECOND, 0); // Clear the millis part. Silly API.
  //calendar.set(2000, 0, 1, 0, 0, 0); // Note that months are 0-based
  // java zigbee epoch 946684800000
  //

  Date date = new Date();
  String zone = location.timeZone.getRawOffset() + " DST " + location.timeZone.getDSTSavings();

	log.debug "sync time for ${device.deviceNetworkId} at ${date} location: ${zone}"

	long millis = date.getTime(); // Millis since Unix epoch
  millis -= 946684800000;  // adjust for ZigBee EPOCH
  // adjust for time zone and DST offset
	millis += location.timeZone.getRawOffset() + location.timeZone.getDSTSavings();
	//convert to seconds
	millis /= 1000;

	// print to a string for hex capture
  String s = String.format("%08X", millis);
	// hex capture for message format
  String data = " " + s.substring(6, 8) + " " + s.substring(4, 6) + " " + s.substring(2, 4)+ " " + s.substring(0, 2);

	log.trace "time data: ${data}"
	[
  "raw 0x201 {04 21 11 00 02 0f 00 23 ${data} }", "delay 100",
  "send 0x${device.deviceNetworkId} 1 1", "delay 200",
  ]
}


def configure() {

	log.debug "binding to Thermostat and Fan Control cluster"
	[
		"zdo bind 0x${device.deviceNetworkId} 1 1 0x201 {${device.zigbeeId}} {}", "delay 200",
		"zdo bind 0x${device.deviceNetworkId} 1 1 0x204 {${device.zigbeeId}} {}", "delay 200",
        //"zcl global send-me-a-report [cluster:2] [attributeId:2] [dataType:1] [minReportTime:2] [maxReport-Time:2] [reportableChange:-1]"
  		"zcl global send-me-a-report 0x201 0x0000 0x29 20 30 {01}", "delay 100",
  		"send 0x${device.deviceNetworkId} 1 1", "delay 200",

]
}

private hex(value) {
	new BigInteger(Math.round(value).toString()).toString(16)
}
