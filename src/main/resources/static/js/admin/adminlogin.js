document.addEventListener('DOMContentLoaded', () => {
	setupEventListeners();
});


function setupEventListeners() {
    const forgotPasswordLink = document.getElementById('forgotPassword');
    if (forgotPasswordLink) {
        forgotPasswordLink.addEventListener('click', (e) => {
            e.preventDefault();
            handleForgotPassword();
        });
    }
}

async function handleLogin() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const rememberMe = document.getElementById('rememberMe').checked;
    const errorMessage = document.getElementById('errorMessage');

    try {
        // 這裡應該是實際的 API 呼叫
//         const response = await fetch('your-api-endpoint', {
//             method: 'POST',
//             headers: { 'Content-Type': 'application/json' },
//             body: JSON.stringify({ email, password, rememberMe })
//         });

        // 模擬登入成功
         const loginSuccessful = true; // 這應該是基於 API 響應

        if (loginSuccessful) {
            // 儲存登入狀態
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userToken', 'sample-token');
            
            if (rememberMe) {
                localStorage.setItem('rememberedEmail', email);
            } else {
                localStorage.removeItem('rememberedEmail');
            }

            // 重定向到原始請求頁面或預設頁面
            window.location.href = '/admin/adminBackend';
        } else {
            showError('登入失敗，請檢查您的帳號密碼。');
        }
    } catch (error) {
        console.error('登入錯誤:', error);
        showError('發生錯誤，請稍後再試。');
    }
}

function handleForgotPassword() {
    // 實作忘記密碼功能
    alert('忘記密碼功能即將推出');
}


