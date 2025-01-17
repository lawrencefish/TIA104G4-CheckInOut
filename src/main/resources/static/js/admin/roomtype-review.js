document.addEventListener('DOMContentLoaded', async () => {
    // 獲取 URL 中的 roomTypeId
    const pathSegments = window.location.pathname.split('/');
    const roomTypeId = pathSegments[pathSegments.length - 1]; // 從路徑中取得ID

    if (roomTypeId) {
        try {
            const response = await fetch(`/adminRoomType/detail/${roomTypeId}`);
            if (!response.ok) {
                throw new Error(`HTTP錯誤 ! 狀態碼: ${response.status}`);
            }
            const roomData = await response.json();
            loadRoomData(roomData);
        } catch (error) {
            console.error('取得房型資料時發生錯誤:', error);
        }
    }

    // 初始化顯示評論
//    displayReviews();
	
	// 	初始化其他功能
    initializeEventListeners();
});

// 初始化事件監聽器
function initializeEventListeners() {
    // 圖片預覽功能
    document.querySelectorAll('.thumbnail').forEach(thumb => {
        thumb.addEventListener('click', function() {
            document.getElementById('mainImage').src = this.src;
        });
    });

    // 評論換頁功能
    document.getElementById('prevPage').addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            displayReviews();
        }
    });

    document.getElementById('nextPage').addEventListener('click', () => {
        if (currentPage * reviewsPerPage < reviews.length) {
            currentPage++;
            displayReviews();
        }
    });
}

// 審核功能也需要更新為使用實際的 API
async function approveRoom() {
    const comment = document.querySelector('.comment-box').value;
    if (!comment.trim()) {
        alert('請輸入審核意見');
        return;
    }
    
    if (confirm('確定要通過此房型審核嗎？')) {
        try {
            const pathSegments = window.location.pathname.split('/');
            const roomTypeId = pathSegments[pathSegments.length - 1];
            
            const response = await fetch(`/adminRoomType/review/${roomTypeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    status: 1, // 通過
                    comment: comment
                })
            });

            if (!response.ok) {
                throw new Error('審核失敗');
            }

            alert('審核已通過！');
            window.location.href = '/adminRoomType/list';
            
        } catch (error) {
            console.error('審核過程發生錯誤:', error);
            alert('審核失敗，請稍後再試');
        }
    }
}

// 載入房型資料
function loadRoomData(roomData) {
    // 設定飯店名稱
//    document.querySelector('.hotel-name').textContent = roomData.hotelName;
    
    // 設定房型名稱
//    document.querySelector('.hotel-info h2').textContent = roomData.roomType;
    
    // 設定房型資訊
//    const roomInfoText = `${roomData.maxPerson}｜ 附陽台`;
//    document.querySelector('.detail-item:nth-child(1) p').textContent = roomInfoText;
    
    // 設定設施資訊
    document.querySelector('.detail-item:nth-child(2) p').textContent = 
        roomData.facilities.join('｜');
    
    // 設定描述
//    document.querySelector('.detail-item:nth-child(3) p').textContent = 
//        roomData.description;
    
    // 設定圖片
//    const mainImage = document.getElementById('mainImage');
//    mainImage.src = roomData.images[0];
//    
//    const thumbnails = document.querySelectorAll('.thumbnail');
//    thumbnails.forEach((thumb, index) => {
//        if (roomData.images[index]) {
//            thumb.src = roomData.images[index];
//        }
//    });
}


// 分頁設定
let currentPage = 1;
const reviewsPerPage = 3;

// 顯示評論
//function displayReviews() {
//    const reviewsContainer = document.getElementById('reviewsContainer');
//    reviewsContainer.innerHTML = ''; // 清空現有內容
//
//    const startIndex = (currentPage - 1) * reviewsPerPage;
//    const endIndex = startIndex + reviewsPerPage;
//    const currentReviews = reviews.slice(startIndex, endIndex);
//
//    currentReviews.forEach(review => {
//        const reviewCard = document.createElement('div');
//        reviewCard.classList.add('review-card');
//
//        reviewCard.innerHTML = `
//            <div class="review-header">
//                <span class="review-name">${review.name}</span>
//                <span class="review-stars">${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</span>
//            </div>
//            <p class="review-content">${review.content}</p>
//        `;
//        reviewsContainer.appendChild(reviewCard);
//    });

    // 更新按鈕狀態
//    document.getElementById('prevPage').disabled = currentPage === 1;
//    document.getElementById('nextPage').disabled = endIndex >= reviews.length;
//}




// 評論換頁功能
document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage--;
        displayReviews();
    }
});

document.getElementById('nextPage').addEventListener('click', () => {
    if (currentPage * reviewsPerPage < reviews.length) {
        currentPage++;
        displayReviews();
    }
    // 自動載入房型資料（模擬API呼叫）
    loadRoomData();
});

