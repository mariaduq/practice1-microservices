let socket = new WebSocket("ws://"+window.location.host+"/notifications");

function connect() {

    socket.onopen = function(e) {
        console.log("Connected to WebSocket server");
    };

    socket.onmessage = function(event) {
        console.log("Received message: " + event.data);
    };

    socket.onclose = function(event) {
        console.log("Disconnected from WebSocket server");
    };

    socket.onerror = function(error) {
        console.error("Error en la conexión del socket", error);
    }
}

function createTask() {
    var content = document.getElementById("content").value;
    socket.send(content);
    document.getElementById("content").value = "";

    var task = {
        id: 0,
        text: content
    };

    var jsonTask = JSON.stringify(task);

    var xhr = new XMLHttpRequest();
      xhr.open("POST", "/tasks");
      xhr.setRequestHeader("Content-Type", "application/json");
      xhr.send(jsonTask);
}
