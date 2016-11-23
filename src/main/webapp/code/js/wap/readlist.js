/**
 * Created by Administrator on 2016/10/12 0012.
 */
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
var total = 0;
var count = 1;
var pageSize = 20;
$(function () {
    if (is_weixin()) {
        var thirdLogin = $.cookie("thirdLogin");
        if (thirdLogin == null || thirdLogin == undefined || thirdLogin.length <= 0) {
            var result = confirm("还未绑定微信，请先绑定微信再获取文章");
            if (result) {
                location.href = "/frontPage/wap/mycenter.html";
            } else {
                location.href = "/frontPage/wap/index.html";
            }
        } else {
            if(isiOS||isAndroid){
                loadReadlist();
            }else {
                document.body.innerHTML = "请在手机端打开此页面"
            }
        }
    } else {
        document.body.innerHTML = "请在微信打开此页面"
    }
})
function is_weixin() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
        return true;
    } else {
        return false;
    }
}
function loadReadlist() {
    var request = JSON.stringify(new ArticleSerach(0, count, pageSize));
    var cookietime=$.cookie($.cookie("memberId") + "time");
    if(cookietime==null||cookietime==undefined){
        cookietime=0;
        var timeEnd = new Date().getTime() - cookietime;
    }else {
        var timeEnd = new Date().getTime() - cookietime;
    }
    console.log(timeEnd)
    if (timeEnd<2000){
        alert("手速太快");
        setTimeout(function(){
            $.cookie($.cookie("memberId") + "time",{expires:-1})
            loadReadlist();
        },5000)
    }else{
        console.log("count" + count);
        $.ajax({
            type: 'post',
            contentType: "application/json",
            dataType: 'json',
            url: '/pc/admin/article/getAllArticle',
            data: request,
            beforeSend: function (XMLHttpRequest) {
                getMemberRequestHeaderMsg(XMLHttpRequest)
            },
            success: function (params){
                var json = eval(params); //数组
                console.log("json数据为：" + params);
                console.log($.cookie("memberId"));
                if (json.data.list != null && json.errorCode == 0) {
                    total = json.data.total;
                    json.data.list.forEach(function (article) {
                        var li = document.createElement("li");
                        li.setAttribute("onClick", "articleSubmit(this)");
                        li.id = article.id;
                        var div1 = document.createElement('div');
                        hashMap.Set(li.id, article.url);
                        div1.className = "col-xs-2 listimg";
                        var img = document.createElement('img');
                        img.setAttribute("src", article.imageUrl);
                        div1.appendChild(img);
                        var div2 = document.createElement('div');
                        div2.className = "readdetails col-xs-10";
                        var h5 = document.createElement('h5');
                        h5.className = "articltitle";
                        h5.innerHTML = article.title;
                        var p = document.createElement('p');
                        p.innerHTML = "剩余阅读数："
                        var span = document.createElement('span');
                        span.innerHTML = article.readIncreaseNumber;//阅读数名称修改
                        p.appendChild(span);
                        div2.appendChild(h5);
                        div2.appendChild(p);
                        li.appendChild(div1);
                        li.appendChild(div2);
                        document.body.appendChild(li);
                        document.getElementsByTagName('ul')[0].appendChild(li);//动态添加文章（li标签）
                    })
                } else if (json.data.list == null && json.errorCode == 0) {
                    $(".nullp").css("display", "block");
                }else if (json.errorCode ==4){
                    alert(json.message)
                }
                else {
                    alert("获取数据失败")
                }
            },
            error: function (data) {
                console.log(data);
                alert("页面加载错误，请重试");
            }
        })
    }
}
var articleSubmit=function(obj){
    var articleId=obj.getAttribute("id");
    var articleUrl=hashMap.Get(articleId);
    console.log(articleId);
    $.ajax({
        type: 'post',
        url: '/pc/admin/article/pcreadArticleRequest',
        data: {articleId: articleId},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success: function(params){
            var json = eval(params); //数组
            console.log("json数据为：" + params)
            if (json != null && json.errorCode == 0) {
                obj.style.display = 'none';
                //$.cookie($.cookie("memberId") + "readMark", '', {expires: -1})
                //$.cookie($.cookie("memberId") + "time", new Date().getTime(), {path: '/frontPage/wap'})
                location.href = articleUrl;
            } else if (json != null && json.errorCode == 4) {
                alert(json.message);
                location.reload();
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
function ArticleSerach(searchWay, pageIndex, pageSize) {
    this.searchWay = searchWay
    this.pageIndex = pageIndex
    this.pageSize = pageSize
}
