document.addEventListener('DOMContentLoaded', () => {
  initializeButtons();
  initializeModals();
  loadHotelData();
});

async function loadHotelData() {
    try {
        // 從 URL 獲取飯店 ID
        const urlParams = new URLSearchParams(window.location.search);
        const hotelId = urlParams.get('hotelId');
        
        if (!hotelId) {
            console.error('No hotel ID provided');
            return;
        }

        // 獲取飯店資料
        const response = await fetch(`/adminHotel/industryReview/${hotelId}`);
        if (!response.ok) throw new Error('Failed to fetch hotel data');
        
        const hotelData = await response.json();
        
        // 更新頁面顯示
        document.getElementById('company-name').querySelector('.content').textContent = hotelData.companyName;
        document.getElementById('company-id').querySelector('.content').textContent = hotelData.taxId;
        document.getElementById('city').querySelector('.content').textContent = hotelData.city;
        document.getElementById('area').querySelector('.content').textContent = hotelData.district;
        document.getElementById('address').querySelector('.content').textContent = hotelData.address;
        document.getElementById('phone').querySelector('.content').textContent = hotelData.phoneNumber;
        document.getElementById('email').querySelector('.content').textContent = hotelData.email;
        
        // 更新 Google Maps
//        if (hotelData.latitude && hotelData.longitude) {
//            updateGoogleMap(hotelData.latitude, hotelData.longitude);
//        }

        // 載入證件圖片
        loadDocumentImages(hotelId);
        
    } catch (error) {
        console.error('Error loading hotel data:', error);
        alert('載入飯店資料失敗');
    }
}

async function loadDocumentImages(hotelId) {
	try {
        // 確保元素存在
        const image1 = document.getElementById('image1');
        const image2 = document.getElementById('image2');
        const image3 = document.getElementById('image3');

        if (!image1 || !image2 || !image3) {
            console.error('找不到圖片元素');
            return;
        }

        // 使用正確的 API 路徑
        image1.src = `/adminHotel/industry/documents/${hotelId}/idFront`;
        image2.src = `/adminHotel/industry/documents/${hotelId}/idBack`;
        image3.src = `/adminHotel/industry/documents/${hotelId}/license`;

        // 添加錯誤處理
        [image1, image2, image3].forEach(img => {
            img.onerror = function() {
                console.error(`圖片載入失敗: ${this.src}`);
                this.src = '/path/to/default/image.jpg'; // 設置預設圖片
            };
        });

    } catch (error) {
        console.error('載入證件圖片時發生錯誤:', error);
    }
}

function updateGoogleMap(lat, lng) {
	try {
        const mapContainer = document.querySelector('.map-container');
        if (!mapContainer) {
            console.warn('Map container not found');
            return;
        }

        let iframe = mapContainer.querySelector('iframe');
        
        // 如果 iframe 不存在，創建一個新的
        if (!iframe) {
            iframe = document.createElement('iframe');
            iframe.className = 'map-iframe';
            iframe.style.width = '100%';
            iframe.style.height = '300px';
            iframe.style.border = 'none';
            mapContainer.appendChild(iframe);
        }

        const mapUrl = `https://www.google.com/maps/embed/v1/place?key=AIzaSyBts9LvRN8v8N2T2eQVwekbmVOHDiBvuZE&q=${lat},${lng}`;
        iframe.src = mapUrl;
    } catch (error) {
        console.error('Error updating Google Map:', error);
    }
}

async function handlePass() {
    if (confirm('確定要通過此業者的申請嗎？')) {
        const urlParams = new URLSearchParams(window.location.search);
        const hotelId = urlParams.get('hotelId');
        
		if (!hotelId) {
            console.error('找不到飯店ID');
            alert('處理失敗：找不到飯店ID');
            return;
        }
		
        try {
			// 發送審核通過請求到後端
            const response = await fetch(`/adminHotel/industry/review/${hotelId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `status=1`
            });
            
            if (!response.ok) throw new Error('Failed to approve hotel');
            
			
			alert('審核已通過！業者狀態已更新。');
            // 修正跳轉路徑
            window.location.href = '/admin/reviewBackend';
            
        } catch (error) {
            console.error('Error during approval:', error);
            alert('審核處理失敗，請稍後再試');
        }
    }
}

function handleReject() {
    const rejectModal = document.getElementById('rejectModal');
    rejectModal.classList.add('show');
}

async function submitReject(reason) {
    if (!reason.trim()) {
        alert('請輸入駁回原因');
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const hotelId = urlParams.get('hotelId');
    
	if (!hotelId) {
        console.error('找不到飯店ID');
        alert('處理失敗：找不到飯店ID');
        return;
    }
	
    try {
		// 發送駁回請求到後端
        const response = await fetch(`/adminHotel/industry/review/${hotelId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `status=2&reason=${encodeURIComponent(reason.trim())}` // 使用x-www-form-urlencoded格式
	    });
        
        if (!response.ok) throw new Error('Failed to reject hotel');
        
		// 關閉駁回modal並清空輸入
        document.getElementById('rejectModal').classList.remove('show');
        document.getElementById('rejectReason').value = '';
        
        alert('已駁回申請，系統已發送通知信給業者');
        // 修正跳轉路徑
        window.location.href = '/admin/reviewBackend';
        
    } catch (error) {
        console.error('Error during rejection:', error);
        alert('駁回處理失敗，請稍後再試');
    }
}

function initializeButtons() {
  const passButton = document.querySelector('.pass');
  const rejectButton = document.querySelector('.reject');
  
  passButton.addEventListener('click', handlePass);
  rejectButton.addEventListener('click', handleReject);
}

function initializeModals() {
  // 圖片預覽模態框
  const imageModal = document.getElementById('imageModal');
  const imageBoxes = document.querySelectorAll('.image-box');
  const modalImage = document.getElementById('modalImage');

  imageBoxes.forEach(box => {
      box.addEventListener('click', (e) => {
          const img = box.querySelector('img');
          if (img) {
              modalImage.src = img.src;
              imageModal.classList.add('show');
          }
      });
  });

  // 駁回模態框
  const rejectModal = document.getElementById('rejectModal');
  
  // 綁定所有關閉按鈕
  document.querySelectorAll('.modal-close').forEach(closeBtn => {
      closeBtn.addEventListener('click', () => {
          closeBtn.closest('.modal').classList.remove('show');
      });
  });

  // 點擊模態框背景關閉
  document.querySelectorAll('.modal').forEach(modal => {
      modal.addEventListener('click', (e) => {
          if (e.target === modal) {
              modal.classList.remove('show');
          }
      });
  });

  // 駁回模態框的按鈕事件
  if (rejectModal) {
      const cancelBtn = rejectModal.querySelector('.cancel-button');
      const submitBtn = rejectModal.querySelector('.submit-button');
      const textarea = rejectModal.querySelector('#rejectReason');

      cancelBtn.addEventListener('click', () => {
          rejectModal.classList.remove('show');
          textarea.value = '';
      });

      submitBtn.addEventListener('click', () => {
          submitReject(textarea.value);
      });
  }
}

//function handlePass() {
//  if (confirm('確定要通過此業者的申請嗎？')) {
//      updateVendorStatus('active');
//      showNotification('審核通過！業者狀態已更新為啟用中', 'success');
//  }
//}
//
//function handleReject() {
//  const rejectModal = document.getElementById('rejectModal');
//  rejectModal.classList.add('show');
//}
//
//function submitReject(reason) {
//  reason = reason.trim();
//  if (!reason) {
//      showNotification('請輸入駁回原因', 'error');
//      return;
//  }
//
//  updateVendorStatus('rejected', reason);
//  document.getElementById('rejectModal').classList.remove('show');
//  document.getElementById('rejectReason').value = '';
//  showNotification('已駁回申請，訊息已發送給業者', 'success');
//}

//function updateVendorStatus(status, reason = '') {
//  // 模擬 API 請求
//  const vendorId = document.getElementById('company-id').querySelector('.content').textContent;
//  const requestBody = {
//      vendorId: vendorId,
//      status: status,
//      rejectionReason: reason,
//      timestamp: new Date().toISOString()
//  };
//
//  // 這裡應該發送到後端 API
//  console.log('發送狀態更新:', requestBody);
//  
//  // 模擬 API 回應
//  setTimeout(() => {
//      console.log('狀態更新成功');
//  }, 500);
//}

// 輔助函數：顯示通知
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    // 5秒後自動移除通知
    setTimeout(() => {
        notification.remove();
    }, 5000);
}