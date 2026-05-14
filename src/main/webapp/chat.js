
const wsUrl = "ws://" + window.location.host + "/Projet_DevWeb2/tchatEndpoint";
const webSocket = new WebSocket(wsUrl);

webSocket.onopen = function(event) {
    console.log("Connecté au serveur de tchat !");
    const messagesBox = document.getElementById("messages-box");
    messagesBox.innerHTML += "<p style='color: green; font-style: italic;'>Connecté au tchat.</p>";
};

webSocket.onmessage = function(event) {
    const messagesBox = document.getElementById("messages-box");
    messagesBox.innerHTML += "<p style='margin: 5px 0;'>" + event.data + "</p>";
    messagesBox.scrollTop = messagesBox.scrollHeight;
};

function sendMessage() {
    const input = document.getElementById("message-input");
    const message = input.value.trim();
    
    if (message !== "") {
        webSocket.send(pseudoUtilisateur + " : " + message);
        input.value = ""; 
    }
}

function handleKeyPress(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
}