// 當前編輯的新聞 ID
let currentNewsId = null;

// 頁面加載完成後執行
document.addEventListener('DOMContentLoaded', function() {
    
//	const urlParams = new URLSearchParams(window.location.search);
//    const newsId = urlParams.get('id');
//    
//    if (newsId) {
//        fetchNewsImage(newsId);
//    }
	
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
        
        // 修改圖片處理部分
        if (newsData.newsImg) {
            const previewImage = document.getElementById('previewImage');
            // 如果是 Base64 格式的圖片數據
            if (typeof newsData.newsImg === 'string' && newsData.newsImg.startsWith('data:image')) {
                previewImage.src = newsData.newsImg;
            } else {
                // 如果不是 Base64 格式，則調用圖片 API
                fetch(`/api/news/${newsId}/image`)
                    .then(response => {
                        if (!response.ok) throw new Error('圖片加載失敗');
                        return response.blob();
                    })
                    .then(blob => {
                        const url = URL.createObjectURL(blob);
                        previewImage.src = url;
                        previewImage.style.display = 'block';
                    })
                    .catch(error => {
                        console.error('圖片載入失敗:', error);
                        showMessage('圖片載入失敗', 'error');
                    });
            }
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

    fileInput.addEventListener('change', async function(e) {
        const file = e.target.files[0];
        if (!file) return;

        // 驗證檔案類型
        if (!file.type.startsWith('image/')) {
            showMessage('請上傳圖片檔案', 'error');
            return;
        }

        try {
            // 壓縮圖片並預覽
            const processedImage = await resizeImage(file, 800, 600);
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImage.src = e.target.result;
            };
            reader.readAsDataURL(processedImage);
        } catch (error) {
            console.error('圖片處理失敗:', error);
            showMessage('圖片處理失敗', 'error');
        }
    });
}
// 提交新聞表單
async function submitNews(event) {
    event.preventDefault();
    
    try {
        // 取得表單數據
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;
        const publishDate = document.getElementById('publishDate').value;
        const startDate = document.getElementById('startDate').value;
        const imageFile = document.getElementById('photoUpload').files[0];

        // 檢查並壓縮圖片
        let processedImage = null;
        if (imageFile) {
            processedImage = await resizeImage(imageFile, 800, 600); // 設定最大寬高
        }

        // 創建 FormData
        const formData = new FormData();
        formData.append('newsTitle', title);
        formData.append('description', content);
        formData.append('createTime', publishDate);
        formData.append('postTime', startDate);
        
        if (processedImage) {
            formData.append('newsImg', processedImage);
        }

        // 取得 newsId（如果是編輯模式）
        const urlParams = new URLSearchParams(window.location.search);
        const newsId = urlParams.get('id');
        
        // 設定請求 URL 和方法
        const url = newsId ? `/api/news/${newsId}` : '/api/news';
        const method = newsId ? 'PUT' : 'POST';

        // 發送請求
        const response = await fetch(url, {
            method: method,
            body: formData
        });

        if (!response.ok) {
            throw new Error('提交失敗');
        }

        showMessage('新聞保存成功', 'success');
        
        // 成功後跳轉
        setTimeout(() => {
            window.location.href = '/admin/latestNews';
        }, 1500);

    } catch (error) {
        console.error('提交失敗:', error);
        showMessage('提交失敗，請重試', 'error');
    }
}
	
// 圖片壓縮函數
function resizeImage(file, maxWidth, maxHeight) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = function(event) {
            const img = new Image();
            img.onload = function() {
                const canvas = document.createElement('canvas');
                let width = img.width;
                let height = img.height;

                // 計算新的尺寸，保持比例
                if (width > height) {
                    if (width > maxWidth) {
                        height = Math.round(height * maxWidth / width);
                        width = maxWidth;
                    }
                } else {
                    if (height > maxHeight) {
                        width = Math.round(width * maxHeight / height);
                        height = maxHeight;
                    }
                }

                canvas.width = width;
                canvas.height = height;

                // 繪製調整大小後的圖片
                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, width, height);

                // 轉換為 blob，使用較低的品質
                canvas.toBlob((blob) => {
                    resolve(new File([blob], file.name, {
                        type: 'image/jpeg',
                        lastModified: Date.now()
                    }));
                }, 'image/jpeg', 0.6); // 設定 JPEG 品質為 0.6
            };
            img.onerror = reject;
            img.src = event.target.result;
        };
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}

// 格式化日期用於input標籤
function formatDateForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
}

// 顯示消息提示
function showMessage(message, type = 'info') {
    // 移除已存在的消息
    const existingMessages = document.querySelectorAll('.message');
    existingMessages.forEach(msg => msg.remove());

    // 創建新消息元素
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    
    // 創建圖標元素
    const icon = document.createElement('span');
    switch(type) {
        case 'success':
            icon.innerHTML = '✓ ';
            break;
        case 'error':
            icon.innerHTML = '✕ ';
            break;
        default:
            icon.innerHTML = 'ℹ ';
    }
    
    // 添加圖標和消息文本
    messageDiv.appendChild(icon);
    messageDiv.appendChild(document.createTextNode(message));
    
    // 添加到頁面
    document.body.appendChild(messageDiv);
    
    // 設定自動消失
    setTimeout(() => {
        messageDiv.classList.add('message-fade-out');
        setTimeout(() => {
            messageDiv.remove();
        }, 500);
    }, 3000);

    // 添加點擊關閉功能
    messageDiv.addEventListener('click', () => {
        messageDiv.classList.add('message-fade-out');
        setTimeout(() => {
            messageDiv.remove();
        }, 500);
    });
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

// 抓取單張圖片
function fetchNewsImage(newsId) {
    // 使用正確的元素 ID
    const previewImage = document.getElementById('previewImage');
    const loading = document.getElementById('loading');
    const error = document.getElementById('error');
    
    // 檢查元素是否存在
    if (!previewImage || !loading || !error) {
        console.error('Required DOM elements not found');
        return;
    }
    
    // 顯示加載中
    loading.style.display = 'block';
    previewImage.style.display = 'none';
    error.style.display = 'none';
    
    fetch(`/api/news/${newsId}/image`)
        .then(response => {
            if (!response.ok) {
                throw new Error('圖片加載失敗');
            }
            return response.blob();
        })
        .then(blob => {
            const url = URL.createObjectURL(blob);
            previewImage.src = url;
            previewImage.style.display = 'block';
            loading.style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
            error.style.display = 'block';
            error.textContent = '圖片加載失敗';
            loading.style.display = 'none';
        });
}

