<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/10/13
  Time: 18:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0" name="viewport" />
    <title>我的邀请码</title>
    <link href="../../code/css/base.css" rel="stylesheet" type="text/css"/>
    <link href="../../code/css/wapstyle.css" rel="stylesheet" type="text/css"/>
    <script src="../../code/js/jquery.min.js"></script>
    <script src="../../code/js/jquery.cookie.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script>
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: '${appid}', // 必填，公众号的唯一标识
            timestamp: '${timestamp}', // 必填，生成签名的时间戳
            nonceStr: '${nonceStr}', // 必填，生成签名的随机串
            signature: '${signature}',// 必填，签名，见附录1
            jsApiList: ["onMenuShareAppMessage","onMenuShareTimeline"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        $(function(){
            var inviteheight=$(window).height();
            var invitewidth=$(window).width();
            var wayheight=$(".myinviteway").height();
            var waywidth=$(".myinviteway").width();
            $(".myinviteway").css("top",inviteheight-wayheight);
            $(".myinviteway").css("left",invitewidth/2-waywidth/2);
            $(".btoninvite").click(function(){
                $("#cover").css("display","block");
                $(".myinviteway").css("display","block");
                $(".nogo").css("height",inviteheight);
                $(".nogo").css("overflow","hidden");
            })
            $("#myinvitecancle").click(function(){
                $("#cover").css("display","none");
                $(".myinviteway").css("display","none");
                $(".nogo").css("overflow","auto");
            })
            var obj=document.getElementById("cover");
            obj.addEventListener("touchstart",function(e){
                $("#cover").css("display","none");
                $(".myinviteway").css("display","none");
                $(".nogo").css("overflow","auto");
            },false);
        })
        wx.ready(function () {
            $(".myinviteway>li:eq(1)").click(function () {
                wx.onMenuShareAppMessage({
                    title: '微淘米 一款可以赚钱的APP', // 分享标题
                    desc: '听说只要玩就可以赚钱赶紧过来瞧瞧吧', // 分享描述
                    link: 'http://www.weitaomi.cn/frontPage/invite.html?memberId='+ $.cookie("memberId"), // 分享链接
                    imgUrl: '../../code/img/320.png', // 分享图标
                    type: '', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                        location.href="http://weitaomi.cn/frontPage/wap/index.html"
                    },
                    cancel: function () {
                        location.href="http://weitaomi.cn/frontPage/wap/index.html"
                    }
                });
            })
        })
    </script>
</head>
<body>
<div class="nogo">
    <div class="myinvite">
        <img src="../../code/img/myerweima.png"/>
        <h5>成功邀请好友<br/>平台奖励好友收入的<span>10%</span></h5>
        <input class="btoninvite payconfirm" type="button" value="邀请好友"/>
    </div>
    <div id="cover"></div>
    <ul class="myinviteway">
        <li>分享朋友圈</li>
        <li>邀请好友</li>
        <li>保存链接</li>
        <li id="myinvitecancle">取消</li>
    </ul>
</div>
</body>
</html>