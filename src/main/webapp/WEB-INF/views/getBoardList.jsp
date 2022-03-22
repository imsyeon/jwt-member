<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>getBoardList.jsp</title>
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
</head>
<body>

<h1>게시글 목록</h1>
<form action="/" method="GET">
    <div>
        <input type="text" name="searchKeyword">
        <input id="submitBtn" type="submit" value="Search"
               class="search__button">
    </div>
</form>
<table boarder="1" cellpadding="0" cellspacing="0" width="700">
    <tr>
        <th>번호</th>
        <th class="title">제목</th>
        <th>작성자</th>
        <th>등록일</th>
    </tr>
    <c:forEach var="board" items="${boardList.content}">
        <tr align="center">
            <td>${board.seq}</td>
            <td align="left"><a
                    href="getBoard?seq=${board.seq}">${board.title}</a></td>
            <td>${board.writer}</td>
            <td><fmt:formatDate value="${board.createDate}"
                                pattern="yyyy-MM-dd"></fmt:formatDate></td>
        </tr>
    </c:forEach>

</table>
<hr>

<a href="insertBoard">[새글 등록]</a>
<a href="/?sort=seq&dir=asc&searchKeyword=${param.searchKeyword}&page=0">[오름차순]</a>
<a href="/?sort=seq&dir=desc&searchKeyword=${param.searchKeyword}&page=0">[내림차순]</a>

<!-- 페이징 영역 시작 -->
<div class="paging">
    <ul>
        <!-- 이전 -->
        <c:choose>
            <c:when test="${boardList.first}"> </c:when>
            <c:otherwise>

                <a class="page-link"
                   href="/?searchKeyword=${param.searchKeyword}&page=${boardList.number-1}&sort=${sort}&dir=${dir}">이전</a>

            </c:otherwise> </c:choose>
        <!-- 페이지 그룹 -->
        <c:forEach begin="${startBlockPage}" end="${endBlockPage}" var="i">
            <c:choose>
                <c:when test="${boardList.pageable.pageNumber+1 == i}">
                    <a class="page-link"
                       href="/?searchKeyword=${param.searchKeyword}&page=${i-1}&sort=${sort}&dir=${dir}">${i}</a>

                </c:when> <c:otherwise>
                <a class="page-link"
                   href="/?searchKeyword=${param.searchKeyword}&page=${i-1}&sort=${sort}&dir=${dir}">${i}</a>

            </c:otherwise>
            </c:choose>
        </c:forEach>
        <!-- 다음 -->
        <c:choose>
            <c:when test="${boardList.last}">

            </c:when>
            <c:otherwise>
                <a class="page-link"
                   href="/?searchKeyword=${param.searchKeyword}&page=${boardList.number+1}&sort=${sort}&dir=${dir}">다음</a>


            </c:otherwise>
        </c:choose>
    </ul>
</div>


</body>
</html>