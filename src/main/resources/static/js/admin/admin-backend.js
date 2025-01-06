$(document).ready(function () {
    // === 配置常數 ===
    const CONFIG = {
        itemsPerPage: 10,
        defaultSortField: 'adminId',
        defaultSortDirection: 'desc',
        passwordMinLength: 8,
		apiEndpoints: {
		            list: '/admin/list/api',
		            edit: '/admin/edit',
		            add: '/admin/add',
		            updateStatus: '/admin/updateStatus'
		        }
    };

    // === 全局變數 ===
    let currentPage = 1;
    let currentSort = {
        field: CONFIG.defaultSortField,
        direction: CONFIG.defaultSortDirection
    };

    // === 輔助函數 ===
	function escapeHtml(unsafe) {
	    if (!unsafe) return '';
	    return unsafe
	        .toString()
	        .replace(/&/g, "&amp;")
	        .replace(/</g, "&lt;")
	        .replace(/>/g, "&gt;")
	        .replace(/"/g, "&quot;")
	        .replace(/'/g, "&#039;");
	}
	
	function handleError(error, defaultMessage = '操作失敗') {
	        console.error('Error details:', error);
	        let errorMessage = defaultMessage;
	        
	        if (error.responseJSON && error.responseJSON.message) {
	            errorMessage = error.responseJSON.message;
	        } else if (error.status === 403) {
	            errorMessage = '您沒有權限執行此操作';
	        } else if (error.status === 401) {
	            errorMessage = '您的登入已過期，請重新登入';
	            window.location.href = '/admin/login';
	            return;
	        }

			// Show error in UI
		       const $errorAlert = $('<div>')
		           .addClass('error-alert')
		           .text(errorMessage)
		           .appendTo('body');
		           
		       setTimeout(() => $errorAlert.fadeOut(() => $errorAlert.remove()), 3000);
		   }

    const validateData = {
        phone: (phone) => {
            const isValid = /^[0-9]{10}$/.test(phone);
            return {
                isValid,
                errors: isValid ? [] : ['請輸入10位數字的電話號碼']
            };
        },
        email: (email) => {
            const isValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
            return {
                isValid,
                errors: isValid ? [] : ['請輸入有效的電子郵件地址']
            };
        },
        password: (password) => {
            const errors = [];
            if (password.length < CONFIG.passwordMinLength) {
                errors.push(`密碼長度至少需要${CONFIG.passwordMinLength}個字符`);
            }
            if (!/[A-Z]/.test(password)) {
                errors.push('密碼必須包含至少一個大寫字母');
            }
            if (!/[a-z]/.test(password)) {
                errors.push('密碼必須包含至少一個小寫字母');
            }
            if (!/[0-9]/.test(password)) {
                errors.push('密碼必須包含至少一個數字');
            }
            return {
                isValid: errors.length === 0,
                errors
            };
        }
    };
	
	adminAccount: (account) => {
//            const isValid = /^[a-zA-Z0-9_]{4,20}$/.test(account);
            return {
                isValid: true,
                errors: []
            };
        }

    // === API 請求處理函數 ===
    const api = {
        get: async (url, params = {}) => {
            try {
                const response = await $.ajax({
                    url: url,
                    method: 'GET',
                    data: params,
					headers: {
                        'Accept': 'application/json'
                    }
                });
                return response;
            } catch (error) {
                handleError(error, 'API請求失敗');
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
                        'Accept': 'application/json'
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
                        'Accept': 'application/json'
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
        const $tableBody = $('#userTableBody');

        try {
			$loadingSpinner.show();
            $tableBody.empty().append('<tr><td colspan="9" class="text-center">載入中...</td></tr>');
            // 構建查詢參數
            const params = {
			    page: page,
			    size: CONFIG.itemsPerPage,
				sort: `${currentSort.field},${currentSort.direction}`,
			    ...filters
			};
			
			Object.keys(params).forEach(key => {
			           if (params[key] == null) {
			               delete params[key];
			           }
			       });

	       const response = await api.get(CONFIG.apiEndpoints.list, params);
			
		// 修改資料處理邏輯，添加錯誤檢查
		if (!response || (!Array.isArray(response) && !response.content)) {
                throw new Error('Invalid response format from server');
            }

            const admins = Array.isArray(response) ? response : 
                (response.content || response.data || []);
            
            if (admins.length === 0) {
                $tableBody.html('<tr><td colspan="9" class="text-center">無資料</td></tr>');
                return;
            }

            const totalPages = Math.ceil(admins.length / CONFIG.itemsPerPage);
            renderAdminTable(admins);
            renderPagination(totalPages);
            
        } catch (error) {
            console.error('Error fetching admin list:', error);
            $tableBody.html('<tr><td colspan="9" class="text-center text-red-600">載入失敗，請稍後再試</td></tr>');
            handleError(error, '獲取管理員列表失敗');
        } finally {
            $loadingSpinner.hide();
        }
    }

    // === 修改：表格渲染函數，適配資料庫欄位 ===
    function renderAdminTable(admins) {
        const $tableBody = $('#userTableBody');
        $tableBody.empty();

        admins.forEach(admin => {
            const statusClass = admin.status === 1 ? 'status-active' : 'status-disabled';
            const statusText = admin.status === 1 ? '啟用中' : '停用';
            const actionButtonClass = admin.status === 1 ? 'disable-btn' : 'enable-btn';
            const actionButtonText = admin.status === 1 ? '停用' : '啟用';
            
			const $row = $(`
				<tr data-admin-id="${escapeHtml(admin.adminId)}">
                    <td>${escapeHtml(admin.adminId)}</td>
                    <td>${escapeHtml(admin.adminAccount)}</td>
                    <td>${admin.permissions === 1 ? '資深管理員' : '管理員'}</td>
                    <td>
                        <span class="status-badge ${statusClass}">${statusText}</span>
                    </td>
                    <td>${formatDate(admin.createTime)}</td>
                    <td>${escapeHtml(admin.phoneNumber)}</td>
                    <td>${escapeHtml(admin.email)}</td>
                    <td>${admin.lastLoginTime ? formatDate(admin.lastLoginTime) : '-'}</td>
                    <td class="action-buttons">
                        <button class="button detail-btn">詳細資訊</button>
                        <button class="button status-btn ${actionButtonClass}" 
                                ${admin.permissions === 1 ? 'disabled' : ''}>
                            ${actionButtonText}
                        </button>
                    </td>
                </tr>
            `);

            $row.find('.detail-btn').on('click', () => showAdminDetails(admin));
            $row.find('.status-btn').on('click', function() {
               if (!$(this).prop('disabled')) {
                   toggleAdminStatus(admin);
               }
           });
		   
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
		if (!admin) {
            handleError(new Error('無效的管理員資料'));
            return;
        }
		
        const modalHtml = `
		<div class="modal-content admin-details">
            <span class="close-btn">&times;</span>
            <h2>管理員詳細資訊</h2>
            <form id="adminDetailsForm" class="detail-form">
                <input type="hidden" name="adminId" value="${(admin.adminId)}">
                
                <div class="form-group">
                    <label>管理員ID：</label>
                    <input type="text" value="${(admin.adminId)}" name="adminId" disabled>
                </div>
                
                <div class="form-group">
                    <label>帳號：</label>
                    <input type="text" value="${(admin.adminAccount)}" name="adminAccount" disabled>
                </div>
                
                <div class="form-group">
                    <label>權限：</label>
                    <select name="permissions" ${admin.permissions === 1 ? 'disabled' : ''}>
                        <option value="0" ${admin.permissions === 0 ? 'selected' : ''}>管理員</option>
                        <option value="1" ${admin.permissions === 1 ? 'selected' : ''}>資深管理員</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>電話：</label>
                    <input type="tel" name="phoneNumber" value="${escapeHtml(admin.phoneNumber)}" 
                           pattern="[0-9]{10}" title="請輸入10位數字的電話號碼">
                    <span class="error-message" id="phoneError"></span>
                </div>
                
                <div class="form-group">
                    <label>電子信箱：</label>
                    <input type="email" name="email" value="${escapeHtml(admin.email)}">
                    <span class="error-message" id="emailError"></span>
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
			
			// 清除先前的錯誤訊息
			$('.error-message').text('');

            // 驗證表單數據
            const validations = {
                phoneNumber: validateData.phone(formData.phoneNumber),
                email: validateData.email(formData.email)
            };

            const errors = Object.entries(validations)
                .filter(([_, validation]) => !validation.isValid)
				.map(([field, validation]) => {
                    $(`#${field}Error`).text(validation.errors[0]);
                    return validation.errors;
                })
				.flat();

            if (errors.length > 0) {
                return;
            }

            try {
                // 發送API請求更新管理員資料
				const response = await api.post(CONFIG.apiEndpoints.edit, {
                    adminId: parseInt(formData.adminId),
                    permissions: parseInt(formData.permissions),
                    phoneNumber: formData.phoneNumber,
                    email: formData.email
                });
                
                 // 重新獲取列表
                await fetchAdminList(currentPage, getSearchFilters());
                $modal.fadeOut(() => $modal.remove());
                
				const $successAlert = $('<div>')
                   .addClass('success-alert')
                   .text('更新成功')
                   .appendTo('body');
                   
                setTimeout(() => $successAlert.fadeOut(() => $successAlert.remove()), 3000);
				        
            } catch (error) {
                handleError(error, '更新管理員資料失敗');
            }
        });
    }

    // === 修改：管理員狀態切換，使用API ===
    async function toggleAdminStatus(admin) {
		if (!admin || admin.permissions === 1) {
            handleError(new Error('無法更改資深管理員狀態'));
            return;
        }
		
        const newStatus = admin.status === 1 ? 0 : 1;
        const actionText = admin.status === 1 ? '停用' : '啟用';
        
        if (confirm(`確定要${actionText}管理員 ${escapeHtml(admin.adminAccount)} 的帳號嗎？`)) {
            try {
                await api.post(CONFIG.apiEndpoints.updateStatus, {
				    adminId: admin.adminId,
				    status: newStatus
				});
                
                await fetchAdminList(currentPage, getSearchFilters());
				
				const $successAlert = $('<div>')
                    .addClass('success-alert')
                    .text(`${actionText}成功`)
                    .appendTo('body');
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
		const filters = {
		        keyword: $('#keyword').val() || '',
		    };
		    
		    // 修改狀態篩選的處理方式
		    const status = $('#filterStatus').val();
		    if (status && status !== 'all') {
		        filters.status = parseInt(status); // 轉換為數字
		    }
		    
		    // 修改權限篩選的處理方式
		    const permissions = $('#filterPermissions').val();
		    if (permissions && permissions !== 'all') {
		        filters.permissions = parseInt(permissions); // 轉換為數字
		    }
		    
		    return filters;
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