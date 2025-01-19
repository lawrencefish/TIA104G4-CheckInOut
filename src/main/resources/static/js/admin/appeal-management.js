// 檢查登入狀態和權限
// checkLoginStatus();


// 全局變量
let currentPage = 1;
const pageSize = 10;
let totalPages = 10;
let currentTable = 'member'; // 預設為會員申訴表格

// 初始化頁面
function initializePage() {
    renderTableHeaders(); // 初始化表頭
    loadComplaints('member'); // 預設顯示會員申訴
    setupEventListeners();
    updatePagination();
}

// 設置事件監聽器
function setupEventListeners() {
    // 切換按鈕事件
    document.getElementById('memberComplaintsBtn').addEventListener('click', () => {
        switchTable('member');
    });
    document.getElementById('businessComplaintsBtn').addEventListener('click', () => {
        switchTable('business');
    });

    // 篩選相關
    document.getElementById('filterBtn').addEventListener('click', handleFilter);
    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') handleFilter();
    });

    // 分頁相關
    document.getElementById('prevPage').addEventListener('click', handlePrevPage);
    document.getElementById('nextPage').addEventListener('click', handleNextPage);

    // 模態窗相關
    document.querySelector('.close').addEventListener('click', closeModal);
    document.getElementById('modalCancel').addEventListener('click', closeModal);
    document.getElementById('modalSubmit').addEventListener('click', handleComplaintSubmit);
    
    // 點擊模態窗外部關閉
    document.getElementById('complaintModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('complaintModal')) {
            closeModal();
        }
    });

    // 日期選擇器限制
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('endDate').max = today;
    document.getElementById('startDate').addEventListener('change', function() {
        document.getElementById('endDate').min = this.value;
    });

    // 添加鍵盤事件監聽
    document.addEventListener('keydown', function(e) {
        const modal = document.getElementById('imageModal');
        if (modal.style.display === 'block') {
            if (e.key === 'Escape') {
                modal.style.display = 'none';
            }
        }
    });
}

// 載入申訴數據
// async function loadComplaints() {
//     try {
//         const filters = getFilters();
//         const url = new URL('/api/admin/complaints', window.location.origin);
//         Object.keys(filters).forEach(key => {
//             if (filters[key]) url.searchParams.append(key, filters[key]);
//         });
//         url.searchParams.append('page', currentPage);
//         url.searchParams.append('limit', pageSize);

//         const response = await fetch(url, {
//             credentials: 'include'
//         });
        
//         if (!response.ok) throw new Error('載入數據失敗');
        
//         const data = await response.json();
        
//         if (data.status === 'success') {
//             renderComplaints(data.data.complaints);
//             updateStatistics(data.data.statistics);
//             totalPages = Math.ceil(data.data.total / pageSize);
//             updatePagination();
//         } else {
//             throw new Error(data.message || '載入數據失敗');
//         }
//     } catch (error) {
//         console.error('載入申訴數據失敗:', error);
//         showToast(error.message, 'error');
//     }
// }

function switchTable(tableType) {
    currentTable = tableType;

    // 更新按鈕狀態
    document.getElementById('memberComplaintsBtn').classList.toggle('active', tableType === 'member');
    document.getElementById('businessComplaintsBtn').classList.toggle('active', tableType === 'business');

    // 重置分頁
    currentPage = 1;

    // 更新表頭
    renderTableHeaders();

    // 重新載入數據
    loadComplaints(tableType);
}

// 暫時使用的測試資料
function loadComplaints(tableType) {
    const mockData = tableType === 'member' ? getMockMemberData() : getMockBusinessData();

    renderComplaints(mockData.complaints);
    updateStatistics(mockData.statistics);
}

function getMockMemberData() {
    return {
        complaints: [
            { contact_us_id: 1, create_time: '2024-03-25', member_account: 'member1@example.com', hotel_name: '飯店A', contact_us_text: '會員申訴內容A', review_status: 0 },
            { contact_us_id: 2, create_time: '2024-03-26', member_account: 'member2@example.com', hotel_name: '飯店B', contact_us_text: '會員申訴內容B', review_status: 1 }
        ],
        statistics: { total: 2, pending: 1, processing: 1, completed: 0 }
    };
}

function getMockBusinessData() {
    return {
        complaints: [
            { contact_us_id: 101, create_time: '2024-04-01', hotel_name: '飯店X', member_account: 'business1@example.com', contact_us_text: '業者申訴內容X', review_status: 2 },
            { contact_us_id: 102, create_time: '2024-04-02', hotel_name: '飯店Y', member_account: 'business2@example.com', contact_us_text: '業者申訴內容Y', review_status: 3 }
        ],
        statistics: { total: 2, pending: 0, processing: 0, completed: 2 }
    };
}
//***************************************** */

// 獲取篩選條件
function getFilters() {
    return {
        status: document.getElementById('statusFilter').value,
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value,
        keyword: document.getElementById('searchInput').value
    };
}

// 渲染申訴列表
function renderComplaints(complaints) {
    const tableBody = document.getElementById('complaintsTable');
    
    if (!complaints || complaints.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center">無申訴數據</td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = complaints.map(complaint => {
        const accountField = currentTable === 'member' ? escapeHtml(complaint.member_account) : escapeHtml(complaint.hotel_name);
        const hotelField = currentTable === 'member' ? escapeHtml(complaint.hotel_name) : escapeHtml(complaint.member_account);

        return `
            <tr>
                <td>CP${String(complaint.contact_us_id).padStart(6, '0')}</td>
                <td>${formatDateTime(complaint.create_time)}</td>
                <td>${accountField}</td>
                <td>${hotelField}</td>
                <td class="truncate">${escapeHtml(complaint.contact_us_text.substring(0, 50))}${complaint.contact_us_text.length > 50 ? '...' : ''}</td>
                <td>
                    <span class="status-badge status-${complaint.review_status}">
                        ${getStatusText(complaint.review_status)}
                    </span>
                </td>
                <td>
                    <button class="action-btn" onclick="openComplaintDetail(${complaint.contact_us_id})">
                        查看詳情
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function renderTableHeaders() {
    const tableHeader = `
        <tr>
            <th>申訴編號</th>
            <th>提交時間</th>
            <th>${currentTable === 'member' ? '會員帳號' : '飯店名稱'}</th>
            <th>${currentTable === 'member' ? '飯店名稱' : '會員帳號'}</th>
            <th>申訴內容</th>
            <th>處理狀態</th>
            <th>操作</th>
        </tr>
    `;
    document.querySelector('#complaintsTable').innerHTML = tableHeader;

    // 選擇 <thead> 而非 <tbody>
    const thead = document.querySelector('thead');
    if (thead) {
        thead.innerHTML = tableHeader;
    } else {
        console.error('未找到 <thead> 節點');
    }
}

// 更新統計數據
function updateStatistics(statistics) {
    document.getElementById('totalComplaints').textContent = statistics.total || 0;
    document.getElementById('pendingComplaints').textContent = statistics.pending || 0;
    document.getElementById('processingComplaints').textContent = statistics.processing || 0;
    document.getElementById('completedComplaints').textContent = statistics.completed || 0;
}

// 打開申訴詳情
// async function openComplaintDetail(id) {
//     try {
//         const response = await fetch(`/api/admin/complaints/${id}`, {
//             credentials: 'include'
//         });
        
//         if (!response.ok) throw new Error('載入詳情失敗');
        
//         const data = await response.json();
        
//         if (data.status === 'success') {
//             const complaint = data.data;
            
//             // 填充模態窗數據
//             document.getElementById('modalComplaintId').textContent = `CP${String(complaint.contact_us_id).padStart(6, '0')}`;
//             document.getElementById('modalSubmitTime').textContent = formatDateTime(complaint.create_time);
//             document.getElementById('modalMemberAccount').textContent = escapeHtml(complaint.member_account);
//             document.getElementById('modalHotelName').textContent = escapeHtml(complaint.hotel_name);
//             document.getElementById('modalContent').textContent = complaint.contact_us_text;
//             document.getElementById('modalStatus').value = complaint.review_status;
//             document.getElementById('modalMethod').value = complaint.method || '';
//             document.getElementById('modalReply').value = complaint.reply_content || '';
            
//             // 存儲當前處理的申訴ID
//             document.getElementById('modalSubmit').dataset.complaintId = id;
            
//             // 顯示模態窗
//             document.getElementById('complaintModal').style.display = 'block';
//         } else {
//             throw new Error(data.message || '載入詳情失敗');
//         }
//     } catch (error) {
//         console.error('載入申訴詳情失敗:', error);
//         showToast(error.message, 'error');
//     }
// }

// 替換成測試用的函數
function openComplaintDetail(id) {
    // 測試用的假資料
    const mockComplaint = {
        contact_us_id: id,
        create_time: '2024-03-25 14:30:00',
        member_name: '王小明',
        member_email: 'test@example.com',
        hotel_name: '測試飯店',
        order_id: '000123',
        check_in_date: '2024-04-01',
        contact_us_text: '這是測試用的申訴詳細內容...',
        review_status: 0,
        method: '',
        reply_content: '',
        complaint_pics: [  // 改為圖片陣列
            '/img/單人房.webp',
            '/img/雙人房.webp',
        ]
    };

    // 填充模態窗數據
    document.getElementById('modalComplaintId').textContent = `CP${String(mockComplaint.contact_us_id).padStart(6, '0')}`;
    document.getElementById('modalSubmitTime').textContent = formatDateTime(mockComplaint.create_time);
    document.getElementById('modalMemberName').textContent = mockComplaint.member_name;
    document.getElementById('modalMemberEmail').textContent = mockComplaint.member_email;
    document.getElementById('modalHotelName').textContent = mockComplaint.hotel_name;
    document.getElementById('modalOrderId').textContent = mockComplaint.order_id ? `OD${mockComplaint.order_id.padStart(6, '0')}` : '無訂單';
    document.getElementById('modalCheckInDate').textContent = mockComplaint.check_in_date ? formatDate(mockComplaint.check_in_date) : '無入住日期';
    document.getElementById('modalContent').textContent = mockComplaint.contact_us_text;
    document.getElementById('modalStatus').value = mockComplaint.review_status;
    document.getElementById('modalMethod').value = mockComplaint.method;
    document.getElementById('modalReply').value = mockComplaint.reply_content;

    

    // 處理多張圖片顯示
    const imageContainer = document.getElementById('modalImage');
    if (mockComplaint.complaint_pics && mockComplaint.complaint_pics.length > 0) {
        imageContainer.innerHTML = mockComplaint.complaint_pics.map((pic, index) => `
            <img src="${pic}" 
                alt="申訴圖片 ${index + 1}" 
                class="complaint-image"
                onclick="showEnlargedImage('${pic}', ${index + 1}, ${mockComplaint.complaint_pics.length})"
            >
        `).join('');
    } else {
        imageContainer.innerHTML = '<p class="no-image">無圖片</p>';
    }


    // 存儲當前處理的申訴ID
    document.getElementById('modalSubmit').dataset.complaintId = id;
    
    // 顯示模態窗
    document.getElementById('complaintModal').style.display = 'block';
}

// 顯示放大圖片
function showEnlargedImage(src, currentNum, totalNum) {
    const modal = document.getElementById('imageModal');
    const enlargedImg = document.getElementById('enlargedImg');
    const counter = document.getElementById('imageCounter');
    
    enlargedImg.src = src;
    counter.textContent = `${currentNum} / ${totalNum}`;
    modal.style.display = 'block';
    // 點擊圖片模態窗關閉
    modal.onclick = function() {
        modal.style.display = 'none';
    };
}

// 新增日期格式化函數
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).format(date);
}
//****************************** */

// 處理申訴提交
// async function handleComplaintSubmit() {
//     const complaintId = document.getElementById('modalSubmit').dataset.complaintId;
//     const method = document.getElementById('modalMethod').value.trim();
//     const replyContent = document.getElementById('modalReply').value.trim();
//     const status = document.getElementById('modalStatus').value;

//     // 表單驗證
//     if (!method) {
//         showToast('請填寫處理方式', 'warning');
//         return;
//     }
//     if (!replyContent) {
//         showToast('請填寫回覆內容', 'warning');
//         return;
//     }

//     try {
//         const response = await fetch(`/api/admin/complaints/${complaintId}`, {
//             method: 'PUT',
//             credentials: 'include',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify({
//                 method,
//                 reply_content: replyContent,
//                 review_status: status
//             })
//         });

//         if (!response.ok) throw new Error('更新失敗');

//         const data = await response.json();

//         if (data.status === 'success') {
//             showToast('申訴處理成功', 'success');
//             closeModal();
//             loadComplaints();
//         } else {
//             throw new Error(data.message || '更新失敗');
//         }
//     } catch (error) {
//         console.error('提交申訴處理失敗:', error);
//         showToast(error.message, 'error');
//     }
// }

// 簡化版的處理申訴提交函數
function handleComplaintSubmit() {
    // 1. 獲取表單數據
    const complaintId = document.getElementById('modalSubmit').dataset.complaintId;
    const method = document.getElementById('modalMethod').value.trim();
    const replyContent = document.getElementById('modalReply').value.trim();
    const status = document.getElementById('modalStatus').value;

    // 2. 表單驗證
    if (!method) {
        alert('請填寫處理方式');
        return;
    }
    if (!replyContent) {
        alert('請填寫回覆內容');
        return;
    }

    // 3. 打印要提交的數據，檢查是否正確
    console.log('要提交的申訴數據：', {
        complaintId,
        method,
        replyContent,
        status
    });

    // 4. 模擬成功提交並更新頁面狀態
    updateComplaintStatus(complaintId, status);
    
    // 5. 模擬成功提交
    alert('申訴處理成功（測試）');
    closeModal();
    
    // 6. 可以在控制台查看最終結果
    console.log('申訴已處理完成，最終狀態：', status);
}

// 更新申訴狀態的函數
function updateComplaintStatus(complaintId, newStatus) {
    // 找到對應的表格列
    const tableRows = document.querySelectorAll('#complaintsTable tr');
    tableRows.forEach(row => {
        const idCell = row.querySelector('td:first-child');
        if (idCell && idCell.textContent === `CP${String(complaintId).padStart(6, '0')}`) {
            // 更新狀態欄位
            const statusCell = row.querySelector('td:nth-last-child(2)');
            if (statusCell) {
                statusCell.innerHTML = `
                    <span class="status-badge status-${newStatus}">
                        ${getStatusText(newStatus)}
                    </span>
                `;
            }
        }
    });

    // 更新統計數據
    const stats = {
        total: parseInt(document.getElementById('totalComplaints').textContent),
        pending: parseInt(document.getElementById('pendingComplaints').textContent),
        processing: parseInt(document.getElementById('processingComplaints').textContent),
        completed: parseInt(document.getElementById('completedComplaints').textContent)
    };

    // 根據狀態調整計數
    if (newStatus === '0') {
        stats.pending++;
        stats.processing = Math.max(0, stats.processing - 1);
        stats.completed = Math.max(0, stats.completed - 1);
    } else if (newStatus === '1') {
        stats.pending = Math.max(0, stats.pending - 1);
        stats.processing++;
        stats.completed = Math.max(0, stats.completed - 1);
    } else if (newStatus === '2') {
        stats.pending = Math.max(0, stats.pending - 1);
        stats.processing = Math.max(0, stats.processing - 1);
        stats.completed++;
    }

    // 更新顯示的統計數據
    document.getElementById('pendingComplaints').textContent = stats.pending;
    document.getElementById('processingComplaints').textContent = stats.processing;
    document.getElementById('completedComplaints').textContent = stats.completed;
}
/**************************************************** */
// 處理篩選
function handleFilter() {
    currentPage = 1;
    loadComplaints();
}

// 處理分頁
function handlePrevPage() {
    if (currentPage > 1) {
        currentPage--;
        loadComplaints();
    }
}

function handleNextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        loadComplaints();
    }
}

// 更新分頁信息
function updatePagination() {
    document.getElementById('pageInfo').textContent = `第 ${currentPage} 頁，共 ${totalPages} 頁`;
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPages;
}

// 關閉模態窗
function closeModal() {
    document.getElementById('complaintModal').style.display = 'none';
    // 清空表單
    document.getElementById('modalMethod').value = '';
    document.getElementById('modalReply').value = '';
}

// 工具函數
function formatDateTime(dateString) {
    const date = new Date(dateString);
    const options = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
    };
    return new Intl.DateTimeFormat('zh-TW', options).format(date);
}

function getStatusText(status) {
    const statusMap = {
        0: '待處理',
        1: '處理中',
        2: '已完成',
        3: '已關閉'
    };
    return statusMap[status] || '未知狀態';
}

function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// Toast 通知
function showToast(message, type = 'info') {
    // 假設使用了某個 Toast 库
    // 這裡可以替換成您項目中實際使用的 Toast 組件
    if (window.Toastify) {
        Toastify({
            text: message,
            duration: 3000,
            gravity: "top",
            position: "center",
            className: type
        }).showToast();
    } else {
        alert(message);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 等待 header.js 中的 loadCommonElements 完成
        await new Promise(resolve => {
            const checkHeader = setInterval(() => {
                if (document.querySelector('.admin-profile-trigger')) {
                    clearInterval(checkHeader);
                    resolve();
                }
            }, 100);
        });

        // 初始化頁面
        initializePage();
    } catch (error) {
        console.error('初始化失敗:', error);
    }
});