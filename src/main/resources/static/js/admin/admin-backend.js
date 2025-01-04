// === 修改：移除模擬資料，改用資料庫資料 ===
$(document).ready(function () {
    const CONFIG = {
        itemsPerPage: 10,
        defaultSortField: 'adminId',
        defaultSortDirection: 'desc',
        passwordMinLength: 8
    };

    let currentPage = 1;
    let currentSort = {
        field: CONFIG.defaultSortField,
        direction: CONFIG.defaultSortDirection
    };

    // === 新增：統一的API請求處理函數 ===
    const api = {
        get: async (url, params = {}) => {
            try {
                const response = await $.ajax({
                    url: url,
                    method: 'GET',
                    data: params,
                });
                return response;
            } catch (error) {
                handleError(error);
                throw error;
            }
        },
        post: async (url, data = {}) => {
            try {
                const response = await $.ajax({
                    url: url,
                    method: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                return response;
            } catch (error) {
                handleError(error);
                throw error;
            }
        },
        put: async (url, data = {}) => {
            try {
                const response = await $.ajax({
                    url: url,
                    method: 'PUT',
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                return response;
            } catch (error) {
                handleError(error);
                throw error;
            }
        }
    };

    // === 修改：使用資料庫資料的管理員列表獲取 ===
    async function fetchAdminList(page, filters = {}) {
        const $loadingSpinner = $('#loadingSpinner');
        $loadingSpinner.show();

        try {
            // 構建API請求參數
            const params = {
                page: page,
                size: CONFIG.itemsPerPage,
                sortField: currentSort.field,
                sortDirection: currentSort.direction,
                ...filters
            };

            // 發送API請求獲取管理員列表
            const response = await api.get('/admin/adminBackend', params);
			
            const admins = await response.json(); // 因為後端直接返回 List<Admin>
			const totalPages = Math.ceil(admins.length / CONFIG.itemsPerPage); // 在前端計算總頁數
            
			// 更新UI
            renderAdminTable(admins);
            renderPagination(totalPages);
        } catch (error) {
            console.error(error, '獲取管理員列表失敗');
			alert('獲取資料失敗，請稍後再試');
	    } finally {
            $loadingSpinner.hide();
        }
    }

    // === 修改：表格渲染函數，適配資料庫欄位 ===
    function renderAdminTable(admins) {
        const $tableBody = $('#userTableBody');
        $tableBody.empty();

        admins.forEach(admin => {
            const $row = $(`
                <tr data-admin-id="${admin.adminId}">
                    <td>${admin.adminId}</td>
                    <td>${escapeHtml(admin.adminAccount)}</td>
                    <td>${admin.permissions === 1 ? '資深管理員' : '管理員'}</td>
                    <td>
                        <span class="status-badge ${admin.status === 1 ? 'status-active' : 'status-disabled'}">
                            ${admin.status === 1 ? '啟用中' : '停用'}
                        </span>
                    </td>
                    <td>${formatDate(admin.createTime)}</td>
                    <td>${escapeHtml(admin.phoneNumber)}</td>
                    <td>${escapeHtml(admin.email)}</td>
                    <td>${admin.lastLoginTime ? formatDate(admin.lastLoginTime) : '-'}</td>
                    <td class="action-buttons">
                        <button class="button detail-btn">詳細資訊</button>
                        <button class="button status-btn ${admin.status === 1 ? 'disable-btn' : 'enable-btn'}">
                            ${admin.status === 1 ? '停用' : '啟用'}
                        </button>
                    </td>
                </tr>
            `);

            $row.find('.detail-btn').on('click', () => showAdminDetails(admin));
            $row.find('.status-btn').on('click', () => toggleAdminStatus(admin));

            $tableBody.append($row);
        });
    }

    // === 新增：日期格式化函數 ===
    function formatDate(dateString) {
        return new Date(dateString).toLocaleString('zh-TW', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    }

    // === 修改：更新管理員詳細資訊視窗 ===
    function showAdminDetails(admin) {
        const modalHtml = `
            <div class="modal-content admin-details">
                <span class="close-btn">&times;</span>
                <h2>管理員詳細資訊</h2>
                <form id="adminDetailsForm" class="detail-form">
                    <input type="hidden" name="adminId" value="${admin.adminId}">
                    
                    <div class="form-group">
                        <label>管理員ID：</label>
                        <input type="text" value="${admin.adminId}" disabled>
                    </div>
                    
                    <div class="form-group">
                        <label>帳號：</label>
                        <input type="text" value="${escapeHtml(admin.adminAccount)}" disabled>
                    </div>
                    
                    <div class="form-group">
                        <label>權限：</label>
                        <select name="permissions">
                            <option value="0" ${admin.permissions === 0 ? 'selected' : ''}>管理員</option>
                            <option value="1" ${admin.permissions === 1 ? 'selected' : ''}>資深管理員</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label>電話：</label>
                        <input type="tel" name="phoneNumber" value="${escapeHtml(admin.phoneNumber)}" required
                               pattern="[0-9]{10}" title="請輸入10位數字的電話號碼">
                    </div>
                    
                    <div class="form-group">
                        <label>電子信箱：</label>
                        <input type="email" name="email" value="${escapeHtml(admin.email)}" required>
                    </div>
                    
                    <div class="form-group">
                        <label>狀態：</label>
                        <input type="text" value="${admin.status === 1 ? '啟用中' : '停用'}" disabled>
                    </div>
                    
                    <div class="form-group">
                        <label>建立時間：</label>
                        <input type="text" value="${formatDate(admin.createTime)}" disabled>
                    </div>
                    
                    <div class="form-group">
                        <label>最後登入：</label>
                        <input type="text" value="${admin.lastLoginTime ? formatDate(admin.lastLoginTime) : '-'}" disabled>
                    </div>
                    
                    <button type="submit" class="button save-btn">儲存修改</button>
                </form>
            </div>
        `;

        const $modal = $('<div>').addClass('modal').html(modalHtml);
        $('body').append($modal);
        $modal.fadeIn();

        // 關閉按鈕事件
        $modal.find('.close-btn').on('click', () => {
            $modal.fadeOut(() => $modal.remove());
        });

        // === 修改：表單提交處理，使用API更新資料 ===
        $modal.find('#adminDetailsForm').on('submit', async function(e) {
            e.preventDefault();
            
            const formData = {};
            $(this).serializeArray().forEach(item => {
                formData[item.name] = item.value;
            });

            // 驗證表單數據
            const validations = {
                phoneNumber: validateData.phone(formData.phoneNumber),
                email: validateData.email(formData.email)
            };

            const errors = Object.entries(validations)
                .filter(([_, validation]) => !validation.isValid)
                .map(([field, validation]) => validation.errors)
                .flat();

            if (errors.length > 0) {
                alert(errors.join('\n'));
                return;
            }

            try {
                // 發送API請求更新管理員資料
                await api.post('/admin/edit', {
					adminId: formData.adminId,
				    adminAccount: formData.adminAccount,
				    permissions: parseInt(formData.permissions),
				    phoneNumber: formData.phoneNumber,
				    email: formData.email
					});
                
                 重新獲取列表
                await fetchAdminList(currentPage, getSearchFilters());
                $modal.fadeOut(() => $modal.remove());
                
                alert('更新成功');
            } catch (error) {
                handleError(error, '更新管理員資料失敗');
            }
        });
    }

    // === 修改：管理員狀態切換，使用API ===
    async function toggleAdminStatus(admin) {
        const newStatus = admin.status === 1 ? 0 : 1;
        const actionText = admin.status === 1 ? '停用' : '啟用';
        
        if (confirm(`確定要${actionText}管理員 ${escapeHtml(admin.adminAccount)} 的帳號嗎？`)) {
            try {
                await api.post('/admin/updateStatus', {
				    adminId: admin.adminId,
				    status: newStatus
				});
                
                await fetchAdminList(currentPage, getSearchFilters());
                alert(`${actionText}成功`);
            } catch (error) {
                handleError(error, `${actionText}管理員帳號失敗`);
            }
        }
    }

    // === 修改：搜尋和篩選，使用後端篩選 ===
    $('#searchBtn').on('click', async function() {
        const filters = getSearchFilters();
        currentPage = 1;
        await fetchAdminList(currentPage, filters);
    });

    function getSearchFilters() {
        return {
            keyword: $('#keyword').val(),
            status: $('#filterStatus').val(),
            permissions: $('#filterPermissions').val()
        };
    }

    // === 修改：分頁控制更新 ===
    function renderPagination(totalPages) {
        const $pagination = $('#pagination');
        $pagination.empty();

        if (currentPage > 1) {
            $pagination.append(
                $('<button>')
                    .addClass('pagination-button')
                    .text('上一頁')
                    .on('click', async () => {
                        currentPage--;
                        await fetchAdminList(currentPage, getSearchFilters());
                    })
            );
        }

        for (let i = 1; i <= totalPages; i++) {
            $pagination.append(
                $('<button>')
                    .addClass('pagination-button')
                    .toggleClass('active', i === currentPage)
                    .text(i)
                    .on('click', async () => {
                        currentPage = i;
                        await fetchAdminList(currentPage, getSearchFilters());
                    })
            );
        }

        if (currentPage < totalPages) {
            $pagination.append(
                $('<button>')
                    .addClass('pagination-button')
                    .text('下一頁')
                    .on('click', async () => {
                        currentPage++;
                        await fetchAdminList(currentPage, getSearchFilters());
                    })
            );
        }
    }

    // === 修改：新增管理員表單提交 ===
    $('#addAdminForm').on('submit', async function(e) {
        e.preventDefault();
        
        const formData = {};
        $(this).serializeArray().forEach(item => {
            formData[item.name] = item.value;
        });

        // 驗證密碼
        if (formData.password !== formData.confirmPassword) {
            $('#passwordError').text('密碼不一致');
            return;
        }

        // 驗證密碼強度
        const passwordValidation = validateData.password(formData.password);
        if (!passwordValidation.isValid) {
            $('#passwordError').text(passwordValidation.errors.join('\n'));
            return;
        }

        try {
            // 發送API請求新增管理員
            await api.post('/admin/add', formData);
            
            // 重新獲取列表
            await fetchAdminList(1, getSearchFilters());
            
            // 清空表單並關閉視窗
            this.reset();
            $('#addAdminModal').fadeOut();
            $('#passwordError').text('');
            
            alert('新增成功');
        } catch (error) {
            handleError(error, '新增管理員失敗');
        }
    });

    // 初始化
    fetchAdminList(currentPage);
});