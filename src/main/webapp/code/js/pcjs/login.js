/**
 * Created by Administrator on 2016/10/20 0020.
 */
$(function(){
    $(".loginbutton").click(function(){
        $("#form").attr("enctype","multipart/form-data");
        var memberName=$("#memberName").val();
        var password=$("#password").val();
        $.ajax({
            type: 'post',
            url: '/pc/admin/member/login',
            data: {mobileOrName:memberName,password:password},
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("from","1")
            },
            success: function (params) {
                var param = eval(params);
                if (param != null && param.errorCode == 0) {
                    var data = param.data;
                    if (data.id != null){
                        $.cookie("memberId", data.id, {expires: 30, path: "/frontPage/wap"})
                        $.cookie("memberName", encodeURI(data.memberName), {expires: 30, path: "/frontPage/wap"})
                        $.cookie("password", data.password, {expires: 30, path: "/frontPage/wap"})
                        $.cookie("birth", data.birth, {expires: 30, path: "/frontPage/wap"})
                        $.cookie("sex", data.sex, {expires: 30, path: "/frontPage/wap"})
                        var image = data.imageUrl;
                        if (image.indexOf("http") < 0) {
                            image = "http://weitaomi.b0.upaiyun.com" + image
                        }
                        $.cookie("imageUrl", image, {expires: 30, path: "/frontPage/wap"})
                        $.cookie("telephone", data.telephone, {expires: 30, path: "/frontPage/wap"})
                        if(data.officialAccountList!=null&&data.officialAccountList!=undefined)
                            $.cookie("officialAccountList", JSON.stringify(data.officialAccountList), {
                                expires: 30,
                                path: "/frontPage/wap"
                            })
                        if(data.thirdLogin!=null&&data.thirdLogin!=undefined)
                            $.cookie("thirdLogin", JSON.stringify(data.thirdLogin), {expires: 30, path: "/frontPage/wap"})
                        if(data.payList!=null&&data.payList!=undefined)
                            $.cookie("payList", JSON.stringify(data.payList), {expires: 30, path: "/frontPage/wap"})
                        location.href = "/frontPage/wap/index.html";
                    }
                } else if (params.errorCode == 4) {
                    alert(params.message)
                }
            }
        });
    });
    $("#sendVerifyCode").click( function (){
        var telephone = $("#telephone").val().trim();
        if (telephone==null || !telephone.match(/^1\d{10}$/)){
            alert("手机号码不正确")
        }else {
            $.ajax({
                type: 'get',
                url: '/pc/admin/member/sendIndentifyCode',
                data: {telephone:telephone},
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
        if(!$("#rmemberName").val()){
            alert("用户名不能为空");
        }else if($("#rmemberName").val().length>10){
            alert("您输入的用户名过长");
        }
        else if($("#rpassword").val().length<6||$("#password").val().length>16){
            alert("密码长度必须为6到16位");
        }else{
            $.ajax({
                type: 'post',
                url: '/pc/admin/member/register',
                dataType:"json",
                data: request,
                contentType: "application/json",
                beforeSend: function (XMLHttpRequest) {
                    XMLHttpRequest.setRequestHeader("from","2")
                },
                success: function (params) {
                    var data=eval(params)
                    var errorCode=data.errorCode
                    if (errorCode==0){
                        if (data.data){
                            $(".loginbg").css("display","block");
                            $(".registerbg").css("display","none");
                        }
                    } else if (errorCode==4){
                        alert(data.message)
                    }
                }
            })
        }
    })
    $(".wordpage").click(function(){
        $(".loginbg").css("display","none");
        $(".registerbg").css("display","block");
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
    delete requestObj.identifyCode
    var obj=new ReuqestObj(requestObj,invitedCode,identifyCode)
    return JSON.stringify(obj)
}