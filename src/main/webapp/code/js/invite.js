$(function(){
    if (is_weixin()){
        $(".bodybox").css("display","block");
    } else {
        document.body.innerHTML = "请在微信打开此页面"
    }
    $("#sendVerifyCode").click( function (){
        var imgcode = $("#imgcode").val().trim();
        var telephone = $("#telephone").val().trim();
        if(imgcode==null){
            alert("图片验证码不能为空");
        }
        else if(telephone==null || !telephone.match(/^1\d{10}$/)){
            alert("手机号码不正确")
        }else {
            $.ajax({
                type: 'get',
                url: '/pc/admin/member/sendIndentifyCode',
                timeout:180000,
                data: {telephone:telephone,imageCode:imgcode},
                beforeSend: function (XMLHttpRequest) {
                    getMemberRequestHeaderMsg(XMLHttpRequest)
                } ,
                success: function (params) {
                    var data=eval(params)
                    var errorCode=data.errorCode
                    if (errorCode==0){

                    } else if (errorCode==4){
                        alert(data.message)
                    }
                }
            });
            $("#sendVerifyCode").css("display","none");
            $("#downtime").css("display","block");
            countDown();
            setTimeout(function (){
                $("#sendVerifyCode").css("display","block");
                $("#downtime").css("display","none");
            }, SS*1000);
        }
    });
    $("#register").click( function (){
        $("#form").attr("enctype","multipart/form-data");
        var requestObj = eval($("#registerForm").serializeObject())
        var request =getRegeisterParams(requestObj);
        var telephone = $("#telephone").val().trim();
        if(!$("#memberName").val()){
            alert("用户名不能为空");
        }else if($("#memberName").val().length>10){
            alert("您输入的用户名过长");
        }else if($("#password").val().length<6||$("#password").val().length>16){
            alert("密码长度必须为6到16位");
        }else if($("#password").val()!=$("#repassword").val()){
            alert("密码不一致，请重新输入");
        }else if(telephone==null || !telephone.match(/^1\d{10}$/)){
            alert("手机号码不正确");
        }else{
            $("#register").attr('disabled', true);
            $("#register").css('background', "#bebebe");
            $.ajax({
                type: 'post',
                url: '/pc/admin/member/register',
                dataType:"json",
                timeout:180000,
                data: request,
                contentType: "application/json",
                beforeSend: function (XMLHttpRequest) {
                    getMemberRequestHeaderMsg(XMLHttpRequest)
                } ,
                success: function (params) {
                    var data=eval(params)
                    var errorCode=data.errorCode;
                    if (errorCode==0){
                        if (data.data){
                            setTimeout(download(),5000);
                        }
                    } else if (errorCode==4){
                        alert(data.message);
                        $("#register").attr('disabled', false);
                        $("#register").css('background', "#fa4a18");
                    }
                }, error:function(){
                    alert(data.message);
                    $("#register").attr('disabled', false);
                    $("#register").css('background', "#fa4a18");
                }
            });
        }
    })
});
function getRegeisterParams(requestObj){
    var invitedCode = requestObj.invitedCode
    var identifyCode = requestObj.identifyCode
    delete requestObj.invitedCode
    delete requestObj.identifyCode
    delete requestObj.repassword
    delete requestObj.code
    var obj=new ReuqestObj(requestObj,invitedCode,identifyCode)
    return JSON.stringify(obj)
}



function getinvitecode(){
    memberId=GetQueryString("memberId");
    console.log("获取的的数据为："+memberId);
    $.ajax({
        type: 'get',
        dataType: 'json',
        url: '/pc/admin/member/getInvitedCode',
        timeout:180000,
        data: {memberId:memberId},
        header:{
            "Access-Control-Allow-Origin":"true"
        },
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (params) {
            var json = eval(params); //数组
            console.log("json数据为："+params)
            if (json!=null&&json.errorCode==0){
                $("#target").empty()
                $("#target").append(json.data)
            }else{ alert("获取邀请码失败")}
        },
        error:function (data){
            console.log(data)
            alert("页面加载错误，请重试");
        }
    });
}

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


var EV_MsgBox_ID="";

//弹出对话窗口(msgID-要显示的div的id)
function EV_modeAlert(msgID){
    //创建大大的背景框
    var bgObj=document.createElement("div");
    bgObj.setAttribute('id','EV_bgModeAlertDiv');
    document.body.appendChild(bgObj);
    //背景框满窗口显示
    EV_Show_bgDiv();
    //把要显示的div居中显示
    EV_MsgBox_ID=msgID;
    EV_Show_msgDiv();
}

//关闭对话窗口
function EV_closeAlert(){
    var msgObj=document.getElementById(EV_MsgBox_ID);
    var bgObj=document.getElementById("EV_bgModeAlertDiv");
    msgObj.style.display="none";
    document.body.removeChild(bgObj);
    EV_MsgBox_ID="";
}

//窗口大小改变时更正显示大小和位置
window.onresize=function(){
    if (EV_MsgBox_ID.length>0){
        EV_Show_bgDiv();
        EV_Show_msgDiv();
    }
}

//窗口滚动条拖动时更正显示大小和位置
window.onscroll=function(){
    if (EV_MsgBox_ID.length>0){
        EV_Show_bgDiv();
        EV_Show_msgDiv();
    }
}

//把要显示的div居中显示
function EV_Show_msgDiv(){
    var msgObj   = document.getElementById(EV_MsgBox_ID);
    msgObj.style.display  = "block";
    var msgWidth = msgObj.scrollWidth;
    var msgHeight= msgObj.scrollHeight;
    var bgTop=EV_myScrollTop();
    var bgLeft=EV_myScrollLeft();
    var bgWidth=EV_myClientWidth();
    var bgHeight=EV_myClientHeight();
    var msgTop=bgTop+Math.round((bgHeight-msgHeight)/2);
    var msgLeft=bgLeft+Math.round((bgWidth-msgWidth)/2);
    msgObj.style.position = "absolute";
    msgObj.style.top      = msgTop+"px";
    msgObj.style.left     = msgLeft+"px";
    msgObj.style.zIndex   = "10001";

}
//背景框满窗口显示
function EV_Show_bgDiv(){
    var bgObj=document.getElementById("EV_bgModeAlertDiv");
    var bgWidth=EV_myClientWidth();
    var bgHeight=EV_myClientHeight();
    var bgTop=EV_myScrollTop();
    var bgLeft=EV_myScrollLeft();
    bgObj.style.position   = "absolute";
    bgObj.style.top        = bgTop+"px";
    bgObj.style.left       = bgLeft+"px";
    bgObj.style.width      = bgWidth + "px";
    bgObj.style.height     = bgHeight + "px";
    bgObj.style.zIndex     = "10000";
    bgObj.style.background = "#777";
    bgObj.style.filter     = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=60,finishOpacity=60);";
    bgObj.style.opacity    = "0.6";
}
//网页被卷去的上高度
function EV_myScrollTop(){
    var n=window.pageYOffset
        || document.documentElement.scrollTop
        || document.body.scrollTop || 0;
    return n;
}
//网页被卷去的左宽度
function EV_myScrollLeft(){
    var n=window.pageXOffset
        || document.documentElement.scrollLeft
        || document.body.scrollLeft || 0;
    return n;
}
//网页可见区域宽
function EV_myClientWidth(){
    var n=document.documentElement.clientWidth
        || document.body.clientWidth || 0;
    return n;
}
//网页可见区域高
function EV_myClientHeight(){
    var n=document.body.clientHeight || 0;
    return n;
}

function ReuqestObj(obj,invtedCode,identifyCode){
    this.member=obj
    this.invitedCode=invtedCode
    this.identifyCode=identifyCode
    this.flag=0
}
function getRegeisterParams(requestObj){
    var invitedCode = requestObj.invitedCode
    var identifyCode = requestObj.identifyCode
    delete requestObj.invitedCode
    delete requestObj.identifyCode
    delete requestObj.code
    delete  requestObj.repassword
    var obj=new ReuqestObj(requestObj,invitedCode,identifyCode)
    return JSON.stringify(obj)
}
function WxReuqestObj(obj,invtedCode,identifyCode,thirdLoginInfo){
    this.member=obj
    this.invitedCode=invtedCode
    this.identifyCode=identifyCode
    this.thirdLogin=thirdLoginInfo
    this.flag=1
}
function ThirdLoginInfo(openid,unionid,sex,nickname,imageFiles){
    this.type=0
    this.nickname=nickname
    this.sex=sex
    this.unionId=unionid
    this.openId=openid
    this.imageFiles=imageFiles
}
function getWxRegeisterParams(requestObj){
    var invitedCode = requestObj.invitedCode
    var identifyCode = requestObj.identifyCode
    var openid=requestObj.openid
    var province=requestObj.province
    var unionid=requestObj.unionid
    var imageFiles=requestObj.imageFiles
    var city=requestObj.city
    var nickname=requestObj.memberName
    var sex=requestObj.sex
    delete requestObj.invitedCode
    delete requestObj.identifyCode
    delete requestObj.unionid
    delete requestObj.openid
    delete requestObj.imageFiles
    var thirdLoginInfo=new ThirdLoginInfo(openid,unionid,sex,nickname,imageFiles)
    var obj=new WxReuqestObj(requestObj,invitedCode,identifyCode,thirdLoginInfo)
    return JSON.stringify(obj)
}
function download() {
    location.href="http://a.app.qq.com/o/simple.jsp?pkgname=com.woyun.weitaomi"
}
var count=0;
function getMemberRequestHeaderMsg(XMLHttpRequest){
    //var memberId= $.cookie("memberId");
    //if (memberId==null||memberId == undefined){
    //    if (count<1){
    //        alert("登录已过期请重新登录");
    //        count++;
    //        location.href="/frontPage/wap/login.html"
    //    }
    //}
    var password= $.cookie("password");
    XMLHttpRequest.setRequestHeader("memberId",memberId);
    if(isAndroid){
        XMLHttpRequest.setRequestHeader("from",6);
    }else if(isiOS){
        XMLHttpRequest.setRequestHeader("from",7);
    }else {
        XMLHttpRequest.setRequestHeader("from",2);
    }
}
function is_weixin() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
        return true;
    } else {
        return false;
    }
}