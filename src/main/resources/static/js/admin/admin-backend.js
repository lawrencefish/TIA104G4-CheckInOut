document.addEventListener('DOMContentLoaded', function () {
    const API_BASE_URL = 'admin';
    let currentPage = 1;
    const itemsPerPage = 10;

    // 模擬資料（之後可替換為資料庫資料）
//    const mockAdmins = [
//        { id: 1, name: '王大明', permissions: '高階管理員', status: '啟用中', joinDate: '2024-01-15', phone: '0912-345-678', email: 'wang@example.com', lastLogin: '2024-03-20 15:30' },
//        { id: 2, name: '李小華', permissions: '一般管理員', status: '啟用中', joinDate: '2024-01-20', phone: '0923-456-789', email: 'lee@example.com', lastLogin: '2024-03-19 14:20' },
//        { id: 3, name: '張美玲', permissions: '高階管理員', status: '停用', joinDate: '2024-02-01', phone: '0934-567-890', email: 'chang@example.com', lastLogin: '2024-03-15 09:45' },
//        { id: 4, name: '陳志明', permissions: '高階管理員', status: '啟用中', joinDate: '2024-02-05', phone: '0945-678-901', email: 'chen@example.com', lastLogin: '2024-03-20 16:15' },
//        { id: 5, name: '林小琳', permissions: '一般管理員', status: '啟用中', joinDate: '2024-02-10', phone: '0956-789-012', email: 'lin@example.com', lastLogin: '2024-03-20 11:30' },
//        { id: 6, name: '吳建志', permissions: '高階管理員', status: '啟用中', joinDate: '2024-02-15', phone: '0967-890-123', email: 'wu@example.com', lastLogin: '2024-03-19 17:40' },
//        { id: 7, name: '黃雅芳', permissions: '一般管理員', status: '停用', joinDate: '2024-02-20', phone: '0978-901-234', email: 'huang@example.com', lastLogin: '2024-03-18 13:25' },
//        { id: 8, name: '劉俊宏', permissions: '高階管理員', status: '啟用中', joinDate: '2024-02-25', phone: '0989-012-345', email: 'liu@example.com', lastLogin: '2024-03-20 10:15' },
//        { id: 9, name: '周淑華', permissions: '一般管理員', status: '啟用中', joinDate: '2024-03-01', phone: '0990-123-456', email: 'chou@example.com', lastLogin: '2024-03-20 14:50' },
//        { id: 10, name: '謝明德', permissions: '高階管理員', status: '啟用中', joinDate: '2024-03-05', phone: '0901-234-567', email: 'hsieh@example.com', lastLogin: '2024-03-19 16:35' },
//        { id: 11, name: '楊小菁', permissions: '一般管理員', status: '啟用中', joinDate: '2024-03-10', phone: '0912-345-678', email: 'yang@example.com', lastLogin: '2024-03-20 09:20' },
//        { id: 12, name: '郭志豪', permissions: '高階管理員', status: '停用', joinDate: '2024-03-15', phone: '0923-456-789', email: 'kuo@example.com', lastLogin: '2024-03-17 11:45' },
//        { id: 13, name: '許雅婷', permissions: '一般管理員', status: '啟用中', joinDate: '2024-03-16', phone: '0934-567-890', email: 'hsu@example.com', lastLogin: '2024-03-20 15:55' },
//        { id: 14, name: '蔡明翰', permissions: '高階管理員', status: '啟用中', joinDate: '2024-03-17', phone: '0945-678-901', email: 'tsai@example.com', lastLogin: '2024-03-20 13:40' },
//        { id: 15, name: '鄭佩珊', permissions: '一般管理員', status: '啟用中', joinDate: '2024-03-18', phone: '0956-789-012', email: 'cheng@example.com', lastLogin: '2024-03-20 16:30' }
//    ];

    // 模擬操作日誌資料（之後可替換為資料庫資料）
//    const mockLogs = [
//        { id: 1, adminName: '王大明', action: '新增管理員', target: '許雅婷', details: '新增一般管理員帳號', timestamp: '2024-03-20 15:30:22', ip: '192.168.1.101' },
//        { id: 2, adminName: '陳志明', action: '停用帳號', target: '張美玲', details: '停用客服主管帳號', timestamp: '2024-03-20 14:25:15', ip: '192.168.1.102' },
//        { id: 3, adminName: '李小華', action: '修改權限', target: '吳建志', details: '變更為系統管理員', timestamp: '2024-03-20 13:15:45', ip: '192.168.1.103' },
//        { id: 4, adminName: '劉俊宏', action: '重設密碼', target: '林小琳', details: '應用戶要求重設密碼', timestamp: '2024-03-19 17:40:33', ip: '192.168.1.104' },
//        { id: 5, adminName: '王大明', action: '編輯資料', target: '周淑華', details: '更新聯絡電話', timestamp: '2024-03-19 16:20:18', ip: '192.168.1.105' },
//        { id: 6, adminName: '謝明德', action: '啟用帳號', target: '黃雅芳', details: '重新啟用管理員帳號', timestamp: '2024-03-19 15:10:55', ip: '192.168.1.106' },
//        { id: 7, adminName: '陳志明', action: '刪除帳號', target: '測試帳號', details: '刪除測試用帳號', timestamp: '2024-03-19 14:05:42', ip: '192.168.1.107' },
//        { id: 8, adminName: '李小華', action: '修改權限', target: '蔡明翰', details: '變更為客服主管', timestamp: '2024-03-19 11:30:28', ip: '192.168.1.108' },
//        { id: 9, adminName: '王大明', action: '系統設定', target: '系統', details: '更新系統安全設定', timestamp: '2024-03-19 10:15:39', ip: '192.168.1.109' },
//        { id: 10, adminName: '劉俊宏', action: '備份資料', target: '系統', details: '執行系統資料備份', timestamp: '2024-03-19 09:20:51', ip: '192.168.1.110' }
//    ];

    // 修改資料獲取和排序邏輯
    function fetchAdminList(page, filters = {}) {
        const loadingSpinner = document.getElementById('loadingSpinner');
        loadingSpinner.style.display = 'block';

        setTimeout(() => {
            // 先進行篩選
            let filteredAdmins = mockAdmins.filter(admin => {
                const keyword = filters.keyword?.toLowerCase() || '';
                const matchKeyword = !keyword || 
                    admin.name.toLowerCase().includes(keyword) ||
                    admin.email.toLowerCase().includes(keyword) ||
                    admin.phone.includes(keyword);
                
                const matchStatus = !filters.status || filters.status === 'all' || admin.status === filters.status;
                const matchPermissions = !filters.permissions || filters.permissions === 'all' || admin.permissions === filters.permissions;

                return matchKeyword && matchStatus && matchPermissions;
            });

            // 進行排序
            if (currentSort.field) {
                filteredAdmins.sort((a, b) => {
                    let valueA = a[currentSort.field];
                    let valueB = b[currentSort.field];
                    
                    // 處理不同類型的排序
                    if (currentSort.field === 'id') {
                        valueA = parseInt(valueA);
                        valueB = parseInt(valueB);
                    } else if (currentSort.field === 'joinDate' || currentSort.field === 'lastLogin') {
                        valueA = new Date(valueA);
                        valueB = new Date(valueB);
                    }

                    // 根據排序方向返回比較結果
                    const comparison = valueA > valueB ? 1 : -1;
                    return currentSort.direction === 'asc' ? comparison : -comparison;
                });
            } else {
                // 預設按 ID 降序排序
                filteredAdmins.sort((a, b) => b.id - a.id);
            }

            // 分頁處理
            const startIndex = (page - 1) * itemsPerPage;
            const paginatedAdmins = filteredAdmins.slice(startIndex, startIndex + itemsPerPage);
            const totalPages = Math.ceil(filteredAdmins.length / itemsPerPage);

            loadingSpinner.style.display = 'none';
            renderAdminTable(paginatedAdmins);
            renderPagination(totalPages);
        }, 500);
    }

    // 模擬日誌 API 請求（之後可替換為實際 API 呼叫）
    function fetchLogList(page, filters = {}) {
        const loadingSpinner = $('#loadingSpinner');
        loadingSpinner.show();

        // 模擬 API 延遲
        setTimeout(() => {
            // 篩選邏輯
            let filteredLogs = mockLogs.filter(log => {
                const keyword = filters.keyword?.toLowerCase() || '';
                const matchKeyword = !keyword || 
                    log.adminName.toLowerCase().includes(keyword) ||
                    log.action.toLowerCase().includes(keyword) ||
                    log.target.toLowerCase().includes(keyword) ||
                    log.details.toLowerCase().includes(keyword);
                
                const matchAction = !filters.action || filters.action === 'all' || log.action === filters.action;
                const matchDate = !filters.date || log.timestamp.includes(filters.date);

                return matchKeyword && matchAction && matchDate;
            });

            // 分頁邏輯
            const startIndex = (page - 1) * itemsPerPage;
            const paginatedLogs = filteredLogs.slice(startIndex, startIndex + itemsPerPage);

            loadingSpinner.hide();
            renderLogTable(paginatedLogs);
        }, 500);
    }

    // 添加排序狀態
    let currentSort = {
        field: null,
        direction: 'asc'
    };

    // 使用 jQuery 重寫表格渲染函數，添加排序功能
    function renderAdminTable(admins) {
        const $tableBody = $('#userTableBody');
        $tableBody.empty();

        // 排序邏輯
        admins.sort((a, b) => {
            // 如果沒有指定排序欄位，預設按 ID 降序
            if (!currentSort.field) {
                return b.id - a.id;
            }

            let valueA = a[currentSort.field];
            let valueB = b[currentSort.field];
            
            // 處理不同類型的排序
            if (currentSort.field === 'id') {
                valueA = parseInt(valueA);
                valueB = parseInt(valueB);
            } else if (currentSort.field === 'joinDate' || currentSort.field === 'lastLogin') {
                valueA = new Date(valueA);
                valueB = new Date(valueB);
            }

            // 根據排序方向返回比較結果
            const comparison = valueA > valueB ? 1 : -1;
            return currentSort.direction === 'asc' ? comparison : -comparison;
        });

        // 渲染表格內容
        admins.forEach(admin => {
            const $row = $('<tr>').append(
                $('<td>').text(admin.id),
                $('<td>').text(admin.name),
                $('<td>').text(admin.permissions),
                $('<td>').append(
                    $('<span>')
                        .addClass(`status-badge ${admin.status === '啟用中' ? 'status-active' : 'status-disabled'}`)
                        .text(admin.status)
                ),
                $('<td>').text(admin.joinDate),
                $('<td>').text(admin.phone),
                $('<td>').text(admin.email),
                $('<td>').text(admin.lastLogin),
                $('<td>').addClass('action-buttons').append(
                    $('<button>')
                        .addClass('button detail-btn')
                        .text('詳細資訊')
                        .on('click', () => showAdminDetails(admin)),
                    $('<button>')
                        .addClass(`button status-btn ${admin.status === '啟用中' ? 'disable-btn' : 'enable-btn'}`)
                        .text(admin.status === '啟用中' ? '停用' : '啟用')
                        .on('click', () => toggleAdminStatus(admin))
                )
            );
            $tableBody.append($row);
        });
    }

    // 詳細資訊模態框
    function showAdminDetails(admin) {
        const detailsHtml = `
            <div class="modal-content admin-details">
                <span class="close-btn">&times;</span>
                <h2>管理員詳細資訊</h2>
                <form id="adminDetailsForm">
                    <input type="hidden" name="id" value="${admin.id}">
                    <div class="form-group">
                        <label>管理員ID：</label>
                        <input type="text" value="${admin.id}" disabled>
                    </div>
                    <div class="form-group">
                        <label>姓名：</label>
                        <input type="text" name="name" value="${admin.name}">
                    </div>
                    <div class="form-group">
                        <label>權限：</label>
                        <select name="permissions">
                            <option value="1" ${admin.permissions === '管理員' ? 'selected' : ''}>管理員</option>
                            <option value="0" ${admin.permissions === '管理員' ? 'selected' : ''}>管理員</option>
                            
                        </select>
                    </div>
                    <div class="form-group">
                        <label>電話：</label>
                        <input type="text" name="phone" value="${admin.phone}">
                    </div>
                    <div class="form-group">
                        <label>電子信箱：</label>
                        <input type="email" name="email" value="${admin.email}">
                    </div>
                    <div class="form-group">
                        <label>狀態：</label>
                        <input type="text" value="${admin.status}" disabled>
                    </div>
                    <div class="form-group">
                        <label>加入日期：</label>
                        <input type="text" value="${admin.joinDate}" disabled>
                    </div>
                    <div class="form-group">
                        <label>最後登入：</label>
                        <input type="text" value="${admin.lastLogin}" disabled>
                    </div>
                    <button type="submit" class="button save-btn">儲存修改</button>
                </form>
            </div>
        `;

        const $modal = $('<div>').addClass('modal').html(detailsHtml);
        $('body').append($modal);
        $modal.fadeIn();

        // 關閉按鈕事件
        $modal.find('.close-btn').on('click', () => {
            $modal.fadeOut(() => $modal.remove());
        });

        // 表單提交事件
        $modal.find('#adminDetailsForm').on('submit', function(e) {
            e.preventDefault();
            const formData = {};
            $(this).serializeArray().forEach(item => {
                formData[item.name] = item.value;
            });

            // 更新模擬資料
            const index = mockAdmins.findIndex(a => a.id === parseInt(formData.id));
            if (index !== -1) {
                mockAdmins[index] = { ...mockAdmins[index], ...formData };
                
                // 新增操作日誌
                addNewLog(
                    '更新資料',
                    formData.name,
                    '更新管理員基本資料'
                );

                // 重新渲染表格
                fetchAdminList(currentPage, getSearchFilters());
            }

            $modal.fadeOut(() => $modal.remove());
        });
    }

    // 更新搜尋功能
    $('#searchBtn').on('click', function() {
        const filters = {
            keyword: $('#keyword').val().toLowerCase(),
            status: $('#filterStatus').val(),
            permissions: $('#filterPermissions').val(),
        };
        
        currentPage = 1;
        fetchAdminList(currentPage, filters);
    });

    // 更新操作日誌顯示
    function renderLogTable(logs) {
        const $logContainer = $('#logContainer');
        $logContainer.empty();

        if (logs.length === 0) {
            $logContainer.append('<div class="no-logs">暫無操作日誌</div>');
            return;
        }

        logs.forEach(log => {
            const $logItem = $('<div>').addClass('log-item').append(
                $('<div>').addClass('log-header').append(
                    $('<span>').addClass('timestamp').text(log.timestamp),
                    $('<span>').addClass('admin-name').text(` - ${log.adminName} `),
                    $('<span>').addClass('action').text(log.action)
                ),
                $('<div>').addClass('log-details').append(
                    $('<span>').addClass('target').text(`操作對象: ${log.target}`),
                    $('<span>').addClass('details').text(` - ${log.details}`),
                    $('<span>').addClass('ip').text(` (IP: ${log.ip})`)
                )
            );
            $logContainer.append($logItem);
        });
    }

    // 渲染分頁控制
    function renderPagination(totalPages) {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';

        // 上一頁按鈕
        if (currentPage > 1) {
            const prevButton = createPaginationButton('上一頁', currentPage - 1);
            pagination.appendChild(prevButton);
        }

        // 頁碼按鈕
        for (let i = 1; i <= totalPages; i++) {
            const pageButton = createPaginationButton(i, i);
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pagination.appendChild(pageButton);
        }

        // 下一頁按鈕
        if (currentPage < totalPages) {
            const nextButton = createPaginationButton('下一頁', currentPage + 1);
            pagination.appendChild(nextButton);
        }
    }

    // 創建分頁按鈕
    function createPaginationButton(text, page) {
        const button = document.createElement('button');
        button.textContent = text;
        button.classList.add('pagination-button');
        button.addEventListener('click', () => {
            currentPage = page;
            const filters = getSearchFilters();
            fetchAdminList(page, filters);
        });
        return button;
    }

    // 獲取搜尋篩選條件
    function getSearchFilters() {
        return {
            keyword: document.getElementById('keyword').value,
            status: document.getElementById('filterStatus').value,
            permissions: document.getElementById('filterPermissions').value
        };
    }

    // 切換管理員狀態（啟用/停用）
    function toggleAdminStatus(admin) {
        const newStatus = admin.status === '啟用中' ? '停用' : '啟用中';
        const actionText = admin.status === '啟用中' ? '停用' : '啟用';
        
        if (confirm(`確定要${actionText}管理員 ${admin.name} 的帳號嗎？`)) {
            admin.status = newStatus;
            fetchAdminList(currentPage, getSearchFilters());
            
            // 新增操作日誌
            addNewLog(
                `${actionText}帳號`,
                admin.name,
                `${actionText}管理員帳號`
            );
        }
    }

    // 初始化表格排序功能
    function initTableSort() {
        const sortableFields = {
            'id': '管理員編號',
            'name': '管理員名稱',
            'permissions': '權限',
            'status': '狀態',
            'joinDate': '新增日期',
            'phone': '電話',
            'email': '電子信箱',
            'lastLogin': '上次登入時間'
        };

        // 設定預設排序
        currentSort = {
            field: 'id',
            direction: 'desc'
        };

        // 為可排序的欄位添加排序圖標和點擊事件
        Object.entries(sortableFields).forEach(([field, label]) => {
            $(`#adminTable th:contains(${label})`).addClass('sortable').append(
                $('<span>').addClass('sort-icon')
            ).on('click', function() {
                // 切換排序方向
                if (currentSort.field === field) {
                    currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc';
                } else {
                    currentSort.field = field;
                    currentSort.direction = 'desc'; // 新欄位預設降序
                }

                // 更新所有排序圖標
                $('.sort-icon').removeClass('asc desc');
                $(this).find('.sort-icon').addClass(currentSort.direction);

                // 重新獲取並渲染數據
                fetchAdminList(currentPage, getSearchFilters());
            });
        });

        // 初始化時顯示預設排序圖標
        $(`#adminTable th:contains(管理員編號)`)
            .find('.sort-icon')
            .addClass('desc');
    }

    // 當執行管理員操作時，更新日誌
    function addNewLog(action, target, details) {
        const newLog = {
            id: mockLogs.length + 1,
            adminName: 'adminId + 管理員', // 這裡應該使用實際登入的管理員名稱
            action: action,
            target: target,
            details: details,
            timestamp: new Date().toLocaleString('zh-TW', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            }),
            ip: '127.0.0.1' // 這裡應該使用實際的 IP
        };

        mockLogs.unshift(newLog); // 將新日誌加到最前面
        fetchLogList(1); // 重新載入日誌列表
    }

    // 在管理員資料更新時也加入日誌記錄
    function updateAdminDetails(adminId, formData) {
        // 處理更新邏輯...
        
        addNewLog(
            '更新資料',
            formData.name,
            '更新管理員基本資料'
        );
    }

    // 修改新增管理員的邏輯
    $(document).ready(function() {
        initTableSort();
        fetchAdminList(currentPage);
        
        // 新增管理員按鈕點擊事件
        $('#addAdminBtn').off('click').on('click', function() {
            // 清空表單
            $('#addAdminForm')[0].reset();
            $('#passwordError').text('');
            // 顯示模態框
            $('#addAdminModal').fadeIn();
        });

        // 關閉按鈕事件
        $('.close-btn, #closeBtn').off('click').on('click', function() {
            $('#addAdminModal').fadeOut();
            $('#addAdminForm')[0].reset();
            $('#passwordError').text('');
        });

        // 新增管理員表單提交
        $('#addAdminForm').off('submit').on('submit', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const formData = {};
            $(this).serializeArray().forEach(item => {
                formData[item.name] = item.value;
            });

            // 驗證密碼
            if (formData.password !== formData.confirmPassword) {
                $('#passwordError').text('密碼不一致');
                return;
            }

            // 新增管理員
            const newAdmin = {
                id: Math.max(...mockAdmins.map(admin => admin.id)) + 1,
                name: formData.name,
                permissions: formData.permissions,
                status: '啟用中',
                joinDate: new Date().toLocaleDateString(),
                phone: formData.phone,
                email: formData.email,
                lastLogin: '-'
            };

            // 將新管理員加入到模擬資料中
            mockAdmins.push(newAdmin);

            // 新增操作日誌
            addNewLog(
                '新增管理員',
                formData.name,
                '新增管理員帳號'
            );

            // 重新渲染表格
            fetchAdminList(currentPage, getSearchFilters());

            // 清空表單並關閉視窗
            this.reset();
            $('#addAdminModal').fadeOut();
            $('#passwordError').text('');
        });

        // 點擊模態框外部時關閉
        $(window).on('click', function(e) {
            if ($(e.target).is('#addAdminModal')) {
                $('#addAdminModal').fadeOut();
                $('#addAdminForm')[0].reset();
                $('#passwordError').text('');
            }
        });
    });
});