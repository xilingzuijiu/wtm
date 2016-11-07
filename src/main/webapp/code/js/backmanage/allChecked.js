/**
 * Created by Administrator on 2016/11/7.
 */
    //全选反选
function allChecked(){
    //微信支付全选反选
    $("#wechatbtn").click(function(){
        if($(this).prop("checked")==true){
            $('.list').each(function(){
                if($(this).is('.2')){
                    $(this).find(".radiocleck").prop("checked", true);
                    //alert("A");
                }
                var length =$(".radiocleck:checked").length;
                var alllength =$(".radiocleck").length;
                if (length == alllength) {
                    $(".totalcleck").prop("checked", true);
                }
            })
        }else {
            $('.list').each(function(){
                if($(this).is('.2')){
                    $(this).find(".radiocleck").prop("checked", false);
                    $(".totalcleck").prop("checked", false);
                }
            })
        }
    });
    //支部宝支付全选反选
    $("#zfbbtn").click(function(){
        if($(this).prop("checked")==true){
            $('.list').each(function(){
                if($(this).is('.1')){
                    $(this).find(".radiocleck").prop("checked", true);
                }
                var length =$(".radiocleck:checked").length;
                var alllength =$(".radiocleck").length;
                if (length == alllength) {
                    $(".totalcleck").prop("checked", true);
                }
            })
        }else {
            $('.list').each(function(){
                if($(this).is('.1')){
                    $(this).find(".radiocleck").prop("checked", false);
                    $(".totalcleck").prop("checked", false);
                }
            })
        }
    });
    //全选反选
    $(".totalcleck").click(function () {
        if ($(this).prop("checked")==true) {
            $(".totalcleck").prop("checked", true);
            $(".radiocleck").prop("checked", true);
            $("#wechatbtn").prop("checked", true);
            $("#zfbbtn").prop("checked", true);
        } else {
            $(".totalcleck").prop("checked", false);
            $(".radiocleck").prop("checked", false);
            $("#wechatbtn").prop("checked", false);
            $("#zfbbtn").prop("checked", false);
        }
    });
    $(".radiocleck").click(function () {
        var length =$(".radiocleck:checked").length;
        var alllength =$(".radiocleck").length;
        console.log(length);
        console.log(alllength);
        if (length == alllength) {
            $(".totalcleck").prop("checked", true);
            $("#wechatbtn").prop("checked", true);
            $("#zfbbtn").prop("checked", true);
        }else if(length==0){
            $("#zfbbtn").prop("checked", false);
            $("#wechatbtn").prop("checked", false);
        }else {
            $(".totalcleck").prop("checked", false);
            if($(this).parent().parent().parent().is('.2')){
                $("#wechatbtn").prop("checked", false);
            }else{
                $("#zfbbtn").prop("checked", false);
            }
        }
    });
}
