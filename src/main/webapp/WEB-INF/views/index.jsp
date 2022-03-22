<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Spring Boot JSP</title>
    <script src="https://code.jquery.com/jquery-3.4.1.js"
            type="text/javascript"></script>
    <script>
        let page = 0;
        let searchKeyword = '';
        let sort = ''
        let dir = '';


        $(function () {
            boardList();
            boardSelect();
            boardDelete();
            boardInsert();
            form_init();
            boardUpdate();
        });

        function boardList() {
            // console.log('/board/list?page='+page +'&searchKeyword='+searchKeyword+'&sort='+sort + '&dir=' +dir);
            $.ajax({
                url: '/board/list?page=' + page + '&searchKeyword=' + searchKeyword + '&sort=' + sort + '&dir=' + dir,
                method: 'GET',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',

                error: function (error, status, msg) {
                    alert("상태코드 " + status + "에러메시지" + msg);
                },
                success: boradResult
            });
        }

        function boradResult(data) {
            //  let querySelector = document.querySelector('tbody#list').empty();

            $('tbody#list').empty();

            $.each(data.content, function (idx, board) {
                //console.log(data.content);
                //console.log(idx,board);

                $("<tr>")
                    .append($("<td>").html(board.seq))
                    .append($("<td>").html(board.title))
                    .append($("<td>").html(board.content))
                    .append($("<td>").html(board.writer))
                    .append($("<td>").html(board.createDate.substr(0, 10)))
                    .append($("<td>").html("<button class='btn btn-success' id='btnSelect'>조회</button>"))
                    .append($("<td>").html("<button class='btn btn-primary' id='btnDelete'>삭제</button>"))
                    .append($("<input type='hidden' id='hidden_seq'>").val(board.seq))
                    .appendTo('tbody#list');


            });// each
            //  data.content.forEach(function (idx,board){
            //      $("<tr>")
            //              .append($("<td>").html(board.seq))
            //              .append($("<td>").html(board.title))
            //              .append($("<td>").html(board.content))
            //              .append($("<td>").html(board.writer))
            //              .append($("<td>").html(board.createDate))
            //              .append($("<td>").html("<button class='btn btn-success' id='btnSelect'>조회</button>"))
            //              .append($("<td>").html("<button class='btn btn-primary' id='btnDelete'>삭제</button>"))
            //              .append($("<input type='hidden' id='hidden_seq'>").val(board.seq))
            //              .appendTo('tbody#list');
            //         console.log(board.seq);
            // }) ;
            let pNum = '';
            for (let i = 1; i <= data.totalPages; i++) {
                pNum += '<a href="#" onclick="updatePage(' + i + ')" class="page-btn">' + i + '</a>';
                console.log(pNum);

            }
            $('#paging').html(pNum);
        }


        function updatePage(page2) {
            page = page2 - 1
            boardList()
        }

        function search() {
            searchKeyword = $("#searchKeyword").val();
            console.log(searchKeyword);
            boardList()
        }

        function sortSeq(sortOpt, dirOpt) {
            sort = sortOpt;
            dir = dirOpt;
            console.log(sort, dir);

            boardList()
        }

        //상세글
        function boardSelect() {
            $('body').on('click', '#btnSelect', function () {
                let seq = $(this).closest('tr').find('#hidden_seq').val();
                // console.log(seq);
                $.ajax({
                    url: 'board/' + seq,
                    method: 'GET',
                    error: function (error, status, msg) {
                        alert("상태코드 " + status + "에러메시지" + msg);
                    },
                    success: boardSelectResult
                });

            })
        }

        function boardSelectResult(data) {
            let board = data;
            //console.log(board);
            document.querySelector("input[name=seq]").value = board.seq;
            document.querySelector("input[name=title]").value = board.title;
            document.querySelector("input[name=content]").value = board.content;
            document.querySelector("input[name=writer]").value = board.writer;
            document.querySelector("input[name=createDate]").value = board.createDate.substr(0, 10);

        }

        function boardDelete() {

            $('body').on('click', '#btnDelete', function () {
                let seq = $(this).closest('tr').find('#hidden_seq').val();
                let result = confirm('글을 정말 삭제하시겠습니까?');
                console.log('result : ' + result);
                if (result) {
                    $.ajax({
                        url: 'board/' + seq,
                        method: 'DELETE',
                        error: function (error, status, msg) {
                            alert("상태코드 " + status + "에러메시지" + msg);
                        },
                        success: function () {
                            boardList();
                        }
                    });
                }
            });
        }

        function boardInsert() {

            $('body').on('click', '#btnInsert', function () {
                let seq = $("input:text[name='seq']").val();
                let title = $("input:text[name='title']").val();
                let content = $("input:text[name='content']").val();
                let writer = $("input:text[name='writer']").val();
                let createDate = $("input:text[name='createDate']").val();
                let boardObj = {
                    seq: seq,
                    title: title,
                    content: content,
                    writer: writer,
                    createDate: createDate
                };
                let result = confirm('글을 정말 등록하시겠습니까?');
                if (result) {
                    $.ajax({
                        url: '/board/write',
                        method: 'POST',
                        contentType: 'application/json;charset=utf-8',
                        dataType: 'json',
                        data: JSON.stringify(boardObj),

                        error: function (error, status, msg) {
                            alert("상태코드 " + status + "에러메시지" + msg);
                        },
                        success: function (data) {
                            console.log(data);
                            if (data == 1) {
                                $('#btnInit').trigger('click');
                            }
                            boardList();
                        }
                    });
                }
            });
        }

        function form_init() {
            $('#btnInit').on('click', function () {
                $('#insertBoard').each(function () {
                    this.reset();
                })
            });
        }

        function boardUpdate() {

            $('body').on('click', '#btnUpdate', function () {

                let seq = document.querySelector("input[name=seq]").value;
                let title = document.querySelector("input[name=title]").value;
                let content = document.querySelector("input[name=content]").value;
                let writer = document.querySelector("input[name=writer]").value;
                let boardObj = {
                    seq: seq,
                    title: title,
                    content: content,
                    writer: writer
                };
                let result = confirm('글을 정말 수정하시겠습니까?');
                console.log('result : ' + result);
                if (result) {
                    $.ajax({
                        url: 'board/' + seq,
                        method: 'PATCH',
                        contentType: 'application/json;charset=utf-8',
                        dataType: 'json',
                        data: JSON.stringify(boardObj),
                        error: function (error, status, msg) {
                            alert("상태코드 " + status + "에러메시지" + msg);
                        },
                        success: function (data) {
                            console.log(data);
                            if (data == 1) {
                                $('#btnInit').trigger('click');
                            }
                            boardList();
                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

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

<div class="text-center">
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