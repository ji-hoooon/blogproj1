<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>

<div class="container my-3">
    <div class="my-board-box row">
<%--        for문으로 글 출력--%>
<%--        안에 있는 데이터를 가져와야하므로, '.'으로 접근--%>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="../layout/header.jsp" %>
    <div class="container my-3">
        <div class="my-board-box row">
            <c:forEach items="${boardPG.content}" var="board">
                <%-- 글 아이템 시작 --%>
                <div class="card col-lg-3 pt-2">
                    <img class="card-img-top" style="height: 250px;" src="/images/dora.png" alt="Card image">
                    <hr/>
                    <div class="card-body">
                        <div>작성자 : ${board.user.username}</div>
                        <h4 class="card-title my-text-ellipsis">${board.title}</h4>
                        <a href="/board/${board.id}" class="btn btn-primary">상세보기</a>
                    </div>
                </div>
                <%-- 글 아이템 끝 --%>
            </c:forEach>
        </div>
        <ul class="pagination mt-3 d-flex justify-content-center">
            <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
            <li class="page-item"><a class="page-link" href="#">Next</a></li>
        </ul>
    </div>
    <%@ include file="../layout/footer.jsp" %>
<%--        &lt;%&ndash; 글 아이템 시작 &ndash;%&gt;--%>
<%--        <div class="card col-lg-3 pt-2">--%>
<%--            <img class="card-img-top" style="height: 250px;" src="/images/dora.png" alt="Card image">--%>
<%--            <hr/>--%>
<%--            <div class="card-body">--%>
<%--                <div>작성자 : 홍길동</div>--%>
<%--                <h4 class="card-title my-text-ellipsis">글제목</h4>--%>
<%--                <a href="/board/1" class="btn btn-primary">상세보기</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        &lt;%&ndash; 글 아이템 끝 &ndash;%&gt;--%>

<%--        &lt;%&ndash; 글 아이템 시작 &ndash;%&gt;--%>
<%--        <div class="card col-lg-3 pt-2">--%>
<%--            <img class="card-img-top" style="height: 250px;" src="/images/dora.png" alt="Card image">--%>
<%--            <hr/>--%>
<%--            <div class="card-body">--%>
<%--                <div>작성자 : 홍길동</div>--%>
<%--                <h4 class="card-title my-text-ellipsis">글제목</h4>--%>
<%--                <a href="/board/1" class="btn btn-primary">상세보기</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        &lt;%&ndash; 글 아이템 끝 &ndash;%&gt;--%>

<%--        &lt;%&ndash; 글 아이템 시작 &ndash;%&gt;--%>
<%--        <div class="card col-lg-3 pt-2">--%>
<%--            <img class="card-img-top" style="height: 250px;" src="/images/dora.png" alt="Card image">--%>
<%--            <hr/>--%>
<%--            <div class="card-body">--%>
<%--                <div>작성자 : 홍길동</div>--%>
<%--                <h4 class="card-title my-text-ellipsis">글제목</h4>--%>
<%--                <a href="/board/1" class="btn btn-primary">상세보기</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        &lt;%&ndash; 글 아이템 끝 &ndash;%&gt;--%>

<%--        &lt;%&ndash; 글 아이템 시작 &ndash;%&gt;--%>
<%--        <div class="card col-lg-3 pt-2">--%>
<%--            <img class="card-img-top" style="height: 250px;" src="/images/dora.png" alt="Card image">--%>
<%--            <hr/>--%>
<%--            <div class="card-body">--%>
<%--                <div>작성자 : 홍길동</div>--%>
<%--                <h4 class="card-title my-text-ellipsis">글제목</h4>--%>
<%--                <a href="/board/1" class="btn btn-primary">상세보기</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        &lt;%&ndash; 글 아이템 끝 &ndash;%&gt;--%>

    </div>

    <ul class="pagination mt-3 d-flex justify-content-center">
<%--        <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>--%>
<%--        <li class="page-item"><a class="page-link" href="#">Next</a></li>--%>
    <li class="page-item ${boardPG.first ? "disabled" : ""}"><a class="page-link" href="/?page=${boardPG.number -1}">Previous</a></li>
    <li class="page-item ${boardPG.last ? "disabled" : ""}"><a class="page-link" href="/?page=${boardPG.number +1}">Next</a></li>
    </ul>
</div>

<%@ include file="../layout/footer.jsp" %>