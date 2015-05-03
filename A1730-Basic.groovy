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

		attribute "temperatureUnit","string"
    attribute "displayTemperature","number"
		attribute "displaySetpoint", "string"
		command 	"raiseSetpoint"
		command 	"lowerSetpoint"
		attribute "upButtonState", "string"
		attribute "downButtonState", "string"

    attribute "modeStatus", "string"
		attribute "runningMode", "string"
    attribute "lockLevel", "string"

		command "setThermostatTime"
  	command "lock"

  attribute "prorgammingOperation", "number"
  attribute "prorgammingOperationDisplay", "string"
  command   "Program"

  attribute "setpointHold", "string"
	attribute "setpointHoldDisplay", "string"
	command "Hold"
  attribute "holdExpiary", "string"

	attribute "lastTimeSync", "string"

	attribute "target", "number"
	attribute "duty", "number"
	attribute "gain", "number"
	attribute "integrator", "number"
	attribute "hvacstate", "number"


	fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0201,0204,0B05", outClusters: "000A, 0019"

	}

	// simulator metadata
	simulator { }
  // pref
     preferences {

		input ("hold_time", "enum", title: "Default Hold Time in Hours",
        description: "Default Hold Duration in hours",
        range: "1..24", options: ["No Hold", "2 Hours", "4 Hours", "8 Hours", "12 Hours", "1 Day"],
        displayDuringSetup: false)
        input ("sync_clock", "boolean", title: "Synchronize Thermostat Clock Automatically?", options: ["Yes","No"])
        input ("lock_level", "enum", title: "Thermostat Screen Lock Level", options: ["Full","Mode Only", "Setpoint"])
				input ("temp_unit", "enum", title: "Temperature Unit", options: ["Celsius","Fahrenheit"])

 	}

	tiles {
		valueTile("temperature", "displayTemperature", width: 2, height: 2) {
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
			state "auto",  action:"thermostat.setThermostatMode", icon:"st.thermostat.auto"
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


    standardTile("lock", "lockLevel", inactiveLabel: false, decoration: "flat") {
        state "Unlocked",  icon:"st.Kids.kids10", action:"lock", label:'${name}'
        state "Mode Only",  icon:"st.Kids.kids19", action:"lock", label:'${name}'
        state "Setpoint",  icon:"st.Kids.kids19", action:"lock", label:'${name}'
        state "Full",  icon:"st.Kids.kids19", action:"lock", label:'${name}'
    }

		controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setHeatingSetpoint", action:"thermostat.setHeatingSetpoint", backgroundColor:"#d04e00"
		}
		valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "heat", label:'${currentValue}° heat', unit:"F", backgroundColor:"#ffffff"
		}
		controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false, range: "$min..$max") {
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


    valueTile("scheduleText", "prorgammingOperation", inactiveLabel: false, decoration: "flat", width: 2) {
        state "default", label: 'Schedule'
    }
    valueTile("schedule", "prorgammingOperationDisplay", inactiveLabel: false, decoration: "flat") {
        state "default", action:"Program", label: '${currentValue}'
    }

    valueTile("hold", "setpointHoldDisplay", inactiveLabel: false, decoration: "flat", width: 3) {
            state "setpointHold", action:"Hold", label: '${currentValue}'
		}

		valueTile("setpoint", "displaySetpoint", width: 2, height: 2)
		{
			state("displaySetpoint", label: '${currentValue}°',
				backgroundColor: "#919191")
		}

				standardTile("upButton", "upButtonState", inactiveLabel: false) {
			state "normal", action:"raiseSetpoint", backgroundColor:"#919191", icon:"st.thermostat.thermostat-up"
			state "pressed", action:"raiseSetpoint", backgroundColor:"#ff0000", icon:"st.thermostat.thermostat-up"
		}
		standardTile("downButton", "downButtonState", inactiveLabel: false) {
			state "normal", action:"lowerSetpoint", backgroundColor:"#919191", icon:"st.thermostat.thermostat-down"
			state "pressed", action:"lowerSetpoint", backgroundColor:"#ff9191", icon:"st.thermostat.thermostat-down"
		}


		main "temperature"
		details([
      "temperature", "mode", "hvacStatus",
			"setpoint","upButton","downButton",
			"scheduleText", "schedule",
	    "hold",
    "heatSliderControl", "heatingSetpoint",
     "coolSliderControl", "coolingSetpoint",
     "lock", "refresh", "configure"])
	}
}


def getMin() {
	32
}

def getMax() {
	85
}

// parse events into attributes
def parse(String description) {
	log.debug "Parse description $description"
	def map = [:]
	if (description?.startsWith("read attr -")) {
		def descMap = parseDescriptionAsMap(description)
		log.debug "Desc Map: $descMap"


    if (descMap.cluster == "0201")
		{
			switch(descMap.attrId)
			{
            case "0000":
  						log.trace "TEMP"
  						map.name = "temperature"
  						map.value = getTemperature(descMap.value)
							sendEvent("name":"displayTemperature", "value": getDisplayTemperature(descMap.value))

  					break;
            case "0005":
            //log.debug "hex time: ${descMap.value}"
            	if (descMap.encoding ==  "23")
                {
            	    	map.name = "holdExpiary"
                  	map.value = "${convertToTime(descMap.value).getTime()}"
                    log.trace "HOLD EXPIRY: ${descMap.value} is ${map.value}"

                    updateHoldLabel("HoldExp", "${map.value}")
  				}
            break;
						case "0008":
						log.trace "mfg target"
						map.name = "target"
						map.value = Integer.parseInt(descMap.value, 16)
						break;
						case "0009":
						log.trace "mfg duty"
						map.name = "duty"
						map.value = Integer.parseInt(descMap.value, 16)
						break;
						case "000A":
						log.trace "integrator"
						map.name = "integrator"
						map.value = Integer.parseInt(descMap.value, 16)
						break;
						case "000B":
						log.trace "gain"
						map.name = "gain"
						map.value = Integer.parseInt(descMap.value, 16)
						break;
						case "000C":
						log.trace "hvacstate"
						map.name = "hvacstate"
						map.value = Integer.parseInt(descMap.value, 16)
						hvacstate
						break;

  				  case "0011":
  						log.trace "COOLING SETPOINT"
  						map.name = "coolingSetpoint"
  						map.value = getDisplayTemperature(descMap.value)
							updateSetpoint(map.name,map.value)
  					break;
  					case "0012":
  						log.trace "HEATING SETPOINT"
  						map.name = "heatingSetpoint"
  						map.value = getDisplayTemperature(descMap.value)
							updateSetpoint(map.name,map.value)
  					break;
  					case "001c":
  						log.trace "MODE"
  						map.name = "thermostatMode"
  						map.value = getModeMap()[descMap.value]
							updateSetpoint(map.name,map.value)
  					break;
						case "001e":   //running mode enum8
							log.trace "running mode: ${descMap.value}"
		          map.name = "runningMode"
							map.value = getModeMap()[descMap.value]
							updateSetpoint(map.name,map.value)
						break;
            case "0023":   // setpoint hold enum8
            log.trace "setpoint Hold: ${descMap.value}"
            map.name = "setpointHold"
            map.value = getHoldMap()[descMap.value]
            updateHoldLabel("Hold", map.value)
            break;
            case "0024":   // hold duration int16u
            log.trace "setpoint Hold Duration: ${descMap.value}"
            map.name = "setpointHoldDuration"
            map.value = Integer.parseInt(descMap.value, 16)
            //sendEvent("name":"setpointHoldDurationDisplay", "value": timeLength(map.value))
            break;
            case "0025":   // thermostat programming operation bitmap8
  					log.trace "prorgamming Operation: ${descMap.value}"
  					map.name = "prorgammingOperation"
                      def val = getProgrammingMap()[Integer.parseInt(descMap.value, 16) & 0x01]
  					sendEvent("name":"prorgammingOperationDisplay", "value": val)
  					log.trace "prorgamming Operation is : ${val}"
                      map.value = descMap.value
  					break;
            case "0029":
							// relay state
              log.trace "MODE STATUS"
              map.name = "modeStatus"
              map.value = getModeStatus(descMap.value)
            break;
      }
    } else if (descMap.cluster == "0204")
    {
      if (descMap.attrId == "0001")
      {
  			log.debug "LOCK LEVEL"
  			map.name = "lockLevel"
  			map.value = getLockMap()[descMap.value]
      }
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

def getProgrammingMap() { [
	0:"Off",
	1:"On"
]}

def getModeMap() { [
	"00":"off",
	"01":"auto",
	"03":"cool",
	"04":"heat"
]}

def getFanModeMap() { [
	"04":"fanOn",
	"05":"fanAuto"
]}

def getHoldMap()
{[
	"00":"Off",
	"01":"On"
]}


def updateSetpoint(attrib, val)
{
	def cool = device.currentState("coolingSetpoint")?.value
	def heat = device.currentState("heatingSetpoint")?.value
	def runningMode = device.currentState("runningMode")?.value
	def mode = device.currentState("thermostatMode")?.value
	log.trace "cool: $cool heat: $heat running mode: $runningMode mode: $mode"

	def value = '--';


	if ("heat"  == mode && heat != null)
		value = heat;
	else if ("cool"  == mode && cool != null)
		value = cool;
    else if ("auto" == mode && runningMode == "cool" && cool != null)
    	value = cool;
    else if ("auto" == mode && runningMode == "heat" && heat != null)
    	value = heat;

	sendEvent("name":"displaySetpoint", "value": value)
}

def raiseSetpoint()
{
	sendEvent("name":"upButtonState", "value": "pressed")
	sendEvent("name":"upButtonState", "value": "normal")
	adjustSetpoint(5)
}

def lowerSetpoint()
{
	sendEvent("name":"downButtonState", "value": "pressed")
	sendEvent("name":"downButtonState", "value": "normal")
	adjustSetpoint(-5)
}

def adjustSetpoint(value)
{
	def runningMode = device.currentState("runningMode")?.value
	def mode = device.currentState("thermostatMode")?.value

		//default to both heat and cool
    def modeData = 0x02

    if ("heat" == mode || "heat" == runningMode)
    		modeData = "00"
    else if ("cool" == mode || "cool" == runningMode)
    	modeData = "01"

    def amountData = String.format("%02X", value)[-2..-1]

    log.debug "raise  mode: $modeData amout: $amountData"

	[
	"st cmd 0x${device.deviceNetworkId} 1 0x201 0 {" + modeData + " " + amountData + "}"
	] + readAttributesCommand(0x201, [
			0x11,  //cooling set point
			0x12,  //heating set point
			0x1E,  // setpoint hold
			0x23,	 // running mode
			0x29,  // relay state
			//0x30,  // set point change source
			//0x32,  // set point change timestamp
			])
}


def getDisplayTemperature(value)
{
	def celsius = Integer.parseInt(value, 16);

	log.trace "getting temperature: $celsius and unit: ${settings.temp_unit}";

	if ((settings.temp_unit ?: "Fahrenheit") == "Celsius") {
		celsius = (((celsius + 4) / 10) as Integer) / 10;
	} else {
		celsius = (celsiusToFahrenheit(celsius/10) as Integer)/ 10;
	}

	log.trace "getting temperature: " + celsius;

	return celsius;
}

def updateHoldLabel(attr, value)
{
	def currentHold = (device?.currentState("setpointHold")?.value)?: "..."

    def holdExp = device?.currentState("holdExpiary")?.value
		holdExp = holdExp?: "${(new Date()).getTime()}"

	if ("Hold" == attr)
    {
    	currentHold = value
    }


		log.trace "holdexp ${holdExp} and ${(holdExp == null)} and ${new Date().getTime()}"

		if ("HoldExp" == attr)
		{
			holdExp = value
		}
		boolean past = ( (new Date(holdExp.toLong()).getTime())  < (new Date().getTime()))

		if ("HoldExp" == attr)
		{
  			// in case currentHold is lagging, this means there actually is hold
			if (!past)
				currentHold = "On"
    }

	def holdString = (currentHold == "On")?
			( (past)? "Is On" : "Ends ${compareWithNow(holdExp.toLong())}") :
			((currentHold == "Off")? " is Off" : " ...")
    //log?.trace "HOLD STRING: ${holdString}"

    sendEvent("name":"setpointHoldDisplay", "value": "Hold ${holdString}")
}

def getSetPointHoldDuration()
{
	def holdTime = 0

    log.debug "settings : ${settings.hold_time[0..1]}"

    if (settings.hold_time.contains("Hours"))
    {
    	holdTime = Integer.parseInt(settings.hold_time[0..1].trim())
    }
    else if (settings.hold_time.contains("Day"))
    {
    	holdTime = Integer.parseInt(settings.hold_time[0..1].trim()) * 24
    }

    def currentHoldDuration = device.currentState("setpointHoldDuration")?.value

 	log.debug "hours : $holdTime and setpointhold $currentHoldDuration "

    if (Short.parseShort(currentHoldDuration) != (holdTime * 60))
    {
    	[
        	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x24 0x21 {" +
            String.format("%04X", ((holdTime * 60) as Short))  // switch to zigbee endian

            + "}", "delay 100",
			"st rattr 0x${device.deviceNetworkId} 1 0x201 0x24", "delay 200",
		]

    } else
    {
    	[]
    }

}

def Hold()
{
	log.trace "hold dur " + getSetPointHoldDuration()

	log.debug "Flip Hold "
	def currentHold = device.currentState("setpointHold")?.value

	log.debug "current Hold ${currentHold}"


	def next = (currentHold == "On") ? "00" : "01"
	def nextHold = getHoldMap()[next]

	log.debug "switching hold from $currentHold to $nextHold next: $next"

	sendEvent("name":"setpointHold", "value":nextHold)

	// set the duration first if it's changed
	getSetPointHoldDuration() +
    [
    "st wattr 0x${device.deviceNetworkId} 1 0x201 0x23 0x30 {$next}", "delay 100" ,
    "raw 0x201 {04 21 11 00 00 05 00 }","delay 200",      // hold expiry time
  	"send 0x${device.deviceNetworkId} 1 1", "delay 200",
    ] + readAttributesCommand(0x201, [
				0x11,  //cooling set point
				0x12,  //heating set point
				0x1E,  //running mode
				0x23,	 //setpoint hold
				0x29,  //relay state
				//0x30,  // set point change source
				//0x32,  // set point change timestamp
				])
}

def compareWithNow(d)
{
	log.trace "called Compare with: $d"
	long mins = (new Date(d)).getTime() - (new Date()).getTime()

	mins /= 1000 * 60;

    log.trace "mins: ${mins}"

    boolean past = (mins < 0)
    def ret = (past)? "" : "in "

    if (past)
    	mins *= -1;

    log.trace "mins: ${mins}"

    float t = 0;
	// minutes
	if (mins < 60)
	{
			ret +=  (mins as Integer) + " min" + ((mins > 1)? 's' : '')
	}else if (mins < 1440)
	{
		t = ( Math.round((14 + mins)/30) as Integer) / 2
        ret += t + " hr" +  ((t > 1)? 's' : '')
	} else
    {
		t = (Math.round((359 + mins)/720) as Integer) / 2
        ret +=  t + " day" + ((t > 1)? 's' : '')
	}
    ret += (past)? " ago": ""

    log.trace "ret: ${ret}"

    ret
}

def convertToTime(data)
{
	def time = Integer.parseInt(data, 16) as long;
    time *= 1000;
    time += 946684800000; // 481418694
    time -= location.timeZone.getRawOffset() + location.timeZone.getDSTSavings();

    def d = new Date(time);

    //log.debug "time: " + time + " date: " + d;

	return d;
}

def Program()
{
	log.debug "Program"
   	def currentSched = device.currentState("prorgammingOperation")?.value

	log.debug "Program Hold ${currentSched}"


    def next = Integer.parseInt(currentSched, 16);
    if ( (next & 0x01) == 0x01)
    	next = next & 0xfe;
    else
    	next = next | 0x01;

	def nextSched = getProgrammingMap()[next & 0x01]

	log.debug "switching Program from $currentSched to $nextSched next: $next"

	//sendEvent("name":"prorgammingOperation", "value":next)
    //sendEvent("name":"prorgammingOperationDisplay", "value":nextSched)

    [
    "st wattr 0x${device.deviceNetworkId} 1 0x201 0x25 0x18 {$next}", "delay 100"
		] + readAttributesCommand(0x201, [
			  0x25,  //programming operations
				0x11,  //cooling set point
				0x12,  //heating set point
				0x1E,  // setpoint hold
				0x23,	 // running mode
				0x29,  // relay state
				//0x30,  // set point change source
				//0x32,  // set point change timestamp
				])
}


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

def checkLastTimeSync(delay)
{
	def lastSync = device.currentState("lastTimeSync")?.value
    if (!lastSync)
    	lastSync = "${new Date(0)}"

    if (settings.sync_clock ?: false && lastSync != new Date(0))
    	{
        	sendEvent("name":"lastTimeSync", "value":"${new Date(0)}")
    	}



	long duration = (new Date()).getTime() - (new Date(lastSync)).getTime()

    log.debug "check Time: $lastSync duration: ${duration} settings.sync_clock: ${settings.sync_clock}"
	if (duration > 86400000)
		{
			sendEvent("name":"lastTimeSync", "value":"${new Date()}")
			return setThermostatTime()
		}



	return []
}

def refresh()
{
	log.debug "refresh called"

	//sendEvent("name":"holdExpiary", "value": "")

	checkLastTimeSync(2000) +
 	readAttributesCommand(0x201, [
			0x00,  // temperature
			0x11,  // cooling set point
			0x12,  // heating set point
			0x1C,  // mode enum8
			0x1E,  // running mode
			0x23,	 // setpoint hold enum8
			0x24,  // hold duration int16u
			0x25,  // thermostat programming operation bitmap8
			0x29,  // relay state bitmap8
			//0x30,  // set point change source
			//0x32,  // set point change timestamp
			]) +
			[
	    "st rattr 0x${device.deviceNetworkId} 1 0x204 0x01", "delay 200",  // lock status
	    "raw 0x201 {04 21 11 00 00 05 00 }"                , "delay 500",       // hold expiary
	    "send 0x${device.deviceNetworkId} 1 1"             , "delay 600",
			]
}

def readAttributesCommand(cluster, attribList)
{
	def attrString = ''


	/*
	// reading multiple attributes in one command currently
	// causes parsing problems

	for ( val in attribList ) {
		//def val = Short.decode(i)
    attrString += ' ' + String.format("%02X %02X", val & 0xff , (val >> 8) & 0xff)
	}

	log.trace "list: " + attrString

	["raw "+ cluster + " {00 00 00 $attrString}","delay 500",
	"send 0x${device.deviceNetworkId} 1 1", "delay 1000",
	]
	*/

	def list = []
	attrString =  "st rattr 0x${device.deviceNetworkId} 1 $cluster "

	for ( val in attribList ) {
		list.add(attrString + String.format("0x%02X", val))
		list.add("delay 200")
	}

	log.trace "list: ${list}"

	list
}


def readHvacData()
{
	[
	"raw 0x201 {04 21 11 00 00 08 00 }",      // target
	"send 0x${device.deviceNetworkId} 1 1",
	"raw 0x201 {04 21 11 00 00 09 00 }",      // duty
	"send 0x${device.deviceNetworkId} 1 1",
	"raw 0x201 {04 21 11 00 00 0A 00 }",      // integrator
	"send 0x${device.deviceNetworkId} 1 1",
	"raw 0x201 {04 21 11 00 00 0B 00 }",      // gain
	"send 0x${device.deviceNetworkId} 1 1",
	"raw 0x201 {04 21 11 00 00 0C 00 }",      // hvac
	"send 0x${device.deviceNetworkId} 1 1",
	]
}

def poll() {
	log.debug "Executing 'poll'"
	//refresh()
	readHvacData()
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
	[
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x12 0x29 {" + hex(celsius*100) + "}"
	] + readAttributesCommand(0x201, [
			0x12,  //heating set point
			0x1E,  // setpoint hold
			0x23,	 // running mode
			0x29,  // relay state
			//0x30,  // set point change source
			//0x32,  // set point change timestamp
			])
}

def setCoolingSetpoint(degrees) {
	def degreesInteger = degrees as Integer
	log.debug "setCoolingSetpoint({$degreesInteger} ${temperatureScale})"
	sendEvent("name":"coolingSetpoint", "value":degreesInteger)
	def celsius = (getTemperatureScale() == "C") ? degreesInteger : (fahrenheitToCelsius(degreesInteger) as Double).round(2)
	[
    "st wattr 0x${device.deviceNetworkId} 1 0x201 0x11 0x29 {" + hex(celsius*100) + "}",
  ] + readAttributesCommand(0x201, [
			0x11,  //cooling set point
			0x1E,  // setpoint hold
			0x23,	 // running mode
			0x29,  // relay state
			//0x30,  // set point change source
			//0x32,  // set point change timestamp
			])
}

def modes() {
	["off", "heat", "cool", "auto"]
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

def setThermostatMode() {
	log.debug "switching thermostatMode"
	def currentMode = device.currentState("thermostatMode")?.value
	def modeOrder = modes()
	def index = modeOrder.indexOf(currentMode)
	def next = index >= 0 && index < modeOrder.size() - 1 ? modeOrder[index + 1] : modeOrder[0]
	log.debug "switching mode from $currentMode to $next"

	setThermostatMode(next)
}

def setThermostatMode(String next) {
	def val = (getModeMap().find { it.value == next }?.key)?: "00"

	sendEvent("name":"thermostatMode", "value":next)

	log.debug "setThermostatMode({$value})"

	[
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {$val}",
	"delay 100"
	] + readAttributesCommand(0x201, [0x11, 0x12, 0x1C, 0x1E, 0x23, 0x29, 0x30, 0x32])
}

def setThermostatFanMode(String value) {
	log.debug "setThermostatFanMode({$value})"
	"$value"()
}

def off() {
	setThermostatMode("off")
}

def cool() {
	setThermostatMode("cool")}

def heat() {
	setThermostatMode("heat")
}

def auto() {
	setThermostatMode("auto")
}

def on() {
	fanOn()
}

def fanOn() {
	log.debug "fanOn"
	sendEvent("name":"thermostatFanMode", "value":"fanOn")
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {04}"
}


def fanAuto() {
	log.debug "fanAuto"
	sendEvent("name":"thermostatFanMode", "value":"fanAuto")
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {05}"
}

def updated()
{
    log.debug "updated, scheduling set time"
	def lastSync = device.currentState("lastTimeSync")?.value
	log.debug "update last sync: $lastSync"
    // reset the last sync time if this no
	if ((settings.sync_clock ?: false) == false)
			{
            	log.debug "resetting last sync time.  Used to be: $lastSync"
                // this sendevent does not get executed somehow
                sendEvent("name":"lastTimeSync", "value":"${new Date(0)}")

      }
    // run every two minutes
    //schedule("0 0/2 * * * ?", setThermostatTime)
    //unschedule()
    //log.trace "commands: "  + device

}

def getLockMap()
{[
  "00":"Unlocked",
  "01":"Mode Only",
  "02":"Setpoint",
  "03":"Full",
  "04":"Full",
  "05":"Full",

]}
def lock()
{

	def currentLock = device.currentState("lockLevel")?.value
  def val = getLockMap().find { it.value == currentLock }?.key



  log.debug "current lock is: ${val}"

  if (val == "00")
      val = getLockMap().find { it.value == (settings.lock_level ?: "Full") }?.key
  else
      val = "00"

  log.debug "sending ${val} as the new lock value"

  [ //"st wattr 0x${device.deviceNetworkId} 1 0x204 1 0x30 {${val}}",
   "st rattr 0x${device.deviceNetworkId} 1 0x204 0x01", "delay 500"
  ]
}


def setThermostatTime()
{

  log.debug "setting time called.  Sending to: ${device.deviceNetworkId}"

  if ((settings.sync_clock ?: false))
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
  "raw 0x201 {04 21 11 00 02 0f 00 23 ${data} }",
  "send 0x${device.deviceNetworkId} 1 1"
  ]
}

def configure() {

 // location.temperatureScale = "F"

	log.debug "binding to Thermostat and UI cluster"
	[
			"zdo bind 0x${device.deviceNetworkId} 1 1 0x201 {${device.zigbeeId}} {}", "delay 200",
			"zdo bind 0x${device.deviceNetworkId} 1 1 0x204 {${device.zigbeeId}} {}", "delay 200",
        //"zcl global send-me-a-report [cluster:2] [attributeId:2] [dataType:1] [minReportTime:2] [maxReport-Time:2] [reportableChange:-1]"
  		"zcl global send-me-a-report 0x201 0x0000 0x29 20 0xffff {32}", "delay 100",  // report temperature changes over 1F
  		"send 0x${device.deviceNetworkId} 1 1", "delay 200",
        "zcl global send-me-a-report 0x201 0x0011 0x29 25 0xffff {32}", "delay 300",  // report cooling setpoint
  		"send 0x${device.deviceNetworkId} 1 1", "delay 400",
        "zcl global send-me-a-report 0x201 0x0012 0x29 35 0xffff {32}", "delay 500",  // report heating setpoint
        "send 0x${device.deviceNetworkId} 1 1", "delay 600",
        "zcl global send-me-a-report 0x201 0x001C 0x30 45 1000", "delay 700",  // report mode
         "send 0x${device.deviceNetworkId} 1 1", "delay 800",
        "zcl global send-me-a-report 0x201 0x0029 0x19 20 0xffff", "delay 900",  	   // report relay status
         "send 0x${device.deviceNetworkId} 1 1", "delay 1000",
        "zcl global send-me-a-report 0x201 0x0030 0x30 20 0xffff", "delay 1100",  	   // set point changed source
         "send 0x${device.deviceNetworkId} 1 1", "delay 1200",
        "zcl global send-me-a-report 0x201 0x0025 0x19 20 0xffff {32}", "delay 1300",	// schedule on/off
         "send 0x${device.deviceNetworkId} 1 1", "delay 1400",
      	"zcl global send-me-a-report 0x201 0x0023 0x30 20 0xffff", "delay 1500",		// hold
        "send 0x${device.deviceNetworkId} 1 1", "delay 1600",
        "zcl global send-me-a-report 0x201 0x001E 0x30 20 0xffff", "delay 1700",		// running mode
        "send 0x${device.deviceNetworkId} 1 1", "delay 1800",

]
}

private hex(value) {
	new BigInteger(Math.round(value).toString()).toString(16)
}
