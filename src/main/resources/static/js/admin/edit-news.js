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
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    const publishDate = document.getElementById('publishDate').value;
    const startDate = document.getElementById('startDate').value;
	const imageFile = document.getElementById('photoUpload').files[0];
	
	// 如果有圖片，先壓縮
    let compressedImage = null;
    if (imageFile) {
        compressedImage = await compressImage(imageFile);
    }
	
	const formData = new FormData();
	    formData.append('newsTitle', title);
	    formData.append('description', content);
	    formData.append('createTime', publishDate);
	    formData.append('postTime', startDate);
	    if (imageFile) {
	        formData.append('newsImg', imageFile);
	    }

	    try {
	        // 獲取當前新聞 ID（如果是編輯模式）
	        const urlParams = new URLSearchParams(window.location.search);
	        const newsId = urlParams.get('id');
	        
	        const url = newsId ? `/api/news/${newsId}` : '/api/news';
	        const method = newsId ? 'PUT' : 'POST';

	        // 發送請求
	        const response = await fetch(url, {
	            method: method,
	            body: formData  // 直接發送 FormData，不設置 Content-Type
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
	
// 圖片壓縮函數
async function compressImage(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = new Image();
            img.onload = function() {
                const canvas = document.createElement('canvas');
                let width = img.width;
                let height = img.height;
                
                // 設置最大尺寸
                const MAX_WIDTH = 1200;
                const MAX_HEIGHT = 1200;

                if (width > height) {
                    if (width > MAX_WIDTH) {
                        height *= MAX_WIDTH / width;
                        width = MAX_WIDTH;
                    }
                } else {
                    if (height > MAX_HEIGHT) {
                        width *= MAX_HEIGHT / height;
                        height = MAX_HEIGHT;
                    }
                }

                canvas.width = width;
                canvas.height = height;

                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, width, height);

                // 轉換為 blob
                canvas.toBlob(
                    (blob) => {
                        resolve(new File([blob], file.name, {
                            type: 'image/jpeg',
                            lastModified: Date.now()
                        }));
                    },
                    'image/jpeg',
                    0.7  // 壓縮質量，0.7 通常是個好的平衡點
                );
            };
            img.onerror = reject;
            img.src = e.target.result;
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

// 抓取單張圖片
function fetchNewsImage(newsId) {
    const imgElement = document.getElementById('newsImage');
    const loadingElement = document.getElementById('loading');
    const errorElement = document.getElementById('error');
	
	// 如果元素不存在，直接返回
	    if (!loading || !previewImage || !error) {
	        console.error('Required DOM elements not found');
	        return;
	    }
    
    // 顯示加載中
    loadingElement.style.display = 'block';
    imgElement.style.display = 'none';
    errorElement.style.display = 'none';
    
    fetch(`/api/news/${newsId}/image`)
        .then(response => {
            if (!response.ok) {
                throw new Error('圖片加載失敗');
            }
            return response.blob();
        })
        .then(blob => {
            const url = URL.createObjectURL(blob);
            imgElement.src = url;
            imgElement.style.display = 'block';
            loadingElement.style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error);
            errorElement.textContent = '圖片加載失敗';
            errorElement.style.display = 'block';
            loadingElement.style.display = 'none';
        });
}

// 使用示例
fetchNewsImage(1);