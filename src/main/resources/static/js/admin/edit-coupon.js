// 全局變量
let isEditMode = false;
let currentCouponId = null;

// 頁面載入完成後執行
document.addEventListener('DOMContentLoaded', function() {
    // 設置日期輸入限制
    setDateConstraints();
    // 設置所有輸入框的監聽器，實時更新預覽
    setupFormListeners();

    // 檢查 URL 參數，判斷是編輯模式還是創建模式
    const urlParams = new URLSearchParams(window.location.search);
    const couponId = urlParams.get('id');
    const pageTitle = document.querySelector('.admin-banner h1');
    pageTitle.textContent = couponId ? '編輯優惠券' : '創建優惠券';
    
    if (couponId) {
        isEditMode = true;
        currentCouponId = parseInt(couponId);
        loadCouponData(currentCouponId);
        // 更改按鈕文字為"更新優惠券"
        document.querySelector('button[type="submit"]').textContent = '更新優惠券';
    } else {
        // 創建模式，設置預設值
        setDefaultValues();
    }
});

// 設置日期輸入限制
function setDateConstraints() {
    const today = new Date().toISOString().split('T')[0];
    const maxDate = new Date();
    maxDate.setFullYear(maxDate.getFullYear() + 1); // 最多允許設置一年後的日期
    
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    
    startDateInput.min = today;
    startDateInput.max = maxDate.toISOString().split('T')[0];
    endDateInput.min = today;
    endDateInput.max = maxDate.toISOString().split('T')[0];
}

// 設置表單輸入監聽
function setupFormListeners() {
    const form = document.getElementById('couponForm');
    const inputs = form.querySelectorAll('input');

    inputs.forEach(input => {
        input.addEventListener('input', updatePreview);
    });

    // 特別處理日期輸入的邏輯
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    startDateInput.addEventListener('change', function() {
        if (endDateInput.value && endDateInput.value < this.value) {
            endDateInput.value = this.value;
        }
        endDateInput.min = this.value;
        updatePreview();
    });

    endDateInput.addEventListener('change', function() {
        if (startDateInput.value && this.value < startDateInput.value) {
            this.value = startDateInput.value;
        }
        updatePreview();
    });
}

// 設置預設值
function setDefaultValues() {
    const today = new Date().toISOString().split('T')[0];
    const defaultEndDate = new Date();
    defaultEndDate.setMonth(defaultEndDate.getMonth() + 1); // 預設有效期為一個月

    document.getElementById('startDate').value = today;
    document.getElementById('endDate').value = defaultEndDate.toISOString().split('T')[0];
    document.getElementById('amount').value = '200';
    document.getElementById('minSpend').value = '1000';
    document.getElementById('couponName').value = '新優惠券';
    
    // 更新預覽
    updatePreview();
}

// 載入優惠券數據
function loadCouponData(id) {
    // 在實際應用中，這裡應該調用 API 獲取優惠券數據
    // 這裡使用模擬數據
    const coupon = coupons.find(c => c.id === id);
    
    if (coupon) {
        document.getElementById('couponName').value = coupon.name;
        document.getElementById('amount').value = coupon.amount || 200;
        document.getElementById('startDate').value = coupon.startDate || formatDateForInput(new Date());
        document.getElementById('endDate').value = coupon.validUntil;
        document.getElementById('minSpend').value = coupon.minSpend;
        
        // 更新預覽
        updatePreview();
    } else {
        showMessage('找不到指定的優惠券', 'error');
        setTimeout(() => {
            window.location.href = 'coupon-management.html';
        }, 2000);
    }
}

// 更新預覽
function updatePreview() {
    const name = document.getElementById('couponName').value || '優惠券名稱';
    const amount = document.getElementById('amount').value || '0';
    const startDate = formatDate(document.getElementById('startDate').value);
    const endDate = formatDate(document.getElementById('endDate').value);
    const minSpend = document.getElementById('minSpend').value || '0';

    document.getElementById('previewName').textContent = name;
    document.getElementById('previewAmount').textContent = amount;
    document.getElementById('previewStartDate').textContent = startDate;
    document.getElementById('previewEndDate').textContent = endDate;
    document.getElementById('previewMinSpend').textContent = minSpend;
}

// 提交表單
async function submitCoupon(event) {
    event.preventDefault();
    
    // 收集表單數據
    const formData = {
        name: document.getElementById('couponName').value,
        amount: parseInt(document.getElementById('amount').value),
        startDate: document.getElementById('startDate').value,
        validUntil: document.getElementById('endDate').value,
        minSpend: parseInt(document.getElementById('minSpend').value)
    };

    // 驗證數據
    if (!validateCouponData(formData)) {
        return;
    }

    try {
        if (isEditMode) {
            // 更新現有優惠券
            // 在實際應用中，這裡應該調用 API
            const index = coupons.findIndex(c => c.id === currentCouponId);
            if (index !== -1) {
                coupons[index] = { ...coupons[index], ...formData };
            }
        } else {
            // 創建新優惠券
            // 在實際應用中，這裡應該調用 API
            const newCoupon = {
                id: coupons.length + 1,
                ...formData,
                redemptionCount: 0
            };
            coupons.push(newCoupon);
        }

        showMessage('優惠券已成功保存', 'success');
        setTimeout(() => {
            window.location.href = 'coupon-management.html';
        }, 1500);
    } catch (error) {
        console.error('保存優惠券時發生錯誤:', error);
        showMessage('保存失敗，請重試', 'error');
    }
}

// 驗證優惠券數據
function validateCouponData(data) {
    // 驗證金額
    if (data.amount <= 0) {
        showMessage('優惠金額必須大於 0', 'error');
        return false;
    }

    // 驗證最低消費金額
    if (data.minSpend <= 0) {
        showMessage('最低消費金額必須大於 0', 'error');
        return false;
    }

    // 驗證優惠金額不能大於最低消費金額
    if (data.amount >= data.minSpend) {
        showMessage('優惠金額不能大於或等於最低消費金額', 'error');
        return false;
    }

    // 驗證日期
    const startDate = new Date(data.startDate);
    const endDate = new Date(data.validUntil);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (startDate < today) {
        showMessage('開始日期不能早於今天', 'error');
        return false;
    }

    if (endDate < startDate) {
        showMessage('結束日期不能早於開始日期', 'error');
        return false;
    }

    return true;
}

// 格式化日期顯示
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-TW');
}

// 格式化日期為 input[type="date"] 格式
function formatDateForInput(date) {
    return date.toISOString().split('T')[0];
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

// 模擬優惠券數據（實際應用中應該從服務器獲取）
const coupons = [
    {
        id: 1,
        name: "新年特惠券",
        amount: 200,
        description: "新年期間全館商品折扣",
        redemptionCount: 150,
        minSpend: 1000,
        startDate: "2024-01-01",
        validUntil: "2024-12-31"
    },
    {
        id: 2,
        name: "生日優惠券",
        amount: 100,
        description: "會員生日專屬優惠",
        redemptionCount: 80,
        minSpend: 500,
        startDate: "2024-01-01",
        validUntil: "2024-12-31"
    }
];