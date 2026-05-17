
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Editor</title>
		<link rel="stylesheet" href="style.css">
	</head>
	<body>
	<%-- Valeur de l'attribut "error":
	0: Pas d'erreur
	1: Formulaire non rempli correctement 
	2: Utilisateur banni
	3: Utilisateur n'existe pas
	4: Impossible de se bannir soi meme
	5: L'utilisateur est deja banni
	6: Utilisateur set to VIEWER
	7: Utilisateur n'existe pas
	8: Impossible de se set soi meme to VIEWER
	9: L'utilisateur est deja un VIEWER
	10: Utilisateur set to WRITER
	11: Utilisateur n'existe pas
	12: Impossible de se set soi meme to WRITER
	13: L'utilisateur est deja un WRITER
	14: Document sauvegzrdee --%>
	<% String role = (String) request.getAttribute("role"); int erreur = (int) request.getAttribute("error"); HashMap<String, String> userList = (HashMap<String, String>) request.getAttribute("userList"); String content = (String)request.getAttribute("content"); %>
		<h1 id="EditorTitle">Editor</h1>
		
		<div id="document">
		
			<h2 id="EditorDocumentTitle">Document</h2>
			<form method="post" onsubmit="syncContent()">
			    <div id="toolbar">
			        <button type="button" onclick="fmt('bold')" title="Gras"><b>G</b></button>
			        <button type="button" onclick="fmt('italic')" title="Italique"><i>I</i></button>
			        <button type="button" onclick="fmt('underline')" title="Souligné"><u>S</u></button>
			        <button type="button" onclick="fmt('strikeThrough')" title="Barré"><s>B</s></button>
			        <select onchange="fmt('fontSize', this.value); this.selectedIndex=0">
			            <option value="">Taille</option>
			            <option value="1">Petit</option>
			            <option value="3">Normal</option>
			            <option value="5">Grand</option>
			            <option value="7">Très grand</option>
			        </select>
			        <button type="button" onclick="fmt('justifyLeft')" title="Gauche">◀</button>
			        <button type="button" onclick="fmt('justifyCenter')" title="Centre">▣</button>
			        <button type="button" onclick="fmt('justifyRight')" title="Droite">▶</button>
			    </div>
			
			    <% if(role.equals("OWNER") || role.equals("WRITER")){ %>
				    <div id="editor" contenteditable="true">${content}</div>
				<% } else { %>
				    <div id="editor" contenteditable="false">${content}</div>
				<% } %>
			    <input type="hidden" name="content" id="hiddenContent">
			
			    <br>
			    <% if(role.equals("OWNER") || role.equals("WRITER")){ %>
			    <button type="submit" name="save" class="SubmitButton" onclick="syncContent()">Save</button>
			    <button type="submit" name="download" class="SubmitButton" onclick="syncContent()">Download</button>
			    
			    <% } %>
			    <% if(role.equals("OWNER")){ %>
			    <button type="submit" name="delete" class="SubmitButton">Delete</button>
			    <% } %>
			</form>
		    
		<% if(erreur == 14){ %>
			<p class="ErrorText">Document saved successfully.</p>
		<% } %>
		</div>
		
		<div id="tchat">
		    <div id="messages-box" style="height: 300px; overflow-y: scroll; border: 1px solid #ccc; padding: 10px;">
		        </div>
		    
		    <input type="text" id="message-input" placeholder="Écrivez un message..." onkeypress="handleKeyPress(event)"/>
		    <button onclick="sendMessage()">Envoyer</button>
		</div>
		
		<div id="userListBlock">
		
			<h2 id ="EditorUserListTitle">Owner & Writers List</h2>
			
			<ul id="userList">
			<c:forEach var="entry" items="${userList}">
   	 			<li>${entry.key} : ${entry.value}</li>
			</c:forEach>
			</ul>
		
		</div>
	<% if(role.equals("OWNER")){ %>
		<div id="RoleForm">
			
			<h2 id="EditorRoleTitle">Edit roles</h2>
			
			<form method="post">
				
				<input id="EditorUsername" name="username" class="InputBox" type="text" placeholder="Username">
					
				<button type="submit" id="EditorBan" name="ban" class="SubmitButton">Ban</button>
					
				<button type="submit" id="EditorViewer" name="viewer" class="SubmitButton">SetToViewer</button>
				
				<button type="submit" id="EditorWriter" name="writer" class="SubmitButton">SetToWriter</button>
				
			</form>
		<% if(erreur == 1){ %>
			<p class="ErrorText">The form is not correctly filled.</p>
		<% }else if(erreur == 2){ %>
			<p class="ErrorText">User banned successfully.</p>
		<% }else if(erreur == 3){ %>
			<p class="ErrorText">This user doesn't exist.</p>
		<% }else if(erreur == 4){ %>
			<p class="ErrorText">You cannot ban yourself.</p>
		<% }else if(erreur == 5){ %>
			<p class="ErrorText">This user is already banned.</p>
		<% }else if(erreur == 6){ %>
			<p class="ErrorText">User set to VIEWER successfully.</p>
		<% }else if(erreur == 7){ %>
			<p class="ErrorText">This user doesn't exist.</p>
		<% }else if(erreur == 8){ %>
			<p class="ErrorText">You cannot set yourself to VIEWER.</p>
		<% }else if(erreur == 9){ %>
			<p class="ErrorText">This user is already a VIEWER.</p>
		<% }else if(erreur == 10){ %>
			<p class="ErrorText">User set to WRITER successfully.</p>
		<% }else if(erreur == 11){ %>
			<p class="ErrorText">This user doesn't exist.</p>
		<% }else if(erreur == 12){ %>
			<p class="ErrorText">You cannot set yourself to WRITER.</p>
		<% }else if(erreur == 13){ %>
			<p class="ErrorText">This user is already a WRITER.</p>
		<% } %>
		</div>
		
		</div>
	<% } %>
		<script>
		    var pseudoUtilisateur = "${not empty sessionScope.user ? sessionScope.user.username : 'Utilisateur'}";
		    var contextPath = "${pageContext.request.contextPath}";
		    var docName = new URLSearchParams(window.location.search).get('doc');
		</script>
		<script src="${pageContext.request.contextPath}/chat.js"></script>
		<script src="${pageContext.request.contextPath}/editor.js"></script>
	</body>
</html>