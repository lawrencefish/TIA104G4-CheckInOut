document.addEventListener('DOMContentLoaded', async () => {
    // 初始化載入訂單資料
    try {
        await loadOrders();
    } catch (error) {
        console.error('載入訂單資料失敗:', error);
		showErrorMessage('載入訂單時發生錯誤，請稍後再試');
    }
});

// 載入訂單資料
async function loadOrders() {
	// 顯示載入中狀態
	showLoading();
    
	try {
        // API 呼叫獲取訂單資料
        const response = await fetch('/api/member/orders', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
                // 如果需要認證，可以加入 Authorization header
                // 'Authorization': `Bearer ${getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const orders = await response.json();
        updateOrdersList(orders);
    } catch (error) {
        console.error('獲取訂單資料失敗:', error);
        throw error;
	} finally {
        // 隱藏載入中狀態
        hideLoading();
    }
}

// 載入訂單列表
function updateOrdersList(orders) {
    const ordersContainer = document.getElementById('ordersList');
    const totalOrdersElement = document.getElementById('totalOrders');
	
	// 更新總訂單數
    totalOrdersElement.textContent = orders.length;
    
    // 清空現有內容
    ordersContainer.innerHTML = '';

    if (orders.length === 0) {
		showNoOrdersMessage(ordersContainer);
        return;
    }

    orders.forEach(order => {
        const orderElement = createOrderElement(order);
        ordersContainer.appendChild(orderElement);
    });
}

// 創建訂單元素
function createOrderElement(order) {
    const orderContainer = document.createElement('div');
    orderContainer.className = 'order-container';
	// 使用template literal建立訂單內容
    orderContainer.innerHTML = `
        <div class="order-image-container">
            <img src="${order.hotelImage}" alt="${order.hotelName}" class="order-image">
        </div>
        <div class="order-details">
            <h4 class="hotel-name">${order.hotelName}</h4>
            <div class="order-info">
                <div class="info-row">
                    <span class="label">入住日期：</span>
                    <span class="value">${formatDate(order.checkIn)}</span>
                </div>
                <div class="info-row">
                    <span class="label">退房日期：</span>
                    <span class="value">${formatDate(order.checkOut)}</span>
                </div>
                <div class="info-row">
                    <span class="label">入住人數：</span>
                    <span class="value">${order.guests}人</span>
                </div>
                <div class="order-price">
                    <span class="label">總金額：</span>
                    <span class="value">NT$ ${formatPrice(order.price)}</span>
                </div>
            </div>
        </div>
    `;

    // 添加點擊事件導向訂單詳情頁
    orderContainer.addEventListener('click', () => {
        window.location.href = `/order-details/${order.id}`;
    });

    return orderContainer;
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
}

// 格式化價格
function formatPrice(price) {
    return price.toLocaleString('zh-TW');
}

// 顯示無訂單訊息
function showNoOrdersMessage(container) {
    const messageElement = document.createElement('div');
    messageElement.className = 'no-orders-message';
    messageElement.textContent = '目前沒有訂單記錄';
    container.appendChild(messageElement);
}

// 顯示載入中狀態
function showLoading() {
    const loadingElement = document.createElement('div');
    loadingElement.className = 'loading';
    loadingElement.textContent = '載入中...';
    document.querySelector('.orders').appendChild(loadingElement);
}

// 隱藏載入中狀態
function hideLoading() {
    const loadingElement = document.querySelector('.loading');
    if (loadingElement) {
        loadingElement.remove();
    }
}

// 顯示錯誤訊息
function showErrorMessage(message) {
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    document.querySelector('.orders').appendChild(errorElement);
}