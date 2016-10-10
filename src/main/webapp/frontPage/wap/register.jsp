<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/10/9
  Time: 14:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0" name="viewport" />
    <title>注册页面</title>
    <link href="../../code/css/base.css" rel="stylesheet" type="text/css"/>
    <link href="../../code/css/wapstyle.css" rel="stylesheet" type="text/css"/>
    <script src="../../code/js/jquery.min.js"></script>
    <script type="text/javascript" src="../../code/js/time_js.js"></script>
    <script type="text/javascript" src="../../code/js/invite.js"></script>
    <script>
        $(function(){
            $("#sendVerifyCode").click( function (){
                var telephone = $("#telephone").val().trim();
                if (telephone==null || !telephone.match(/^1\d{10}$/)){
                    alert("手机号码不正确")
                }else {
                    $.ajax({
                        type: 'get',
                        url: '/pc/admin/member/sendIndentifyCode',
                        data: {telephone:telephone},
                        success: function (params) {
                            var data=eval(params)
                            var errorCode=data.errorCode
                            if (errorCode==0){

                            } else if (errorCode==4){
                                alert(data.message)
                            }
                        }

                    })
                    $("#sendVerifyCode").css("display","none");
                    $("#downtime").css("display","block");
                    countDown();
                    setTimeout(function () {
                        $("#sendVerifyCode").css("display","block");
                        $("#downtime").css("display","none");
                    }, SS*1000);
                }
                <% request.getAttribute(""); %>
            })
            $("#register").click( function (){
                $("#form").attr("enctype","multipart/form-data");
                var requestObj = eval($("#registerForm").serializeObject());
                var request =getWxRegeisterParams(requestObj);
                if(!$("#memberName").val()){
                    alert("用户名不能为空");
                }else if($("#password").val().length<6){
                    alert("密码长度不能小于6位");
                }else{
                    $.ajax({
                        type: 'post',
                        url: '/pc/admin/member/register',
                        dataType:"json",
                        data: request,
                        contentType: "application/json",
                        beforeSend: function (XMLHttpRequest) {
                            XMLHttpRequest.setRequestHeader("from","2")
                        },
                        success: function (params) {
                            var data=eval(params)
                            var errorCode=data.errorCode
                            if (errorCode==0){
                                if (data.data){
                                    location.href="frontPage/wap/login.html"
                                }
                            } else if (errorCode==4){
                                alert(data.message)
                            }
                        }

                    })
                }


            })
        })

    </script>
</head>
<body>
<%
    String nickname=(String)request.getAttribute("nickname");
    Integer sex=(Integer)request.getAttribute("sex");
    String openid=(String)request.getAttribute("openid");
    String city=(String)request.getAttribute("city");
    String province=(String)request.getAttribute("province");
    String unionid=(String)request.getAttribute("unionid");
%>
<h3 class="registerpage">微淘米账户注册表</h3>
<form id="registerForm">
    <input type="text" name="memberName" id="memberName" value="<%= nickname %>" placeholder="请输入用户名" autofocus>
    <input type="password" name="password" id="password" value="" placeholder="请输入密码">
    <div id="telverify">
        <input type="text" name="telephone" id="telephone" value="" placeholder="请输入手机号">
        <a id="sendVerifyCode" >发送验证码</a>
        <div id="downtime"></div>
    </div>
    <input type="text" name="identifyCode" class="code" value="" placeholder="请输入验证码">
    <input type="text" name="invitedCode" class="code" id="invitedcode" value="" placeholder="请输入邀请码">
    <input type="hidden" name="unionid" value="<%= unionid %>" >
    <input type="hidden" name="province" value="<%= province %>" >
    <input type="hidden" name="city" value="<%= city %>" >
    <input type="hidden" name="openid" value="<%= openid %>" >
    <input type="hidden" name="sex" value="<%= sex %>" >

</form>
<a id="register" class="payconfirm"> 注&nbsp&nbsp册</a>
</body>
</html>
