/**
 * Created by Administrator on 2016/10/21.
 */
function chooseMenu(number) {
    switch (number) {
        case 0:
            $(".middlecontent iframe").attr("src", "/frontPage/sellerPage/addofficial.html");
            $(".middlecontent iframe a").attr("href", "/frontPage/sellerPage/addofficial.html");
            break;
        case 1:
            $(".middlecontent iframe").attr("src", "/frontPage/sellerPage/publishArticle.html");
            $(".middlecontent iframe a").attr("href", "/frontPage/sellerPage/publishArticle.html");
            break;
        case 2:
            $(".middlecontent iframe").attr("src", "/frontPage/sellerPage/publishAccounts.html");
            $(".middlecontent iframe a").attr("href", "/frontPage/sellerPage/publishAccounts.html");
            break;
        case 4:
            $(".middlecontent iframe").attr("src", "/frontPage/sellerPage/paycash.html");
            $(".middlecontent iframe a").attr("href", "/frontPage/sellerPage/paycash.html");
    }
}

function initializePage() {
    $(".loginenter h5").text(decodeURI($.cookie("memberName")));
    $.ajax({
        url:"/pc/admin/member/updateMemberScore",
        type:"post",
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (data) {
            var json=eval(data)
            if(json.errorCode==0){
                var obj=json.data
                $(".loginenter p span").text(obj.memberScore.toFixed(2))
            }
        }
    })

    $.ajax({
        type: 'post',
        url: '/pc/admin/official/getOfficialAccountList',
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success: function (params) {
            if (params.errorCode == 0) {
                $(".threeul").empty()
                var li = '';

                if (!Array.prototype.forEach) {
                    Array.prototype.forEach = function(fun /*, thisp*/){
                        var len = this.length;
                        if (typeof fun != "function")
                            throw new TypeError();
                        var thisp = arguments[1];
                        for (var i = 0; i < len; i++){
                            if (i in this)
                                fun.call(thisp, this[i], i, this);
                        }
                    };
                }//使ie6、7、8支持forEach
                params.data.forEach(function (account) {
                    li = li + '<li onclick="showOfficialAccountDetail(' + account.id + ')">' + account.userName + '</li>';
                })
                $(".threeul").append(li)
            }
        }, error: function (data) {
            alert("页面加载错误，请重试");
        }
    })
}
function initializeTaskDetailPage(officialId) {
    getFollowTaskListByPage(officialId,1,3,null)
    getReadTaskListByPage(officialId,1,3,null)

}
function getFollowTaskListByPage(officialId,pageIndex,pageSize){
    $.ajax({
        type: 'post',
        url: '/pc/admin/official/getTaskPoolDto',
        data: {officialAccountId: officialId, type:0,pageIndex:pageIndex,pageSize:pageSize},
        success: function (params) {
            if (params.errorCode == 0) {
                $("#followlist").empty()
                dealWithData(params.data,'followlist','followPage',pageIndex)
            }
        }, error: function (data) {
            alert("页面加载错误，请重试");
        }
    })
}
function getReadTaskListByPage(officialId,pageIndex,pageSize){
    $.ajax({
        type: 'post',
        url: '/pc/admin/official/getTaskPoolDto',
        data: {officialAccountId: officialId, type: 1,pageIndex:pageIndex,pageSize:pageSize},
        success: function (params) {
            if (params.errorCode == 0) {
                $("#readList").empty()
                dealWithData(params.data,'readList','readPage',pageIndex)
            }
        }, error: function (data) {
            alert("页面加载错误，请重试");
        }
    })
}
function showOfficialAccountDetail(id) {
    $(".middlecontent iframe").attr("src", "/frontPage/sellerPage/taskgl.html?officialId=" + id);
    $(".middlecontent iframe a").attr("href", "/frontPage/sellerPage/taskgl.html?officialId=" + id);
}

var count = 0;
function getMemberRequestHeaderMsg(XMLHttpRequest) {
    var memberId = $.cookie("memberId");
    if (memberId == null || memberId == undefined) {
        if (count < 1) {
            alert("登录已过期请重新登录");
            count++;
            location.href = "/frontPage/wtmpc/login.html"
        }
    }
    var password = $.cookie("password");
    XMLHttpRequest.setRequestHeader("memberId", memberId);
    XMLHttpRequest.setRequestHeader("from", 2);
}

$(".exit").click(function () {
    var result = confirm("是否退出当前账号？");
    if (result) {
        clearCookie()
        location.href = "/frontPage/wtmpc/login.html";
    }
})

function clearCookie() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString()
    }
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}


function dealWithData(data,listId,pageId,pageIndex){
    var totalPage=Math.floor(data.total/3)+1
    $("#"+pageId+">li:eq(2)>label>span").text(totalPage)
    if (pageId=='followPage'){
        var middle=1;
        if (pageIndex==2){
            middle=pageIndex-1
        }
        if (pageIndex>3){
            middle=pageIndex-2
        }
        var eleme='';
        var first='<li class="pageactive"><a onclick="getFollowTaskListByPage(officialId,$(this).text(),3)">'+pageIndex+'</a></li>'
        for (var i=middle;i<=(middle+4)&&i<=totalPage;i++){
            if (i==pageIndex){
                eleme = eleme+first;
            }else eleme = eleme+'<li><a onclick="getFollowTaskListByPage(officialId,$(this).text(),3,$(this).parentNode)">'+i+'</a></li>'
        }
        $("#followPage>span").empty()
        $("#followPage>span").append(eleme)
    }
    if (pageId=='readPage'){
        var middle=1;
        if (pageIndex==2){
            middle=pageIndex-1
        }
        if (pageIndex>3){
            middle=pageIndex-2
        }
        var eleme='';
        var first='<li class="pageactive"><a onclick="getReadTaskListByPage(officialId,$(this).text(),3)">'+pageIndex+'</a></li>'
        for (var i=middle;i<=(middle+4)&&i<=totalPage;i++){
            if (i==pageIndex){
                eleme = eleme+first;
            } else {
                eleme = eleme+'<li><a onclick="getReadTaskListByPage(officialId,$(this).text(),3)">'+i+'</a></li>'
            }
        }
        $("#readPage>span").empty()
        $("#readPage>span").append(eleme)
    }
    var divlist=''
    data.list.forEach(function (task) {
        divlist= divlist +getElementDetail(task.totalScore,task.singleScore,task.needNumber,task.remainDays,task.isPublishNow,task.isAccessUndercarriage,task.articleTitle,task.taskType,task.createTime)
    })
    $("#"+listId+"").append(divlist)
}

function getElementDetail(totalScore,singleScore,needNumber,remainDays,isPublishNow,isAccessUndercarriage,articleTitle,taskType,createTime){

    if (taskType==0){
        articleTitle='关注公众号'
    }
    var option=''
    if (isPublishNow==0){
        option='<option name="" value="0">已下线</option>';
    }
    if (isPublishNow==1&&isAccessUndercarriage==0){
        option='<option name="" value="1">已上线</option>';
    }
    if (isPublishNow==1&&isAccessUndercarriage==1){
        option='<option name="" value="0">已下线</option><option selected="selected" value="1">已上线</option>';
    }
    var ele='<div class="gtaskbox">'+
        '<ul class="readtaskul">'+
        '<li class="readlibox">'+
        '<ul class="smallul">'+
        '<li class="smallibox">'+
        '<h6>任务状态</h6>'+
        '<p><label>'+
        '<select onchange="changeState(this)">'+ option +
        '</select>'+
        '</label></p>'+
        '</li>'+
        '<li class="smallibox">'+
        '<h6>单个任务奖励</h6>'+
        '<p>'+singleScore+'</p>'+
        '</li>'+
        '<li class="smallibox">'+
        '<h6>所需数量</h6>'+
        '<p>'+needNumber+'</p>'+
        '</li>'+
        '<li class="smallibox">'+
        '<h6>发布时间</h6>'+
        '<p>'+getLocalTime(createTime)+'</p>'+
        '</li>'+
        '<li class="smallibox">'+
        '<h6>剩余时间</h6>'+
        '<p>'+formatSeconds(remainDays)+'</p>'+
        '</li>'+
        '</ul>'+
        '</li>'+
        '</ul>'+
        '<div class="tasksmalltitle">'+
        '<h5>'+articleTitle+'</h5>'+
        '<p>剩余米币</p>'+
        '<h6>'+totalScore+'</h6>'+
        '</div>'+
        '</div>'
    return ele;
}

var getLocalTime=function(timestamp){
    console.log(timestamp);
    function add0(y) {
        return y < 10 ? '0' + y : y
    }
    var time = new Date(timestamp * 1000);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return add0(y) + '/' +add0(m) + '/' + add0(d) + ' ' + add0(h) + ':' + add0(mm)+ ':' + add0(s);
}

function formatSeconds(value) {
    var theTime = parseInt(value);// 秒
    var theTime1 = 0;// 分
    var theTime2 = 0;// 小时
    if(theTime > 60) {
        theTime1 = parseInt(theTime/60);
        theTime = parseInt(theTime%60);
        if(theTime1 > 60) {
            theTime2 = parseInt(theTime1/60);
            theTime1 = parseInt(theTime1%60);
        }
    }
    var result = ""+parseInt(theTime)+"秒";
    if(theTime1 > 0) {
        result = ""+parseInt(theTime1)+"分"+result;
    }
    if(theTime2 > 0) {
        result = ""+parseInt(theTime2)+"小时"+result;
    }
    return result;
}
function changeState(obj){
    $(obj).find("option:selected").val()
}

function clearCookie() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString()
    }
}