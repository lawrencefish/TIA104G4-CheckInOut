$(document).ready(function () {
    loadCart();
    $(document).on("click", "[data-delete-btn]", function () {
        let roomId = $(this).data("delete-btn");
        deletCart(roomId);
    });

    $(document).on("click", ".hotel-selection", function () {
        // 先移除所有已選擇的樣式
        $(".hotel-selection").removeClass("border-primary bg-light shadow-lg");
        // 為當前選擇的飯店添加選擇效果
        $(this).addClass("border-primary bg-light shadow-lg");
        // 確保 radio 也被選中
        $(this).find("input[type='radio']").prop("checked", true);
        calcPrice();
    });

    $('#checkout-button').on('click', (e) => {
        e.preventDefault();
        if ($('input[name="selectedHotel"]:checked').length != 0) {
            checkOutCart();
        } else {
            showModal("請選取要結帳的旅館");
        }
    })

    $('#clearBtn').on('click', (e) => {
        e.preventDefault();
        $("input[name='selectedHotel']").prop("checked", false);
        $(".hotel-selection").removeClass("border-primary bg-light shadow-lg");
        calcPrice();
    })

});

function checkOutCart() {
    checkLogin().then(isLoggedIn => {
        if (isLoggedIn) {
            let hoteldSelected = $('input[name="selectedHotel"]:checked').val();
            if (hoteldSelected) {
                $.post("/order/api/cart/addOrder",
                    { hotelId: hoteldSelected },
                    function (response) {
                        if (response.message == "ok") {
                            window.location.href = '/user/checkout';
                        } else {
                            showModal("發生了錯誤，請稍後再嘗試");
                        }
                    }
                );
            }
        }
    });
}

function loadCart() {
    return $.ajax({
        url: '/order/api/cart/get',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            updateCart(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });

}

function deletCart(id) {
    $.post("/order/api/cart/delete", { roomTypeId: id }, function (response) {
        console.log(response);
        showModal(response.message);
        loadCart();
        setTimeout(calcPrice, 100);
        showLoginView();
    });
}

function checkLogin() {
    return new Promise(resolve => {
        $.post("/user/api/loginCheck", function (response) {
            if (response.message == "not login") {
                showModal("登入後才能進行結帳！");
                showModal();
                resolve(false); // 未登入，返回 false
            } else {
                resolve(true); // 已登入，返回 true
            }
        });
    });
}

function calcPrice() {
    let hotel = $('input[name="selectedHotel"]:checked').closest('div');
    let rooms = $('input[name="selectedHotel"]:checked').closest('div').find('.hotel-rooms');
    let roomNum = rooms.find('.room-item').length;
    let price = 0;
    rooms.find('.room-item').each(function (index, ele) {
        price += parseInt($(this).find('.hotel-price').text());
    })
    $('#total-price').text(price);
    $('#total-rooms').text(roomNum);
    $('#select-hotel').text(hotel.find('.hotel-name').text())
}


function updateCart(dataArray) {
    $('#cart-container').html("");
    dataArray.forEach(data => {
        if (data.empty != "true") {
            let hotelName = data.hotelName;
            let hotelId = data.hotelId;
            let divClass = '.hotel' + hotelId;
            let review = data.review;
            let html = `
    <label class="form-check-label w-100">
        <div class="form-check border rounded p-3 mb-3 position-relative shadow-sm hotel-selection">
            <input class="form-check-input d-none" type="radio" name="selectedHotel" value="${hotelId}">
            
            <div class="d-flex align-items-center">
                <img src="/booking/api/image/room/${hotelId}/0" alt="${hotelName}" class="rounded hotel-image me-3"
                     style="width: 180px; height: 120px; object-fit: cover;">

                <div class="flex-grow-1">
                    <h5 class="hotel-name fw-bold mb-1">
                        <a href="/user/hotel_detail/${hotelId}" class="text-decoration-none text-dark">${hotelName}</a>
                    </h5>
                    
                    <!-- 評分區塊 -->
                    <p class="hotel-rating text-muted mb-0 ${review > 0 ? '' : 'd-none'}">
                        <strong>評分：</strong> <span>${review}</span> <span class="star-${hotelId} text-warning"></span>
                    </p>
                </div>
            </div>

            <!-- 房型列表 -->
            <div class="hotel-rooms hotel${hotelId} mt-3 "></div>
        </div>
    </label>
`
            $('#cart-container').append(html);

            let starContainerClass = '.star-' + hotelId;
            let starContainer = document.querySelector(starContainerClass);
            displayStars(review, starContainer);

            data.cartDetailList.forEach(cartDetail => {
                let roomName = cartDetail.roomName;
                let roomId = cartDetail.roomTypeId;
                let roomNum = cartDetail.roomNum;
                let guestNum = cartDetail.guestNum;
                let checkInDate = cartDetail.checkInDate;
                let checkOutDate = cartDetail.checkOutDate;
                let checkIn = new Date(checkInDate);
                let checkOut = new Date(checkOutDate);
                let timeDiff = (checkOut - checkIn) / (1000 * 60 * 60 * 24);
                let breakfast = cartDetail.breakfast;
                let totalBreakfastPrice = cartDetail.totalbreakPrice ? cartDetail.totalbreakPrice  : 0;
                let totalPrice = cartDetail.totalPrice ;
                let html =`
<div class="card room-item border rounded p-3 mb-2" data-room-id="${roomId}">
    <div class="card-body">
        <h4 class="card-title fw-bold text-truncate">${roomName}</h4>

        <!-- 使用 Bootstrap row，確保不超過 12 格 -->
        <div class="row align-items-center">

            <!-- 圖片區塊：桌機 3 格，手機滿版 -->
            <div class="col-md-3 col-12 text-center mb-2 mb-md-0">
                <img src="/booking/api/image/room/${roomId}/0" 
                     class="rounded img-fluid room-image" 
                     style="max-width: 100%; height: auto; object-fit: cover;">
            </div>

            <!-- 房間資訊區塊：桌機 6 格，手機滿版 -->
            <div class="col-md-6 col-12">
                <p class="mb-1 room-info"><strong>入住日期：</strong> ${checkInDate} - ${checkOutDate}</p>
                <p class="mb-1 room-info"><strong>入住人數：</strong> ${guestNum} 人</p>
                <p class="mb-1 room-info"><strong>住宿天數：</strong> ${timeDiff} 晚</p>
                <p class="mb-1 room-info"><strong>房間數：</strong> ${roomNum} 間</p>

                <!-- 總價 -->
                <p class="fw-bold total-price"><strong>總價：</strong> NTD$ <span class="hotel-price">${totalPrice}</span></p>

                <!-- 早餐資訊（分兩行顯示） -->
                <p class="mb-1 text-muted small ${breakfast == 0 ? 'd-none' : ''}">
                    <strong>已含早餐價格：</strong>
                </p>
                <p class="mb-1 text-muted small ${breakfast == 0 ? 'd-none' : ''}">
                    每人每晚 NTD$ <span class="breakfast-price">${Math.floor(totalBreakfastPrice / guestNum / timeDiff)}</span>
                    ・ 總價 NTD$ <span class="breakfast-total-price">${totalBreakfastPrice}</span>
                </p>
            </div>

            <!-- 按鈕區塊：桌機 3 格，手機滿版 -->
            <div class="col-md-3 col-12 text-end d-flex flex-column align-items-md-end align-items-center">
                <button class="btn btn-danger btn-sm remove-room w-50 mt-2" data-delete-btn="${roomId}">移除</button>
            </div>

        </div>
    </div>
</div>
                `
                $(divClass).append(html);
            });
        } else {
            let html = `
                    <div class="container d-flex flex-column align-items-center justify-content-center text-center" style = "min-height: 500px;">
                        <div>
                            <!-- 放大購物車圖示 -->
                            <h1 class="mb-3 text-secondary">
                                <i class="bi bi-cart-x" style="font-size: 4rem;"></i>
                            </h1>

                            <h4 class="fw-bold text-dark">您的購物車是空的</h4>
                            <p class="text-muted">快去挑選您喜歡的房型吧！</p>

                            <!-- 按鈕區塊 -->
                            <a href="/user/" class="btn btn-secondary px-4 py-2 mt-3">返回首頁</a>
                        </div>
                </div>
                    `
            $('#cart-container').append(html);
        }
    })
}


function displayStars(rating, starContainer) {
    starContainer.innerHTML = "";
    // 顯示 5 顆星的評分
    for (let i = 1; i <= 5; i++) {
        if (rating >= i) {
            // 完整星星
            starContainer.innerHTML += '<i class="bi bi-star-fill star"></i>';
        } else if (rating >= i - 0.5) {
            // 半顆星星
            starContainer.innerHTML += '<i class="bi bi-star-half star"></i>';
        } else {
            // 空星星
            starContainer.innerHTML += '<i class="bi bi-star star"></i>';
        }
    }
}


