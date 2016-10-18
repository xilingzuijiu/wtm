/**
 * Created by Administrator on 2016/10/12 0012.
 */
var hashMap = {
    Set : function(key,value){this[key] = value},
    Get : function(key){return this[key]},
    Contains : function(key){return this.Get(key) == null?false:true},
    Remove : function(key){delete this[key]}
}
var total=0;
var count=1;
var pageSize=7;
$(function(){
    var height=$(window).height();
    var width=$(window).width();
    var markheight=$(".markbox").height();
    var markwidth=$(".markbox").width();
    $(".markbox").css("left",width/2-markwidth/2);
    $(".markbox").css("top",height/2-markheight/2);
    var thirdLogin=$.cookie("thirdLogin");
    if (thirdLogin==null||thirdLogin==undefined||thirdLogin.length<=0){
        var result=confirm("还未绑定微信，请先绑定微信再获取文章");
        if(result){
            location.href="/frontPage/wap/mycenter.html";
        }else {location.href="/frontPage/wap/index.html";}
    }else{
        loadofficiallist();
    }
})
$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();               //滚动条距离顶部的高度
    var scrollHeight = $(document).height();                   //当前页面的总高度
    var windowHeight = $(this).height();                   //当前可视的页面高度
    if(scrollTop + windowHeight >= scrollHeight){        //距离顶部+当前高度 >=文档总高度 即代表滑动到底部
        console.log(Math.ceil(total/pageSize)+"shi");
        count++;
        if(count<=Math.ceil(total/pageSize)){
            loadofficiallist();
        }
        if(count==Math.ceil(total/pageSize)){
            var p = document.createElement('p');
            p.className="loadmore"
            p.innerHTML = "没有更多";
            $("body").append(p);
        }
    }else if(scrollTop<=0){         //滚动条距离顶部的高度小于等于0
//                    location.reload();
    }
});
function loadofficiallist(){
    var request=JSON.stringify(new ArticleSerach(0,count,pageSize));
    $.ajax({
        type: 'post',
        dataType: 'json',
        url: '/pc/admin/official/getFollowOfficialAccountList',
        data:request,
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success: function (params) {
            var json = eval(params); //数组
            console.log("json数据为：" + params);
            console.log("memberId为"+$.cookie("memberId"));
            if (json.data!= null && json.errorCode == 0) {
                json.data.forEach(function(official){
                    var li = document.createElement("li");
                    li.className = "col-xs-4";
                    li.id = official.originId;
                    hashMap.Set(li.id, official.addUrl);
                    li.setAttribute("onClick","officialSubmit(this)");
                    var img = document.createElement('img');
                    img.setAttribute("src", official.imageUrl);

                    var h5 = document.createElement('h5');
                    h5.innerHTML = official.username;
                    var p = document.createElement('p');
                    p.innerHTML = "关注奖励："
                    var span = document.createElement('span');
                    span.innerHTML = official.rewardCount+"米币";
                    p.appendChild(span);
                    var input = document.createElement('input');
                    input.setAttribute("type", "button");
                    input.setAttribute("value", "点头像关注");
                    li.appendChild(img);
                    li.appendChild(h5);
                    li.appendChild(p);
                    li.appendChild(input);
                    document.body.appendChild(li);
                    document.getElementsByTagName('ul')[0].appendChild(li);//动态添加文章（li标签）
                    var officialUrl=hashMap.Get(li.id);
                })
            }else{
                $(".nullp").css("display","block");
            }
        }
    })
}

var officialSubmit = function(obj){
//            var unionId=JSON.parse($.cookie("thirdLogin")).unionId;
    var originId=obj.getAttribute("id");
    var officialUrl=hashMap.Get(originId);
    var requestData=JSON.stringify(new OfficialAccountMsg(originId))
    console.log(originId);
    $.ajax({
        type:"post",
        url:'/pc/admin/official/markAddRecord',
        contentType: "application/json",
        data:requestData,
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        header:{
            "contentType":"application/json"
        },
        success: function (params) {
            var json=eval(params); //数组
            console.log("json数据为："+params)
            if (json!=null&&json.errorCode==0) {
                //obj.style.display='none';
                location.href=officialUrl;
            }else if(json!=null&&json.errorCode==4){
                //$(".lookover").text(json.message);
                var result=confirm(json.message);
                if(result){
                    var memberId=$.cookie("memberId");
                    console.log("获取的memberId为"+memberId)
                    $.ajax({
                        type:"post",
                        url:'/pc/admin/official/getOfficialAccountMsgList',
                        beforeSend: function (XMLHttpRequest) {
                            getMemberRequestHeaderMsg(XMLHttpRequest)
                        },
                        success: function (params) {
                            var json = eval(params); //数组
                            console.log("标记数据是"+json.data);
                            if (json!=null&&json.errorCode==0){
                                var ulength=$(".marklist li").length;
                                console.log("ul长度为"+$(".marklist").length);
                                    $('.marklist').empty();
                                    json.data.forEach(function(official){
                                        var li=document.createElement('li');
                                        var img=document.createElement('img');
                                        img.setAttribute("src",official.imageUrl);
                                        var h5=document.createElement('h5');
                                        h5.innerHTML=official.accountName;
                                        li.appendChild(img);
                                        li.appendChild(h5);
                                        document.getElementsByTagName('ul')[1].appendChild(li);
                                        $("#cover").css("display","block");
                                        $(".markbox").css("display","block");
                                        $(".leftbutn").click(function(){
                                            $("#cover").css("display","none");
                                            $(".markbox").css("display","none");
                                        })
                                        $(".rightbutn").click(function(){
                                            $.ajax({
                                                type:"post",
                                                url:'/pc/admin/official/signOfficialAccountMsgList',
                                                beforeSend: function (XMLHttpRequest) {
                                                    getMemberRequestHeaderMsg(XMLHttpRequest)
                                                },
                                                success: function (params) {
                                                    var json = eval(params); //数组
                                                    if (json != null && json.errorCode == 0) {
                                                        $("#cover").css("display", "none");
                                                        $(".markbox").css("display", "none");
                                                        $("#prompt").fadeIn(500);
                                                        $("#prompt").text("标记成功");
                                                        $('#prompt').delay(600).fadeOut(350);
                                                    }
                                                }
                                            })
                                        })
                                    })


                        }else{
                                alert("获取数据失败2");
                            }
                        },error:function (data){
                            console.log(data)
                            alert("页面加载错误2，请重试");
                        }
                    })
                }
            }else{ alert("获取数据失败")}
        },
        error:function (data){
            console.log(data)
            alert("页面加载错误，请重试");
        }
    })
}
function getMemberRequestHeaderMsg(XMLHttpRequest){
    var memberId= $.cookie("memberId");
    if (memberId==null||memberId == undefined){
        alert("登录已过期请重新登录");
        location.href="login.html"

    }
    var password= $.cookie("password");
    XMLHttpRequest.setRequestHeader("memberId",memberId);
    XMLHttpRequest.setRequestHeader("from",2);
}
function ArticleSerach(searchWay,pageIndex,pageSize){
    this.searchWay=searchWay
    this.pageIndex=pageIndex
    this.pageSize=pageSize
}
function OfficialAccountMsg(originId){
    this.originId=originId
}