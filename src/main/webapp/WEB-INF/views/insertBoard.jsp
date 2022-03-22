<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>getBoardList.jsp</title>
    <style>
        th {
            width: 100px;
            background-color: rgb(200, 150, 200);
            align: center;

        }

        .title {
            width: 300px;
        }
    </style>
</head>
<body>

<h1>새글 등록</h1>


<form action="insertBoard" method="post">

    <table border="1">
        <tr>
            <th>Title</th>
            <td><input type="text" name="title" size="40"/></td>
        </tr>
        <tr>
            <th>Writer</th>
            <td><input type="text" name="writer" size="40"/></td>
        </tr>
        <tr>
            <th>Content</th>
            <td><textarea name="content" cols="40" rows="10"></textarea></td>

        </tr>

        <tr>
            <td colspan="2">
                <input type="submit" value="새글 등록"/>
            </td>
        </tr>
    </table>
</form>


</body>
</html>