<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Editor</title>
	</head>
	<body>
	<% String role = (String) request.getAttribute("role"); %>
	
		<h1 id="EditorTitle">Editor</h1>
		
		<div id="document">
		
			<h2 id="EditorDocumentTitle">Document</h2>
		
		</div>
		
		<div id="chat">
		
			<h2 id="EditorChatTitle">Chat</h2>
		
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
			
		</div>
		
		<div id="DeleteForm">
			
			<h2 id="EditorDeleteTitle">Delete Document</h2>
			
			<form method="post">
					
				<button type="submit" id="EditorDelete" name="delete" class="SubmitButton">Delete</button>
					
			</form>
			
		</div>
	<% } %>

	</body>
</html>