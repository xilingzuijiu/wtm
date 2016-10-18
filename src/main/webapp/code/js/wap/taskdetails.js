/**
 * Created by Administrator on 2016/10/14 0014.
 */
$(function(){
    var taskHistoryId=$.cookie("taskHistoryId");
    console.log(taskHistoryId);
    $.ajax({
        type: 'post',
//                dataType: 'json',
        url: '/pc/admin/memberTask/getMemberTaskInfoDetail',
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
})
function format(timestamp) {
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
}//时间戳变换格式