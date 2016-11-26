/**
 * Created by Administrator on 2016/11/10.
 */
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
        timeout:180000,
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
        timeout:180000,
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success: function (params) {
            if (params.errorCode == 0) {
                $(".threeul").empty();
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
        timeout:180000,
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
        timeout:180000,
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
    var listId=listId;
    var memberId= $.cookie("memberId");
    data.list.forEach(function (task) {
        if (task.taskType==0){
            task.articleTitle='关注公众号'
        }
        var div0=document.createElement("div");
        div0.className="gtaskbox";
        var ul0=document.createElement("ul");
        ul0.className="readtaskul";
        var li0=document.createElement("li");
        li0.className="readlibox";
        var ul1=document.createElement("ul");
        ul1.className="smallul";
        var li1=document.createElement("li");
        li1.className="smallibox";
        var h60=document.createElement("h6");
        h60.innerHTML="任务情况";
        var p0=document.createElement("p");
        var label=document.createElement("label");
        var input=document.createElement('input');
        input.className="switchOn";
        input.setAttribute("type","checkbox");
        input.setAttribute("data-on-color","primary");
        input.setAttribute("data-off-color","warning");
        input.setAttribute("data-size","small");
        input.setAttribute("data-on-text","已上线");
        input.setAttribute("data-off-text","已下线");
        input.setAttribute("id",task.taskId);
        //input.setAttribute("onclick","javascript:underCarriage(this.value)");
        label.appendChild(input);
        p0.appendChild(label);
        li1.appendChild(h60);
        li1.appendChild(p0);
        var li2=document.createElement("li");
        li2.className="smallibox";
        var h61=document.createElement("h6");
        h61.innerHTML="单个任务奖励";
        var p1=document.createElement("p");
        p1.innerHTML=task.singleScore;
        li2.appendChild(h61);
        li2.appendChild(p1);
        var li3=document.createElement("li");
        li3.className="smallibox";
        var h62=document.createElement("h6");
        h62.innerHTML="所需数量";
        var p2=document.createElement("p");
        p2.innerHTML=task.needNumber;
        li3.appendChild(h62);
        li3.appendChild(p2);
        var li4=document.createElement("li");
        li4.className="smallibox";
        var h63=document.createElement("h6");
        h63.innerHTML="发布时间";
        var p3=document.createElement("p");
        p3.innerHTML=getLocalTime(task.createTime);
        li4.appendChild(h63);
        li4.appendChild(p3);
        var li5=document.createElement("li");
        li5.className="smallibox";
        var h64=document.createElement("h6");
        h64.innerHTML="剩余时间";
        var p4=document.createElement("p");
        p4.innerHTML=formatSeconds(task.remainDays);
        li5.appendChild(h64);
        li5.appendChild(p4);
        ul1.appendChild(li1);
        ul1.appendChild(li2);
        ul1.appendChild(li3);
        ul1.appendChild(li4);
        ul1.appendChild(li5);
        li0.appendChild(ul1);
        ul0.appendChild(li0);
        var div1=document.createElement("div");
        div1.className="tasksmalltitle";
        var h50=document.createElement("h5");
        h50.innerHTML=task.articleTitle;
        var p5=document.createElement("p");
        p5.innerHTML="剩余数量";
        var h65=document.createElement("h6");
        if(task.remainNumber==null){
            h65.innerHTML=0;
        }else {
            h65.innerHTML=task.remainNumber;
        }
        div1.appendChild(h50);
        div1.appendChild(p5);
        div1.appendChild(h65);
        div0.appendChild(div1);
        div0.appendChild(ul0);
        document.getElementById(""+listId+"").appendChild(div0);
        $("#"+task.taskId+"").bootstrapSwitch('state', true);
        if (task.isPublishNow==0){
            $("#"+task.taskId+"").bootstrapSwitch('state', false);
            $("#"+task.taskId+"").bootstrapSwitch("disabled","true");
        }
        if (task.isPublishNow==1&&task.isAccessUndercarriage==0){
            $("#"+task.taskId+"").bootstrapSwitch('state', true);
            $("#"+task.taskId+"").bootstrapSwitch("disabled","true");
        }
        if (task.isPublishNow==1&&task.isAccessUndercarriage==1){
            $("#"+task.taskId+"").bootstrapSwitch('state', true);
            $("#"+task.taskId+"").on({
                'switchChange.bootstrapSwitch': function (event, state) {
                    if (state != true) {
                        console.log("memberId是"+memberId);
                        $.ajax({
                            type: 'post',
                            url: '/pc/admin/official/updateTaskPoolDto',
                            timeout:180000,
                            data:{memberId:memberId,taskPoolId:task.taskId,isPublishNow:0},
                            beforeSend: function (XMLHttpRequest) {
                                getMemberRequestHeaderMsg(XMLHttpRequest)
                            } ,
                            success: function (params) {
                                console.log("memberId第二次是"+memberId);
                                var json = eval(params);
                                if (json.data==true && json.errorCode == 0) {
                                    console.log("下线成功");
                                    alert("下线成功");
                                    $("#"+task.taskId+"").bootstrapSwitch("disabled", "true");
                                }else{
                                    alert("下线未成功，请重新执行");
                                    console.log("下线未成功");
                                    $("#"+task.taskId+"").bootstrapSwitch('state', true);
                                }
                            }, error: function (data) {
                                alert("页面加载错误，请重试");
                            }
                        });
                    }
                }
            })
        }
    });
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