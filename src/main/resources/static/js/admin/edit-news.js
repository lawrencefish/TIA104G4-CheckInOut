// 當前編輯的新聞 ID
let currentNewsId = null;

// 頁面加載完成後執行
document.addEventListener('DOMContentLoaded', function() {
    initializePage();
});

// 載入頁面組件
async function loadComponents() {
    try {
        // 載入header
        const headerResponse = await fetch('./backend-header.html');
        const headerHtml = await headerResponse.text();
        document.getElementById('header').innerHTML = headerHtml;

        // 載入footer
        const footerResponse = await fetch('./backend-footer.html');
        const footerHtml = await footerResponse.text();
        document.getElementById('footer').innerHTML = footerHtml;
    } catch (error) {
        console.error('載入組件失敗:', error);
    }
}

// 初始化頁面
async function initializePage() {
    // 載入頁面組件
    await loadComponents();
    
    // 設置日期輸入框的最大和最小值
    setDateConstraints();
    
    // 從 URL 獲取新聞 ID（如果是編輯模式）
    const urlParams = new URLSearchParams(window.location.search);
    const newsId = urlParams.get('id');
    
    // 設置表單標題
    const pageTitle = document.querySelector('.admin-banner h1');
    pageTitle.textContent = newsId ? '編輯消息' : '創建消息';
    
    // 如果是編輯模式，載入新聞數據
    if (newsId) {
        loadNewsData(newsId);
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
        // 在實際應用中，這裡應該是從 API 獲取數據
        // 這裡使用模擬數據作為示例
        const newsData = {
            id: newsId,
            title: "示例新聞標題",
            content: "示例新聞內容...",
            publishDate: "2024-01-01",
            startDate: "2024-01-01",
            imageUrl: "./img/sale.jpg"
        };

        // 填充表單
        document.getElementById('title').value = newsData.title;
        document.getElementById('content').value = newsData.content;
        document.getElementById('publishDate').value = newsData.publishDate;
        document.getElementById('startDate').value = newsData.startDate;
        document.getElementById('previewImage').src = newsData.imageUrl;

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
    const formData = {
        title: document.getElementById('title').value,
        content: document.getElementById('content').value,
        publishDate: document.getElementById('publishDate').value,
        startDate: document.getElementById('startDate').value,
        image: document.getElementById('photoUpload').files[0]
    };

    // 驗證日期
    if (new Date(formData.startDate) < new Date(formData.publishDate)) {
        showMessage('開始時間不能早於發布時間', 'error');
        return;
    }

    try {
        // 在實際應用中，這裡應該調用 API 提交數據
        // 模擬 API 調用
        await new Promise(resolve => setTimeout(resolve, 1000));

        showMessage('新聞保存成功', 'success');
        
        // 延遲後返回列表頁面
        setTimeout(() => {
            window.location.href = 'latest-news.html';
        }, 1500);

    } catch (error) {
        console.error('提交新聞失敗:', error);
        showMessage('提交失敗，請重試', 'error');
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

// 日期輸入監聽
document.getElementById('publishDate').addEventListener('change', function(e) {
    const startDate = document.getElementById('startDate');
    // 確保開始時間不早於發布時間
    if (startDate.value < e.target.value) {
        startDate.value = e.target.value;
    }
    startDate.min = e.target.value;
});