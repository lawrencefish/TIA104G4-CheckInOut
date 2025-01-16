function loadCart(){
    return $.ajax({
        url: '/order/api/getCart',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });

}

loadCart();


