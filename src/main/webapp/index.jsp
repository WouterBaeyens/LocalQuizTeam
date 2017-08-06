<%-- 
    Document   : home
    Created on : Jun 29, 2017, 1:03:15 AM
    Author     : Wouter
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<c:url value="/resources/js/jquery.js"/>"></script>
        <script src="<c:url value="/resources/js/custom.js"/>"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>

        <!-- Bootstrap Core CSS -->
        <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/custom.css"/>" rel="stylesheet">

        <title>Agata Quiz Team</title>
    </head>
    <body>
        
        <nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Agata Quiz Team</a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <form id="addMemberForm" action="/LocalQuizTeam/api/member/update" method="post" class="navbar-form navbar-left">
        <div class="form-group">
        <button type="submit" class="btn btn-default">Add member</button>
            <input type="text" class="form-control" name="name" placeholder="name">
          <input type="text" class="form-control" name="email" placeholder="email">
        </div>
      </form>
    
   <ul class="nav navbar-nav">
    <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Remove member <span class="caret"></span></a>
          <ul class="dropdown-menu">
                                <c:forEach var="member" items="${it.members}">
                                <!--<li onclick="post('URL','email',<c:out value="${member.email}" />)">-->
                                <li onclick="deleteMember('<c:out value="${member.email}" />')">
                                    <a><c:out value="${member.name}" /></a>
                                    <!--
                                    <form action="/LocalQuizTeam/quizinfo/member/delete" method="post" class="inline" onsubmit="setTimeout(function () { window.location.reload(); }, 100)">
                                        <input type="hidden" name="email" value="<c:out value="${member.email}" />">
                                        <input type="submit" value="<c:out value="${member.name}" />" class="link-button"/>
                                    </form>-->
                                </li>
                                
                                </c:forEach>
          </ul>
        </li>
   </ul>
    </div>
        </nav>
        <div id="err_block" style="white-space: pre-wrap;background-color: #a94442;    top: -20px;     z-index : 1; 
"><p id="err_list"><c:out value="${it.errorMessage}" /></p></div>
        
      
        <p>
            Welcome to the web-page for our team the "Agata Quiz Team"!
        </p>
        <p>
            In the table below you can keep track of who wants to join which quiz, 
            as well as add and remove participants for each quiz.
        </p>
        <button type="button" onclick="refresh()" class="btn btn-default btn-sm">
          <span class="glyphicon glyphicon-refresh"></span> Refresh
        </button>
        <table class="table">
            <thead>
            <th> add participant </th>
            <th> quiz </th>
            <th> date </th>
            <th> location </th>
            <th> participants </th>
        </thead>
        <tbody>
            <c:forEach var="quiz" items="${it.quizzes}">
                <tr>
                    <td>  <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">
                                <span class="glyphicon glyphicon-triangle-bottom"></span>
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <c:forEach var="member" items="${it.members}">
                                    <li onclick="subscribeMember('<c:out value="${member.email}" />','<c:out value="${quiz.id}" />')">
                                        <a><c:out value="${member.name}" /></a></li>
                                </c:forEach>
                            </ul>
                        </div>   
                    </td>
                    <td>  <c:out value="${quiz.name}" />  </td>
                    <td>  <c:out value="${quiz.date}" />  </td>
                    <td>  <c:out value="${quiz.adress}" />  </td>
                    <td>  <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">
                                ${fn:length(quiz.participants)} /  <c:out value="${quiz.minPeople}" />
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <c:forEach var="participant" items="${quiz.participants}">
                                    <li style="padding-right:12%;margin-left: 5%">
                                        <div>
                                            <c:out value="${participant.name}" />
                                            <a onclick="unsubscribeMember('<c:out value="${participant.email}" />','<c:out value="${quiz.id}" />')">
                                                <span class="glyphicon glyphicon-remove" style="float:right"></span></a>
                                        </div></li>
                                    
                                    
                                    
                                    
                                    <!--<li style="padding-right:12%;margin-left: 5%">
                                        <p>
                                            <c:out value="${participant.name}" />
                                            <a href="http://localhost:8080/LocalQuizTeam/quizinfo/quiz/<c:out value="${quiz.id}" />/unsubscribe/<c:out value="${participant.email}" />"> 
                                                <span class="glyphicon glyphicon-remove" style="float:right"></span>
                                            </a>
                                        </p></li>-->
                                </c:forEach>
                            </ul>
                        </div>  
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
