

def p =    "read attr - raw: 64E90102010825001801, dni: 64E9, endpoint: 01, cluster: 0201, size: 08, attrId: 0025, encoding: 18, value: 01"
def pp =   "read attr - raw: 64E901020114110029F6091200296608, dni: 64E9, endpoint: 01, cluster: 0201, size: 14, attrId: 0011, encoding: 29, value: 086629001209f6"
def ppp =  "read attr - raw: 64E9010201101C0030001E003000, dni: 64E9, endpoint: 01, cluster: 0201, size: 10, attrId: 001c, encoding: 30, value: 0030001e00"
def pppp = "read attr - raw: 64E901020114110029F6091200296608, dni: 64E9, endpoint: 01, cluster: 0201, size: 14, attrId: 0011, encoding: 29, value: 086629001209f6"

def getDataLengthByType(t)
{
// number of bytes in each static data type
  def map = ["08":1,	"09":2,	"0a":3,	"0b":4,	"0c":5,	"0d":6,	"0e":7,	"0f":8,	"10":1,	"18":1,	"19":2,	"1a":3,	"1b":4,
  "1c":5,"1d":6,	"1e":7,	"1f":8,	"20":1,	"21":2,	"22":3,	"23":4,	"24":5,	"25":6,	"26":7,	"27":8,	"28":1,	"29":2,
  "2a":3,	"2b":4,	"2c":5,	"2d":6,	"2e":7,	"2f":8,	"30":1,	"31":2,	"38":2,	"39":4,	"40":8,	"e0":4,	"e1":4,	"e2":4,
  "e8":2,	"e9":2,	"ea":4,	"f0":8,	"f1":16]

// return number of hex chars
  return map.get(t) * 2
}

def parseDescriptionAsMap(description) {
  def map = (description - "read attr - ").split(",").inject([:]) { map, param ->
  def nameAndValue = param.split(":")
  map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
  }

    def attrId = map.get('attrId')
    def encoding = map.get('encoding')
    def value = map.get('value')
    def list = [];

  if (getDataLengthByType(map.get('encoding')) < map.get('value').length()) {
    def raw = map.get('raw')

    def size = Long.parseLong(''+ map.get('size'), 16)
    def index = 12;
    def len

    while((index-12) < size) {
       attrId = flipHexStringEndianness(raw[index..(index+3)])
       index+= 4;
       encoding = raw[index..(index+1)]
       index+= 2;
       len =getDataLengthByType(encoding)
       println 'len:' + len + 'index' + index
       value = flipHexStringEndianness(raw[index..(index+len-1)])
       println 'val:' + raw[index..(index+len-1)]
       index+=len;
       list += ['attrId': attrId, 'encoding':encoding, 'value':value]

       println 'index:' + index + ':' + size
    }
  }
  else {
    list += ['attrId': attrId, 'encoding':encoding, 'value':value]
  }
  map.remove('value')
  map.remove('encoding')
  map.remove('attrId')
  map += ['attrs' : list ]
}

def flipHexStringEndianness(s)
{
  s = s.reverse()
  sb = new StringBuilder()
  for (int i=0; i < s.length() -1; i+=2)
   sb.append(s.charAt(i+1)).append(s.charAt(i))
  sb
}

def parse(String description) {
	log.debug "Parse description $description"
	def map = [:]
	if (description?.startsWith("read attr -")) {

		//TODO: Parse RAW strings for multiple attributes
		def descMap = parseDescriptionAsMap(description)
		log.debug "Desc Map: $descMap"

    for ( atMap in descMap.attrs)
    {
       println atMap

    }
}


def res = parse(ppp)
res.attrs

​
