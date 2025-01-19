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
    // 設置搜尋框事件監聽
    setupSearchListener();
}

// 設置搜尋框事件監聽
function setupSearchListener() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                const form = searchInput.closest('form');
                if (form) {
                    form.submit();
                }
            }
        });
    }
}

// 刪除優惠券
async function deleteCoupon(id) {
    if (!confirm('確定要刪除這個優惠券嗎？')) {
        return;
    }

    try {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `/admin/coupon/delete/${id}`;

        const response = await fetch(`/admin/coupon/delete/${id}`, {
            method: 'POST',
        });

        if (response.ok) {
            showMessage('刪除成功', 'success');
            // 延遲一下再重新載入頁面，讓使用者能看到成功訊息
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        } else {
            throw new Error('刪除失敗');
        }
    } catch (error) {
        console.error('刪除優惠券失敗:', error);
        showMessage('刪除失敗，請重試', 'error');
    }
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

// 格式化貨幣顯示
function formatCurrency(amount) {
    return `NT$ ${Number(amount).toLocaleString()}`;
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