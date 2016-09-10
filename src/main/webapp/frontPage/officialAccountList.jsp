<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/13
  Time: 14:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"  pageEncoding="UTF-8" %>
<html>
<head>
    <title>公众号列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
<form role="form">
    <div class="form-group">
        <label for="name">名称</label>
        <input type="text" class="form-control" id="name"
               placeholder="请输入名称">
    </div>
    <div class="form-group">
        <label for="inputfile">文件输入</label>
        <input type="file" id="inputfile">
        <p class="help-block">这里是块级帮助文本的实例。</p>
    </div>
    <div class="checkbox">
        <label>
            <input type="checkbox"> 请打勾
        </label>
    </div>
    <button type="submit" class="btn btn-default">提交</button>
</form>
</body>
</html>
