// 當前編輯的新聞 ID
let currentNewsId = null;

// 頁面加載完成後執行
document.addEventListener('DOMContentLoaded', function() {
    initializePage();
});

// 初始化頁面
async function initializePage() {
    // 設置日期輸入框的限制
    setDateConstraints();
    
    // 從 URL 獲取新聞 ID（如果是編輯模式）
    const urlParams = new URLSearchParams(window.location.search);
    currentNewsId = urlParams.get('id');
    
    // 設置表單標題
    const pageTitle = document.querySelector('.admin-banner h1');
    pageTitle.textContent = currentNewsId ? '編輯消息' : '創建消息';
    
    // 如果是編輯模式，載入新聞數據
    if (currentNewsId) {
        await loadNewsData(currentNewsId);
    } else {
        // 如果是創建模式，設置預設日期
        setDefaultDates();
    }

    // 設置圖片上傳監聽
    setupImageUpload();
}

// 設置日期輸入限制
function setDateConstraints() {
    const today = new Date().toISOString().split('T')[0];
    const maxDate = new Date();
    maxDate.setFullYear(maxDate.getFullYear() + 1); // 最多允許設置一年後的日期
    
    const publishDateInput = document.getElementById('publishDate');
    const startDateInput = document.getElementById('startDate');
    
    publishDateInput.max = maxDate.toISOString().split('T')[0];
    publishDateInput.min = today;
    startDateInput.min = today;
    startDateInput.max = maxDate.toISOString().split('T')[0];
}

// 設置預設日期
function setDefaultDates() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('publishDate').value = today;
    document.getElementById('startDate').value = today;
}

// 載入現有新聞數據
async function loadNewsData(newsId) {
    try {
        const response = await fetch(`/api/news/${newsId}`);
        if (!response.ok) {
            throw new Error('獲取數據失敗');
        }
        
        const newsData = await response.json();

        // 填充表單
        document.getElementById('title').value = newsData.newsTitle;
        document.getElementById('content').value = newsData.description;
        document.getElementById('publishDate').value = formatDateForInput(newsData.createTime);
        document.getElementById('startDate').value = formatDateForInput(newsData.postTime);
        
        // 如果有圖片，設置圖片預覽
        if (newsData.newsImg) {
            document.getElementById('previewImage').src = newsData.newsImg;
        }

    } catch (error) {
        console.error('載入新聞數據失敗:', error);
        showMessage('載入數據失敗，請重試', 'error');
    }
}

// 設置圖片上傳處理
function setupImageUpload() {
    const fileInput = document.getElementById('photoUpload');
    const previewImage = document.getElementById('previewImage');

    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (!file) return;

        // 驗證文件類型
        if (!file.type.startsWith('image/')) {
            showMessage('請上傳圖片文件', 'error');
            return;
        }

        // 驗證文件大小（最大 5MB）
        if (file.size > 5 * 1024 * 1024) {
            showMessage('圖片大小不能超過 5MB', 'error');
            return;
        }

        // 預覽圖片
        const reader = new FileReader();
        reader.onload = function(e) {
            previewImage.src = e.target.result;
        };
        reader.readAsDataURL(file);
    });
}

// 提交新聞表單
async function submitNews(event) {
    event.preventDefault();

    // 獲取表單數據
    const formData = new FormData();
    formData.append('newsTitle', document.getElementById('title').value);
    formData.append('description', document.getElementById('content').value);
    formData.append('createTime', document.getElementById('publishDate').value);
    formData.append('postTime', document.getElementById('startDate').value);
    
    const imageFile = document.getElementById('photoUpload').files[0];
    if (imageFile) {
        formData.append('newsImg', imageFile);
    }

    // 驗證日期
    if (new Date(formData.get('postTime')) < new Date(formData.get('createTime'))) {
        showMessage('開始時間不能早於發布時間', 'error');
        return;
    }

    try {
        // 將 FormData 轉換為 JSON 格式
        const newsData = {
            newsTitle: formData.get('newsTitle'),
            description: formData.get('description'),
            createTime: formData.get('createTime'),
            postTime: formData.get('postTime')
        };

        const url = currentNewsId ? `/api/news/${currentNewsId}` : '/api/news';
        const method = currentNewsId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newsData)
        });

        if (!response.ok) {
            throw new Error('提交失敗');
        }

        showMessage('新聞保存成功', 'success');
        
        // 延遲後返回列表頁面
        setTimeout(() => {
            window.location.href = '/admin/latestNews';
        }, 1500);

    } catch (error) {
        console.error('提交新聞失敗:', error);
        showMessage('提交失敗，請重試', 'error');
    }
}

// 格式化日期用於input標籤
function formatDateForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
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

// 日期輸入監聽
document.getElementById('publishDate').addEventListener('change', function(e) {
    const startDate = document.getElementById('startDate');
    // 確保開始時間不早於發布時間
    if (startDate.value < e.target.value) {
        startDate.value = e.target.value;
    }
    startDate.min = e.target.value;
});