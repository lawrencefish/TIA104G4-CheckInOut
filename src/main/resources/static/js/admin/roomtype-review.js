document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 從 URL 獲取房型 ID
        const pathSegments = window.location.pathname.split('/');
        const roomTypeId = pathSegments[pathSegments.length - 1];

        if (!roomTypeId) {
            console.error('找不到房型 ID');
            return;
        }

        console.log('正在獲取房型 ID:', roomTypeId);
        await loadRoomData(roomTypeId);
        initializeEventListeners();

    } catch (error) {
        console.error('初始化頁面時發生錯誤:', error);
    }
});

// 更新房型基本資訊的函數
function updateRoomInfo(roomData) {
    // 更新飯店名稱
    const hotelNameEl = document.querySelector('.hotel-name');
    if (hotelNameEl) {
        hotelNameEl.textContent = roomData.hotel?.name || '未知飯店';
    }

    // 更新房型名稱
    const roomNameEl = document.querySelector('.room-header h2');
    if (roomNameEl) {
        roomNameEl.textContent = roomData.roomName || '未知房型';
    }

    // 更新可容納人數和早餐資訊
    const roomInfoEl = document.querySelector('.room-details .detail-item span');
    if (roomInfoEl) {
        let infoText = `${roomData.maxPerson || 0}人`;
        infoText += roomData.breakfast === 1 ? '｜附早餐' : '｜不附早餐';
        roomInfoEl.textContent = infoText;
    }

    // 更新房型描述
    const descriptionEl = document.querySelector('.detail-item:last-child p');
    if (descriptionEl) {
        descriptionEl.textContent = roomData.description || '暫無描述';
    }
}

// 載入房型資料的主要函數
async function loadRoomData(roomTypeId) {
    try {
//        const response = await fetch(`/adminRoomType/detail/${roomTypeId}`);
//        if (!response.ok) {
//            throw new Error(`HTTP 錯誤！狀態碼：${response.status}`);
//        }
//
//        const roomData = await response.json();
//        console.log('獲取到的房型資料:', roomData);
//		
//		// 檢查飯店資料
//        if (!roomData.hotel || !roomData.hotel.name) {
//            console.warn('找不到飯店資料:', roomData);
//            // 不更新飯店名稱，保留原本伺服器端渲染的值
//            // 或者從其他地方取得飯店資料
//            const hotelNameEl = document.querySelector('.hotel-name');
//            if (hotelNameEl && hotelNameEl.textContent && hotelNameEl.textContent !== '未知飯店') {
//                // 保留現有的飯店名稱
//                roomData.hotel = {
//                    name: hotelNameEl.textContent
//                };
//            }
//        }
//        
//        updateRoomInfo(roomData);
        
        // 初始化圖片切換功能
        initializeImageSwitching();
        
//        if (roomData.roomTypeFacilities && roomData.roomTypeFacilities.length > 0) {
//            loadFacilities(roomData.roomTypeFacilities);
//        }
    } catch (error) {
        console.error('載入房型資料時發生錯誤:', error);
    }
}

// 初始化圖片切換功能
function initializeImageSwitching() {
    const thumbnails = document.querySelectorAll('.thumbnail');
    const mainImage = document.getElementById('mainImage');

    if (!mainImage) {
        console.error('找不到主圖片元素');
        return;
    }

    // 設置第一張縮圖為選中狀態
    if (thumbnails.length > 0) {
        thumbnails[0].classList.add('selected');
    }

    // 為每個縮圖添加點擊事件
    thumbnails.forEach(thumbnail => {
        thumbnail.addEventListener('click', function() {
            // 更新主圖
            mainImage.src = this.src;
            
            // 更新縮圖選中狀態
            thumbnails.forEach(thumb => thumb.classList.remove('selected'));
            this.classList.add('selected');
        });
    });
}

// 初始化事件監聽器
function initializeEventListeners() {
    // 通過按鈕
    const approveBtn = document.createElement('button');
    approveBtn.className = 'btn-approve';
    approveBtn.textContent = '通過';
    approveBtn.onclick = () => handleApprove();

    // 駁回按鈕
    const rejectBtn = document.createElement('button');
    rejectBtn.className = 'btn-reject';
    rejectBtn.textContent = '駁回';
    rejectBtn.onclick = () => handleReject();

    // 加入按鈕到操作區域
    const actionButtons = document.querySelector('.action-buttons');
    if (actionButtons) {
        actionButtons.innerHTML = ''; // 清除現有按鈕
        actionButtons.appendChild(approveBtn);
        actionButtons.appendChild(rejectBtn);
    }
}

// 處理通過審核
async function handleApprove() {
    if (confirm('確定要通過此房型審核嗎？')) {
        await updateRoomStatus(1);
    }
}

// 處理駁回審核
async function handleReject() {
    // 創建彈窗
    const dialog = document.createElement('div');
    dialog.className = 'reject-dialog';
    dialog.innerHTML = `
        <div class="dialog-content">
            <h3>駁回原因</h3>
            <textarea id="rejectReason" placeholder="請輸入駁回原因..."></textarea>
            <div class="dialog-buttons">
                <button onclick="submitReject()" class="btn-confirm">確認</button>
                <button onclick="closeDialog()" class="btn-cancel">取消</button>
            </div>
        </div>
    `;
    
    document.body.appendChild(dialog);
    
    // 添加關閉彈窗的功能
    window.closeDialog = function() {
        document.body.removeChild(dialog);
    };
    
    // 添加提交駁回的功能
    window.submitReject = async function() {
        const reason = document.getElementById('rejectReason').value;
        if (!reason.trim()) {
            alert('請輸入駁回原因');
            return;
        }
        await updateRoomStatus(2, reason);
        closeDialog();
    };
}

// 更新房型狀態
async function updateRoomStatus(status, comment = '') {
    try {
        const pathSegments = window.location.pathname.split('/');
        const roomTypeId = pathSegments[pathSegments.length - 1];
        
        // 建立 URL 參數
        const params = new URLSearchParams({
            status: status.toString()
        });
        
        const response = await fetch(`/adminRoomType/${roomTypeId}/review?${params.toString()}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                reviewComment: comment
            })
        });

        if (!response.ok) {
            throw new Error('審核操作失敗');
        }

        alert(status === 1 ? '審核已通過！' : '已駁回此房型！');
        window.location.href = '/admin/reviewBackend';
        
    } catch (error) {
        console.error('審核過程發生錯誤:', error);
        alert('審核失敗，請稍後再試');
    }
}