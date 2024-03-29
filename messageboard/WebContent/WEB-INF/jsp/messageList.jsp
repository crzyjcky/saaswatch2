<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>
<head>
<title>Message List</title>
</head>
<body>
	<c:forEach items="${messages}" var="message">
		<table>
			<tr>
				<td>Tenant ID</td>
				<td>${message.tenantId}</td>
			</tr>
			<tr>
				<td>Author</td>
				<td>${message.author}</td>
			</tr>
			<tr>
				<td>Title</td>
				<td>${message.title}</td>
			</tr>
			<tr>
				<td>Body</td>
				<td>${message.body}</td>
			</tr>
			<tr>
				<td colspan="2"><a href="messageDelete?messageId=${message.id}">Delete</a>
				</td>
			</tr>
		</table>
		<hr />
	</c:forEach>
	<a href="messagePost.htm">Post</a>
	<br />
	<br />
	<br />
	<br />
	<a href="<c:url value="/login.jsp" />">Login</a>
	<br />
	<br />
	<a href="<c:url value="/j_spring_security_logout" />">Logout</a>
	<br />
	<br />

</body>
</html>