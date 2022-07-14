<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File List</title>
</head>
<body>
	<h1 align="center">Index File</h1>
	<br/>
	<table border="1" cellpadding="10">
		<tr>
			<th>Name</th>
			<th>Size (kb)</th>
			<th>Href</th>
		</tr>
		<c:forEach var="file" items="${index}">
		<tr>
			<td>${file.fileName}</td>
			<td>${file.size}</td>
            <c:if test="${(file.isFile == false)}">
                <td>
                    <a href="open?relativePath=${file.relativePath}" >Open</a>
                </td>
            </c:if>
            <c:if test="${(file.isFile == true)}">
                <td>
                    <a href="${file.relativePath}" >View</a>

                </td>
            </c:if>
		</tr>		
		</c:forEach>
	</table>
</body>
</html>