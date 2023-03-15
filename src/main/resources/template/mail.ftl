<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <p>Hello, ${user.username}</p>
    <p>Someone, hopefully you, has requested to reset the password for your Airport account on http://localhost:8080</p>
    <p>If you did not perform this request, you can safely ignore this email. </p>
    <p>Otherwise, click the link below to complete the process.</p>
    <a href="http://localhost:8080/password/edit?reset_password_token=${token}" rel="link">Reset password</a>
</body>
</html>