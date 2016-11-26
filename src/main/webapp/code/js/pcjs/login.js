/**
 * Created by Administrator on 2016/10/20 0020.
 */
$(function(){
    $(".logo").click(function(){
window.location.href="/frontPage/wtmpc/index.html"
    })
    $(".loginbutton").click(function(){
        $("#form").attr("enctype","multipart/form-data");
        var memberName=$("#memberName").val();
        var password=$("#password").val();
        $.ajax({
            type: 'post',
            url: '/pc/admin/member/login',
            timeout:180000,
            data: {mobileOrName:memberName,password:password},
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("from","1")
            },
            success: function (params) {
                var param = eval(params);
                if (param != null && param.errorCode == 0) {
                    var data = param.data;
                    if (data.id != null){
                        $.cookie("memberId", data.id, {expires: 30, path: "/frontPage/sellerPage"})
                        $.cookie("memberName", encodeURI(data.memberName), {expires: 30, path: "/frontPage/sellerPage"})
                        $.cookie("password", data.password, {expires: 30, path: "/frontPage/sellerPage"})
                        $.cookie("birth", data.birth, {expires: 30, path: "/frontPage/sellerPage"})
                        $.cookie("sex", data.sex, {expires: 30, path: "/frontPage/sellerPage"})
                        var image = data.imageUrl;
                        if (image.indexOf("http") < 0) {
                            image = "http://weitaomi.b0.upaiyun.com" + image
                        }
                        $.cookie("imageUrl", image, {expires: 30, path: "/frontPage/sellerPage"})
                        $.cookie("telephone", data.telephone, {expires: 30, path: "/frontPage/sellerPage"})
                        if(data.officialAccountList!=null&&data.officialAccountList!=undefined)
                            $.cookie("officialAccountList", JSON.stringify(data.officialAccountList), {
                                expires: 30,
                                path: "/frontPage/sellerPage"
                            })
                        if(data.thirdLogin!=null&&data.thirdLogin!=undefined)
                            $.cookie("thirdLogin", JSON.stringify(data.thirdLogin), {expires: 30, path: "/frontPage/sellerPage"})
                        if(data.payList!=null&&data.payList!=undefined)
                            $.cookie("payList", JSON.stringify(data.payList), {expires: 30, path: "/frontPage/sellerPage"})
                        location.href = "/frontPage/sellerPage/business.html";
                    }
                } else if (params.errorCode == 4) {
                    alert(params.message)
                }
            }
        });
    });
    $("#sendVerifyCode").click( function (){
        var imgcode = $("#imgcode").val().trim();
        var telephone = $("#telephone").val().trim();
        if(imgcode==null){
            alert("图片验证码不能为空");
        }else if (telephone==null || !telephone.match(/^1\d{10}$/)){
            alert("手机号码不正确")
        }else {
            $.ajax({
                type: 'get',
                url: '/pc/admin/member/sendIndentifyCode',
                timeout:180000,
                data: {telephone:telephone,imageCode:imgcode},
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
    })
    $("#register").click( function (){
        $("#form").attr("enctype","multipart/form-data");
        var requestObj = eval($("#registerForm").serializeObject())
        var request =getRegeisterParams(requestObj);
        var telephone = $("#telephone").val().trim();
        if(!$("#rmemberName").val()){
            alert("用户名不能为空");
        }else if($("#rmemberName").val().length>10){
            alert("您输入的用户名过长");
        }else if($("#rpassword").val().length<6||$("#password").val().length>16){
            alert("密码长度必须为6到16位");
        }else if($("#rpassword").val()!=$("#repassword").val()){
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
                    XMLHttpRequest.setRequestHeader("from","2")
                },
                success: function (params) {
                    var data=eval(params)
                    var errorCode=data.errorCode;
                    if (errorCode==0){
                        if (data.data){
                            alert("注册成功，请登录");
                            $(".loginbg").css("display","block");
                            $(".registerbg").css("display","none");
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
            })
        }
    })
    $(".wordpage").click(function(){
        $(".loginbg").css("display","none");
        $(".registerbg").css("display","block");
    })
    $("#loginback").click(function(){
        $(".loginbg").css("display","block");
        $(".registerbg").css("display","none");
    })
})
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
    delete requestObj.code
    delete requestObj.identifyCode
    delete requestObj.repassword
    var obj=new ReuqestObj(requestObj,invitedCode,identifyCode)
    return JSON.stringify(obj)
}