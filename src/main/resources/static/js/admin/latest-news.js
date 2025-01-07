// 定義新聞數據陣列（實際應用中可能來自API）
//let newsData = [
//    {
//        id: 1,
//        title: "新年活動公告",
//        content: "新年期間將舉辦特別活動...",
//        startDate: "2024-01-01",
//        publishDate: "2023-12-25"
//    },
//    // 可以添加更多測試數據
//];

// 頁面加載完成後執行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化頁面
    initializePage();
});

// 初始化頁面
function initializePage() {
    // 載入頁面組件
//    loadComponents();
    // 顯示新聞列表
//    displayNews(newsData);
    // 設置日期輸入框的最大值為今天
    setMaxDates();
}

// 載入頁面組件（header和footer）
//async function loadComponents() {
//    try {
//        // 載入header
//        const headerResponse = await fetch('./backend-header.html');
//        const headerHtml = await headerResponse.text();
//        document.getElementById('header').innerHTML = headerHtml;
//
//        // 初始化 header 功能
//        initializeHeader();
//
//        // 載入footer
//        const footerResponse = await fetch('./backend-footer.html');
//        const footerHtml = await footerResponse.text();
//        document.getElementById('footer').innerHTML = footerHtml;
//    } catch (error) {
//        console.error('載入組件失敗:', error);
//    }
//}

// 設置日期輸入框的最大值
function setMaxDates() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').max = today;
    document.getElementById('publishDate').max = today;
}

// 顯示消息列表
//function displayNews(news) {
//    const tableBody = document.getElementById('newsTableBody');
//    tableBody.innerHTML = '';
//
//    news.forEach(item => {
//        const row = document.createElement('tr');
//        row.innerHTML = `
//            <td>${escapeHtml(item.title)}</td>
//            <td>${escapeHtml(item.content)}</td>
//            <td>${formatDate(item.startDate)}</td>
//            <td>${formatDate(item.publishDate)}</td>
//            <td>
//                <button class="btn btn-edit" onclick="editNews(${item.id})">編輯</button>
//                <button class="btn btn-delete" onclick="deleteNews(${item.id})">刪除</button>
//            </td>
//        `;
//        tableBody.appendChild(row);
//    });
//}

// 搜尋消息
function searchNews() {
    const searchTitle = document.getElementById('searchTitle').value.toLowerCase();
    const startDate = document.getElementById('startDate').value;
    const publishDate = document.getElementById('publishDate').value;

    const filteredNews = newsData.filter(item => {
        const titleMatch = item.title.toLowerCase().includes(searchTitle);
        const startDateMatch = !startDate || item.startDate >= startDate;
        const publishDateMatch = !publishDate || item.publishDate >= publishDate;

        return titleMatch && startDateMatch && publishDateMatch;
    });

    displayNews(filteredNews);
}

// 創建消息
function createNews() {
    // 跳轉到編輯頁面，不帶 id 參數表示是創建模式
    window.location.href = 'edit-news.html';
}

// 編輯消息
function editNews(id) {
    const news = newsData.find(item => item.id === id);
    if (!news) {
        showMessage('找不到該新聞', 'error');
        return;
    }

    // 跳轉到編輯頁面，帶上 id 參數表示是編輯模式
    window.location.href = `edit-news.html?id=${id}`;
}

// 刪除消息
function deleteNews(id) {
    if (!confirm('確定要刪除這則新聞嗎？')) {
        return;
    }

    // 在實際應用中，這裡應該調用API
    newsData = newsData.filter(item => item.id !== id);
    displayNews(newsData);
    showMessage('新聞已刪除', 'success');
}

// 顯示消息提示
function showMessage(message, type = 'info') {
    // 創建消息元素
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.textContent = message;

    // 添加到頁面
    document.body.appendChild(messageDiv);

    // 3秒後自動移除
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