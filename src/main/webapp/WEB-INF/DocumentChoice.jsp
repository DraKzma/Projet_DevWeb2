<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="projet_dw2.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Document Choice</title>
	</head>
	<body>
	<%-- Valeur de l'attribut "error":
	0: Pas d'erreur
	1: Formulaire non rempli correctement 
	2: Document du même nom déjà existant dans la base (create) 
	3: Document inexistant (find) 
	4: L'utilisateur n'as pas access au document (find) --%>
	<% int erreur = (int) request.getAttribute("error"); User user = (User) session.getAttribute("user"); int role = user.getPermissions(); %>
		<div id="DocumentChoiceBlock">
		
			<h1 id="DocumentChoiceTitle">Document Choice</h1>
		
			<form method="post">
			
				<input id="DocumentChoiceName" name="DocName" class="InputBox" type="text" placeholder="DocName">
				
				<button type="submit" id="DocumentChoiceCreate" name="create" class="SubmitButton">Create</button>
				
				<button type="submit" id="DocumentChoiceFind" name="find" class="SubmitButton">Find</button>
			
			</form>
		<% if(erreur == 1){ %>
			<p class="ErrorText">The form is not correctly filled.</p>
		<% } else if(erreur == 2){ %>
			<p class="ErrorText">A document with this name already exists.</p>
		<% } else if(erreur == 3){ %>
			<p class ="ErrorText">This document doesn't exist.</p>		
		<% }else if (erreur == 4){ %>
			<p class="ErrorText">You do not have access to this document.</p>
		<% } %>
		</div>
		<% if(role == 2){ %>
		<div id="DocumentChoiceDeleteUser">
		
			<form method="post">
			
				<h2 id="DocumentChoiceDeleteUserTitle">Delete User</h2>
			
				<input id="DocumentChoiceUsername" name="Username" class="InputBox" type="text" placeholder="Username">
				
				<button type="submit" id="DocumentChoiceDeleteUser" name="deleteUser" class="SubmitButton">DeleteDocument</button>
			
			</form>
			
		</div>
		
		<div id="DocumentChoiceDeleteDocument">
		
			<form method="post">
			
				<h2 id="DocumentChoiceDeleteDocumentTitle">Delete Document</h2>
			
				<input id="DocumentChoiceDocumentName" name="documentName" class="InputBox" type="text" placeholder="DocumentName">
				
				<button type="submit" id="DocumentChoiceDeleteDocument" name="deleteDocument" class="SubmitButton">DeleteUser</button>
			
			</form>
			
		</div>
		<% } %>
		<div id="DocumentChoiceSignOffBlock">
		
			<span class="DocumentChoiceSpan"><a href="http://localhost:8080/Projet_DevWeb2/SignOff" class="DocumentChoicebuttonLink" id="DocumentChoiceSignOff">Sign Off</a></span>
		
		</div>
		
		
	</body>
</html>