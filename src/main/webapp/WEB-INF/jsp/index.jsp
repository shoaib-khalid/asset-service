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
			<th>Name</th><th>File Type</th><th>Size (byte)</th><th>View</th>
		</tr>
		<c:forEach var="file" items="${index}">
		<tr>
			<td>${file.fileName}</td>
            <td>${file.fileType}</td>
			<td>${file.size}</td>
            <c:if test="${(file.fileType == 'File folder')}">
                <td>Folder</td>
            </c:if>
            <c:if test="${(file.fileType != 'File folder')}">
                <td>View</td>
            </c:if>
		</tr>		
		</c:forEach>
	</table>
</body>
</html>