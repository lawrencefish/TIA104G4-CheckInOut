let comment = {
    orderId : 0,
    rating: 0,
    commentContet :""
};

$(document).ready(function () {
    loadOrder(function (data) {
        updateTab(data); // ✅ 在這裡正確使用 AJAX 回應數據
    });
    tabSelect();
    // setRating(0);

    $(document).on("click", ".orderBtn", function () {
        let orderId = $(this).data("order-id");
        console.log("按鈕 ID:", $(this).data("order-id")); // 顯示按鈕 ID
        loadOrder(function (data) {
            updateDetail(data, orderId); // ✅ 確保數據載入後才更新訂單詳情
        });
    });

    $(document).on("click", ".cancelBtn", function () {
        let orderId = $(this).data("order-id");
        cancelOrder(orderId);
    })

    let currentRating = 0; // 目前評分值

    document.querySelectorAll("#starContainer .star").forEach(star => {
        // **點擊星星：更新評分**
        star.addEventListener("click", function () {
            let rating = parseInt(this.getAttribute("data-value"));
            setRating(rating);
        });

        // **滑鼠移入：即時顯示亮度**
        star.addEventListener("mouseover", function () {
            let rating = parseInt(this.getAttribute("data-value"));
            highlightStars(rating);
        });

        // **滑鼠移出：回復當前評分**
        star.addEventListener("mouseleave", function () {
            highlightStars(currentRating);
        });
    });

    $(document).on("click", ".sendCommentBtn", function (e) {
        e.preventDefault();
        setComment();
    })

});

function tabSelect() {
    $('#orderTabs .nav-link').on('click', function () {
        // 取得點擊的 tab 對應的 data-tab 屬性值
        let targetTab = $(this).data('tab');
        console.log(targetTab);
        // 1. 隱藏所有 tab-panel
        $('.tab-panel').addClass('d-none');

        // 2. 顯示對應的 tab-panel
        $('#' + targetTab).removeClass('d-none');

        // 3. 移除所有 nav-link 的 active 樣式
        $('#orderTabs .nav-link').removeClass('active');

        // 4. 為點擊的按鈕添加 active 樣式
        $(this).addClass('active');
    });

}

function setComment(){
    comment.commentContet = $('#commentContent').val();
    comment.orderId =  $("#id").text();
    comment.rating = $("#ratingDisplay").text();
    sendComment(function(data){
        console.log(data);
        let modalInstance = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
        if (modalInstance) {
            modalInstance.hide();
        }
        showModal(data.message);
        loadOrder(function (data) {
            updateTab(data); 
        });
    })
}


function sendComment(callback) {
    $.ajax({
        url: '/order/api/comment/send',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(comment),
        success: function (data) {
            if (callback) callback(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText);
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });
}


function loadOrder(callback) {
    $.ajax({
        url: '/order/api/order/getMemberOrder',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if (callback) callback(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX 請求發生錯誤:', textStatus, errorThrown);
            console.log('響應文本:', jqXHR.responseText);
        }
    });
}

function cancelOrderPost(callback, id) {
    console.log(id);
    $.post("/order/api/order/cancel", { orderId: id }, function (data) {
        console.log("取消訂單成功:", data);
        if (callback) callback(data);
    })
}


function updateTab(dataArray) {
    $('#past').html("");
    $('#future').html("");
    $('#cancelled').html("");
    let minDate;
    dataArray.forEach((data) => {
        let hotelId = data.hotelId;
        let hotelName = data.hotelName;
        let checkInDate = data.checkInDate.split("T")[0];
        let checkOutDate = data.checkOutDate.split("T")[0];
        let today = new Date().setHours(0, 0, 0, 0);
        let inputCID = new Date(checkInDate);
        let inputCOD = new Date(checkOutDate);
        let orderId = data.orderId

        let status = data.status;
        let inputStatus;
        switch (status) {
            case 0:
                if (today > inputCID) {
                    inputStatus = "逾期未入住";
                } else {
                    inputStatus = "預訂成功"
                }
                break;
            case 1:
                if (today > inputCOD) {
                inputStatus = "逾期未入住";
                }else{
                inputStatus = "入住中"
                }
                break;
            case 2:
                inputStatus = "完成訂單"
                break;
            case 3:
                inputStatus = "訂單取消"
                break;
        }

        let tabHtml =
            `
			<div class="p-3 border rounded-3 mb-3 bg-white">
				<div class="row g-3">
					<!-- 飯店圖片 -->
					<div class="col-auto">
						<img src="/booking/api/image/room/${hotelId}/0" class="img-fluid rounded"
							style="width:120px; height:80px; object-fit:cover;" />
					</div>

					<!-- 訂單資訊：編號、飯店、日期 -->
					<div class="col d-flex flex-column justify-content-center">
						<!-- 訂單編號 -->
						<p class="text-muted mb-1">
							訂單編號: <strong>${orderId}</strong>
						</p>
						<!-- 飯店名稱 -->
						<h5 class="fw-bold mb-1">${hotelName}</h5>
						<!-- 入住 & 退房日期 -->
						<p class="text-muted mb-0">
							入住：${checkInDate}
							<br>
							退房：${checkOutDate}
						</p>
					</div>

					<!-- 按鈕：管理預訂 -->
					<div class="col-auto d-flex flex-column align-items-center justify-content-around btnArea" >
                        <h5><span class="badge">${inputStatus}</span></h5>
						<button class="btn btn-primary mt-2 orderBtn" data-order-id="${orderId}">
							管理預訂
						</button>
					</div>
				</div>
			</div>
`
        if (today > inputCID && (status !== 3)) {
            $('#past').append(tabHtml);
        } else if (today < inputCID && status == 0) {
            if(minDate == null || minDate== ""){
                minDate = today;
            }
            if (inputCID <= minDate){
                $('#future').append(tabHtml);
                minDate = inputCID;
            }else{
                $('#future').prepend(tabHtml);
            }
        } else if (status == 3) {
            $('#cancelled').append(tabHtml);
        }

        $('.btnArea').find('.badge').each(function () {
            let statusText = $(this).text().trim(); // 確保去掉空格
            $(this).removeClass("bg-warning bg-success bg-info bg-danger bg-secondary"); // 先移除所有可能的 class

            if (statusText === "逾期未入住") {
                $(this).addClass("bg-danger");
            } else if (statusText === "預訂成功") {
                $(this).addClass("bg-success");
            } else if (statusText === "入住中") {
                $(this).addClass("bg-info");
            } else if (statusText === "完成訂單") {
                $(this).addClass("bg-success");
            } else {
                $(this).addClass("bg-secondary"); // 預設灰色
            }
        });

    })
    let emptyHtml = `
        <div class="container d-flex justify-content-center align-items-center" style="height: 70vh;">
            <div class="text-center border rounded-3 p-4 shadow-sm bg-white w-100">
                <i class="bi bi-cart-x text-secondary" style="font-size: 60px;"></i>
                <h4 class="fw-bold mt-3">目前沒有訂單</h4>
                <p class="text-muted">這裡目前是空的，趕快去選擇適合的飯店吧！</p>
                <a href="/user" class="btn btn-primary">立即預訂</a>
            </div>
        </div>
    `
    if ($('#past').html().trim() === "") {
        $('#past').html(emptyHtml);
    }
    if ($('#future').html().trim() === "") {
        $('#future').html(emptyHtml);
    }
    if ($('#cancelled').html().trim() === "") {
        $('#cancelled').html(emptyHtml);
    }

}

function cancelOrder(id) {
    let existingModal = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
    if (existingModal) {
        existingModal.hide(); // ✅ 關閉舊的 Modal
    }
    let html = `
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h5 class="modal-title fw-bold" id="cancelOrderLabel">確認取消編號${id}號訂單？</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <p class="fs-5">請確認是否要取消此訂單，取消後無法復原。</p>
                
            <!-- Modal Footer -->
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">返回</button>
                <button type="button" class="btn btn-danger" id="confirmCancelBtn">確認取消</button>
            </div>
        </div>
    `
    let $html = $(html);
    let addHtml = $html.prop("outerHTML");
    console.log(addHtml);
    showModal(addHtml, false);

    $(document).off("click", "#confirmCancelBtn").on("click", "#confirmCancelBtn", function () {
        let orderId = $(this).data("order-id");

        cancelOrderPost(function (data) {
            // **關閉 Modal**
            let modalInstance = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
            if (modalInstance) {
                modalInstance.hide();
            }
            // **顯示成功訊息**
            showModal(data.message);

            loadOrder(function (data) {
                updateTab(data);
            });
        }, id);
    });
}


function updateDetail(data, id) {
    let d = data.find(d => d.orderId === id);
    let hotelId = d.hotelId;
    let status = d.status;
    let hotelName = d.hotelName;
    let orderId = d.orderId;
    let checkInDate = d.checkInDate.split("T")[0];
    let checkOutDate = d.checkOutDate.split("T")[0];
    let inputCID = new Date(checkInDate);
    let twoDaysAfter = new Date(Date.now() + 2 * 24 * 60 * 60 * 1000);
    let guestLastName = d.guestLastName;
    let guestFirstName = d.guestFirstName;
    let NcreateTime = d.createTime;
    let createTime = new Date(NcreateTime).toISOString().replace('T', ' ').slice(0, 16);
    let discount = d.discount;
    let totalPriceAdd = 0;
    let creditcardNum = d.creditcardNum.substring(11, 15);
    let commentReply = d.commentReply != null ? d.commentReply : "";
    let comment = d.commentContent != null ? d.commentContent : "";
    let rating = d.rating != null ? d.rating : "";
    let memo = d.memo != null ? d.memo : "";

    let modalHtml = `
            <div class="modal-content border-0 bg-white rounded-3">
                <div class="modal-header text-center">
                    <h5 class="modal-title w-100 fw-bold">訂單明細</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body p-4">
                    <!-- 旅館資訊 -->
                    <div class="mb-4">
                        <div class="d-flex align-items-center mb-3">
                            <img src="/booking/api/image/hotel/${hotelId}/0"
                                class="rounded me-3"
                                style="width: 120px; height: 80px; object-fit: cover;">
                                <h5 id="hotelName" class="fw-bold mb-0">${hotelName}</h5>
                        </div>
                    </div>
                <div class="roomType">
                </div>
                    <hr>

                        <!-- 訂單資訊（獨立區塊） -->
                        <div class="p-3 border rounded-3 mb-4 bg-white">
                            <h6 class="fw-bold text-center mb-3">訂單資訊</h6>
                            <p class="mb-2">
                                <strong>訂單編號：</strong>
                                <span id="id">${orderId}</span>
                            </p>

                            <p class="mb-2">
                                <strong>入住日期：</strong>
                                <span id="checkInDate">${checkInDate}</span> -
                                <span id="checkOutDate">${checkOutDate}</span>
                            </p>

                            <p class="mb-2">
                                <strong>訂單完成時間：</strong>
                                <span id="createTime">${createTime}</span>
                            </p>

                            <p class="mb-2">
                                <strong>入住人：</strong>
                                <span id="guestName">${guestLastName} ${guestFirstName}</span>
                            </p>

                            <p class="mb-2 ${memo == "" ? "d-none" : ""} ">
                                <strong>備註：</strong>
                                <span id="memo"> ${memo} </span>
                            </p>

                            <p class="mb-2 ${discount == 0 ? "d-none" : ""} ">
                                <strong>折扣：</strong>
                                <span id="discount">NT$ ${discount}</span>
                            </p>

                            <p class="mb-2">
                                <strong>訂單總價：</strong>
                                <span id="totalPrice" class="text-danger fw-bold">NTD$ ${totalPriceAdd}</span>
                            </p>

                            <p class="mb-0">
                                <strong>信用卡：</strong>
                                <span id="creditcardNum">**** **** **** ${creditcardNum}</span>
                            </p>

                        </div>

                        <!-- 取消訂單按鈕 -->
                        <div class="cancelOrder mb-4 ${ (status != 0 || twoDaysAfter > inputCID) ? "d-none" : "" } "">
                            <button class="btn btn-danger w-100 fw-bold cancelBtn" data-order-id="${orderId}">取消訂單</button>
                        </div>
                </div>
            `
    let $modal = $(modalHtml);
    $modal.empty();
    $modal = $(modalHtml);
    let toCommentHTML = `
            <hr>
        <div class="p-3 rounded-3 bg-white commentArea border">
                <h5 class="fw-bold text-center mb-4">評論</h5>

                <!--星級評分 -->
                <div class="d-flex align-items-center mb-3">
                    <strong class="me-2">評分：</strong>
                    <!-- 顯示 5 顆星，點擊更新評分 -->
                    <div id="starContainer" class="d-flex">
                        <span class="star fs-4 me-1" data-value="1" onclick="setRating(1)">★</span>
                        <span class="star fs-4 me-1" data-value="2" onclick="setRating(2)">★</span>
                        <span class="star fs-4 me-1" data-value="3" onclick="setRating(3)">★</span>
                        <span class="star fs-4 me-1" data-value="4" onclick="setRating(4)">★</span>
                        <span class="star fs-4 me-1" data-value="5" onclick="setRating(5)">★</span>
                    </div>

                    <!-- 目前評分（透過 JS 動態顯示） -->
                    <span id="ratingDisplay" class="ms-3 text-secondary"></span>
                </div>

                <!--評論輸入框 -->
                <label for="commentContent" class="fw-bold">您的評論：</label>
                <textarea id="commentContent" class="form-control mt-2" rows="3" placeholder="寫下您的評論..."></textarea>

                <div class="mt-3 text-end">
                    <button class="btn btn-primary btn-sm fw-bold sendCommentBtn">提交評論</button>
                </div>
            </div>
        `
    let commentHtml = `
        <hr>
        <div class="p-3 rounded-3 bg-white commentArea border">
            <h5 class="fw-bold text-center mb-3">評論</h5>

            <!-- 星級評分 -->
            <div class="d-flex flex-column align-items-center mb-3">
                <div class="starContainer" class="d-flex">
                    <span class="star fs-4 me-1" data-value="1">★</span>
                    <span class="star fs-4 me-1" data-value="2">★</span>
                    <span class="star fs-4 me-1" data-value="3">★</span>
                    <span class="star fs-4 me-1" data-value="4">★</span>
                    <span class="star fs-4 me-1" data-value="5">★</span>
                    <span class="ratingDisplay" class="ms-3 text-secondary">${rating} 星</span>
                </div>
            </div>

            <!-- 評論 -->
            <p class="mb-2 ${comment.trim() === "" ? " d-none" : "" }"><strong>評論：</strong> ${comment}</p>

            <!-- 業者回覆 -->
            <p class="mb-0 ${commentReply.trim() === "" ? " d-none" : "" }"><strong>業者回覆：</strong> ${commentReply}</p>
        </div> 
`
    if(rating == "" && comment =="" && status == 2){
        $modal.find('.cancelOrder').after(toCommentHTML);

    }else if (status == 2){
        $modal.find('.cancelOrder').after(commentHtml);
        for(let i = 0 ; i < rating ; i++){
            $modal.find('.starContainer .star')[i].classList.add("active");
        }
    }

    d.orderDetails.forEach(de => {
        let roomName = de.roomName;
        let roomTypeId = de.roomTypeId;
        let guestNum = de.guestNum;
        let breakfast = de.breakfast;
        let roomNum = de.roomNum;
        let totalPrice = de.totalPrice;
        let totalBreakfastPrice = de.totalBreakfastPrice;
        let roomTypeHtml = `
            <div class="mb-4 ps-3">
                <!-- 單人房 3 -->
                <div class="p-3 rounded-3 border mb-3 bg-white">
                    <h6 class="fw-bold mb-3">${roomName}</h6>
                    <div class="d-flex">
                        <img src=/booking/api/image/room/${roomTypeId}/0 alt="房型圖片"
                        class="img-thumbnail me-3 rounded"
                             style="width: 100px; height: 60px; object-fit: cover;">
                        <div class="text-start">
                            <p class="mb-2"><strong>入住人數：</strong>${guestNum} 人</p>
                            <p class="mb-2"><strong>房間數：</strong>${roomNum} 間</p>
                            <p class="mb-2">
                                <strong>總價：</strong>
                                <span id="roomPrice" class="text-danger fw-bold">NT$ ${totalPrice}</span>
                            </p>
                            <p class="mb-0 text-muted ${breakfast !== 1 ? " d-none" : ""} ">
                            <strong>已含早餐價格：</strong>NT$ ${totalBreakfastPrice}
                        </p>
                    </div>
                </div>
            </div>
    `
        totalPriceAdd += (parseInt(totalPrice));
        $modal.find('.roomType').append(roomTypeHtml);
    })
    if(discount != 0 ){
        totalPriceAdd -= discount;
    }
    $modal.find('#totalPrice').text("NT$"+totalPriceAdd);
    let html = $modal.prop("outerHTML");

    showModal(html, false);
} 


function setRating(rating) {
    currentRating = rating; // 設定當前評分
    highlightStars(rating);

    // **更新顯示評分**
    document.getElementById("ratingDisplay").textContent = `${rating}`;
}

function highlightStars(rating) {
    document.querySelectorAll("#starContainer .star").forEach((star, index) => {
        star.classList.toggle("active", index < rating);
    });
}
