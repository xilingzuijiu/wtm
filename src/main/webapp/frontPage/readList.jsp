<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/11/23
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!--<meta http-equiv="refresh" content="10">-->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0" name="viewport"/>
    <title>阅读列表</title>
    <link href="/code/css/base.css" rel="stylesheet" type="text/css"/>
    <link href="/code/css/style.css" rel="stylesheet" type="text/css"/>
    <script src="/code/js/jquery.min.js"></script>
    <script src="/code/js/jquery.cookie.js"></script>
    <script>
        var uuid;
        var articleId;
        function GetQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null)return unescape(r[2]);
            return null;
        }
        var hashMap = {
            Set: function (key, value) {
                this[key] = value
            },
            Get: function (key) {
                return this[key]
            },
            Contains: function (key) {
                return this.Get(key) == null ? false : true
            },
            Remove: function (key) {
                delete this[key]
            }
        }
        $(function () {
            if(is_weixin()){
                uuid = GetQueryString('uuid');
                var u = navigator.userAgent;
                var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;//android终端
                var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);// ios终端
                if(isiOS||isAndroid){
                    var cookietime=$.cookie(uuid+"flag");
                    if (cookietime!=null&&cookietime!=undefined){
                        $.cookie(uuid+"flag","",{expires:-1})
                        location.reload()
                    }else {
                        loadreadlist()
                    }
                }else{
                    document.body.innerHTML = "请在手机端打开此页面";
                }
            }else {
                $(".readlist").empty();
                document.body.innerHTML="请在微信打开此页面";
            }
        });
        function is_weixin(){
            var ua = navigator.userAgent.toLowerCase();
            if(ua.match(/MicroMessenger/i)=="micromessenger") {
                return true;
            } else {
                return false;
            }
        }
        function loadreadlist(){
            uuid = GetQueryString('uuid');
            console.log(uuid);
            var cookietime=$.cookie(uuid);
            if(cookietime==null||cookietime==undefined){
                cookietime=0;
            }
            var timeEnd = new Date().getTime() - cookietime;
            console.log("timeEnd"+timeEnd);
            if (timeEnd < 2000) {
                alert("手速太快");
                var articleUrl=$.cookie(uuid+"articleUrl")
                $.cookie(uuid,'',{expires:-1})
                location.href=articleUrl
            } else {
                var parameters =${articleReadRecordDtoList}
                var leng = parameters.length
                for (var i = 0; i < leng; i++) {
                    var timestamp = parameters[i].createTime;
                    function add0(m) {
                        return m < 10 ? '0' + m : m
                    }
                    function format(timestamp) {
                        var time = new Date(timestamp * 1000);
                        var y = time.getFullYear();
                        var m = time.getMonth() + 1;
                        var d = time.getDate();
                        var h = time.getHours();
                        var mm = time.getMinutes();
                        var s = time.getSeconds();
                        return add0(m) + '/' + add0(d) + ' ' + add0(h) + ':' + add0(mm);
                    }//时间戳变换格式
                    var li = document.createElement('li');
                    li.setAttribute("onClick", "articleSubmit(this)")
//                            a.setAttribute("href",'javascript:articleSubmit()');
                    li.id = parameters[i].articleId;
                    hashMap.Set(li.id, parameters[i].articleUrl);
                    //var a=document.createElement('a');
                    var div1 = document.createElement('div');
                    div1.className = "tablebox";
                    var img = document.createElement('img');
                    img.className = "logo";
                    img.setAttribute("src", parameters[i].imageUrl);
                    var div2 = document.createElement('div');
                    div2.className = "readcont";
                    var h3 = document.createElement('h3');
                    h3.innerHTML = parameters[i].title;
                    var div3 = document.createElement('div');
                    div3.className = "readtool";
                    var p2 = document.createElement('p');
                    p2.className = "thistime";
                    p2.innerHTML = format(timestamp);
                    var p3 = document.createElement('p');
                    p3.className = "clickcount";
                    p3.innerHTML = "点击：";
                    var span = document.createElement('span');
                    span.innerHTML =parameters[i].singleScore + "米";
                    div2.appendChild(h3);
                    div1.appendChild(img);
                    div1.appendChild(div2);
                    li.appendChild(div1);
                    li.appendChild(div3);
                    p3.appendChild(span);
                    div3.appendChild(p2);
                    div3.appendChild(p3);
                    document.body.appendChild(li);
                    document.getElementsByTagName('ul')[0].appendChild(li);//动态添加文章（li标签）
                }
            }
        }
        var articleSubmit = function (obj) {
            var articleId = obj.getAttribute("id");
            var articleUrl = hashMap.Get(articleId);
            console.log(articleId);
            $.ajax({
                type: 'post',
                url: '/pc/admin/article/readArticleRequest',
                data: {uuid:uuid,articleId: articleId},
                header: {
                    "Access-Control-Allow-Origin": "true"
                },
                success: function (params) {
                    var json = eval(params); //数组
                    console.log("json数据为：" + params)
                    if (json != null && json.errorCode == 0) {
                        obj.remove()
                        $.cookie(uuid,new Date().getTime())
                        $.cookie(uuid+"flag","1")
                        $.cookie(uuid+"articleUrl",articleUrl)
                        location.href = articleUrl;
                    } else if (json != null && json.errorCode == 4) {
                        alert(json.message);
                    } else {
                        alert("获取数据失败")
                    }
                },
                error: function (data) {
                    console.log(data)
                    alert("页面加载错误，请重试");
                }
            })
        }
    </script>
</head>
<body>
<ul class="readlist">
</ul>
</body>
</html>