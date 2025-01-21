// 頁面載入完成後執行
document.addEventListener('DOMContentLoaded', function() {
    console.log('頁面載入完成');
    setupDateConstraints();
    setupFormListeners();
    setupFormValidation();
	setupErrorClearance();
});

// 設置日期限制
function setupDateConstraints() {
    const activeDate = document.getElementById('activeDate');
    const expiryDate = document.getElementById('expiryDate');
    
    if (!activeDate || !expiryDate) {
        console.error('找不到日期輸入欄位');
        return;
    }
    
    try {
        // 設置最小日期為今天
        const today = new Date();
        const todayStr = today.toISOString().split('T')[0];
        
        // 設置預設值
        if (!activeDate.value) {
            activeDate.value = todayStr;
        }
        
        if (!expiryDate.value) {
            const defaultEndDate = new Date(today);
            defaultEndDate.setMonth(today.getMonth() + 1);
            expiryDate.value = defaultEndDate.toISOString().split('T')[0];
        }
        
        // 設置最小日期限制
        activeDate.min = todayStr;
        expiryDate.min = activeDate.value;
        
        console.log('日期設置完成:', {
            activeDate: activeDate.value,
            expiryDate: expiryDate.value
        });
    } catch (error) {
        console.error('設置日期限制時發生錯誤：', error);
    }
}

// 設置表單監聽器
function setupFormListeners() {
    const form = document.querySelector('form');
    if (!form) {
        console.error('找不到表單');
        return;
    }

    // 監聽所有輸入欄位的變化
    const inputs = form.querySelectorAll('input, textarea');
    inputs.forEach(input => {
        input.addEventListener('input', updatePreview);
    });

    // 特別處理日期輸入
    const activeDate = document.getElementById('activeDate');
    const expiryDate = document.getElementById('expiryDate');

    if (activeDate && expiryDate) {
        activeDate.addEventListener('change', function() {
            if (expiryDate.value && expiryDate.value < this.value) {
                expiryDate.value = this.value;
            }
            expiryDate.min = this.value;
            updatePreview();
        });

        expiryDate.addEventListener('change', updatePreview);
    }
}

// 設置表單驗證和提交
function setupFormValidation() {
    const form = document.querySelector('form');
    if (!form) {
        console.error('找不到表單');
        return;
    }

    form.addEventListener('submit', async function(event) {
        event.preventDefault();
        
        // 清除所有錯誤訊息
        clearAllErrors();
        
        // 前端驗證
        const isValid = validateForm();
        console.log('表單驗證結果：', isValid);
        
        if (!isValid) {
            console.log('表單驗證失敗');
            return;
        }

	 try {
	        const formData = new FormData(form);
	        
	        // 格式化日期
	        const activeDate = document.getElementById('activeDate');
	        const expiryDate = document.getElementById('expiryDate');
	        
	        // 確保日期格式為 ISO 格式 (YYYY-MM-DD)
	        if (activeDate.value) {
	            const date = new Date(activeDate.value);
	            formData.set('activeDate', date.toISOString().split('T')[0]);
	        }
	        
	        if (expiryDate.value) {
	            const date = new Date(expiryDate.value);
	            formData.set('expiryDate', date.toISOString().split('T')[0]);
	        }
	        
	        // 輸出要提交的數據
	        console.log('準備提交的數據：');
	        for (let [key, value] of formData.entries()) {
	            console.log(`${key}: ${value}`);
	        }

	        const response = await fetch(form.action, {
	            method: 'POST',
	            body: formData
	        });

	        const data = await response.json();
	        
	        if (response.ok) {
	            showMessage('保存成功', 'success');
	            setTimeout(() => {
	                window.location.href = '/admin/coupon';
	            }, 1000);
	        } else {
	            handleSaveErrors(data);
	        }
	    } catch (error) {
	        console.error('保存失敗:', error);
	        showMessage('保存失敗，請重試', 'error');
	    }
	});
}

// 處理保存錯誤
function handleSaveErrors(data) {
    if (Array.isArray(data)) {
        // 處理驗證錯誤
        data.forEach(error => {
            showFieldError(error.field, error.message);
        });
    } else if (data.message) {
        // 處理業務邏輯錯誤
        if (data.message.includes('到期日期')) {
            showFieldError('expiryDate', data.message);
        } else if (data.message.includes('折扣金額')) {
            showFieldError('discountAmount', data.message);
        } else if (data.message.includes('最低消費金額')) {
            showFieldError('minSpend', data.message);
        } else {
            showMessage(data.message, 'error');
        }
    }
}

// 更新預覽
function updatePreview() {
    const namePreview = document.getElementById('previewName');
    const amountPreview = document.getElementById('previewAmount');
    const startDatePreview = document.getElementById('previewStartDate');
    const endDatePreview = document.getElementById('previewEndDate');
    const minSpendPreview = document.getElementById('previewMinSpend');

    if (namePreview) {
        namePreview.textContent = document.getElementById('couponName').value || '優惠券名稱';
    }
    if (amountPreview) {
        amountPreview.textContent = document.getElementById('discountAmount').value || '0';
    }
    if (startDatePreview) {
        startDatePreview.textContent = formatDate(document.getElementById('activeDate').value);
    }
    if (endDatePreview) {
        endDatePreview.textContent = formatDate(document.getElementById('expiryDate').value);
    }
    if (minSpendPreview) {
        minSpendPreview.textContent = document.getElementById('minSpend').value || '0';
    }
}

// 驗證表單
function validateForm() {
    let isValid = true;
    
    // 清除所有錯誤提示
    clearAllErrors();
    
    // 驗證優惠金額
    const discountAmount = document.getElementById('discountAmount');
    const minSpend = document.getElementById('minSpend');
    
    // 添加 debug 日誌
    console.log('驗證數值：', {
        discountAmount: discountAmount.value,
        minSpend: minSpend.value
    });
    
    if (!discountAmount.value || parseInt(discountAmount.value) <= 0) {
        showFieldError('discountAmount', '優惠金額必須大於 0');
        isValid = false;
    }

    if (!minSpend.value || parseInt(minSpend.value) <= 0) {
        showFieldError('minSpend', '最低消費金額必須大於 0');
        isValid = false;
    }

    // 驗證優惠金額與最低消費金額的關係
    if (parseInt(discountAmount.value) >= parseInt(minSpend.value)) {
        showFieldError('discountAmount', '優惠金額不能大於或等於最低消費金額');
        isValid = false;
    }

    // 驗證日期
    const activeDate = document.getElementById('activeDate');
    const expiryDate = document.getElementById('expiryDate');
    
    // 添加 debug 日誌
    console.log('驗證日期：', {
        activeDate: activeDate.value,
        expiryDate: expiryDate.value
    });

    if (!activeDate.value) {
        showFieldError('activeDate', '請選擇生效日期');
        isValid = false;
    }
    
    if (!expiryDate.value) {
        showFieldError('expiryDate', '請選擇到期日期');
        isValid = false;
    }
    
    if (activeDate.value && expiryDate.value) {
        const activeDateObj = new Date(activeDate.value);
        const expiryDateObj = new Date(expiryDate.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        
        console.log('日期比較：', {
            today: today,
            activeDate: activeDateObj,
            expiryDate: expiryDateObj
        });

        if (activeDateObj < today) {
            showFieldError('activeDate', '生效日期不能早於今天');
            isValid = false;
        }

        if (expiryDateObj < activeDateObj) {
            showFieldError('expiryDate', '到期日期不能早於生效日期');
            isValid = false;
        }
    }

    console.log('驗證結果：', isValid);
    return isValid;
}

// 格式化日期顯示
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-TW');
}

// 顯示消息提示
function showMessage(message, type = 'info') {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.textContent = message;
    document.body.appendChild(messageDiv);
    setTimeout(() => messageDiv.remove(), 3000);
}

// 顯示欄位錯誤訊息
function showFieldError(fieldId, message) {
    const field = document.getElementById(fieldId);
    if (!field) return;
    
    field.classList.add('is-invalid');
    
    let errorDiv = field.nextElementSibling;
    if (!errorDiv || !errorDiv.classList.contains('error-message')) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message text-danger';
        field.parentNode.insertBefore(errorDiv, field.nextSibling);
    }
    errorDiv.textContent = message;
}

// 清除所有錯誤提示
function clearAllErrors() {
    document.querySelectorAll('.is-invalid').forEach(field => {
        field.classList.remove('is-invalid');
    });
    document.querySelectorAll('.error-message').forEach(errorDiv => {
        errorDiv.remove();
    });
}

// 為每個輸入欄位添加監聽器，當輸入時清除該欄位的錯誤提示
function setupErrorClearance() {
    document.querySelectorAll('input, textarea').forEach(field => {
        field.addEventListener('input', () => {
            field.classList.remove('is-invalid');
            const errorDiv = field.nextElementSibling;
            if (errorDiv && errorDiv.classList.contains('error-message')) {
                errorDiv.remove();
            }
        });
    });
}