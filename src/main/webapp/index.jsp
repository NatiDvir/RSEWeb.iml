<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome!!</title>
    <script src="common/jquery-2.0.3.min.js"></script>
    <script src="index.js"></script>
    <style>
        form{
            padding:3px;
            border: aqua solid 1px;
            border-radius: 5px
        }
        *{
            margin: 5px;
        }
    </style>
</head>
<body>
<h1>Sign up/Login</h1>
<form id="loginForm" method="post" style="" action="login">
    <input type="radio" name="admin" id="adminRB" value="admin">admin</input>
    <input type="radio" name="admin" id="userRB" value="user">user</input><br/>
    <input type="text" name="username"/>   <br/>
    <button type="submit">Log in</button>
</form>
<br/>
</body>
</html>