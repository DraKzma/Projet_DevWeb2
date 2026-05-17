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

// Gestion du bouton Download côté client
const downloadBtn = document.querySelector('button[name="download"]');
if (downloadBtn) {
    downloadBtn.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();

        const content = editor.innerHTML;
        const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
        const url = URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = docName + '.txt';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    });
}

ws.onopen = () => console.log("WebSocket éditeur connecté");
ws.onclose = () => console.log("WebSocket éditeur déconnecté");
