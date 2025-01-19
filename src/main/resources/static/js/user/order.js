const html = `
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">訂單明細</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="row">
                    <!-- 左側：旅館資訊 -->
                    <div class="col-md-4">
                        <img src="" alt="旅館圖片" class="img-fluid rounded shadow mb-3">
                        <h4 id="hotelName" class="text-primary fw-bold">旅館名稱</h4>
                        <p><strong>地址：</strong> <span id="hotelAddress">旅館地址</span></p>
                        <p><strong>電話：</strong> <span id="hotelPhone">旅館電話</span></p>
                        <p><strong>Email：</strong> <span id="hotelEmail">旅館 Email</span></p>
                    </div>

                    <!-- 右側：訂單與房間資訊 -->
                    <div class="col-md-8">
                        <!-- 訂單資訊 -->
                        <div class="p-3 border rounded shadow-sm mb-3 bg-light">
                            <h5 class="fw-bold">訂單資訊</h5>
                            <p><strong>入住人：</strong> <span id="guestName">入住人姓名</span></p>
                            <p><strong>備註：</strong> <span id="memo">備註內容</span></p>
                            <p><strong>總價：</strong> <span id="totalAmount" class="text-danger fw-bold">NT$ 0</span></p>
                            <p><strong>信用卡：</strong> <span id="creditcardNum">**** **** **** 1234</span></p>
                        </div>

                        <!-- 評論資訊 -->
                        <div class="p-3 border rounded shadow-sm mb-3">
                            <h5 class="fw-bold">評論</h5>
                            <p><strong>評分：</strong> <span id="rating" class="text-warning">★★★★★</span></p>
                            <p><strong>評論內容：</strong> <span id="commentContent">這是一個範例評論</span></p>
                            <p><strong>商家回覆：</strong> <span id="commentReply">感謝您的評論！</span></p>
                        </div>

                        <!-- 房型資訊 -->
                        <div class="p-3 border rounded shadow-sm">
                            <h5 class="fw-bold">房型資訊</h5>
                            <div class="d-flex align-items-start">
                                <img src="" alt="房型圖片" class="img-thumbnail me-3" style="width: 150px;">
                                <div>
                                    <p><strong>房型名稱：</strong> <span id="roomName">豪華雙人房</span></p>
                                    <p><strong>入住日期：</strong> <span id="checkInDate">2024-01-20</span></p>
                                    <p><strong>退房日期：</strong> <span id="checkOutDate">2024-01-25</span></p>
                                    <p><strong>入住人數：</strong> <span id="guestNum">2</span> 人</p>
                                    <p><strong>房價：</strong> <span id="roomPrice" class="text-danger fw-bold">NT$ 5000</span></p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- 右側結束 -->
                </div> <!-- row 結束 -->
            </div> <!-- modal-body 結束 -->
        </div>
`
function loadOrder() {
    return $.ajax({
        url: '/order/api/order/getMemberOrder',
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


$(document).ready(function () {
    loadOrder();
})