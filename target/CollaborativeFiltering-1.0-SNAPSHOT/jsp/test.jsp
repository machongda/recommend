<%--
  Created by IntelliJ IDEA.
  User: mada
  Date: 2020/4/7
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍欢迎页</title>
</head>
<body>
bId:${book.BId}<br>
name:${book.name}<br>
<img src="${book.coverPath}" alt="封面"/>
authorName:${book.authorName}<br>
publishingHouse:${book.publishingHouse}<br>
publishingYear:${book.publishingYear}<br>
labels:${book.labels}<br>
briefIntroduction:${book.briefIntroduction}<br>
name:${book.score}<br>
name:${book.pointNumber}<br>
</body>
</html>
