<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html>
<style type="text/css">
    th {
        width: 100px;
        background-color: rgb(200, 150, 200);
        align: center;

    }

    .title {
        width: 300px;
    }

</style>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>

</head>
<body>
<h1>게시글</h1>
<form action="updateBoard" method="post">
    <input type="hidden" name="seq" value="${board.seq}"/>
    <table border="1">
        <tr>
            <th>제목</th>
            <td><input type="text" name="title" value="${board.title}"
                       size="40"/></td>
        </tr>
        <tr>
            <th>작성자</th>
            <td>${board.writer}</td>
        </tr>
        <tr>
            <th>내용</th>

            <td><textarea name="content" rows="20"
                          cols="80">${board.content}</textarea></td>
        </tr>
        <tr>
            <th>등록일</th>
            <td><fmt:formatDate value="${board.createDate}"
                                pattern="yyyy-MM-dd"></fmt:formatDate></td>
        </tr>

        <tr>
            <td colspan="2" align="center"><input type="submit" value="게시글 수정">
            </td>
        </tr>
    </table>
</form>
<a href="deleteBoard?seq=${board.seq}">[게시글 삭제]</a>
<a href="/">[게시글 목록]</a>
</body>
</html>