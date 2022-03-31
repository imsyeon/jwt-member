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


function board_write() {

    let boardWriteDiv = document.getElementById("boardWriteDiv");
    let boardListDiv = document.getElementById("boardListDiv");

    boardWriteDiv.style.display = "block";
    boardListDiv.style.display = "none";

}

function board_list() {

    let boardListDiv = document.getElementById("boardListDiv");
    let boardWriteDiv = document.getElementById("boardWriteDiv");
    let searchKeywordId = document.getElementById("searchKeyword");

    boardListDiv.style.display = "block";
    boardWriteDiv.style.display = "none";
    searchKeywordId.value ='';

    searchKeyword = searchKeywordId.value;
    page = 0;
    boardList();
}


function boardList() {

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


    $('tbody#list').empty();

    $.each(data.content, function (idx, board) {


        $("<tr>")
            .append($("<td>").html(board.seq))
            .append($("<td>").html(board.title))
            .append($("<td>").html(board.content))
            .append($("<td>").html(board.writer))
            .append($("<td>").html(board.createDate.substr(0, 10)))
            .append($("<td>").html("<button class='btn btn-success' id='btnSelect' onclick='board_write();'>조회</button>"))
            .append($("<td>").html("<button class='btn btn-primary' id='btnDelete'>삭제</button>"))
            .append($("<input type='hidden' id='hidden_seq'>").val(board.seq))
            .appendTo('tbody#list');


    });

    let pNum = '';
    for (let i = 1; i <= data.totalPages; i++) {
        pNum += '<a href="#" onclick="updatePage(' + i + ')" class="page-btn">' + i + '</a>';


    }
    $('#paging').html(pNum);
}


function updatePage(page2) {
    page = page2 - 1
    boardList()
}

function search() {
    page =0;
    searchKeyword = $("#searchKeyword").val();
    boardList()
}

function sortSeq(sortOpt, dirOpt) {
    sort = sortOpt;
    dir = dirOpt;

    boardList()
}

//상세글
function boardSelect() {
    $('body').on('click', '#btnSelect', function () {
        let seq = $(this).closest('tr').find('#hidden_seq').val();

        $.ajax({
            url: '/board/' + seq,
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

        if (result) {
            $.ajax({
                url: '/board/' + seq,
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

        if (result) {
            $.ajax({
                url: '/board/' + seq,
                method: 'PATCH',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                data: JSON.stringify(boardObj),
                error: function (error, status, msg) {
                    alert("상태코드 " + status + "에러메시지" + msg);
                },
                success: function (data) {

                    if (data == 1) {
                        $('#btnInit').trigger('click');
                    }
                    boardList();
                }
            });
        }
    });
}
