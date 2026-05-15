package projet_dw2;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/editorEndpoint")
public class EditorWebSocket {

    // Un Set de sessions par document
    private static Map<String, Set<Session>> rooms = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String docName = session.getQueryString().replace("doc=", "");
        rooms.computeIfAbsent(docName, k -> Collections.synchronizedSet(new HashSet<>()))
             .add(session);
    }

    @OnMessage
    public void onMessage(String message, Session sender) throws IOException {
        // message = "doc=monDoc||<contenu HTML>"
        String[] parts = message.split("\\|\\|", 2);
        if(parts.length < 2) return;
        String docName = parts[0].replace("doc=", "");
        String content = parts[1];

        Set<Session> room = rooms.get(docName);
        if(room == null) return;

        // Broadcast à tous sauf l'expéditeur (comme ton ChatEndpoint mais filtré par doc)
        for(Session s : room) {
            if(!s.getId().equals(sender.getId()) && s.isOpen()) {
                s.getBasicRemote().sendText(content);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String docName = session.getQueryString().replace("doc=", "");
        Set<Session> room = rooms.get(docName);
        if(room != null) {
            room.remove(session);
            if(room.isEmpty()) rooms.remove(docName);
        }
    }
}