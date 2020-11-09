

log = console.log

const MQTT_ADDR = "167.99.135.224";
const MQTT_PORT = 8080;

var clientId = `id_${parseInt(Math.random() * 1000)}`;
var client = new Messaging.Client(MQTT_ADDR, MQTT_PORT, clientId);

var options = {
  timeout: 3,
  onSuccess: function () {
    log(`MQTT: Connected: ${MQTT_ADDR}:${MQTT_PORT}, id: ${clientId}`);
    mqtt_subscribe('ggg')
    mqtt_subscribe('ping')
    mqtt_publish("test", "ping")
  },
  onFailure: function (message) {
    log(`Error: MQTT: Connection: ${MQTT_ADDR}:${MQTT_PORT}:${message.errorMessage}`);
  }
};

client.onConnectionLost = function (responseObject) {
  alert(`Connection lost: Please, press OK, and wait for reboot ( ${responseObject.errorMessage} )`);
  location.reload();
};


client.onMessageArrived = function (message) {
  log(`MQTT: Topic: ${message.destinationName}, Data: ${message.payloadString}`);
  msgs_check(message.destinationName, message.payloadString);
};

var mqtt_publish = function (payload, topic, _qos) {
  if (typeof _qos == "undefined") _qos = 2;
  var message = new Messaging.Message(payload);
  message.destinationName = topic;
  message.qos = _qos;
  client.send(message);
};

var mqtt_subscribe = function (topic, _qos) {
  if (typeof _qos == "undefined") _qos = 2;
  client.subscribe(topic, { qos: _qos });
};

var mqtt_connect = function () {
  client.connect(options);
};

var mqtt_disconnect = function () {
  client.disconnect();
};

mqtt_connect();


function id(id) {
  return document.querySelector(`#${id}`)
}

// id("btn").onclick = function () {
//   mqtt_publish(id('msg').value, id('topic').value)
// }

function msgs_check(topicStr, dataStr) {
  if (topicStr == "ping") {
    log('MQTT: PING (' + dataStr + ')');
  }
  if (topicStr == 'ggg' && dataStr == 'x') {
    id('text').innerHTML = "Privet"
  }
}

const byCls = function(cls){
    return document.getElementsByClassName(cls)
}

const colorPalet = document.querySelectorAll('.color-box')

colorPalet.forEach(element => 
    element.addEventListener('click', function(){
        let payload = this.style["background-color"]
        console.log(payload.slice(4,-1))
        mqtt_publish(payload.slice(4,-1), "/control")})
);
