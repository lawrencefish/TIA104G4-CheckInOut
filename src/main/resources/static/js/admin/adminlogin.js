document.addEventListener('DOMContentLoaded', () => {
    initializeApp();
});

function initializeApp() {
    loadHeaderFooter();
    setupFormValidation();
    setupEventListeners();
}

function loadHeaderFooter() {
    // 載入 header
    fetch('backend-header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
        })
        .catch(error => console.error('載入 header 失敗:', error));

    // 載入 footer
    fetch('backend-footer.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('footer').innerHTML = data;
        })
        .catch(error => console.error('載入 footer 失敗:', error));
}

function setupFormValidation() {
    const form = document.getElementById('loginForm');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        e.stopPropagation();

        if (form.checkValidity()) {
            await handleLogin();
        }

        form.classList.add('was-validated');
    });
}

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
            const redirectUrl = localStorage.getItem('redirectUrl') || 'admin-backend.html';
            localStorage.removeItem('redirectUrl'); // 清除重定向 URL
            window.location.href = redirectUrl;
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

function showError(message) {
    const errorElement = document.getElementById('errorMessage');
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.classList.remove('d-none');
    }
}
