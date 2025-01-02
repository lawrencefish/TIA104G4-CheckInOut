// industry-review.js
document.addEventListener('DOMContentLoaded', () => {
  initializeButtons();
  initializeModals();
});

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

function handlePass() {
  if (confirm('確定要通過此業者的申請嗎？')) {
      updateVendorStatus('active');
      showNotification('審核通過！業者狀態已更新為啟用中', 'success');
  }
}

function handleReject() {
  const rejectModal = document.getElementById('rejectModal');
  rejectModal.classList.add('show');
}

function submitReject(reason) {
  reason = reason.trim();
  if (!reason) {
      showNotification('請輸入駁回原因', 'error');
      return;
  }

  updateVendorStatus('rejected', reason);
  document.getElementById('rejectModal').classList.remove('show');
  document.getElementById('rejectReason').value = '';
  showNotification('已駁回申請，訊息已發送給業者', 'success');
}

function updateVendorStatus(status, reason = '') {
  // 模擬 API 請求
  const vendorId = document.getElementById('company-id').querySelector('.content').textContent;
  const requestBody = {
      vendorId: vendorId,
      status: status,
      rejectionReason: reason,
      timestamp: new Date().toISOString()
  };

  // 這裡應該發送到後端 API
  console.log('發送狀態更新:', requestBody);
  
  // 模擬 API 回應
  setTimeout(() => {
      console.log('狀態更新成功');
  }, 500);
}

function showNotification(message, type) {
  const notification = document.createElement('div');
  notification.className = `notification ${type}`;
  
  const text = document.createElement('span');
  text.textContent = message;
  
  const closeBtn = document.createElement('span');
  closeBtn.className = 'notification-close';
  closeBtn.textContent = '×';
  closeBtn.onclick = () => notification.remove();
  
  notification.appendChild(text);
  notification.appendChild(closeBtn);
  document.body.appendChild(notification);
  
  setTimeout(() => notification.remove(), 5000);
}