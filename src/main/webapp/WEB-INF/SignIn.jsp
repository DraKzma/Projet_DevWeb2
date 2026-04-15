<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sign In</title>
	</head>
	<body>
	<%-- Valeur de l'attribut "error":
	0: Pas d'erreur
	1: Formulaire non rempli correctement
	2: Mot de passe incorrect --%>
	<% int erreur = (int) request.getAttribute("error"); %>
		<div id="SignInBlock">
		
			<h1 id="SignInTitle">Sign In</h1>
		
			<form method="post">
			<% if(erreur == 0){ %>
				<input id="SignInUsername" name="username" class="InputBox" type="text" placeholder="username">
			<% }else{ %>
				<input id="SignInUsername" name="username" class="InputBox" type="text" placeholder="username" value="${param.username}">
			<% } %>
				<input id="SignInPassword" name="password" class="InputBox" type="password" placeholder="password">
				
				<button type="submit" id="SignUpSubmit" name="submit" class="SubmitButton">Sign In</button>
			
			</form>
		<% if(erreur == 1){ %>
			<p class="ErrorText">The form is not correctly filled.</p>
		<% }else if(erreur == 2){ %>
			<p class="ErrorText">The provided password for this user is incorrect.</p>
		<% } %>
		</div>
		
		
	</body>
</html>