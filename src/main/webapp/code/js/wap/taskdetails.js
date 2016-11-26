/**
 * Created by Administrator on 2016/10/14 0014.
 */
$(function(){
    var taskHistoryId=$.cookie("taskHistoryId");
    console.log(taskHistoryId);
    $.ajax({
        type: 'post',
        url: '/pc/admin/memberTask/getMemberTaskInfoDetail',
        timeout:180000,
        data: {taskHistoryId:taskHistoryId},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params){
            var json=eval(params);
            if(json.data!=null&&json.errorCode==0){
                $(".taskdetailsheader h5").text(json.data[0].taskName);
                $(".taskdetailsheader h6").text(json.data[0].pointCount+"米");
                $(".taskdetailsbody .taskname").text(json.data[0].taskDesc);
                var timestamp=json.data[0].createTime;
                $(".taskdetailsbody .tasktime").html(format(timestamp));
            }else {
                $(".nullp").css("display","block");
                $(".taskdetailsheader").css("display","none");
                $(".taskdetailsbody").css("display","none");
            }
        },
        error:function (data) {
            alert("页面加载错误，请重试");
        }
    })
});
function format(timestamp) {
    return new Date(+new Date(Math.floor(timestamp) * 1000)+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
}//时间戳变换格式