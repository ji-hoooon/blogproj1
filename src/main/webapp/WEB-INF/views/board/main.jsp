<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>

<div class="container my-3">
    <div class="d-flex justify-content-end mb-1">
        <form class="d-flex col-lg-3" action="/" method="get">
            <input class="form-control" type="text" placeholder="Search" name="keyword">
            <%-- get요청은 Body가 없으므로, QueryString or PathVariable --%>
            <%--PK, FK가 아니므로 쿼리스트링으로 전달--%>
            <button class="btn btn-primary">Search</button>
        </form>
    </div>
    <div class="my-board-box row">
        <%--        for문으로 글 출력--%>
        <%--        안에 있는 데이터를 가져와야하므로, '.'으로 접근--%>
        <c:forEach items="${boardPG.content}" var="board">
            <%-- 글 아이템 시작 --%>
            <div class="card col-lg-3 pt-2">
                    <%--                    <img class="card-img-top" style="height: 250px;" src="/images/dora.png" alt="Card image">--%>
                <img class="card-img-top" style="height: 250px;" src="${board.thumbnail}" alt="Card image">
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
    <%--        <ul class="pagination mt-3 d-flex justify-content-center">--%>
    <%--            <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>--%>
    <%--            <li class="page-item"><a class="page-link" href="#">Next</a></li>--%>
    <%--        </ul>--%>
    <%--    </div>--%>
    <%--    <%@ include file="../layout/footer.jsp" %>--%>
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

    <%--    </div>--%>

    <ul class="pagination mt-3 d-flex justify-content-center">
        <%--        <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>--%>
        <%--        <li class="page-item"><a class="page-link" href="#">Next</a></li>--%>
        <li class="page-item ${boardPG.first ? "disabled" : ""}"><a class="page-link"
                                                                    href="/?page=${boardPG.number -1}&keyword=${param.keyword}">Previous</a></li>
        <li class="page-item ${boardPG.last ? "disabled" : ""}"><a class="page-link" href="/?page=${boardPG.number +1}&keyword=${param.keyword}">Next</a>
        </li>
    </ul>
</div>

<%@ include file="../layout/footer.jsp" %>