document.getElementById('contactForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    // 驗證表單
    const form = event.target;
    let isValid = true;
    
    form.querySelectorAll('[required]').forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
        }
    });
    
    if (!isValid) {
        alert('請填寫所有必填欄位！');
        return;
    }
    
    // 準備表單資料
    const formData = new FormData();
    formData.append('firstName', document.getElementById('firstName').value);
    formData.append('email', document.getElementById('email').value);
    formData.append('orderNumber', document.getElementById('orderNumber').value);
    formData.append('hotelName', document.getElementById('hotelName').value);
    formData.append('checkInDate', document.getElementById('checkInDate').value);
    formData.append('message', document.getElementById('message').value);
    
    // 處理照片上傳
    const photoInput = document.getElementById('photo');
    if (photoInput.files[0]) {
        formData.append('photo', photoInput.files[0]);
    }
    
    try {
        // 發送 API 請求
        const response = await fetch('/api/contact', {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            throw new Error('伺服器回應錯誤');
        }
        
        const result = await response.json();
        
        if (result.success) {
            alert('表單成功送出！我們將在三到五個工作天回覆');
            form.reset();
            document.getElementById('photoPreview').innerHTML = '';
        } else {
            alert(result.message || '表單提交失敗，請稍後再試');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('發生錯誤，請稍後再試');
    }
});

// 保持原有的照片預覽功能
document.getElementById('photo').addEventListener('change', function(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('photoPreview');
    preview.innerHTML = '';
    
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
});