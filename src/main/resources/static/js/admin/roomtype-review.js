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
    displayReviews();
});

// 載入房型資料
function loadRoomData(roomData) {
    // 設定飯店名稱
    document.querySelector('.hotel-name').textContent = roomData.hotelName;
    
    // 設定房型名稱
    document.querySelector('.hotel-info h2').textContent = roomData.roomType;
    
    // 設定房型資訊
    const roomInfoText = `${roomData.capacity}｜${roomData.bedType}｜${roomData.area}｜附陽台`;
    document.querySelector('.detail-item:nth-child(1) p').textContent = roomInfoText;
    
    // 設定設施資訊
    document.querySelector('.detail-item:nth-child(2) p').textContent = 
        roomData.facilities.join('｜');
    
    // 設定描述
    document.querySelector('.detail-item:nth-child(3) p').textContent = 
        roomData.description;
    
    // 設定圖片
    const mainImage = document.getElementById('mainImage');
    mainImage.src = roomData.images[0];
    
    const thumbnails = document.querySelectorAll('.thumbnail');
    thumbnails.forEach((thumb, index) => {
        if (roomData.images[index]) {
            thumb.src = roomData.images[index];
        }
    });
}

// 模擬評論資料
//const reviews = [
//    { name: "王小明", rating: 5, content: "房間非常乾淨舒適，服務人員態度很好，值得推薦！" },
//    { name: "李小華", rating: 4, content: "整體不錯，但浴室的水壓可以再改善。" },
//    { name: "張大方", rating: 5, content: "景觀很棒，房間設施齊全，非常推薦！" },
//    { name: "陳小美", rating: 4, content: "床很舒服，但冷氣聲音有點大。" },
//    { name: "林大雄", rating: 3, content: "位置方便，但隔音效果需要改善。" },
//    { name: "黃小琳", rating: 5, content: "服務很周到，環境維護得很好！" },
//    { name: "吳小菁", rating: 4, content: "整體cp值很高，下次會再來！" },
//    { name: "趙大明", rating: 5, content: "交通便利，房間乾淨，服務好！" }
//];

// 分頁設定
let currentPage = 1;
const reviewsPerPage = 3;

// 顯示評論
function displayReviews() {
    const reviewsContainer = document.getElementById('reviewsContainer');
    reviewsContainer.innerHTML = ''; // 清空現有內容

    const startIndex = (currentPage - 1) * reviewsPerPage;
    const endIndex = startIndex + reviewsPerPage;
    const currentReviews = reviews.slice(startIndex, endIndex);

    currentReviews.forEach(review => {
        const reviewCard = document.createElement('div');
        reviewCard.classList.add('review-card');

        reviewCard.innerHTML = `
            <div class="review-header">
                <span class="review-name">${review.name}</span>
                <span class="review-stars">${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</span>
            </div>
            <p class="review-content">${review.content}</p>
        `;
        reviewsContainer.appendChild(reviewCard);
    });

    // 更新按鈕狀態
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = endIndex >= reviews.length;
}

// 圖片預覽功能
document.querySelectorAll('.thumbnail').forEach(thumb => {
    thumb.addEventListener('click', function() {
        document.getElementById('mainImage').src = this.src;
    });
});

// 審核功能
function approveRoom() {
    const comment = document.querySelector('.comment-box').value;
    if (!comment.trim()) {
        alert('請輸入審核意見');
        return;
    }
    if (confirm('確定要通過此房型審核嗎？')) {
        // 這裡可以添加API呼叫
        // 使用 Promise 模擬 API 調用
        new Promise((resolve) => {
            // 模擬 API 調用時間
            setTimeout(() => {
                alert('審核已通過！');
                resolve();
            }, 500);
        }).then(() => {
            // API 調用成功後跳轉
            window.location.href = 'review-backend.html';
        });
    }
}

function rejectRoom() {
    const comment = document.querySelector('.comment-box').value;
    if (!comment.trim()) {
        alert('請輸入駁回原因');
        return;
    }
    if (confirm('確定要駁回此房型申請嗎？')) {
        // 這裡可以添加API呼叫
        // 使用 Promise 模擬 API 調用
        new Promise((resolve) => {
            // 模擬 API 調用時間
            setTimeout(() => {
                alert('申請已駁回！');
                resolve();
            }, 500);
        }).then(() => {
            // API 調用成功後跳轉
            window.location.href = 'review-backend.html';
        });
    }
}

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

// 初始化顯示評論
document.addEventListener('DOMContentLoaded', () => {
    displayReviews();
    
    // 自動載入房型資料（模擬API呼叫）
    loadRoomData();
});