<%--
  Created by IntelliJ IDEA.
  User: imsooyeon
  Date: 2022/03/31
  Time: 3:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판</title>
    <script src="https://code.jquery.com/jquery-3.4.1.js"
            type="text/javascript"></script>

    <script src="/js/board.js" type="text/javascript"></script>
</head>
<body>


<div>
    <button onclick="board_list();" >글목록</button>
    <button onclick="board_write();" >글쓰기</button>
</div>


<div id="boardWriteDiv">
    <div class="text-center">
        <h2>글 등록</h2>
    </div>


    <div class="container p-3">
        <form id="insertBoard" method="post" action="/board/write">
            <table>
                <tr class="form-group">
                    <th><label>글번호 :</label></th>
                    <td><input class="form-control" type="text" name="seq" readonly>
                    </td>
                </tr>
                <tr class="form-group">
                    <th>제목 :</th>
                    <td><input class="form-control" type="text" name="title"></td>
                </tr>
                <tr class="form-group">
                    <th>내용 :</th>
                    <td><input class="form-control" type="text" name="content"></td>
                </tr>
                <tr class="form-group">
                    <th>글쓴이 :</th>
                    <td><input class="form-control" type="text" name="writer"></td>
                </tr>
                <tr class="form-group">
                    <th>날짜 :</th>
                    <td><input class="form-control" type="text" name="createDate">
                    </td>
                </tr>

            </table>

            <div class="container p-3">
                <div class="btn-group">
                    <input type="button" value="등록" class="btn btn-primary"
                           id="btnInsert">
                    <input type="button" value="초기화" class="btn btn-info"
                           id="btnInit">
                    <input type="button" value="수정" class="btn btn-secondary"
                           id="btnUpdate">
                </div>
            </div>
        </form>
    </div>
</div>

<div class="text-center" id="boardListDiv">
    <div>
        <input class="form-control" type="text" id="searchKeyword">
        <button onclick="search()">검색하기</button>
    </div>
    <h2>글 목록 Ajax</h2>

    <table class="table table-striped">
        <thead>
        <br>
        <th>번호
            <button onclick="sortSeq('seq','asc')">오름차순</button>
            <button onclick="sortSeq('seq','desc')">내림차순</button>
        </th>
        <th>제목
            <button onclick="sortSeq('title','asc')">오름차순</button>
            <button onclick="sortSeq('title','desc')">내림차순</button>
        </th>
        <th>내용
            <button onclick="sortSeq('content','asc')">오름차순</button>
            <button onclick="sortSeq('content','desc')">내림차순</button>
        </th>
        <th>작성자
            <button onclick="sortSeq('writer','asc')">오름차순</button>
            <button onclick="sortSeq('writer','desc')">내림차순</button>
        </th>
        <th>등록일
            <button onclick="sortSeq('writer','asc')">오름차순</button>
            <button onclick="sortSeq('writer','desc')">내림차순</button>
        </th>
        <th>&nbsp;</th>
        <th>&nbsp;</th>
        </tr>
        </thead>
        <tbody id='list'>
        </tbody>
    </table>
    <div id="paging">

    </div>
</div>
</body>
</html>
