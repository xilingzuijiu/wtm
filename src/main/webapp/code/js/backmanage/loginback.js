/**
 * Created by Administrator on 2016/11/14.
 */
$(function(){
    $(".loginbutton").click(function(){
        $("#form").attr("enctype","multipart/form-data");
        var realName=$("#memberName").val();
        var password=$("#password").val();
        console.log(realName);
        console.log(password);
        $.ajax({
            type: 'post',
            url: '/backDeal/login',
            data: {realName:realName,password:password},
            //beforeSend: function (XMLHttpRequest) {
            //    XMLHttpRequest.setRequestHeader("from","1")
            //},
            success: function (params) {
                var param = eval(params);
                if (param != null && param.errorCode == 0) {
                    var data = param.data;
                    if (data.id != null){
                        //$.cookie("memberId", data.id, {expires: 30, path: "/frontPage/backward"})
                        //$.cookie("memberName", encodeURI(data.memberName), {expires: 30, path: "/frontPage/backward"})
                        //$.cookie("password", data.password, {expires: 30, path: "/frontPage/backward"})
                        //$.cookie("birth", data.birth, {expires: 30, path: "/frontPage/backward"})
                        //$.cookie("sex", data.sex, {expires: 30, path: "/frontPage/backward"})
                        //var image = data.imageUrl;
                        //if (image.indexOf("http") < 0) {
                        //    image = "http://weitaomi.b0.upaiyun.com" + image
                        //}
                        //$.cookie("imageUrl", image, {expires: 30, path: "/frontPage/backward"})
                        //$.cookie("telephone", data.telephone, {expires: 30, path: "/frontPage/backward"})
                        //if(data.officialAccountList!=null&&data.officialAccountList!=undefined)
                        //    $.cookie("officialAccountList", JSON.stringify(data.officialAccountList), {
                        //        expires: 30,
                        //        path: "/frontPage/sellerPage"
                        //    });
                        //
                        ////if(data.thirdLogin!=null&&data.thirdLogin!=undefined)
                        ////    $.cookie("thirdLogin", JSON.stringify(data.thirdLogin), {expires: 30, path: "/frontPage/sellerPage"})
                        //if(data.payList!=null&&data.payList!=undefined)
                        //    $.cookie("payList", JSON.stringify(data.payList), {expires: 30, path: "/frontPage/sellerPage"})
                        //location.href = "/frontPage/backward/withdraws.html";
                    }
                } else if (params.errorCode == 4) {
                    alert(params.message)
                }
            }
        });
    });
})