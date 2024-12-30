// Header 功能初始化
function initializeHeader() {
    const profileTrigger = document.querySelector('.admin-profile-trigger');
    const dropdownMenu = document.querySelector('.admin-dropdown-menu');

    if (profileTrigger && dropdownMenu) {
        // 點擊頭像時切換選單
        profileTrigger.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdownMenu.classList.toggle('show');
        });

        // 點擊其他地方時關閉選單
        document.addEventListener('click', function() {
            dropdownMenu.classList.remove('show');
        });

        // 防止點擊選單本身時關閉
        dropdownMenu.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    }
}

// 登出功能
function handleLogout() {
    localStorage.clear();
    sessionStorage.clear();
    window.location.href = '/admin/login';
}

// 當 DOM 載入完成後初始化
document.addEventListener('DOMContentLoaded', initializeHeader);