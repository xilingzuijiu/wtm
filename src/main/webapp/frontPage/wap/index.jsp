<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/10/10
  Time: 14:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0" name="viewport" />
    <title>首页</title>
    <link href="../../code/css/common.css" rel="stylesheet" type="text/css"/>
    <script src="../../code/js/wap/common.js"></script>
    <script src="../../code/js/wap/daysign.js"></script>
    <script>
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;//android终端
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);// ios终端
        $(document).ready(function () {
            $(".owner img").click(function(){
                location.href="/frontPage/wap/mycenter.html";
            })
            updateMyscore();
            $(".ownername>span").text(decodeURI($.cookie("memberName")))
            var imageUrl=$.cookie("imageUrl");
            $(".owner img").attr("src",imageUrl);
            //banner图
            var index=0;
            var liwidth=$(document.body).width();
            var leftwidth=liwidth*2.5%
                    console.log(leftwidth);
            $(".homebanner li").css("width",liwidth);
            var liwi=$(".homebanner li").width();
            var length=$(".homebanner li").length;
            $(".homebanner ul").css("width",liwi*length);
            var ulwi=$(".homebanner ul").width();
            function show() {
                if (index == length - 1) {
                    $(".homebanner ul").css("right",0);
                    index = 1;
                } else {
                    index++;
                }
                $(".homebanner ul").animate({right: -index * liwi},400);
            }
            //钱包刷新
            setInterval(show,2000);
            getLocation();
            $(".money").click(function (){
                updateMyscore();
                $("#prompt").fadeIn(500);
                $("#prompt").text("刷新成功");
                $('#prompt').delay(600).fadeOut(350);
            })
            $("#bannerinvite").click(function(){
                location.href="../invite.html?memberId="+ $.cookie('memberId');
            })
        })
    </script>
</head>
<body>
<header>
    <div class="adress">
        <span class="glyphicon glyphicon-map-marker"></span>
        <p id="addressname">中国</p>
    </div>
    <div class="owner">
        <img class="photo" src=""/>
        <div class="ownerdetail">
            <h3 class="ownername"><span>微淘米</span>一款玩就可以赚钱的APP</h3>
        </div>
        <div class="money">
            <p><span>0</span>米<br/>我的钱包</p>
        </div>
    </div>
</header>
<div class="homebanner swiper-container">
    <ul class="swiper-wrapper">
        <li class="swiper-slide">
            <img class="signday" src="../../code/img/fahongbao.png"/>
        </li>
        <li class="swiper-slide">
            <img id="bannerinvite" src="../../code/img/yaohaoyou.png"/>
        </li>
        <li class="swiper-slide">
            <img  class="signday" src="../../code/img/fahongbao.png"/>
        </li>
    </ul>
</div>
<ul class="row indexlist">
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/readlist.html">
            <img src="../../code/img/MainViewImgae/read.png" >
            <div class="caption">
                <h3>文章列表</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/gongzhonghao.html">
            <img src="../../code/img/MainViewImgae/attention.png" >
            <div class="caption">
                <h3>关注公众号</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/pursewithdraw.html">
            <img src="../../code/img/MainViewImgae/mymoney.png" >
            <div class="caption">
                <h3>钱包提现</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/mycenter.html">
            <img src="../../code/img/MainViewImgae/person.png" >
            <div class="caption">
                <h3>个人中心</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/testrecord.html">
            <img src="../../code/img/MainViewImgae/about.png" >
            <div class="caption">
                <h3>任务记录</h3>
            </div>
        </a>
    </li>

    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/invitefriend.html">
            <img src="../../code/img/MainViewImgae/friend.png" >
            <div class="caption">
                <h3>邀请好友</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4 signday">
        <a class="thumbnail">
            <img src="../../code/img/MainViewImgae/new.png" >
            <div class="caption">
                <h3>每日签到</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/rewardrule.html">
            <img src="../../code/img/MainViewImgae/help.png" >
            <div class="caption">
                <h3>奖励规则</h3>
            </div>
        </a>
    </li>
    <li class="col-xs-4">
        <a class="thumbnail" href="/frontPage/wap/question.html">
            <img src="../../code/img/MainViewImgae/tasknotes.png" >
            <div class="caption">
                <h3>常见问题</h3>
            </div>
        </a>
    </li>


</ul>
<div class="daysignbox">
    <img class="daysign" src="../../code/img/MainViewImgae/abputsign2.png"/>
    <img class="signbutton" src=""/>
</div>
<div id="prompt"></div>
</body>
</html>