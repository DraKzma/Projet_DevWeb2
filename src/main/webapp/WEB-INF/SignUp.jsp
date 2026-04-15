<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sign Up</title>
	</head>
	<body>
	<%-- Valeur de l'attribut "error":
	0: Pas d'erreur
	1: Formulaire non rempli correctement
	2: Utilisateur existe déjà --%>
	<% int erreur = (int) request.getAttribute("error"); %>
		<div id="SignUpBlock">
		
			<h1 id="SignUpTitle">Sign Up</h1>
		
			<form method="post">
			<% if(erreur == 0){ %>
				<input id="SignUpUsername" name="username" class="InputBox" type="text" placeholder="username">
			<% }else{ %>
				<input id="SignUpUsername" name="username" class="InputBox" type="text" placeholder="username" value="${param.username}">
			<% } %>
				<input id="SignUpPassword" name="password" class="InputBox" type="password" placeholder="password">
				
				<button type="submit" id="SignUpSubmit" name="submit" class="SubmitButton">Sign Up</button>
			
			</form>
		<% if(erreur == 1){ %>
			<p class="ErrorText">The form is not correctly filled.</p>
		<% }else if(erreur == 2){ %>
			<p class="ErrorText">This username is already taken.</p>
		<% } %>
		</div>


	</body>
</html>