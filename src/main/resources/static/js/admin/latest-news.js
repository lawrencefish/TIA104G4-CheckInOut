// 頁面加載完成後執行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化頁面
    initializePage();
});

// 初始化頁面
function initializePage() {
    // 設置日期輸入框的最大值為今天
    setMaxDates();
    // 載入新聞數據
    loadNewsData();
}

// 設置日期輸入框的最大值
function setMaxDates() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').max = today;
    document.getElementById('publishDate').max = today;
}

// 從API載入新聞數據
async function loadNewsData() {
    try {
        const response = await fetch('/api/news');
        if (!response.ok) {
            throw new Error('獲取數據失敗');
        }
        const newsData = await response.json();
        displayNews(newsData);
    } catch (error) {
        console.error('Error:', error);
        showMessage('獲取新聞數據失敗', 'error');
    }
}

// 顯示消息列表
function displayNews(news) {
    const tableBody = document.getElementById('newsTableBody');
    tableBody.innerHTML = '';

    news.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${escapeHtml(item.newsTitle)}</td>
            <td>${escapeHtml(item.description)}</td>
            <td>${formatDate(item.postTime)}</td>
            <td>${formatDate(item.createTime)}</td>
            <td>
                <button class="btn btn-edit" onclick="editNews(${item.newsId})">編輯</button>
                <button class="btn btn-delete" onclick="deleteNews(${item.newsId})">刪除</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// 搜尋消息
async function searchNews() {
    const searchTitle = document.getElementById('searchTitle').value.toLowerCase();
    const startDate = document.getElementById('startDate').value;
    const publishDate = document.getElementById('publishDate').value;

    try {
        const response = await fetch('/api/news');
        if (!response.ok) {
            throw new Error('獲取數據失敗');
        }
        const newsData = await response.json();

        const filteredNews = newsData.filter(item => {
            const titleMatch = item.newsTitle.toLowerCase().includes(searchTitle);
            const startDateMatch = !startDate || item.postTime >= startDate;
            const publishDateMatch = !publishDate || item.createTime >= publishDate;

            return titleMatch && startDateMatch && publishDateMatch;
        });

        displayNews(filteredNews);
    } catch (error) {
        console.error('Error:', error);
        showMessage('搜尋失敗', 'error');
    }
}

// 創建消息
function createNews() {
    window.location.href = '/admin/editNews';
}

// 編輯消息
function editNews(id) {
    window.location.href = `/admin/editNews?id=${id}`;
}

// 刪除消息
async function deleteNews(id) {
    if (!confirm('確定要刪除這則新聞嗎？')) {
        return;
    }

    try {
        const response = await fetch(`/api/news/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('刪除失敗');
        }

        showMessage('新聞已刪除', 'success');
        loadNewsData(); // 重新載入數據
    } catch (error) {
        console.error('Error:', error);
        showMessage('刪除失敗', 'error');
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

// 格式化日期
function formatDate(dateString) {
    if (!dateString) return '';
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

// 監聽搜尋框的 Enter 鍵事件
document.getElementById('searchTitle').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        searchNews();
    }
});

// 監聽日期輸入框變化
document.getElementById('startDate').addEventListener('change', searchNews);
document.getElementById('publishDate').addEventListener('change', searchNews);