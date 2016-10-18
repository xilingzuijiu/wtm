$(function(){
    var stext;
    $(".wxjump").click(function(){
        location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2282c57276f83fad&redirect_uri=http%3a%2f%2fwww.weitaomi.cn%2fwxRegisterOAuth&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    })
    $(".loginbutton").click(function(){
        $("#form").attr("enctype","multipart/form-data");
        var memberName=$("#memberName").val();
        var password=$("#password").val();
        $.ajax({
            type: 'post',
            url: '/pc/admin/member/login',
            data: {mobileOrName:memberName,password:password},
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("from","2")
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
    })
    $(".wordpage").click(function(){
        location.href="/frontPage/wap/register.html";
    })
})
function fastalert(stext){
    $("#prompt").css("display", "block");
    $("#prompt").text(stext);
    $('#prompt').delay(800).fadeOut(350);
}
function initilizePage(){
    if($.cookie("memberId")!=null&&$.cookie("memberId")!=undefined){
        location.href="frontPage/wap/index.html"
    }
}