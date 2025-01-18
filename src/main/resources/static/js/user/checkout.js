let email;
let lastName;
let firstName;
let checkInDate;
let checkOutDate;
const savedCards = $("#savedCards");
const toggleCardForm = $("#toggleCardForm");
const newCardForm = $("#newCardForm");
const creditCardInput = $("#creditCard");
const expiryDateInput = $("#expiryDate");
const securityCodeInput = $("#securityCode");

let selectedCard = ""; // 儲存信用卡資訊

let orderInfo = {
    email: $('#email').val(),
    lastName: $('#lastName').val(),
    firstName: $('#firstName').val(),
    checkInDate : "",
    checkOutDate : "",
    memo: $('#notes').val().trim(),
    coupon: $('#coupon option:selected').data("id"),
    savedCard: selectedCard,
    finalPrice: $('#finalPrice').text()
}


$(document).ready(function () {
    loadCart();
    loadMember();
    $('#addMemberInfoBtn').on('click', (e) => {
        e.preventDefault();
        updateMemberInfo();
    })

    document.getElementById("coupon").addEventListener("change", function () {
        let tempPrice = parseInt($('.totalPrice').text()) - this.value;
        let html =
            `<p class="fw-bold text-success">折扣：${this.value}
       <br>
        <span>折扣後總價：${tempPrice}</span>
        </p>
       `
        $('.discount-info').html("");
        $('#originalPrice').text("");
        if (this.value > 0) {
            $('#originalPrice').text("$" + $('.totalPrice').text());
            $('.discount-info').append(html);
        }
        $('#finalPrice').text(tempPrice);

    });

    // 監聽已存信用卡選擇變更
    savedCards.on("change", function () {
        if ($(this).val()) {
            // 如果選擇了已存信用卡，隱藏新增信用卡表單
            newCardForm.addClass("d-none");
            creditCardInput.val("");
            expiryDateInput.val("");
            securityCodeInput.val("");
        }
    });

    // 監聽 "新增信用卡" 按鈕
    toggleCardForm.on("click", function () {
        if (newCardForm.hasClass("d-none")) {
            // 顯示輸入表單
            newCardForm.removeClass("d-none");
            savedCards.val(""); // 清空已存信用卡選擇
        } else {
            // 隱藏表單
            newCardForm.addClass("d-none");
        }
    });


    $("form").on("submit", function (event) {
        event.preventDefault(); // 防止表單預設提交行為（避免刷新頁面）
        if (validateCreditCard()) {
            updateOrderInfo();
            console.log(orderInfo);
            checkout();
        }
    });

});


function checkout() {
    return $.ajax({
        url: '/order/api/order/checkout',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(orderInfo),  
        success: function (data) {
            console.log("✅ 訂單成功回應:", data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error("🚨 AJAX 請求發生錯誤:", textStatus, errorThrown);
            console.log("📌 響應文本:", jqXHR.responseText);
            if (jqXHR.responseJSON) {
                console.log("❌ API 回傳錯誤:", jqXHR.responseJSON);
                alert(jqXHR.responseJSON.message || "訂單失敗，請稍後再試！");
            }
        }
    });
}


function orderSend() {
    $.ajax({
        url: "/order/api/checkout", // 你的後端 API URL
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(orderInfo),
        success: function (response) {
            console.log("成功提交訂單：", response);
            alert("訂單提交成功！");
            window.location.href = "/order/confirmation"; // 成功後跳轉
        },
        error: function (xhr, status, error) {
            console.error("發生錯誤：", error);
            alert("訂單提交失敗，請稍後再試！");
        }
    });
}

function updateOrderInfo() {
    orderInfo = {
        email: $('#email').val(),
        lastName: $('#lastName').val(),
        firstName: $('#firstName').val(),
        memo: $('#notes').val().trim(),
        coupon: $('#coupon option:selected').data("id") ? $("#coupon option:selected").data("id") : 0,
        checkInDate :checkInDate,
        checkOutDate : checkOutDate,
        savedCard: selectedCard,
        finalPrice: $('#finalPrice').text().trim()
    };

    console.log("即時更新的 orderInfo：", orderInfo); // 測試輸出
}


function loadCart() {
    return $.ajax({
        url: '/order/api/order/get',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            updateOrder(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });
}

function loadMember() {
    return $.ajax({
        url: '/order/api/order/getMemberInfo',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            updateCoupon(data);
            updateCreditCard(data);
            email = data.email;
            lastName = data.lastName;
            firstName = data.firstName;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });
}

function updateMemberInfo() {
    $('#email').val(email);
    $('#lastName').val(lastName);
    $('#firstName').val(firstName);
}

function updateCreditCard(data) {
    data.creditCardList.forEach((c) => {
        $('#savedCards').append(`<option value="${c.id}">${c.num.substring(0, 4)} **** **** ****</option>`)
    })
}

function updateCoupon(data) {
    data.counponList.forEach((c) => {
        $('#coupon').append(`<option  data-id=${c.id} value="${c.discount}">${c.name}：折扣${c.discount}</option>`)
    })
}

function updateOrder(data) {
    let hotelId = data.hotelId;
    let hotelName = data.hotelName;

    // 清空訂單容器
    $('.hotel-card').empty();

    let hotelCard = `
        <h4 class="fw-bold mb-3">訂單明細</h4>
        <div class="border rounded p-3" style="overflow-y: auto;">
            <!-- 飯店資訊 -->
            <div class="mb-4">
                <div class="d-flex">
                    <img src="/booking/api/image/hotel/${hotelId}/0" class="rounded me-3"
                        style="width: 100px; height: 100px; object-fit: cover;">
                    <div>
                        <h5 class="mb-1">${hotelName}</h5>
                        <p class="fw-bold">總價：NTD$ <span class="totalPrice">0</span></p>
                        <div class="discount-info">
                        </div>
                    </div>
                </div>
            </div>
            <!-- 房型資訊 -->
            <div class="hotel-rooms"></div>
        </div>
    `;

    $('.hotel-card').append(hotelCard);

    let totalOrderPrice = 0; // 計算總價

    data.cartDetailList.forEach((cartDetail) => {
        let roomName = cartDetail.roomName;
        let roomId = cartDetail.roomTypeId;
        let roomNum = cartDetail.roomNum;
        let guestNum = cartDetail.guestNum;
        let RcheckInDate = cartDetail.checkInDate;
        let RcheckOutDate = cartDetail.checkOutDate;
        let checkIn = new Date(RcheckInDate);
        let checkOut = new Date(RcheckOutDate);
        let timeDiff = (checkOut - checkIn) / (1000 * 60 * 60 * 24);
        let breakfast = cartDetail.breakfast;
        let totalPrice = cartDetail.totalPrice;
        let totalBreakfastPrice = cartDetail.totalbreakPrice ? cartDetail.totalbreakPrice * guestNum : 0;
        checkInDate = RcheckInDate;
        checkOutDate = RcheckOutDate;
        totalOrderPrice += totalPrice; // 累加總價

        let roomCard = `
            <div class="card room-item border rounded p-2 mb-2" data-room-id="${roomId}">
                <div class="card-body">
                    <h5 class="fw-bold text-truncate room-title mb-2">${roomName}</h5>
                    <div class="row align-items-center">
                        <!-- 房型圖片 -->
                        <div class="col-md-3 col-12 text-center">
                            <img src="/booking/api/image/room/${roomId}/0"
                                class="rounded img-fluid room-image"
                                style="width: 100%; height: 80px; object-fit: cover;">
                        </div>

                        <!-- 房型資訊 -->
                        <div class="col-md-9 col-12">
                            <p class="mb-1 room-info">
                                <strong>入住日期：</strong> ${RcheckInDate} - ${RcheckOutDate}
                            </p>
                            <p class="mb-1 room-info">
                                <strong>入住：</strong> ${guestNum} 位 ・ ${timeDiff} 晚 × ${roomNum} 間房
                            </p>
                            <p class="mb-1 room-info">
                                <strong>每晚：</strong> NTD$ <span class="price-per-night">${Math.floor(totalPrice / roomNum / timeDiff)}</span>
                            </p>
                            <p class="mb-1 fw-bold total-price">
                                <strong>總價：</strong> NTD$ <span class="room-total-price">${totalPrice}</span>
                            </p>

                            <!-- 早餐資訊（低調顯示） -->
                            <div class="breakfast ${breakfast < 1 ? 'd-none' : ''}">
                                <p class="mb-1 text-muted small breakfast-info"><strong>已含早餐價格：</strong></p>
                                <p class="mb-1 text-muted small">
                                    每人 NTD$ <span class="breakfast-price">${Math.floor(totalBreakfastPrice / guestNum / timeDiff)}</span>
                                    ｜ 總價 NTD$ <span class="breakfast-total-price">${totalBreakfastPrice}</span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        $('.hotel-rooms').append(roomCard);
        $('#finalPrice').text(totalOrderPrice);

    });

    // 更新總價
    $('.totalPrice').text(totalOrderPrice);
}


// 信用卡驗證函數
function validateCreditCard() {
    let savedCardSelected = savedCards.val() !== "";
    let newCardEntered = creditCardInput.val().trim() !== "" &&
        expiryDateInput.val().trim() !== "" &&
        securityCodeInput.val().trim() !== "";

    if (savedCardSelected) {
        selectedCard = savedCards.val(); // 儲存選擇的信用卡
        return true;
    } else if (newCardEntered) {
        selectedCard = {
            cardNumber: creditCardInput.val().trim(),
            expiryDate: expiryDateInput.val().trim(),
            securityCode: securityCodeInput.val().trim()
        };
        return true;
    } else {
        showModal("請選擇已存信用卡，或完整輸入新的信用卡資訊！");
        return false;
    }
}
