// 創建優惠券
function createCoupon() {
    // 跳轉到創建頁面，不帶 id 參數
    window.location.href = 'edit-coupon.html';
}

// 編輯優惠券
function editCoupon(id) {
    // 跳轉到編輯頁面，帶 id 參數
    window.location.href = `edit-coupon.html?id=${id}`;
}

// 模擬優惠券數據
let coupons = [
    {
        id: 1,
        name: "新年特惠券",
        description: "新年期間全館商品折扣",
        redemptionCount: 150,
        minSpend: 1000,
        validUntil: "2024-12-31"
    },
    {
        id: 2,
        name: "生日優惠券",
        description: "會員生日專屬優惠",
        redemptionCount: 80,
        minSpend: 500,
        validUntil: "2024-12-31"
    },
    // 可以添加更多測試數據
];

// 分頁設置
const ITEMS_PER_PAGE = 10;
let currentPage = 1;
let filteredCoupons = [...coupons];

// 頁面加載完成後執行
document.addEventListener('DOMContentLoaded',async function() {
    try {
        // 初始化頁面
        initializePage();
    } catch (error) {
        console.error('Error loading components:', error);
    }
});

// 初始化頁面
function initializePage() {
    // 顯示優惠券列表
    displayCoupons();
    // 設置搜尋框事件監聽
    setupSearchListener();
}


// 設置搜尋框事件監聽
function setupSearchListener() {
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            searchCoupons();
        }
    });
}

// 顯示優惠券列表
function displayCoupons() {
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    const pageData = filteredCoupons.slice(startIndex, endIndex);

    const tableBody = document.getElementById('couponTableBody');
    tableBody.innerHTML = '';

    pageData.forEach(coupon => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${escapeHtml(coupon.name)}</td>
            <td>${escapeHtml(coupon.description)}</td>
            <td>${coupon.redemptionCount}</td>
            <td>${formatCurrency(coupon.minSpend)}</td>
            <td>${formatDate(coupon.validUntil)}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-edit" onclick="editCoupon(${coupon.id})">編輯</button>
                    <button class="btn btn-delete" onclick="deleteCoupon(${coupon.id})">刪除</button>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });

    // 更新分頁
    updatePagination();
}

// 更新分頁控制
function updatePagination() {
    const totalPages = Math.ceil(filteredCoupons.length / ITEMS_PER_PAGE);
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    // 上一頁按鈕
    const prevButton = document.createElement('button');
    prevButton.textContent = '上一頁';
    prevButton.className = 'btn';
    prevButton.disabled = currentPage === 1;
    prevButton.onclick = () => changePage(currentPage - 1);
    pagination.appendChild(prevButton);

    // 頁碼按鈕
    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.className = `btn ${i === currentPage ? 'active' : ''}`;
        pageButton.onclick = () => changePage(i);
        pagination.appendChild(pageButton);
    }

    // 下一頁按鈕
    const nextButton = document.createElement('button');
    nextButton.textContent = '下一頁';
    nextButton.className = 'btn';
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = () => changePage(currentPage + 1);
    pagination.appendChild(nextButton);
}

// 切換頁面
function changePage(page) {
    currentPage = page;
    displayCoupons();
}

// 搜尋優惠券
function searchCoupons() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    
    filteredCoupons = coupons.filter(coupon => 
        coupon.name.toLowerCase().includes(searchTerm) ||
        coupon.description.toLowerCase().includes(searchTerm)
    );

    currentPage = 1;
    displayCoupons();
}

// 編輯優惠券
function editCoupon(id) {
    // 跳轉到編輯頁面
    window.location.href = `edit-coupon.html?id=${id}`;
}

// 刪除優惠券
function deleteCoupon(id) {
    if (!confirm('確定要刪除這個優惠券嗎？')) {
        return;
    }

    try {
        // 在實際應用中，這裡應該調用 API
        coupons = coupons.filter(coupon => coupon.id !== id);
        filteredCoupons = filteredCoupons.filter(coupon => coupon.id !== id);
        
        // 如果當前頁沒有數據了，回到上一頁
        const totalPages = Math.ceil(filteredCoupons.length / ITEMS_PER_PAGE);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }

        displayCoupons();
        showMessage('優惠券已成功刪除', 'success');
    } catch (error) {
        console.error('刪除優惠券失敗:', error);
        showMessage('刪除失敗，請重試', 'error');
    }
}

// 批量刪除選中的優惠券
function deleteSelected() {
    const selectedRows = document.querySelectorAll('#couponTable tbody tr.selected');
    if (selectedRows.length === 0) {
        showMessage('請先選擇要刪除的優惠券', 'warning');
        return;
    }

    if (!confirm(`確定要刪除選中的 ${selectedRows.length} 個優惠券嗎？`)) {
        return;
    }

    try {
        // 在實際應用中，這裡應該調用 API
        selectedRows.forEach(row => {
            const id = parseInt(row.dataset.id);
            coupons = coupons.filter(coupon => coupon.id !== id);
            filteredCoupons = filteredCoupons.filter(coupon => coupon.id !== id);
        });

        displayCoupons();
        showMessage('選中的優惠券已成功刪除', 'success');
    } catch (error) {
        console.error('批量刪除失敗:', error);
        showMessage('刪除失敗，請重試', 'error');
    }
}

// 格式化貨幣顯示
function formatCurrency(amount) {
    return `NT$ ${amount.toLocaleString()}`;
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-TW');
}

// HTML 轉義函數，防止 XSS 攻擊
function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// 顯示消息提示
function showMessage(message, type = 'info') {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.textContent = message;

    document.body.appendChild(messageDiv);

    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}


