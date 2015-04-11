/**
 *	Fidure Thermostat, Based on ZigBee t-stat
 *
 *	Author: Fidure
 *	Date: 2014-12-13
 *  Updated: 2015-04-11
 */
metadata
{
	// Automatically generated. Make future change here.
		definition (name: "Fidure A173X Thermostat", namespace: "smartthings", author: "SmartThings") {
		capability "Thermostat"
		capability "Refresh"
		command "setThermostatTime"

		command "setTemperatureUnit"

	attribute "modeStatus", "string"
    attribute "temperatureUnit","string"
    attribute "displayTemperature","string"
    attribute "displaySetpoint", "string"
    command "raiseSetpoint"
		command "lowerSetpoint"

    attribute "displayCoolSetpoint","string"
	attribute "displayHeatSetpoint","string"

	attribute "setpointHold", "string"
	attribute "setpointHoldDisplay", "string"
	command "Hold"

	attribute "setpointHoldDuration", "number"
	command "setSetpointHoldDuration"
    attribute "setpointHoldDurationDisplay", "String"

    attribute "runningMode", "string"
    attribute "relayState", "string"

    attribute "prorgammingOperation", "number"
    attribute "prorgammingOperationDisplay", "string"
    command   "Schedule"

    attribute "setpointChangeSource", "string"
    attribute "setpointChangeTime", "string"
    attribute "holdExpiary", "string"

		fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0201,0204,0B05", outClusters: "000A, 0019"
	}

	// simulator metadata
	simulator { }

	tiles {

		valueTile("temperature", "displayTemperature", width: 2, height: 2)
		{
			state("temperature", label: '${currentValue}',
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

		/*
		valueTile("modeStatus", "modeStatus", inactiveLabel: false, decoration: "flat", width: 2) {
				state "default", label: '${currentValue}'
			}
		*/
		standardTile("modeStatus", "modeStatus", inactiveLabel: false, decoration: "flat") {
				state "Resting",  label: 'Resting'
				state "Heating",  icon:"st.thermostat.heating"
	      state "Cooling",  icon:"st.thermostat.cooling"
				}


		valueTile("modeText", "device.thermostatMode", inactiveLabel: false, decoration: "flat", width: 2) {
				state "default", label: 'Mode:'
			}

		standardTile("mode", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
			state "Off",   action:"thermostat.setThermostatMode", icon:"st.thermostat.heating-cooling-off"
			state "Heat",  action:"thermostat.setThermostatMode", icon:"st.thermostat.heat"
      		state "Cool",  action:"thermostat.setThermostatMode", icon:"st.thermostat.cool"
			state "Auto",  action:"thermostat.setThermostatMode", icon:"st.thermostat.Auto"
			}

    	standardTile("fanMode", "device.thermostatFanMode", inactiveLabel: false, decoration: "flat") {
			state "fanAuto", label:'${name}', action:"thermostat.setThermostatFanMode"
			state "fanOn", label:'${name}', action:"thermostat.setThermostatFanMode"
		}
		controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setHeatingSetpoint", action:"thermostat.setHeatingSetpoint", backgroundColor:"#d04e00"
		}
		valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "heat", label:'${currentValue}° heat', unit:"C", backgroundColor:"#ffffff"
		}
		controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setCoolingSetpoint", action:"thermostat.setCoolingSetpoint", backgroundColor: "#1e9cbb"
		}
		valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "cool", label:'${currentValue}° cool', unit:"C", backgroundColor:"#ffffff"
		}


        // new stuff
		standardTile("setTime", "device.temperature", inactiveLabel: false, decoration: "flat")
        {
					state "Time", label:'Sync ${name}', action:"setThermostatTime"
		}

		valueTile("heatingSetpoint2", "displayHeatSetpoint", inactiveLabel: false,  decoration: "flat") {
			state "heat", label:'Heat\n${currentValue}', unit:"C", backgroundColor:"#ffffff"
		}

		// cool section
		valueTile("coolingSetpoint2", "displayCoolSetpoint", inactiveLabel: false, decoration: "flat" ) {
			state "cool", label:'Cool\n${currentValue}', unit:"C", backgroundColor:"#ffffff"
		}

		valueTile("unitText", "temperatureUnit", inactiveLabel: false, decoration: "flat", width: 2) {
				state "default", label: 'Unit: '
			}

		valueTile("unit", "temperatureUnit", inactiveLabel: false, decoration: "flat") {
            state "default", label:'°${currentValue}', action:"setTemperatureUnit"
		}
        // new stuff


		// extra features

		controlTile("holdTime", "setpointHoldDuration", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "setSetpointHoldDuration", action:"setSetpointHoldDuration", backgroundColor: "#1e9cbb"
		}


		valueTile("hold", "setpointHoldDisplay", inactiveLabel: false, decoration: "flat", width: 2) {
            state "setpointHold", action:"Hold", label: '${currentValue}'
		}

        valueTile("scheduleText", "prorgammingOperation", inactiveLabel: false, decoration: "flat", width: 2) {
            state "default", action:"Schedule", label: 'Schedule: '
		}
        valueTile("schedule", "prorgammingOperationDisplay", inactiveLabel: false, decoration: "flat") {
            state "default", action:"Schedule", label: '${currentValue}'
		}


    	valueTile("holdDurationDisplay", "setpointHoldDurationDisplay", inactiveLabel: false, decoration: "flat"){
            state "default", label: '${currentValue}'

    }

		chartTile(name: "temperatureChart", attribute: "device.temperature")


		valueTile("setpoint", "displaySetpoint", width: 2, height: 2)
		{
			state("displaySetpoint", label: '${currentValue}',
				backgroundColor: "#919191")
		}

        standardTile("upButton", "device.thermostatSetpoint", inactiveLabel: false) {
			state "setpoint", action:"raiseSetpoint", backgroundColor:"#919191", icon:"st.thermostat.thermostat-up"
		}
		standardTile("downButton", "device.thermostatSetpoint", inactiveLabel: false) {
			state "setpoint", action:"lowerSetpoint", backgroundColor:"#919191", icon:"st.thermostat.thermostat-down"
		}



		standardTile("refresh", "device.temperature", inactiveLabel: false, decoration: "flat") {
			state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		main "temperature"
		details([
			"temperature",

            "mode",  "modeStatus",
			//"modeStatus",
			"setpoint","upButton","downButton",
		// "heatingSetpoint",
		// "coolingSetpoint",
        "hold", "setTime"	,
		"holdTime", "holdDurationDisplay",
        "scheduleText", "schedule",
				"unitText", "unit",
        "heatSliderControl","heatingSetpoint2"	,
        "coolSliderControl",  "coolingSetpoint2",
		 "refresh" ,
		"temperatureChart",
         ])
	}
}


def getDisplayTemp(temp, decor)
{
    try
     {
        def value = temp; //Integer.parseInt(temp);

        if ((value % 2) == 1)
        {
        	// celsius
            value = (value / 10) as Integer;
            value = (((value + 2) / 5) as Integer) * 5;  // round to 0.5
            value /= 10;  // as float
            return value + decor + '°C';
        }
        else
        {
        	// ' °F'
			value = (value / 100) as Double;
            log.trace "celsius: " + value;
            value = celsiusToFahrenheit(value);
            log.trace "Fah: " + value;
            value = value as Integer;  // round to 1
            return value + decor +'°F';

        }
  	 } catch(NumberFormatException nfe)
      {
          return temp + ' °';
      }
	}

def getDisplayUnit()
{
	log.debug "getting display unit"
    def currentUnit = device.currentState("temperatureUnit")?.value
	return (currentUnit == "C") ? "C" : "F"
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
                        sendEvent("name":"displayTemperature",
                        	"value":getDisplayTemp(getZigbeeTemperature(descMap.value), ''))
					break;
				  case "0011":
						log.trace "COOLING SETPOINT"
						map.name = "coolingSetpoint"
						map.value = getTemperature(descMap.value)
                        sendEvent("name":"displayCoolSetpoint",
                        	"value":getDisplayTemp(getZigbeeTemperature(descMap.value), ''))
						updateSetpoint()
					break;
					case "0012":
						log.trace "HEATING SETPOINT"
						map.name = "heatingSetpoint"
						map.value = getTemperature(descMap.value)
                       	sendEvent("name":"displayHeatSetpoint",
                        	"value":getDisplayTemp(getZigbeeTemperature(descMap.value), ''))
						updateSetpoint()
					break;
					case "001c":
						log.trace "MODE"
						map.name = "thermostatMode"
						map.value = getModeMap()[descMap.value]
						updateModeStatus()
						updateSetpoint()
					break;

          case "0005":
          //log.debug "hex time: ${descMap.value}"
          	if (descMap.encoding ==  "23")
              {
          	    //log.trace "hex time: ${descMap.value}"
								map.name = "holdExpiary"
                map.value = convertToTime(descMap.value)
								if (descMap.value != "00000000")
								{
									def str = "Hold Ends:" + compareDate(map.value) ;
									sendEvent("name":"setpointHoldDisplay", "value":str)
                  //log.trace "hold str:" + str;
								}else
                  sendEvent("name":"setpointHoldDisplay", "value":"Hold is Off")

							}
          break;
					case "001e":   //running mode enum8
						log.trace "running mode: ${descMap.value}"
	          map.name = "runningMode"
						map.value = getModeMap()[descMap.value]
						updateModeStatus()
						updateSetpoint()
					break;
					case "0023":   // setpoint hold enum8
					log.trace "setpoint Hold: ${descMap.value}"
					map.name = "setpointHold"
					map.value = getHoldMap()[descMap.value]
					break;
					case "0024":   // hold duration int16u
					log.trace "setpoint Hold Duration: ${descMap.value}"
					map.name = "setpointHoldDuration"
					map.value = Integer.parseInt(descMap.value, 16)
          sendEvent("name":"setpointHoldDurationDisplay", "value": timeLength(map.value))
					break;
					case "0025":   // thermostat programming operation bitmap8
					log.trace "prorgamming Operation: ${descMap.value}"
					map.name = "prorgammingOperation"
                    def val = getProgrammingMap()[Integer.parseInt(descMap.value, 16) & 0x01]
					sendEvent("name":"prorgammingOperationDisplay", "value": val)
					log.trace "prorgamming Operation is : ${val}"
                    map.value = descMap.value
					break;
					case "0029":   // hvac relay state bitmap
					log.trace "relay State: ${descMap.value}"
					map.name = "relayState"
					map.value = getRelayStates(descMap.value)
					log.trace "relay: ${map.value}"
					updateModeStatus()
					break;
					case "0030":   // setpoint change source enum8
					log.trace "setpoint Change Source: ${descMap.value}"
					map.name = "setpointChangeSource"
					map.value = getSetpointSourceMap()[descMap.value]
					log.trace "source: ${map.value}"
					break;
					case "0032":   // setpoint change source UTC time
					log.trace "setpoint Change Time: ${descMap.value}"
					map.name = "setpointChangeTime"
					map.value = convertToTime(descMap.value)
                    log.trace "Time: ${map.value}"
					break;

			}
		} else if (descMap.cluster == "0204")
		{
			switch(descMap.attrId)
			{
				case "0000":
					log.trace "Unit ${descMap.value}"
					map.name = "temperatureUnit"
					map.value = getUnitMap()[descMap.value]
				break;

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

def getUnitMap()
{[
  "00":"C",
  "01":"F"
]}

def getModeMap() { [
	"00":"Off",
	"03":"Cool",
	"04":"Heat"
]}


def getHoldMap()
{[
	"00":"Off",
	"01":"On"
]}

def getProgrammingMap() { [
	0:"Off",
	1:"On"
]}


def updateSetpoint()
{
	def cool = device.currentState("displayCoolSetpoint")?.value
	def heat = device.currentState("displayHeatSetpoint")?.value
	def runningMode = device.currentState("runningMode")?.value
	def mode = device.currentState("thermostatMode")?.value
	log.trace "cool: $cool heat: $heat running mode: $runningMode mode: $mode"

	def value = '--';

	if ("Heat"  == mode && heat != null)
		value = heat;
	else if ("Cool"  == mode && cool != null)
		value = cool;
    else if ("Auto" == mode && runningMode == "Cool" && cool != null)
    	value = cool;
    else if ("Auto" == mode && runningMode == "Heat" && heat != null)
    	value = heat;

	sendEvent("name":"displaySetpoint", "value": value)
}


def raiseSetpoint()
{
	adjustSetpoint(5)
}

def lowerSetpoint()
{
	adjustSetpoint(-5)
}

def adjustSetpoint(value)
{
	def runningMode = device.currentState("runningMode")?.value
	def mode = device.currentState("thermostatMode")?.value

    def modeData = 0xff

    if ("Heat" == mode || "Heat" == runningMode)
    	modeData = String.format("%02X", 0);
    else if ("Cool" == mode || "Cool" == runningMode)
    	modeData = String.format("%02X", 1);
    else
    	return

    def amountData = String.format("%02X", value)[-2..-1]

    log.debug "raise  mode: $modeData amout: $amountData"

	[
	"st cmd 0x${device.deviceNetworkId} 1 0x201 0 {" + modeData + " " + amountData + "}"
	] + readAttributesCommand(0x201,
		[
		0x11, // cooling setpoint
		0x12, // heating setpoint
		0x1E, // running mode
		0x23, // hold
		0x29, // relay status
		0x30, // change source
		0x32  // change source time
		]) +
		getHoldExpiary()
}

def getHoldExpiary()
{
	["raw 0x201 {04 21 11 00 00 05 00 }","delay 500",
	"send 0x${device.deviceNetworkId} 1 1", "delay 1000",
	]
}

def readAttributesCommand(cluster, attribList)
{
	def attrString = ''

	/*
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
		list.add("delay 100")
	}

	log.trace "list: ${list}"

	list
}


def updateModeStatus()
{
	def mode = device.currentState("thermostatMode")?.value
	def relay = device.currentState("relayState")?.value
	def runningMode = device.currentState("runningMode")?.value
    def str = 'Resting'

    if (relay.size() > 1)
    	str = relay;

	log.trace "mode status: " + str + "relay ${relay} mode ${mode} running mode ${runningMode}"

	sendEvent("name":"modeStatus", "value": str)
}

def getRelayStates(value) {

    String[] m = [ "Heating", "Cooling", "Fan", "Heat2", "Cool2", "Fan2", "Fan3"]
	String desc = ''
    value = Integer.parseInt(value, 16)

	for ( i in 0..2 ) {
    if (value & 1 << i)
			desc = m[i]
	}

	return desc
}

def getSetpointSourceMap() { [
	"00":"Manual",
	"01":"Schedule",
	"02":"Remote"
]}

def Schedule()
{
	log.debug "Schedule"
   	def currentSched = device.currentState("prorgammingOperation")?.value

	log.debug "schedule Hold ${currentSched}"


    def next = Integer.parseInt(currentSched, 16);
    if ( (next & 0x01) == 0x01)
    	next = next & 0xfe;
    else
    	next = next | 0x01;

	def nextSched = getProgrammingMap()[next & 0x01]

	log.debug "switching sched from $currentSched to $nextSched next: $next"

	//sendEvent("name":"prorgammingOperation", "value":next)
    //sendEvent("name":"prorgammingOperationDisplay", "value":nextSched)

    [
    "st wattr 0x${device.deviceNetworkId} 1 0x201 0x25 0x18 {$next}", "delay 100" ,
	"st rattr 0x${device.deviceNetworkId} 1 0x201 0x25", "delay 200",
    ]
}

def Hold()
{
	log.debug "Flip Hold "
	def currentHold = device.currentState("setpointHold")?.value

	log.debug "current Hold ${currentHold}"

	def next = (currentHold == "On") ? "00" : "01"
	def nextHold = getHoldMap()[next]

	log.debug "switching hold from $currentHold to $nextHold next: $next"

	sendEvent("name":"setpointHold", "value":nextHold)

    sendEvent("name":"setpointHoldDisplay", "value":(next == "00") ? "Hold is Off": "Hold is On")

    [
    "st wattr 0x${device.deviceNetworkId} 1 0x201 0x23 0x30 {$next}", "delay 100" ,
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x23", "delay 300",
    "raw 0x201 {04 21 11 00 00 05 00 }","delay 500",
  	"send 0x${device.deviceNetworkId} 1 1", "delay 1000",
    ]

}

def toPaddedHex(value, digits)
{
	def str = '0'.multiply(digits) + hex(value)
    str = str[-digits..-1]
    log.trace "hexing $value to $str"

    return str
}

def setSetpointHoldDuration(mins)
{
	def minsInteger = (mins as Integer)
	log.debug "setSetpointHold({$minsInteger})"
	sendEvent("name":"setpointHoldDuration", "value":minsInteger)

	def str = readAttributesCommand(0x201, [0, 5, 0x21, 0x23])

	["st wattr 0x${device.deviceNetworkId} 1 0x201 0x24 0x21 {" + toPaddedHex(minsInteger, 4) + "}", "delay 100",
   "st rattr 0x${device.deviceNetworkId} 1 0x201 0x24", "delay 200",
  ] + str
}


def setTemperatureUnit() {
	log.debug "setting Unit "
	def currentUnit = device.currentState("temperatureUnit")?.value

	log.debug "current Unit ${currentUnit}"

	def next = currentUnit == "C" ? "01" : "00"
	def nextUnit = getUnitMap()[next]

	log.debug "switching unit from $currentUnit to $nextUnit"

	sendEvent("name":"temperatureUnit", "value":nextUnit)

    // write temp unit, read it back along with other displayed temperatures (targets)
    [
     "st wattr 0x${device.deviceNetworkId} 1 0x204 0x00 0x30 {$next}", "delay 100",
	   "st rattr 0x${device.deviceNetworkId} 1 0x204 0x00", "delay 200",
	   "st rattr 0x${device.deviceNetworkId} 1 0x201 0x11", "delay 200",
  	 "st rattr 0x${device.deviceNetworkId} 1 0x201 0x12", "delay 200",
     "st rattr 0x${device.deviceNetworkId} 1 0x201 0x00", "delay 200",
    ]

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

def timeLength(mins)
{
  log.trace "time Length: " + mins
  def str = ''

  if (mins < 60)
  	str = mins + " min" + ((mins > 1)? 's' : '')
  else
  	{
    	mins = (((mins + 14) / 30) as Integer) /2;  // round to 0.5
    	str = mins + " hr" + ((mins > 1)? 's': '')
    }

  log.trace "length: " + str

  return str

}

def compareDate(d)
{
	def
	long mils = d.getTime() - new Date().getTime();
	mils /= 1000 * 60;
    log.trace "mins: " + mils;

	// minutes
	if (mils < 60)
	{
			return (mils as Integer) + " min" + (mils > 1)? 's' : '';
	}else if (mills < 1440)
	{
		return ((mils/60) as Integer) + " hr" +  (mils > 1)? 's' : '';
	} else
		return ((mils/1440) as Integer) + " day" + (mils > 1)? 's' : '';
}


def setThermostatTime()
{
	log.debug "setting time called.  Sending to: ${device.deviceNetworkId}"


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

	[
  "raw 0x201 {04 21 11 00 02 0f 00 23 ${data} }", "delay 100",
  "send 0x${device.deviceNetworkId} 1 1", "delay 200",
  ]
}



def refresh()
{
	log.debug "refresh called"
	[
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x00", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x11", "delay 200",  // coolong setpoint int16u
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x12", "delay 200",  // heating set point int16u
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x1C", "delay 200",  // mode enum8
    "st rattr 0x${device.deviceNetworkId} 1 0x201 0x1E", "delay 200",  //running mode enum8
    "st rattr 0x${device.deviceNetworkId} 1 0x201 0x23", "delay 200",  // setpoint hold enum8
    "st rattr 0x${device.deviceNetworkId} 1 0x201 0x24", "delay 200",  // hold duration int16u
    "st rattr 0x${device.deviceNetworkId} 1 0x201 0x25", "delay 200",  // thermostat programming operation bitmap8
    "st rattr 0x${device.deviceNetworkId} 1 0x201 0x29", "delay 200",  // hvac relay state bitmap
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x30", "delay 200",  // setpoint change source enum8
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x32", "delay 200",  // setpoint change source UTC time
		"st rattr 0x${device.deviceNetworkId} 1 0x204 0x00", "delay 200",	// temperature unit
        "raw 0x201 {04 21 11 00 00 05 00 }","delay 500", "delay 500",       // hold expiary
  		"send 0x${device.deviceNetworkId} 1 1", "delay 1000",
	]
}
// Leaving out for now as its killing the batteries.
//def poll() {
//	log.debug "Executing 'poll'"
//	refresh()
//}

def getTemperature(value) {
	def celsius = Integer.parseInt(value, 16) / 100
	if(getTemperatureScale() == "C"){
		return celsius
	} else {
		return celsiusToFahrenheit(celsius) as Integer
	}
}


def getZigbeeTemperature(value) {
	// take out the last digit
	def celsius = ((Integer.parseInt(value, 16) / 10) as Integer) * 10;

	log.trace "getting temperature: " + celsius;

	//if(getTemperatureScale() == "C"){
    if (getDisplayUnit() == "C") {
		return celsius + 1;
	} else {
		// celsiusToFahrenheit(celsius) as Integer
		return celsius;
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
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x11 0x29 {" + hex(celsius*100) + "}"
}

def modes() {
	["Off", "Heat", "Cool"]
}

def setThermostatMode() {
	log.debug "switching thermostatMode"
	def currentMode = device.currentState("thermostatMode")?.value
	def modeOrder = modes()
	def index = modeOrder.indexOf(currentMode)
	def next = index >= 0 && index < modeOrder.size() - 1 ? modeOrder[index + 1] : modeOrder[0]
	log.debug "switching mode from $currentMode to $next"

	def val = getModeMap().find { it.value == next }?.key

	sendEvent("name":"thermostatMode", "value":next)

	[
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {$val}",
	"delay 100"
	] + readAttributesCommand(0x201, [0x11, 0x12, 0x1C, 0x1E, 0x23, 0x29, 0x30, 0x32])
}


def configure() {

	log.debug "binding to Thermostat and Fan Control cluster"
	[
		"zdo bind 0x${device.deviceNetworkId} 1 1 0x201 {${device.zigbeeId}} {}", "delay 200",
		"zdo bind 0x${device.deviceNetworkId} 1 1 0x202 {${device.zigbeeId}} {}"
	]
}

def installed() {
    subscribe(location, "sunrise", sunriseHandler)
}

def sunriseHandler(evt) {
    log.debug "Sun has risen!"
		setThermostatTime()
}

private hex(value) {
	new BigInteger(Math.round(value).toString()).toString(16)
}
