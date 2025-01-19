function loadOrder() {
    return $.ajax({
        url: '/order/api/order/getMemberOrder',
        type: 'POST',
        timeout: 30000, // 30 秒超時,
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

loadOrder();