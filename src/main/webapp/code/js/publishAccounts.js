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
function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};
function chooseProvince(value){
    var cityArea='<option value="">--请选择--</option>';
//            var value = $('#prov').options[$('#prov').selectedIndex].;
    addressArr.forEach(function(province){
        if (province.province==value){
            var cityList=province.cityList;
            cityList.forEach(function(city){
                cityArea += '<option value="'+city.city+'">'+city.city+'</option>';
            })
        }
    })
    $('#city').empty();
    $('#city').append(cityArea);
};
function changeScore(value){
    $("#div1").empty()
    $("#div1").append(value)
    var total = $("#amount").text().trim();
    var number=$("#input2").val()
    if(eval(total-number*value)<0){
        $("#div1").empty()
        $("#input1").val(0)
        alert("可用米币额度不足")
    }
};
function checkAmount(value){
    var total = $("#amount").text().trim();
    var number=$("#input1").val()
    if(eval(total-number*value)<0){
        alert("可用米币额度不足")
        $("#input2").val(0)
    }
};
function deal(obj){
    var accountList=obj.officialAccountList;
    var elements='';
    var amount=0.00;
    accountList.forEach(function(account){
        amount=account.memberScore;
        var element='<option value="'+account.id+'">'+account.ofiicialAccountName+'</option>';
        elements += element;
    })
    $("#select1").empty();
    $("#select1").append(elements)
    $("#amount").empty()
    $("#amount").append(amount)
    var provinces='<option value="">--请选择--</option>';
    addressArr=obj.addressList;
    addressArr.forEach(function(province){
        provinces +='<option value="'+province.province+'">'+province.province+'</option>';
    })
    $("#prov").empty();
    $("#prov").append(provinces)
    var eles="";
    for(i=1;i<=100;i++){
        eles += '<option value="'+i+'">'+i+'</option>';
    }
    $("#select2").empty();
    $("#select2").append(eles)
};