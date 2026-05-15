const ws = new WebSocket("ws://" + location.host + contextPath + "/editorEndpoint?doc=" + docName);

const editor = document.getElementById('editor');
let MAJ = false;

ws.onmessage = function(event) {
    MAJ = true;
    editor.innerHTML = event.data;
    MAJ = false;
};

editor.addEventListener('input', function() {
    if(!MAJ && ws.readyState == WebSocket.OPEN) {
        ws.send("doc=" + docName + "||" + editor.innerHTML);
    }
});

function fmt(cmd, val) {
    editor.focus();
    document.execCommand(cmd, false, val || null);
    if(ws.readyState == WebSocket.OPEN) {
        ws.send("doc=" + docName + "||" + editor.innerHTML);
    }
}

function syncContent() {
    document.getElementById('hiddenContent').value = editor.innerHTML;
}

ws.onopen = () => console.log("WebSocket éditeur connecté");
ws.onclose = () => console.log("WebSocket éditeur déconnecté");