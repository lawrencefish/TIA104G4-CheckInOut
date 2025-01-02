document.addEventListener('DOMContentLoaded', function() {
    // 分頁切換功能
    const tabs = document.querySelectorAll('.tab-btn');
    const tabPanes = document.querySelectorAll('.tab-pane');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // 移除所有活動狀態
            tabs.forEach(t => t.classList.remove('active'));
            tabPanes.forEach(p => p.classList.remove('active'));

            // 添加當前分頁的活動狀態
            tab.classList.add('active');
            const targetPane = document.getElementById(tab.dataset.tab);
            if (targetPane) {
                targetPane.classList.add('active');
            }
        });
    });

    // 更新時間顯示
    function updateLastLoginTime() {
        const now = new Date();
        const timeString = now.toLocaleString('zh-TW', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        });
        document.getElementById('lastLoginTime').textContent = timeString;
    }

    // 表單驗證函數
    function validateForm() {
        const name = document.querySelector('input[type="text"]').value;
        const email = document.querySelector('input[type="email"]').value;
        const phone = document.querySelector('input[type="tel"]').value;

        // 電子郵件格式驗證
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            showAlert('請輸入有效的電子郵件地址');
            return false;
        }

        // 電話格式驗證
        const phoneRegex = /^[0-9]{4}-[0-9]{3}-[0-9]{3}$/;
        if (!phoneRegex.test(phone)) {
            showAlert('請輸入有效的電話號碼格式（xxxx-xxx-xxx）');
            return false;
        }

        return true;
    }

    // 顯示提示訊息
    function showAlert(message, type = 'error') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        
        // 添加到頁面
        const container = document.querySelector('.container');
        container.insertBefore(alertDiv, container.firstChild);

        // 3秒後自動消失
        setTimeout(() => {
            alertDiv.remove();
        }, 3000);
    }

    // 更新個人資料
    const updateProfileBtn = document.querySelector('.btn-primary');
    if (updateProfileBtn) {
        updateProfileBtn.addEventListener('click', async function(e) {
            e.preventDefault();

            if (!validateForm()) {
                return;
            }

            // 收集表單數據
            const formData = {
                name: document.querySelector('input[type="text"]').value,
                title: document.querySelectorAll('input[type="text"]')[1].value,
                email: document.querySelector('input[type="email"]').value,
                phone: document.querySelector('input[type="tel"]').value
            };

            try {
                // 這裡可以添加實際的 API 調用
                // const response = await fetch('/api/profile/update', {
                //     method: 'POST',
                //     headers: {
                //         'Content-Type': 'application/json',
                //     },
                //     body: JSON.stringify(formData)
                // });

                // 模擬 API 調用成功
                showAlert('資料更新成功！', 'success');
            } catch (error) {
                showAlert('更新失敗，請稍後再試');
                console.error('Error:', error);
            }
        });
    }

    // 密碼變更處理
    const changePasswordBtn = document.querySelector('.btn-outline');
    if (changePasswordBtn) {
        changePasswordBtn.addEventListener('click', function() {
            // 創建密碼變更對話框
            const modal = document.createElement('div');
            modal.className = 'modal';
            modal.innerHTML = `
                <div class="modal-content">
                    <h2>變更密碼</h2>
                    <form id="passwordForm">
                        <div class="form-group">
                            <label>當前密碼</label>
                            <input type="password" id="currentPassword" required>
                        </div>
                        <div class="form-group">
                            <label>新密碼</label>
                            <input type="password" id="newPassword" required>
                        </div>
                        <div class="form-group">
                            <label>確認新密碼</label>
                            <input type="password" id="confirmPassword" required>
                        </div>
                        <div class="modal-actions">
                            <button type="submit" class="btn btn-primary">確認</button>
                            <button type="button" class="btn btn-outline" onclick="this.closest('.modal').remove()">取消</button>
                        </div>
                    </form>
                </div>
            `;

            document.body.appendChild(modal);

            // 添加密碼表單提交處理
            const passwordForm = document.getElementById('passwordForm');
            passwordForm.addEventListener('submit', async function(e) {
                e.preventDefault();

                const currentPassword = document.getElementById('currentPassword').value;
                const newPassword = document.getElementById('newPassword').value;
                const confirmPassword = document.getElementById('confirmPassword').value;

                // 密碼驗證
                if (newPassword.length < 8) {
                    showAlert('新密碼長度必須至少8個字符');
                    return;
                }

                if (newPassword !== confirmPassword) {
                    showAlert('兩次輸入的密碼不一致');
                    return;
                }

                try {
                    // 這裡可以添加實際的密碼更新 API 調用
                    // const response = await fetch('/api/profile/change-password', {
                    //     method: 'POST',
                    //     headers: {
                    //         'Content-Type': 'application/json',
                    //     },
                    //     body: JSON.stringify({
                    //         currentPassword,
                    //         newPassword
                    //     })
                    // });

                    // 模擬 API 調用成功
                    showAlert('密碼更新成功！', 'success');
                    modal.remove();
                } catch (error) {
                    showAlert('密碼更新失敗，請稍後再試');
                    console.error('Error:', error);
                }
            });
        });
    }

    // 安全設定按鈕處理
    const securityBtns = document.querySelectorAll('#security .btn-outline');
    securityBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const action = this.previousElementSibling.querySelector('h3').textContent;
            showAlert(`${action}功能即將推出`, 'info');
        });
    });

    // 操作記錄詳情按鈕處理
    const logDetailBtns = document.querySelectorAll('#logs .btn-text');
    logDetailBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const logTitle = this.previousElementSibling.querySelector('h3').textContent;
            const logTime = this.previousElementSibling.querySelector('p').textContent;
            showAlert(`${logTitle} - ${logTime}`, 'info');
        });
    });

    // 通知項目點擊處理
    const notificationItems = document.querySelectorAll('.notification-item');
    notificationItems.forEach(item => {
        item.addEventListener('click', function() {
            const notificationTitle = this.querySelector('h3').textContent;
            showAlert(`查看通知: ${notificationTitle}`, 'info');
        });
    });

    // CSS 樣式注入
    const style = document.createElement('style');
    style.textContent = `
        .modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .modal-content {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            width: 100%;
            max-width: 400px;
        }

        .modal-actions {
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
            margin-top: 1rem;
        }

        .alert {
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
            color: white;
        }

        .alert-error {
            background-color: #f44336;
        }

        .alert-success {
            background-color: #4caf50;
        }

        .alert-info {
            background-color: #2196f3;
        }
    `;
    document.head.appendChild(style);
});