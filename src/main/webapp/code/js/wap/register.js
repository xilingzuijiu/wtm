/**
 * Created by Administrator on 2016/10/18 0018.
 */
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
            setTimeout(function () {
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
                            $("#prompt").fadeIn(500);
                            $("#prompt").text("注册成功，5s后自动跳转到登录页面");
                            $('#prompt').delay(600).fadeOut(350);
                            setTimeout("location.href='/frontPage/wap/login.html'",5000);
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
})
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
    //var password= $.cookie("password");
    //XMLHttpRequest.setRequestHeader("memberId",memberId);
    if(isAndroid){
        XMLHttpRequest.setRequestHeader("from",6);
    }else if(isiOS){
        XMLHttpRequest.setRequestHeader("from",7);
    }else {
        XMLHttpRequest.setRequestHeader("from",2);
    }
}
function getRegeisterParams(requestObj){
    var invitedCode = requestObj.invitedCode
    var identifyCode = requestObj.identifyCode
    delete requestObj.invitedCode
    delete requestObj.identifyCode
    delete requestObj.repassword
    var obj=new ReuqestObj(requestObj,invitedCode,identifyCode)
    return JSON.stringify(obj)
}
