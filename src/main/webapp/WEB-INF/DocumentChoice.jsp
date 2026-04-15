<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Document Choice</title>
	</head>
	<body>
	<%-- Valeur de l'attribut "error":
	0: Pas d'erreur
	1: Formulaire non rempli correctement --%>
	<% int erreur = (int) request.getAttribute("error"); %>
		<div id="DocumentChoiceBlock">
		
			<h1 id="DocumentChoiceTitle">Document Choice</h1>
		
			<form method="post">
			
				<input id="DocumentChoiceName" name="DocName" class="InputBox" type="text" placeholder="DocName">
				
				<button type="submit" id="DocumentChoiceCreate" name="create" class="SubmitButton">Create</button>
				
				<button type="submit" id="DocumentChoiceFind" name="find" class="SubmitButton">Find</button>
			
			</form>
		<% if(erreur == 1){ %>
			<p class="ErrorText">The form is not correctly filled.</p>
		<% } %>
		</div>
		
		<div id="DocumentChoiceSignOffBlock">
		
			<span class="DocumentChoiceSpan"><a href="http://localhost:8080/Projet_DevWeb2/SignOff" class="DocumentChoicebuttonLink" id="DocumentChoiceSignOff">Sign Off</a></span>
		
		</div>
		
		
	</body>
</html>